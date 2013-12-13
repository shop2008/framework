/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author neillin
 *
 */
public class ControlFieldBinders implements IControlFieldBinders {
	
	private Map<Class<?>, IFieldBinder> binders;
	private final IWorkbenchRTContext context;
	
	public ControlFieldBinders(IWorkbenchRTContext ctx){
		this.context = ctx;
	}

	@Override
	public <M extends IUIComponent> IFieldBinder getFieldBinder(
			Class<M> pmodelClass) {
		if((this.binders == null)||this.binders.isEmpty()){
			return null;
		}
		Class<?> klass = pmodelClass;
		while(klass != Object.class ){
			IFieldBinder binder = this.binders.get(klass);
			if(binder != null){
				return binder;
			}else{
				klass = klass.getSuperclass();
			}
		}
		return this.binders.get(IUIComponent.class);
	}

	@Override
	public <M extends IUIComponent> void registerFieldBinder(
			Class<M> pmodelClass, IFieldBinder binder) {
		if(binders == null){
			binders = new HashMap<Class<?>, IFieldBinder>();
		}
		IFieldBinder old = binders.put(pmodelClass, binder);
		binder.init(this.context);
		if(old != null){
			old.destory();
		}
	}

	@Override
	public <M extends IUIComponent> boolean unregisterFieldBinder(
			Class<M> pmodelClass, IFieldBinder binder) {
		if(binders != null){
			IFieldBinder b = binders.get(pmodelClass);
			if(b == binder){
				binders.remove(pmodelClass);
				b.destory();
				return true;
			}
		}
		return false;
	}

}
