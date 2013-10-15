/*
 * Copyright 2010-2013 WXXR Network Technology
 * Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.tools.generator;

import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.annotation.Entity;
import com.wxxr.mobile.core.tools.AbstractCodeGenerator;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @class desc DaoGenerator.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-9-26 下午6:12:02
 */
public class DaoGenerator extends AbstractCodeGenerator {
	private static final String TEMPATE_NAME = "/META-INF/template/dao.vm";

	private static final Logger log = LoggerFactory.getLogger(DaoGenerator.class);

	private Class<?> getTypeElement(ICodeGenerationContext context, String typeName) {
		Set<Element> rootE = (Set<Element>) context.getRoundEnvironment().getRootElements();
		for (Element element : rootE) {
			TypeElement typeE = (TypeElement) element;
			List<TypeParameterElement> typePs  = (List<TypeParameterElement>) typeE.getTypeParameters();
			for (TypeParameterElement typeP : typePs) {
				System.out.println(typeP.getSimpleName().toString());
			}
			if (typeE.getQualifiedName().toString().equals(typeName)) {
				System.out.println(typeE.getQualifiedName() + "==" + typeName);
				return typeE.asType().getClass();
			}
		}
		return null;
	}

	private DAOModel createDaoModel(Class<?> daoIfClass, TypeElement entityE, ICodeGenerationContext context) {
		if (daoIfClass == null) {
			System.out.println("daoIfClass==null");
			return null;
		}
		System.out.println(daoIfClass.getCanonicalName());
		String elementFQN = entityE.getQualifiedName().toString();
		int idx = elementFQN.lastIndexOf('.');
		String defaultPkg = "";
		String defaultName = elementFQN;
		if (idx > 0) {
			defaultPkg = elementFQN.substring(0, idx);
			defaultName = elementFQN.substring(idx + 1);
		}
		DAOModel daoModel = new DAOModel();
		daoModel.addImport(elementFQN);
		String daoPkg = null;
		if (daoIfClass != null && daoIfClass.isInterface()) {
			daoPkg = daoIfClass.getPackage().getName() + ".dao";
			daoModel.addImport(daoIfClass.getCanonicalName());
			daoModel.setDaoIfName(daoIfClass.getSimpleName());
			daoModel.setDaoIfPkg(daoIfClass.getPackage().getName());
			Method[] methods = daoIfClass.getDeclaredMethods();
			if (methods != null) {
				for (Method method : methods) {
					daoModel.addMethod(method);
				}
			}
		}
		if (StringUtils.isBlank(daoPkg)) {
			daoPkg = defaultPkg;
		}
		daoModel.setPkgName(daoPkg);
		daoModel.setName(defaultName + "DAO");
		return daoModel;
	}

	@Override
	protected void doCodeGeneration(Set<? extends Element> elements, ICodeGenerationContext context) {
		log.info("Generate code for elements : {}", elements);
		Filer filer = context.getProcessingEnvironment().getFiler();
		for (Element element : elements) {
			Entity ann = element.getAnnotation(Entity.class);
			if (ann != null) {
				Class<?> ifDAOClass = null;
				String ifDAOClassName = ann.daoIfClass();
				if (!StringUtils.isBlank(ifDAOClassName)) {
					ifDAOClass = getTypeElement(context, ifDAOClassName);
					TypeElement entityE = (TypeElement) element;
					DAOModel daoModel = createDaoModel(ifDAOClass, entityE, context);
					String elementFQN = entityE.getQualifiedName().toString();
					int idx = elementFQN.lastIndexOf('.');
					String defaultPkg = "";
					String defaultName = elementFQN;
					if (idx > 0) {
						defaultPkg = elementFQN.substring(0, idx);
						defaultName = elementFQN.substring(idx + 1);
					}
					EntityModel entity = new EntityModel();
					entity.setName(defaultName);
					entity.setPkgName(defaultPkg);
					if (daoModel != null) {
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put("daoModel", daoModel);
						attributes.put("model", entity);
						try {
							String text = context.getTemplateRenderer().renderFromFile(TEMPATE_NAME, attributes);
							JavaFileObject file = filer.createSourceFile(daoModel.getPkgName() + "." + daoModel.getName());
							log.info("Generate java class file : {}", file.toUri());
							Writer w = file.openWriter();
							w.write(text);
							w.close();
						} catch (Exception e) {
							log.error("Failed to generate bindable bean class for :" + daoModel.getPkgName() + "." + daoModel.getName());
						}
					}
					/*
					 * List<? extends Element> children =
					 * entityE.getEnclosedElements(); if (children != null) {
					 * for (Element child : children) { if (child.getKind() ==
					 * ElementKind.FIELD) { log.info("Found field {}, type :{}",
					 * child, child.asType()); // TypeElement type = //
					 * (TypeElement
					 * )context.getProcessingEnvironment().getTypeUtils
					 * ().asElement(child.asType());
					 * //model.addField(child.getSimpleName().toString(),
					 * child.asType().toString()); } } }
					 */

				}

			}
		}
	}

}
