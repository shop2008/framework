/**
 * 
 */
package com.wxxr.mobile.tools.model;

import static com.wxxr.mobile.tools.model.ViewModelUtils.addField;
import static com.wxxr.mobile.tools.model.ViewModelUtils.addMethod;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ext.DeclHandler;

import com.sun.source.tree.ImportTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.NetworkConstraintLiteral;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraintLiteral;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;
import com.wxxr.mobile.core.ui.annotation.Convertor;
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
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.annotation.Workbench;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IValueConvertor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.DomainValueChangedEventImpl;
import com.wxxr.mobile.core.ui.common.ELAttributeValueEvaluator;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.ModelUtils;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.ui.common.SimpleProgressGuard;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
@SuppressWarnings({ "unused", "restriction" })
public abstract class ViewModelUtils {
	private static final Logger log = LoggerFactory.getLogger(UIViewModelGenerator.class);
 
	public static WorkbenchModel createWorkbenchModel(ICodeGenerationContext context,TypeElement typeElem,Workbench ann){
		WorkbenchModel model = new WorkbenchModel();
		String pkg = null;
		String elementFQN = typeElem.getQualifiedName().toString();
		model.setWorkbenchClass(elementFQN);
		int idx = elementFQN.lastIndexOf('.');
		String defaultPkg ="";
		String defaultName = elementFQN;
		if(idx > 0){
			defaultPkg = elementFQN.substring(0,idx);
			defaultName = elementFQN.substring(idx+1);
		}
		if(StringUtils.isBlank(pkg)){
			pkg = defaultPkg;
		}
		if(pkg.endsWith(".model")){
			pkg = pkg.substring(0,pkg.length()-6);
		}
		model.setPkgName(pkg+".view");
		model.setName(defaultName+"Descriptor");
		if(StringUtils.isNotBlank(ann.description())){
			model.setDescription(ann.description());
		}
		if(StringUtils.isNotBlank(ann.title())){
			model.setTitle(ann.title());
		}
		Navigation[] navs = ann.exceptionNavigations();
		log.info("Found workbench default navigations , size :"+(navs != null ? navs.length : 0));
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
				nModel.setKeepMenuOpen(nav.keepMenuOpen());
				Parameter[] params = nav.params();
				if(params != null){
					for (int i = 0; i < params.length; i++) {
						com.wxxr.mobile.tools.model.Parameter p = new com.wxxr.mobile.tools.model.Parameter();
						p.setName(params[i].name());
						p.setValue(params[i].value());
						nModel.addParameter(p);
					}
				}
				model.addNavigation(nModel);
			}
		}
		return model;
	}
	
	public static ViewModelClass createViewModelClass(ICodeGenerationContext context,TypeElement typeElem,View ann) {
		String pkg = null; //
//		
//		UIViewModelSourceScanner scanner = new UIViewModelSourceScanner();
		ViewModelClass model = new ViewModelClass(context);
		String elementFQN = typeElem.getQualifiedName().toString();
//		log.warn("Class source :"+context.getTrees().getTree(element));
//		scanner.scan(treePath, context);
		int idx = elementFQN.lastIndexOf('.');
		String defaultPkg ="";
		String defaultName = elementFQN;
		if(idx > 0){
			defaultPkg = elementFQN.substring(0,idx);
			defaultName = elementFQN.substring(idx+1);
		}
		if(StringUtils.isBlank(pkg)){
			pkg = defaultPkg;
		}
		if(pkg.endsWith(".model")){
			pkg = pkg.substring(0,pkg.length()-6);
		}
		model.setApplicationId(pkg);
		model.setPkgName(pkg+".view");
		model.setDefaultName(defaultName);
		model.setName(defaultName+"Model");
		if(StringUtils.isNotBlank(ann.description())){
			model.setDescription(ann.description());
		}
		if((ann.alias() != null)&&(ann.alias().length > 0)){
			model.setAlias(ann.alias());
		}
		model.setToolbarRequired(ann.withToolbar());
		model.setSingleton(ann.singleton());
		model.setProvideSelection(ann.provideSelection());
		if(StringUtils.isNotBlank(ann.name())){
			model.setId(StringUtils.trimToNull(ann.name()));
		}else{
			model.setId(defaultName);
		}
		TreePath treePath = context.getTrees().getPath(context.getProcessingEnvironment().getElementUtils().getTypeElement(elementFQN));
		List<? extends ImportTree> imports = treePath.getCompilationUnit().getImports();
		if(imports != null){
			for (ImportTree importTree : imports) {
				String importClass = importTree.getQualifiedIdentifier().toString();
				TypeElement importElem = context.getProcessingEnvironment().getElementUtils().getTypeElement(importClass);
				if((importElem != null)&&(importElem.getKind() == ElementKind.ANNOTATION_TYPE)){
					continue;
				}
				model.addImport(importClass);
			}
		}
		TypeMirror superClass = typeElem.getSuperclass();
		Types util = context.getProcessingEnvironment().getTypeUtils();
		TypeMirror type = typeElem.asType();
		TypeMirror pageType = context.getProcessingEnvironment().getElementUtils().getTypeElement(IPage.class.getCanonicalName()).asType();
		model.setPage(util.isAssignable(type, pageType));
		if(!util.isSameType(util.getNoType(TypeKind.NONE),superClass)){
			model.setSuperClass(((TypeElement)util.asElement(superClass)).getQualifiedName().toString());
		}else{
			model.setSuperClass(PageBase.class.getCanonicalName());
		}
		List<? extends TypeMirror> interfaces = typeElem.getInterfaces();
		if(interfaces != null){
			for (TypeMirror typeMirror : interfaces) {
				model.addInterface(((TypeElement)util.asElement(typeMirror)).getQualifiedName().toString());
			}
		}
		Navigation[] navs = ann.exceptionNavigations();
		if((navs != null)&&(navs.length > 0)){
			log.info("Found view exception navigations , size :"+(navs != null ? navs.length : 0));
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
				nModel.setKeepMenuOpen(nav.keepMenuOpen());
				Parameter[] params = nav.params();
				if(params != null){
					for (int i = 0; i < params.length; i++) {
						com.wxxr.mobile.tools.model.Parameter p = new com.wxxr.mobile.tools.model.Parameter();
						p.setName(params[i].name());
						p.setValue(params[i].value());
						nModel.addParameter(p);
					}
				}
				model.addNavigation(nModel);
			}
		}

		List<? extends Element> children = typeElem.getEnclosedElements();
		if(children != null){
			for (Element child : children) {
				switch(child.getKind()){
				case FIELD:
					addField(context,model,child);
					break;
				case METHOD:
					addMethod(context,model, child);
					break;
				}
			}
		}
		return model;
	}


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
	
	public static MethodModel createOnUIDestroyMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onContentViewDestroy");
		m.setModifiers("public");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onUIDestroy", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}

	public static MethodModel createOnUICreateMethod(ICodeGenerationContext context,ViewModelClass model, List<MethodModel> methods){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("onContentViewCreated");
		m.setModifiers("public");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("methods", methods);
		String javaStatement = context.getTemplateRenderer().renderMacro("onUICreate", attrs, null);
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
	
	public static MethodModel createInitConvertorsMethod(ICodeGenerationContext context,ViewModelClass model, List<ConvertorField> convs){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("initConvertors");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("convertors", convs);
		String javaStatement = context.getTemplateRenderer().renderMacro("initConvertors", attrs, null);
		m.setJavaStatement(javaStatement);
		return m;
	}
	
	public static MethodModel createDestroyConvertorsMethod(ICodeGenerationContext context,ViewModelClass model, List<ConvertorField> convs){
		Types typeUtil = context.getProcessingEnvironment().getTypeUtils();
		Elements elemUtil = context.getProcessingEnvironment().getElementUtils();
		MethodModel m = new MethodModel();
		m.setClassModel(model);
		m.setMethodName("destroyConvertors");
		m.setModifiers("private");
		m.setReturnType("void");
		Map<String, Object> attrs = new HashMap<String, Object>();
		attrs.put("model", model);
		attrs.put("convertors", convs);
		String javaStatement = context.getTemplateRenderer().renderMacro("destroyConvertors", attrs, null);
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
//		log.info("Field Type "+field.getType()+", field name :"+field.getName());
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
		Convertor conv = elem.getAnnotation(Convertor.class);
		if(field != null){
			if(isFieldAnnotationApplyable(context, elem)){
				FieldModel m = createDataFieldModel(context,model,elem,field,false);
				if(m != null){
					model.addField(m);
				}
			}else{
				log.error("@Field annotation is applyable on member field with type of IDataField !!!");
			}
		}else if(menu != null){
			model.addField(createMenuModel(context,elem,menu));
		}else if(vg != null){
			model.addField(createViewGroupModel(context,elem,vg));
		}else if(conv != null){
			model.addField(createConvertorModel(context,model,elem,conv));
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

	public static DataFieldModel createDataFieldModel(ICodeGenerationContext context,ViewModelClass vModel,Element elem,Field field, boolean failIfUnresolved){
		String convName = StringUtils.trimToNull(field.converter());
		ConvertorField conv = (ConvertorField)vModel.getField(convName);
		if((convName != null)&&(conv == null)){
			if(failIfUnresolved){
				throw new IllegalArgumentException("Cannot found converter named :"+convName);
			}else{
				vModel.addUnresolvedField(elem, field);
				return null;
			}
		}
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
		if(conv != null){
			model.setConvertor(conv);
		}
		updateFieldAttributes(context, elem, field.enableWhen(),field.visibleWhen(),field.attributes(), model);
		return model;
	}
	
	public static ConvertorField createConvertorModel(ICodeGenerationContext context,ViewModelClass vModel,Element elem,Convertor conv){
		ConvertorField model = new ConvertorField();
		model.setClassModel(vModel);
		String s = null;
		if((s = StringUtils.trimToNull(conv.className())) != null){
			model.setClassName(s);
		}
		if(conv.params() != null){
			for (Parameter p : conv.params()) {
				model.addParameter(p.name(),p.value(),p.type());
			}
		}
		updateBasicFieldModel(context,model,elem);
		DeclaredType fieldType = (DeclaredType)elem.asType();
		List<? extends TypeMirror> types = getParameterTypesOfConvertor(context, fieldType);
		if(types != null){
			model.setTargetValueType(types.get(1).toString());
			model.setSourceValueType(types.get(0).toString());
			log.info("Find convertor field :"+model);			
		}else{
			throw new IllegalArgumentException("Invalid convertor field type :["+fieldType+"]");
		}
		return model;
	}
	
	
	private static List<? extends TypeMirror> getParameterTypesOfConvertor(ICodeGenerationContext context,DeclaredType type){
		log.info("Convertor field type :["+type.getClass()+"]/"+type);
//		if(context.getProcessingEnvironment().getTypeUtils().isSubtype(type, interfaceType) == false){
//			return null;
//		}
		List<? extends TypeMirror> types = type.getTypeArguments();
		if((types != null)&&(types.size() == 2)){
			return types;
		}
		List<? extends TypeMirror> superTypes = context.getProcessingEnvironment().getTypeUtils().directSupertypes(type);
		if(superTypes != null){
			for (TypeMirror typeMirror : superTypes) {
				if(typeMirror instanceof DeclaredType){
					types = getParameterTypesOfConvertor(context, (DeclaredType)typeMirror);
					if(types != null){
						return types;
					}
				}
			}
		}
		return null;
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
		}else if((elem.getAnnotation(OnUIDestroy.class) != null)&&(((m.getParameterTypes() == null))||(m.getParameterTypes().length == 0))){
			m.setPhase(LifeCyclePhase.OnUIDestroy);
		}else if((elem.getAnnotation(OnUICreate.class) != null)&&(((m.getParameterTypes() == null))||(m.getParameterTypes().length == 0))){
			m.setPhase(LifeCyclePhase.OnUICreate);
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

	protected static void processSecurityConstraint(ViewModelClass model,Element elem, UICommandModel m){
		SecurityConstraint ann = elem.getAnnotation(SecurityConstraint.class);
		if(ann != null){
			m.setSecurityConstraint(SecurityConstraintLiteral.fromAnnotation(ann));
		}
	}

	protected static void processNetworkConstraint(ViewModelClass model,Element elem, UICommandModel m){
		NetworkConstraint ann = elem.getAnnotation(NetworkConstraint.class);
		if(ann != null){
			m.setNetworkConstraint(NetworkConstraintLiteral.fromAnnotation(ann));
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
			processSecurityConstraint(model, elem, cmdModel);
			processNetworkConstraint(model, elem, cmdModel);
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
					nModel.setKeepMenuOpen(nav.keepMenuOpen());
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
