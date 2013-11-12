/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.tools.ICodeGenerationContext;
import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;
import com.wxxr.mobile.core.tools.generator.ViewModelDomainResolver;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.ELDomainValueModel;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class ViewModelClass extends AbstractClassModel {
	private static final Logger log = LoggerFactory.getLogger(ViewModelClass.class);

	private Map<String,UICommandModel> commandModels;
	private Map<String, ExpressionModel> expressions;
	private Map<String, BeanBindingModel> beanBindings;
	boolean traceRequired,toolbarRequired;
	private String applicationId;
	private String id;
	private boolean isPage;
	private String description;
//	private final ViewModelDomainResolver domainResolver;
	private final ICodeGenerationContext context;
	
	public ViewModelClass(ICodeGenerationContext ctx){
//		domainResolver = new ViewModelDomainResolver(ctx);
		this.context = ctx;
	}
	
	public List<UICommandModel> getCommands() {
		return commandModels == null ? null : new ArrayList<UICommandModel>(commandModels.values());
	}
	
	public List<ExpressionModel> getExpressions() {
		return expressions == null ? null : new ArrayList<ExpressionModel>(expressions.values());
	}

	public List<BeanBindingModel> getBeanBindings() {
		if(this.beanBindings == null){
			return null;
		}
		List<BeanBindingModel> result = new ArrayList<BeanBindingModel>();
		for (BeanBindingModel m : this.beanBindings.values()) {
			if(m.getType() == BindingType.Pojo){
				result.add(m);
			}
		}
		return result.isEmpty() ? null : result;
	}
	
	public List<BeanBindingModel> getServiceBindings() {
		if(this.beanBindings == null){
			return null;
		}
		List<BeanBindingModel> result = new ArrayList<BeanBindingModel>();
		for (BeanBindingModel m : this.beanBindings.values()) {
			if(m.getType() == BindingType.Service){
				result.add(m);
			}
		}
		return result.isEmpty() ? null : result;
	}


	
	public List<MenuModel> getMenus() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<MenuModel> menus = new ArrayList<MenuModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof MenuModel){
				menus.add((MenuModel)field);
			}
		}
		return menus.isEmpty() ? null : menus;
	}

	
	public List<DataFieldModel> getDataFields() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<DataFieldModel> result = new ArrayList<DataFieldModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof DataFieldModel){
				result.add((DataFieldModel)field);
			}
		}
		return result.isEmpty() ? null : result;
	}

	
	public List<ViewGroupModel> getViewGroups() {
		if((this.fields == null)||(this.fields.size() == 0)){
			return null;
		}
		ArrayList<ViewGroupModel> result = new ArrayList<ViewGroupModel>();
		for (FieldModel field : this.fields.values()) {
			if(field instanceof ViewGroupModel){
				result.add((ViewGroupModel)field);
			}
		}
		return result.isEmpty() ? null : result;
	}

	public void addField(String name, Class<?> type){
		name = StringUtils.trimToNull(name);
		if((name == null)||(type == null)){
			throw new IllegalArgumentException("name and type cannot be NULL !");
		}
		addField(name, type.getCanonicalName());
	}
	
	public FieldModel getField(String name){
		return this.fields != null ? this.fields.get(name) : null;
	}
	
	public boolean isTraceRequired() {
		return traceRequired;
	}
	
	public List<MethodModel> getOnCreateMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnCreate);
	}

	public List<MethodModel> getOnHideMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnHide);
	}
	
	public List<MethodModel> getOnShowMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnShow);
	}
	
	public List<MethodModel> getOnDestroyedMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnDestroy);
	}
	
	public List<MethodModel> getOnMenuShowMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnMenuShow);
	}
	
	public List<MethodModel> getOnDataChangedMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnDataChanged);
	}

	public List<MethodModel> getOnMenuHideMethods() {
		return getLifeCycleMethods(LifeCyclePhase.OnMenuHide);
	}
	
	/**
	 * @return
	 */
	protected List<MethodModel> getLifeCycleMethods(LifeCyclePhase phase) {
		List<MethodModel> meths = getMethods();
		if((meths == null)||(meths.size() == 0)){
			return null;
		}
		LinkedList<MethodModel> result = new LinkedList<MethodModel>();
		for (MethodModel m : meths) {
			if(m.getPhase() == phase){
				result.add(m);
			}
		}
		return result.isEmpty() ? null : result;
	}
	

	
	public void addCommandModel(UICommandModel cmd){
		if(this.commandModels == null){
			this.commandModels = new HashMap<String, UICommandModel>();
		}
		cmd.setClassModel(this);
		this.commandModels.put(cmd.getName(), cmd);
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void prepare(ICodeGenerationContext context) {
		List<DataFieldModel> dFields = getDataFields();
		if(dFields != null){
			for (DataFieldModel f : dFields) {
				String name = f.getName()+"Field";
				if(getField(name) == null){
					FieldModel field = ViewModelUtils.createDataFieldField(context, this, f);
					addField(field);
				}
			}
		}
		boolean hasBeanBindings = false;
		List<BeanBindingModel> bbindings = getBeanBindings();
		if((bbindings != null)&&(bbindings.size() > 0)){
			for (BeanBindingModel beanBinding : bbindings) {
				String name = beanBinding.getField().getName()+"Updater";
				if(getField(name) == null){
					FieldModel field = ViewModelUtils.createBeanUpdaterField(context, this, beanBinding);
					addField(field);
				}
			}
			MethodModel m = ViewModelUtils.createInitBeanUpdatersMethod(context, this, bbindings);
			addMethod(m);
			m = ViewModelUtils.createUpdateBindingBeansMethod(context, this, bbindings);
			addMethod(m);
			hasBeanBindings = true;
		}
		
		List<BeanBindingModel> serviceBindings = getServiceBindings();
		int size = (serviceBindings != null) ? serviceBindings.size() : 0;
		if(size > 0){
			MethodModel m = ViewModelUtils.createInjectServicesMethod(context, this, serviceBindings);
			addMethod(m);
		}
		
		List<ExpressionModel> expressions = getExpressions();
		int expSize = (expressions != null) ? expressions.size() : 0;
		if(expSize > 0){
			MethodModel m = ViewModelUtils.createInitAttributesMethod(context, this, expressions);
			addMethod(m);
		}

		List<UICommandModel> cmds = getCommands();
		if((cmds != null)&&(cmds.size() > 0)){
			MethodModel m = ViewModelUtils.createInitCommandsMethod(context, this, cmds);
			addMethod(m);
		}

		List<MenuModel> menus = getMenus();
		if((menus != null)&&(menus.size() > 0)){
			MethodModel m = ViewModelUtils.createInitMenusMethod(context, this, menus);
			addMethod(m);
		}
		
		List<DataFieldModel> dataFields = getDataFields();
		if((dataFields != null)&&(dataFields.size() > 0)){
			addImport(ELDomainValueModel.class.getCanonicalName());
			MethodModel m = ViewModelUtils.createInitDataFieldsMethod(context, this, dataFields);
			addMethod(m);
		}	
		
		List<ViewGroupModel> vGroups = getViewGroups();
		if((vGroups != null)&&(vGroups.size() > 0)){
			MethodModel m = ViewModelUtils.createInitViewGroupsMethod(context, this, vGroups);
			addMethod(m);
		}
		List<MethodModel> createMethods = getOnCreateMethods();
		if((createMethods != null)&&(createMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnCreateMethod(context, this, createMethods);
			addMethod(m);
		}

		List<MethodModel> destroyMethods = getOnDestroyedMethods();
		if((destroyMethods != null)&&(destroyMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnDestroyMethod(context, this, destroyMethods);
			addMethod(m);
		}

		List<MethodModel> onShowMethods = getOnShowMethods();
		if(hasBeanBindings || ((onShowMethods != null)&&(onShowMethods.size() > 0))){
			addImport(IBinding.class.getCanonicalName());
			addImport(IView.class.getCanonicalName());
			MethodModel m = ViewModelUtils.createOnShowMethod(context, this, onShowMethods);
			addMethod(m);
		}

		List<MethodModel> onHideMethods = getOnHideMethods();
		if((onHideMethods != null)&&(onHideMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnHideMethod(context, this, onHideMethods);
			addMethod(m);
		}

		List<MethodModel> onDataChangedMethods = getOnDataChangedMethods();
		if((onDataChangedMethods != null)&&(onDataChangedMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnDataChangedMethod(context, this, onDataChangedMethods);
			addMethod(m);
		}

		List<MethodModel> onMenuShowMethods = getOnMenuShowMethods();
		if((onMenuShowMethods != null)&&(onMenuShowMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnMenuShowMethod(context, this, onMenuShowMethods);
			addMethod(m);
		}

		List<MethodModel> onMenuHideMethods = getOnMenuHideMethods();
		if((onMenuHideMethods != null)&&(onMenuHideMethods.size() > 0)){
			MethodModel m = ViewModelUtils.createOnMenuHideMethod(context, this, onMenuHideMethods);
			addMethod(m);
		}

		addMethod(ViewModelUtils.createInitMethod(context,this));
	}

	public void addExpression(ExpressionModel expr) {
		if(this.expressions == null){
			this.expressions = new HashMap<String, ExpressionModel>();
		}
		this.expressions.put(expr.getKey(), expr);
	}
	
	public void addBeanBinding(BeanBindingModel binding) {
		if(this.beanBindings == null){
			this.beanBindings = new HashMap<String, BeanBindingModel>();
		}
		if(log.isInfoEnabled()){
			log.info("Add Bean binding :"+binding);
		}
		this.beanBindings.put(binding.getField().getName(), binding);
	}

	
	/**
	 * @return the isPage
	 */
	public boolean isPage() {
		return isPage;
	}

	/**
	 * @param isPage the isPage to set
	 */
	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.tools.generator.AbstractClassModel#addField(com.wxxr.mobile.core.tools.generator.FieldModel)
	 */
	@Override
	public void addField(FieldModel field) {
		if(field.getName().equals("log")){
			this.traceRequired = true;
			return;
		}
		super.addField(field);
//		TypeModel typeModel = new TypeModel(field.getType());
//		this.domainResolver.registerBean(field.getName(), context.getProcessingEnvironment().getElementUtils().getTypeElement(typeModel.getType()).asType());
	}

	/**
	 * @return the toolbarRequired
	 */
	public boolean isToolbarRequired() {
		return toolbarRequired;
	}

	/**
	 * @param toolbarRequired the toolbarRequired to set
	 */
	public void setToolbarRequired(boolean toolbarRequired) {
		this.toolbarRequired = toolbarRequired;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

//	/**
//	 * @return the domainResolver
//	 */
//	public ViewModelDomainResolver getDomainResolver() {
//		return domainResolver;
//	}

}
