/**
 * 
 */
package com.wxxr.mobile.core.tools.generator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import javax.lang.model.type.*;
import javax.lang.model.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.source.tree.*;
import com.sun.source.util.*;

import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
@SuppressWarnings({ "unused", "restriction" })
public abstract class ViewModelUtils {
	private static final Logger log = LoggerFactory.getLogger(UIViewModelGenerator.class);
 
	public static void addField(ICodeGenerationContext context,ViewModelClass model, Element elem){
		if((elem.getKind() != ElementKind.FIELD)||(isFieldTypeOfDataField(context, elem))){
			return;
		}
		Field field = elem.getAnnotation(Field.class);
		Menu menu = elem.getAnnotation(Menu.class);
		ViewGroup vg = elem.getAnnotation(ViewGroup.class);
		if(field != null){
			if(isFieldAnnotationApplyable(context, elem)){
				model.addField(createDataFieldModel(context,elem,field));
			}else{
				log.error("@Field annotation is applyable on member field with type of IDataField !!!");
			}
		}else if(menu != null){
			model.addField(createMenuModel(context,elem,menu));
		}else if(vg != null){
			model.addField(createViewGroupModel(context,elem,vg));
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

	public static DataFieldModel createDataFieldModel(ICodeGenerationContext context,Element elem,Field field){
		DataFieldModel model = new DataFieldModel();
		String s = null;
		if((s = StringUtils.trimToNull(field.enableWhen())) != null){
			model.setEnableWhenExpress(s);
		}
		if((s = StringUtils.trimToNull(field.visibleWhen())) != null){
			model.setVisibleWhenExpress(s);
		}
		model.setValueKey(field.valueKey());
		Attribute[] attrs = field.attributes();
		if(attrs != null){
			for (Attribute attr : attrs) {
				model.addAttribute(attr.name(), attr.value());
			}
		}
		updateBasicFieldModel(context, model, elem);
		return model;
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
		String s = null;
		if((s = StringUtils.trimToNull(menu.enableWhen())) != null){
			model.setEnableWhenExpress(s);
		}
		if((s = StringUtils.trimToNull(menu.visibleWhen())) != null){
			model.setVisibleWhenExpress(s);
		}
		model.setItems(menu.items());
		Attribute[] attrs = menu.attributes();
		if(attrs != null){
			for (Attribute attr : attrs) {
				model.addAttribute(attr.name(), attr.value());
			}
		}
		updateBasicFieldModel(context, model, elem);
		return model;
	}
	
	public static ViewGroupModel createViewGroupModel(ICodeGenerationContext context,Element elem,ViewGroup vg){
		ViewGroupModel model = new ViewGroupModel();
		String s = null;
		if((s = StringUtils.trimToNull(vg.enableWhen())) != null){
			model.setEnableWhenExpress(s);
		}
		if((s = StringUtils.trimToNull(vg.visibleWhen())) != null){
			model.setVisibleWhenExpress(s);
		}
		model.setViewIds(vg.viewIds());
		Attribute[] attrs = vg.attributes();
		if(attrs != null){
			for (Attribute attr : attrs) {
				model.addAttribute(attr.name(), attr.value());
			}
		}
		updateBasicFieldModel(context, model, elem);
		return model;
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
		model.addMethod(m);
		Command ann = elem.getAnnotation(Command.class);
		if(ann != null){
			UICommandModel cmdModel = new UICommandModel();
			cmdModel.setMethodName(m.getMethodName());
			if(!StringUtils.isBlank(ann.commandName())){
				cmdModel.setCommandName(ann.commandName());
			}else{
				cmdModel.setCommandName(m.getMethodName());
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
					Parameter[] params = nav.params();
					if(params != null){
						for (int i = 0; i < params.length; i++) {
							com.wxxr.mobile.core.tools.generator.Parameter p = new com.wxxr.mobile.core.tools.generator.Parameter();
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
					mItem.setId(uiItem.id());
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
//		model.addField(elem.getSimpleName().toString(), elem.asType().toString());
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
