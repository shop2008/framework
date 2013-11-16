/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.tree.VariableTree;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnMenuHide;
import com.wxxr.mobile.core.ui.annotation.OnMenuShow;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.DomainValueChangedEventImpl;
import com.wxxr.mobile.core.ui.common.ELAttributeValueEvaluator;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.ModelUtils;
import com.wxxr.mobile.core.ui.common.SimpleProgressGuard;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
@SuppressWarnings({ "unused", "restriction" })
public abstract class ViewModelUtils {
	private static final Logger log = LoggerFactory.getLogger(UIViewModelGenerator.class);
 

	public static MethodModel createInitMethod(ICodeGenerationContext context,ViewModelClass model){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("init");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		String javaStatement = context.getTemplateRenderer().renderMacro("init", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static void createExpressionModel(ViewModelClass model, JavaModel field, String key, String expr){
//		String s = getELExpression(expr);
//		if(s != null){
			ExpressionModel expression = new ExpressionModel();
			expression.setAttributeKey(key);
			expression.setExpression(expr);
			expression.setField(field);
			model.addExpression(expression);
//		}
	}
	
	public static MethodModel createInitViewGroupsMethod(ICodeGenerationContext context,ViewModelClass model, List<ViewGroupModel> groups){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initViewGroups");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("viewGroups", groups);
		String javaStatement = context.getTemplateRenderer().renderMacro("initViewGroups", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}


	public static MethodModel createInitDataFieldsMethod(ICodeGenerationContext context,ViewModelClass model, List<DataFieldModel> fields){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initDataFields");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("dataFields", fields);
		String javaStatement = context.getTemplateRenderer().renderMacro("initDataFields", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	
	
	public static MethodModel createOnCreateMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("callOnCreateMethods");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onCreateMethods", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createOnDestroyMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onDestroy");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onDestroy", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	
	public static MethodModel createOnShowMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onShow");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onShow", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createOnMenuShowMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onMenuShow");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onMenuShow", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createOnMenuHideMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onMenuId");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onMenuHide", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}



	public static MethodModel createOnHideMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onHide");
		m.setModifiers("protected");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onHide", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	
	public static MethodModel createOnDataChangedMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onDataChanged");
		m.setModifiers("protected");
		m.setReturnType("void");
		model.addImport(ValueChangedEvent.class.getCanonicalName());
		m.setParameterTypes(new String[]{ ValueChangedEvent.class.getCanonicalName()});
		m.setParameterNames(new String[]{ "event" });
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onDataChanged", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}


	public static MethodModel createInitMenusMethod(ICodeGenerationContext context,ViewModelClass model, List<MenuModel> menus){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initMenus");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("menus", menus);
		String javaStatement = context.getTemplateRenderer().renderMacro("initMenus", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createInitBeanUpdatersMethod(ICodeGenerationContext context,ViewModelClass model, List<BeanBindingModel> beanBindings){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initBeanUpdaters");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("bindings", beanBindings);
		String javaStatement = context.getTemplateRenderer().renderMacro("initBeanUpdaters", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createUpdateBindingBeansMethod(ICodeGenerationContext context,ViewModelClass model, List<BeanBindingModel> beanBindings){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		model.addImport(ModelUtils.class.getCanonicalName());
		model.addImport(IBindableBean.class.getCanonicalName());
		model.addImport(DomainValueChangedEventImpl.class.getCanonicalName());
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("updateBindingBeans");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("bindings", beanBindings);
		String javaStatement = context.getTemplateRenderer().renderMacro("updateBindingBeans", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	
	public static MethodModel createInjectServicesMethod(ICodeGenerationContext context,ViewModelClass model, List<BeanBindingModel> serviceBindings){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("injectServices");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("bindings", serviceBindings);
		String javaStatement = context.getTemplateRenderer().renderMacro("injectServices", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	public static MethodModel createInitAttributesMethod(ICodeGenerationContext context,ViewModelClass model, List<ExpressionModel> expressions){
		model.addImport(ELAttributeValueEvaluator.class.getCanonicalName());
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initAttributeUpdaters");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("expressions", expressions);
		String javaStatement = context.getTemplateRenderer().renderMacro("initAttributeUpdaters", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	
	public static MethodModel createRegisterBeansMethod(ICodeGenerationContext context,ViewModelClass model, List<BeanField> beans){
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("registerBeans");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("beans", beans);
		String javaStatement = context.getTemplateRenderer().renderMacro("registerBeans", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}



	public static MethodModel createInitCommandsMethod(ICodeGenerationContext context,ViewModelClass model, List<UICommandModel> cmds){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initCommands");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("commands", cmds);
		String javaStatement = context.getTemplateRenderer().renderMacro("initCommands", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static boolean isPrimitiveType(TypeMirror type){
		TypeKind kind = type.getKind();
		return (kind == TypeKind.BOOLEAN)||
				(kind == TypeKind.BYTE)||
				(kind == TypeKind.CHAR)||
				(kind == TypeKind.DOUBLE)||
				(kind == TypeKind.FLOAT)||
				(kind == TypeKind.INT)||
				(kind == TypeKind.SHORT)||
				(kind == TypeKind.LONG);
	}

	public static FieldModel createDataFieldField(ICodeGenerationContext context,ViewModelClass model, DataFieldModel field){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		log.info("Field Type "+field.getType()+", field name :"+field.getName());
		String valType = new TypeModel(field.getType()).getType();
		if(valType.indexOf('.')<0){
			TypeKind kind = TypeKind.valueOf(valType.toUpperCase());
			if(kind != null){
				valType = typeUtil.boxedClass(typeUtil.getPrimitiveType(kind)).toString();
			}
		}
		TypeModel typeMode = new TypeModel(DataField.class.getCanonicalName(), new TypeModel[]{ new TypeModel(valType)});
//		TypeMirror parameterizedType = elemUtil.getTypeElement(field.getType()).asType();
//		TypeElement elem = elemUtil.getTypeElement(DataField.class.getCanonicalName());
//		String fieldType = typeUtil.getDeclaredType(elem, parameterizedType).toString();
		String name = field.getName()+"Field";
		FieldModel f = new FieldModel();
		f.setClassModel(model);
		f.setModifiers("private");
		f.setName(name);
		f.setType(typeMode.toSimpleName(model));
		return f;				
	}
	
	public static FieldModel createBeanUpdaterField(ICodeGenerationContext context,ViewModelClass model, BeanBindingModel binding){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		FieldModel field = binding.getField();
		log.info("Field Type "+field.getType()+", field name :"+field.getName());
		String valType = new TypeModel(field.getType()).getType();
		if(valType.indexOf('.')<0){
			TypeKind kind = TypeKind.valueOf(valType.toUpperCase());
			if(kind != null){
				valType = typeUtil.boxedClass(typeUtil.getPrimitiveType(kind)).toString();
			}
		}
		TypeModel typeMode = new TypeModel(ELBeanValueEvaluator.class.getCanonicalName(), new TypeModel[]{ new TypeModel(valType)});
//		TypeMirror parameterizedType = elemUtil.getTypeElement(field.getType()).asType();
//		TypeElement elem = elemUtil.getTypeElement(DataField.class.getCanonicalName());
//		String fieldType = typeUtil.getDeclaredType(elem, parameterizedType).toString();
		String name = field.getName()+"Updater";
		FieldModel f = new FieldModel();
		f.setClassModel(model);
		f.setModifiers("private");
		f.setName(name);
		f.setType(typeMode.toSimpleName(model));
		return f;				
	}

	
	public static void addField(ICodeGenerationContext context,ViewModelClass model, Element elem){
		if((elem.getKind() != ElementKind.FIELD)||(isFieldTypeOfDataField(context, elem))){
			return;
		}
		Field field = elem.getAnnotation(Field.class);
		Menu menu = elem.getAnnotation(Menu.class);
		ViewGroup vg = elem.getAnnotation(ViewGroup.class);
		Bean bean = elem.getAnnotation(Bean.class);
		if(field != null){
			if(isFieldAnnotationApplyable(context, elem)){
				model.addField(createDataFieldModel(context,model,elem,field));
			}else{
				log.error("@Field annotation is applyable on member field with type of IDataField !!!");
			}
		}else if(menu != null){
			model.addField(createMenuModel(context,elem,menu));
		}else if(vg != null){
			model.addField(createViewGroupModel(context,elem,vg));
		}else if(bean != null){
			model.addField(createBeanFieldModel(context,model,elem,bean));
		}else{
			model.addField(createSimpleFieldModel(context,elem));
		}
	}
	
	public static FieldModel updateBasicFieldModel(ICodeGenerationContext context,FieldModel model,Element elem){
		model.setName(elem.getSimpleName().toString());
		model.setType(elem.asType().toString());
		VariableTree variableTree = (VariableTree)context.getTrees().getTree(elem);
		if(variableTree.getInitializer() != null){
			model.setInitializer(variableTree.getInitializer().toString());
		}
		return model;
	}

	public static FieldModel createSimpleFieldModel(ICodeGenerationContext context,Element elem){
		return updateBasicFieldModel(context, new FieldModel(), elem);
	}

	public static DataFieldModel createDataFieldModel(ICodeGenerationContext context,ViewModelClass vModel,Element elem,Field field){
		DataFieldModel model = new DataFieldModel();
		String s = null;
		if((s = StringUtils.trimToNull(field.binding())) != null){
			FieldBindingModel binding = new FieldBindingModel();
			binding.setExpression(s);
			model.setBinding(binding);
		}
		String key = field.valueKey();
		if(StringUtils.isNotBlank(key)){
			if(key.indexOf('.') < 0){
				vModel.addImport(AttributeKeys.class.getCanonicalName());
				key = "AttributeKeys."+key;
			}else if(key.charAt(0) == '.'){
				key = vModel.getApplicationId()+key;
				int idx = key.lastIndexOf('.');
				String subKey = key.substring(idx);
				key = vModel.addImport(key.substring(0, idx))+subKey;
			}
			model.setValueKey(key);
		}
		updateFieldAttributes(context, elem, field.enableWhen(),field.visibleWhen(),field.attributes(), model);
		return model;
	}

	/**
	 * @param context
	 * @param elem
	 * @param field
	 * @param model
	 */
	protected static void updateFieldAttributes(ICodeGenerationContext context,
			Element elem, String enabledWhen,String visibleWhen, Attribute[] attrs, AttributedFieldModel model) {
		String s;
		if((s = StringUtils.trimToNull(enabledWhen)) != null){
			model.setEnableWhenExpress(s);
		}
		if((s = StringUtils.trimToNull(visibleWhen)) != null){
			model.setVisibleWhenExpress(s);
		}
		if(attrs != null){
			for (Attribute attr : attrs) {
				model.addAttribute(attr.name(), attr.value());
			}
		}
		updateBasicFieldModel(context, model, elem);
	}

	/**
	 * @param context
	 * @param elem
	 */
	protected static boolean isFieldAnnotationApplyable(ICodeGenerationContext context,
			Element elem) {
		return isFieldTypeOfDataField(context, elem) == false;
	}

	/**
	 * @param context
	 * @param elem
	 * @return
	 */
	public static boolean isFieldTypeOfDataField(
			ICodeGenerationContext context, Element elem) {
		Types types = context.getProcessingEnvironment().getTypeUtils();
		Elements elements = context.getProcessingEnvironment().getElementUtils();
		TypeElement dataFieldType = elements.getTypeElement("com.wxxr.mobile.core.ui.api.IDataField");
		if(types.isAssignable(elem.asType(), dataFieldType.asType())){
			return true;
		}
		return false;
	}
	
	public static MenuModel createMenuModel(ICodeGenerationContext context,Element elem,Menu menu){
		MenuModel model = new MenuModel();
		model.setItems(menu.items());
		updateFieldAttributes(context, elem, menu.enableWhen(),menu.visibleWhen(),menu.attributes(), model);
		String s = null;
		if((s = StringUtils.trimToNull(menu.enableWhen())) != null){
			model.setEnableWhenExpress(s);
		}
		if((s = StringUtils.trimToNull(menu.visibleWhen())) != null){
			model.setVisibleWhenExpress(s);
		}
		Attribute[] attrs = menu.attributes();
		if(attrs != null){
			for (Attribute attr : attrs) {
				model.addAttribute(attr.name(), attr.value());
			}
		}
		updateBasicFieldModel(context, model, elem);
		return model;
	}
	
	public static BeanField createBeanFieldModel(ICodeGenerationContext context,ViewModelClass vModel, Element elem,Bean bean){
		BeanField model = new BeanField();
		model.setBeanType(bean.type());
		model.setNullable(bean.nullable());
		model.setValueExpression(bean.express());
		updateBasicFieldModel(context, model, elem);
		if((bean.type() == BindingType.Service)||StringUtils.isNotBlank(bean.express())){
			BeanBindingModel binding = new BeanBindingModel();
			binding.setExpression(StringUtils.trimToNull(bean.express()));
			binding.setField(model);
			binding.setType(bean.type());
			binding.setNullable(bean.nullable());
			vModel.addBeanBinding(binding);
		}
		return model;
	}

	public static ViewGroupModel createViewGroupModel(ICodeGenerationContext context,Element elem,ViewGroup vg){
		ViewGroupModel model = new ViewGroupModel();
		model.setViewIds(vg.viewIds());
		String defaultId = StringUtils.trimToNull(vg.defaultViewId());
		if(defaultId == null){
			defaultId = model.getViewIds()[0];
		}
		model.setDefaultViewId(defaultId);
		updateFieldAttributes(context, elem, vg.enableWhen(), vg.visibleWhen(), vg.attributes(), model);
		return model;
	}
	
	public static String getELExpression(String expression) {
		if((expression.startsWith("${")||expression.startsWith("#{"))&&expression.endsWith("}")){
			return expression.substring(2, expression.length()-1);
		}
		return null;
	}

	public static void addMethod(ICodeGenerationContext context,ViewModelClass model, Element elem){
		if(elem.getKind() != ElementKind.METHOD){
			return;
		}
		ExecutableElement exec = (ExecutableElement)elem;
		MethodModel m = new MethodModel();
		m.setMethodBody(context.getTrees().getTree(exec).getBody().toString());
		m.setMethodName(exec.getSimpleName().toString());
		TypeMirror returnType = exec.getReturnType();
		if((returnType.getKind() != TypeKind.NONE)&&(returnType.getKind() != TypeKind.VOID)){
			m.setReturnType(returnType.toString());
		}
		Set<Modifier> mods = exec.getModifiers();
		if((mods != null)&&(mods.size() > 0)){
			StringBuffer buf = new StringBuffer();
			int cnt = 0;
			for (Modifier mod : mods) {
				if(cnt > 0){
					buf.append(' ');
				}
				buf.append(mod.toString());
				cnt++;
			}
			m.setModifiers(buf.toString());
		}
//		List<? extends TypeParameterElement> types = exec.getTypeParameters();
//		if((types != null)&&(types.size() > 0)){
//			String[] pTypes = new String[types.size()];
//			int cnt = 0;
//			for (TypeParameterElement typeParameterElement : types) {
//				pTypes[cnt++] = typeParameterElement.asType().toString();
//			}
//			m.setParameterTypes(pTypes);
//		}
		List<? extends VariableElement> vars = exec.getParameters();
		if((vars != null)&&(vars.size() > 0)){
			String[] pVars = new String[vars.size()];
			String[] pTypes = new String[vars.size()];
			int cnt = 0;
			for (VariableElement var : vars) {
				pVars[cnt] = var.getSimpleName().toString();
				pTypes[cnt++] = var.asType().toString();
			}
			m.setParameterNames(pVars);
			m.setParameterTypes(pTypes);
		}
		List<? extends TypeMirror> tTypes = exec.getThrownTypes();
		if((tTypes != null)&&(tTypes.size() > 0)){
			String[] pVars = new String[vars.size()];
			int cnt = 0;
			for (TypeMirror var : tTypes) {
				pVars[cnt++] = var.toString();
			}
			m.setThrownTypes(pVars);
		}
		if((elem.getAnnotation(OnCreate.class) != null)&&(((m.getParameterTypes() == null))||(m.getParameterTypes().length == 0))){
			m.setPhase(LifeCyclePhase.OnCreate);
		}else if((elem.getAnnotation(OnShow.class) != null)&&(((m.getParameterTypes() == null))||((m.getParameterTypes().length == 1)&&m.getParameterTypes()[0].startsWith(IBinding.class.getCanonicalName())))){
			model.addImport(IBinding.class.getCanonicalName());
			model.addImport(IView.class.getCanonicalName());
			m.setPhase(LifeCyclePhase.OnShow);
		}else if((elem.getAnnotation(OnHide.class) != null)&&(((m.getParameterTypes() == null))||((m.getParameterTypes().length == 1)&&m.getParameterTypes()[0].startsWith(IBinding.class.getCanonicalName())))){
			model.addImport(IBinding.class.getCanonicalName());
			model.addImport(IView.class.getCanonicalName());
			m.setPhase(LifeCyclePhase.OnHide);
		}else if((elem.getAnnotation(OnDestroy.class) != null)&&(((m.getParameterTypes() == null))||(m.getParameterTypes().length == 0))){
			m.setPhase(LifeCyclePhase.OnDestroy);
		}else if((elem.getAnnotation(OnDataChanged.class) != null)&&(((m.getParameterTypes() == null))||((m.getParameterTypes().length == 1)&&m.getParameterTypes()[0].startsWith(ValueChangedEvent.class.getCanonicalName())))){
			model.addImport(ValueChangedEvent.class.getCanonicalName());
			m.setPhase(LifeCyclePhase.OnDataChanged);
		}else if((elem.getAnnotation(OnMenuShow.class) != null)&&(((m.getParameterTypes() == null))||((m.getParameterTypes().length == 1)&&m.getParameterTypes()[0].equals(String.class.getCanonicalName())))){
			m.setPhase(LifeCyclePhase.OnMenuShow);
		}else if((elem.getAnnotation(OnMenuHide.class) != null)&&(((m.getParameterTypes() == null))||((m.getParameterTypes().length == 1)&&m.getParameterTypes()[0].equals(String.class.getCanonicalName())))){
			m.setPhase(LifeCyclePhase.OnMenuHide);
		}
		model.addMethod(m);
		processCommandAnnotation(model, elem, m);
//		model.addField(elem.getSimpleName().toString(), elem.asType().toString());
	}

	protected static void processProgressGuard(ViewModelClass model,Element elem, UICommandModel m){
		ExeGuard ann = elem.getAnnotation(ExeGuard.class);
		if(ann != null){
			SimpleProgressGuard guard = new SimpleProgressGuard();
			guard.setSilentPeriod(ann.silentPeriod());
			guard.setCancellable(ann.cancellable());
			if(StringUtils.isNotBlank(ann.message())){
				guard.setMessage(ann.message());
			}
			if(StringUtils.isNotBlank(ann.title())){
				guard.setTitle(ann.title());
			}
			if(StringUtils.isNotBlank(ann.sign())){
				guard.setIcon(ann.sign());
			}
			m.setProgressGuard(guard);
		}
	}
	/**
	 * @param model
	 * @param elem
	 * @param m
	 */
	protected static void processCommandAnnotation(ViewModelClass model,
			Element elem, MethodModel m) {
		Command ann = elem.getAnnotation(Command.class);
		if(ann != null){
			UICommandModel cmdModel = new UICommandModel();
			cmdModel.setMethodName(m.getName());
			if(!StringUtils.isBlank(ann.commandName())){
				cmdModel.setName(ann.commandName());
			}else{
				cmdModel.setName(m.getName());
			}
			if(!StringUtils.isBlank(ann.description())){
				cmdModel.setDescription(ann.description());
			}
			if(!StringUtils.isBlank(ann.enableWhen())){
				cmdModel.setEnabledWhenExpress(ann.enableWhen());
			}
			if(!StringUtils.isBlank(ann.visibleWhen())){
				cmdModel.setVisibleWhenExpress(ann.visibleWhen());
			}
			model.addCommandModel(cmdModel);
			processProgressGuard(model, elem, cmdModel);
			Navigation[] navs = ann.navigations();
			if(navs != null){
				for (Navigation nav : navs) {
					NavigationModel nModel = new NavigationModel();
					nModel.setResult(nav.on());
					if(!StringUtils.isBlank(nav.message())){
						nModel.setMessage(nav.message());
					}
					if(!StringUtils.isBlank(nav.showPage())){
						nModel.setToPage(nav.showPage());
					}
					if(!StringUtils.isBlank(nav.showView())){
						nModel.setToView(nav.showView());
					}
					if(!StringUtils.isBlank(nav.showDialog())){
						nModel.setToDialog(nav.showDialog());
					}
					nModel.setCloseCurrentView(nav.closeCurrentView());
					Parameter[] params = nav.params();
					if(params != null){
						for (int i = 0; i < params.length; i++) {
							com.wxxr.mobile.tools.model.Parameter p = new com.wxxr.mobile.tools.model.Parameter();
							p.setName(params[i].name());
							p.setValue(params[i].value());
							nModel.addParameter(p);
						}
					}
					cmdModel.addNavigation(nModel);
				}
			}
			UIItem[] items = ann.uiItems();
			if(items != null){
				for (UIItem uiItem : items) {
					MenuItemModel mItem = new MenuItemModel();
					mItem.setName(uiItem.id());
					mItem.setLabel(uiItem.label());
					if(!StringUtils.isBlank(uiItem.icon())){
						mItem.setIcon(uiItem.icon());
					}
					if(!StringUtils.isBlank(uiItem.description())){
						mItem.setDescription(uiItem.description());
					}
					if(!StringUtils.isBlank(uiItem.enableWhen())){
						mItem.setEnableWhen(uiItem.enableWhen());
					}
					if(!StringUtils.isBlank(uiItem.visibleWhen())){
						mItem.setVisibleWhen(uiItem.visibleWhen());
					}
					cmdModel.addMenuItem(mItem);
				}
			}
		}
	}

	public static String getElementText(ICodeGenerationContext context, Element elem){
		StringWriter sw = new StringWriter();
		context.getProcessingEnvironment().getElementUtils().printElements(sw, elem);
		try {
			sw.close();
		} catch (IOException e) {
		}
		return sw.toString();
	}
}
