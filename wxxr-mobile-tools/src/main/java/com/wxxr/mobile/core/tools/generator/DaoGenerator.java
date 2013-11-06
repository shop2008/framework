/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.tools.generator;

import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.tools.AbstractCodeGenerator;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.dao.annotation.Column;
import com.wxxr.mobile.dao.annotation.Id;
import com.wxxr.mobile.dao.annotation.Table;
import com.wxxr.mobile.dao.generator.Entity;
import com.wxxr.mobile.dao.generator.Property.PropertyBuilder;
import com.wxxr.mobile.dao.generator.Schema;

/**
 * @class desc DaoGenerator2.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-10-15 下午8:14:54
 */
public class DaoGenerator extends AbstractCodeGenerator {
	private static final String TEMPATE_NAME = "/META-INF/template/dao.vm";
	private static final String DAO_SESSION_TEMPATE_NAME = "/META-INF/template/dao-session.vm";
	private static final Logger log = LoggerFactory.getLogger(DaoGenerator.class);
	private Map<String,Schema> schemas = new HashMap<String,Schema>();
	@Override
	protected void doCodeGeneration(Set<? extends Element> elements, ICodeGenerationContext context) {
		log.info("Generate code for elements : {}", elements);
		loadSchema();
		for (Element element : elements) {
			com.wxxr.mobile.dao.annotation.Entity ann = element.getAnnotation(com.wxxr.mobile.dao.annotation.Entity.class);
			if (ann != null) {
				TypeElement entityE = (TypeElement) element;
				generateEntity(entityE, context);
			}
		}
		//generate dao session
		for (Map.Entry<String, Schema> entry: schemas.entrySet()) {
			try {
				generate(context, entry.getValue());
			} catch (Exception e) {
				log.error("Failed to generate db source code");
			}
		}
	}

	private void generateEntity(TypeElement entityE, ICodeGenerationContext context) {
		String elementFQN = entityE.getQualifiedName().toString();
		int idx = elementFQN.lastIndexOf('.');
		String defaultPkg = "";
		String defaultName = elementFQN;
		if (idx > 0) {
			defaultPkg = elementFQN.substring(0, idx);
			defaultName = elementFQN.substring(idx + 1);
		}
		Table table = entityE.getAnnotation(Table.class);
		String schemaName=null;
		String tableName = null;
		if (table != null) {
			tableName = table.name();
			schemaName = table.schema();
		}
		Schema schema = getSchema(schemaName,defaultPkg+".dao");
		Entity entity = null;
		if(schema!=null){
			schema.setDefaultJavaPackageDao(defaultPkg+".dao");
			entity = schema.addEntity(defaultName);
			entity.setJavaPackage(defaultPkg);
			entity.setClassNameDao(defaultName+"Dao");
			entity.setJavaPackageDao(defaultPkg+".dao");
			if (StringUtils.isBlank(tableName)) {
				tableName = StringUtils.upperCase(defaultName);
			}
			entity.setTableName(tableName);
			List<? extends Element> children = entityE.getEnclosedElements();
			if(children != null){
				for (Element child : children) {
					if(child.getKind() == ElementKind.FIELD){
						log.info("Found field {}, type :{}" , child,child.asType());
						Id id = child.getAnnotation(Id.class);
						if(id!=null){
							entity.addIdProperty();
							continue;
						}
						PropertyBuilder pBuilder = addProperty(entity, child.getSimpleName().toString(), child.asType().toString());
						if (pBuilder!=null) {
							Column column = child.getAnnotation(Column.class);
							String columnName = null;
							boolean nullable =true;
							boolean unique = false;
							if (column!=null) {
								columnName = column.name();
								nullable = column.nullable();
								unique = column.unique();
							}						
							if (StringUtils.isNotBlank(columnName)) {
								pBuilder.columnName(columnName);
							}
							if (!nullable) {
								pBuilder.notNull();
							}
							if (unique) {
								pBuilder.unique();
							}
						}
					}
				}
			}
		}
	}

	private PropertyBuilder addProperty(Entity entity,String propertyName,String propertyType){
		log.info(String.format("Add property:[name:%s,type:%s", propertyName,propertyType));
		if (propertyType.equals(String.class.getCanonicalName())) {//string
			return entity.addStringProperty(propertyName);
		}else if (propertyType.equals(Byte.class.getCanonicalName())||propertyType.equals(byte.class.getCanonicalName())) {
			return entity.addByteProperty(propertyName);//byte
		}else if (propertyType.equals(Short.class.getCanonicalName())||propertyType.equals(short.class.getCanonicalName())) {
			return entity.addShortProperty(propertyName);//short
		}else if (propertyType.equals(Integer.class.getCanonicalName())||propertyType.equals(int.class.getCanonicalName())) {
			return entity.addIntProperty(propertyName);//int
		}else if (propertyType.equals(Long.class.getCanonicalName())||propertyType.equals(long.class.getCanonicalName())) {
			return entity.addLongProperty(propertyName);//long
		}else if (propertyType.equals(Double.class.getCanonicalName())||propertyType.equals(double.class.getCanonicalName())) {
			return entity.addDoubleProperty(propertyName);//dobule
		}else if (propertyType.equals(Float.class.getCanonicalName())||propertyType.equals(float.class.getCanonicalName())) {
			return entity.addFloatProperty(propertyName);//float
		}else if (propertyType.equals(Boolean.class.getCanonicalName())||propertyType.equals(boolean.class.getCanonicalName())) {
			return entity.addBooleanProperty(propertyName);//boolean
		}else if (propertyType.equals(byte[].class.getCanonicalName())) {
			return entity.addByteArrayProperty(propertyName);//byte[]
		}else if (propertyType.equals(Date.class.getCanonicalName())) {
			return entity.addDateProperty(propertyName);//date
		}
		return entity.addStringProperty(propertyName);
	}
	private void loadSchema(){
		
	}
	private Schema getSchema(String schemaName,String defaultJavaPackage){
		if (StringUtils.isBlank(schemaName)) {
			schemaName = "default";
		}
		Schema schema = schemas.get(schemaName);
		if (schema==null) {
			if (schemaName.equals("default")) {
				schema = new Schema(0, defaultJavaPackage);
			}
			schemas.put(schemaName, schema);
		}
		return schema;
	}
	private void generate(ICodeGenerationContext context, Schema schema) throws Exception {
		Filer filer = context.getProcessingEnvironment().getFiler();
		if (schema != null) {
			schema.init2ndPass();
			schema.init3ndPass();
			List<Entity> entitys = schema.getEntities();
			if (entitys!=null) {
				for (Entity entity : entitys) {//generate dao classes
					generate(context, entity);
				}
			}
			//generate dao session
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("schema", schema);
			try {
				String text = context.getTemplateRenderer().renderFromFile(DAO_SESSION_TEMPATE_NAME, attributes);
				JavaFileObject file = filer.createSourceFile(schema.getDefaultJavaPackage() + ".DaoSession");
				log.info("Generate java class file : {}", file.toUri());
				Writer w = file.openWriter();
				w.write(text);
				w.close();
			} catch (Exception e) {
				log.error("Failed to generate dao session for :" +schema.getDefaultJavaPackage() + ".DaoSession");
			}
		}
	}
	private void generate(ICodeGenerationContext context, com.wxxr.mobile.dao.generator.Entity entity) throws Exception {
		Filer filer = context.getProcessingEnvironment().getFiler();
		if (entity != null) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("entity", entity);
			attributes.put("schema", entity.getSchema());
			try {
				String text = context.getTemplateRenderer().renderFromFile(TEMPATE_NAME, attributes);
				JavaFileObject file = filer.createSourceFile(entity.getJavaPackageDao() + "." + entity.getClassNameDao());
				log.info("Generate java class file : {}", file.toUri());
				Writer w = file.openWriter();
				w.write(text);
				w.close();
			} catch (Exception e) {
				log.error("Failed to generate dao class for :" + entity.getJavaPackageDao() + "." + entity.getClassNameDao());
			}
		}
	}
}
