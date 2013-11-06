/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.ControlFieldBinders;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * @author neillin
 *
 */
public class SimpleFieldBinderManager implements
		IFieldBinderManager {
	@SuppressWarnings("unused")
	private final IWorkbenchRTContext context;
	
	private Map<Class<?>, ControlFieldBinders> factories = new HashMap<Class<?>, ControlFieldBinders>();

	public SimpleFieldBinderManager(IWorkbenchRTContext ctx){
		this.context = ctx;
	}

	protected ControlFieldBinders getFieldBinders(Class<?> clazz) {
		Class<?> klass = clazz;
		while(klass != Object.class){
			ControlFieldBinders binders = this.factories.get(klass);
			if(binders != null){
				return binders;
			}else{
				klass = klass.getSuperclass();
			}
		}
		return null;
	}
	
	protected <T> ControlFieldBinders getFieldBinders(Class<T> clazz, boolean createIfNotexisting) {
		ControlFieldBinders binders = (ControlFieldBinders)this.factories.get(clazz);
		if((binders == null)&&createIfNotexisting){
			binders = new ControlFieldBinders();
			this.factories.put(clazz, binders);
		}
		return binders;
	}

	@Override
	public <M extends IUIComponent> IFieldBinder getFieldBinder(
			Class<M> pmodelClass, Class<?> controlClass) {
		Class<?> klass = controlClass;
		while(klass != Object.class){
			ControlFieldBinders binders = this.factories.get(klass);
			if((binders != null)&&(binders.getFieldBinder(pmodelClass) != null)){
				return binders.getFieldBinder(pmodelClass);
			}else{
				klass = klass.getSuperclass();
			}
		}
		return null;
	}

	@Override
	public <M extends IUIComponent> void registerFieldBinder(
			Class<M> pmodelClass, Class<?> controlClass,
			IFieldBinder binder) {
		ControlFieldBinders binders = getFieldBinders(controlClass,true);
		binders.registerFieldBinder(pmodelClass, binder);
	}

	@Override
	public <M extends IUIComponent> boolean unregisterFieldBinder(
			Class<M> pmodelClass, Class<?> controlClass,
			IFieldBinder binder) {
		ControlFieldBinders binders = getFieldBinders(controlClass,true);
		if(binders != null){
			return binders.unregisterFieldBinder(pmodelClass, binder);
		}
		return false;
	}

}
