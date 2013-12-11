/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDecoratorFactory;
import com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class SimpleBindingDecoratorRegistry implements
		IBindingDecoratorRegistry {
	private final IWorkbenchRTContext context;
	private Map<String, Class<? extends IBinding<?>>> classes = new HashMap<String, Class<? extends IBinding<?>>>();
	private Map<String,IBindingDecoratorFactory> factories = new HashMap<String, IBindingDecoratorFactory>();
	
	public SimpleBindingDecoratorRegistry(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#registerDecorator(java.lang.String, java.lang.Class)
	 */
	@Override
	public void registerDecorator(String name,
			Class<? extends IBinding<?>> clazz) {
		if(StringUtils.isBlank(name)||(clazz == null)){
			throw new IllegalArgumentException("decorator name and class cannot be NULL");
		}
		synchronized(this.classes){
			this.classes.put(name, clazz);
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#unregisterDecorator(java.lang.String, java.lang.Class)
	 */
	@Override
	public boolean unregisterDecorator(String name,
			Class<? extends IBinding<?>> clazz) {
		synchronized(this.classes) {
			Class<? extends IBinding<?>> klass = this.classes.get(name);
			if(klass == clazz){
				this.classes.remove(name);
				return true;
			}
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#registerDecorator(java.lang.String, com.wxxr.mobile.core.ui.api.IBindingDecoratorFactory)
	 */
	@Override
	public void registerDecorator(String name, IBindingDecoratorFactory factory) {
		if(StringUtils.isBlank(name)||(factory == null)){
			throw new IllegalArgumentException("decorator name and factory cannot be NULL");
		}
		synchronized(this.factories){
			this.factories.put(name, factory);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#unregisterDecorator(java.lang.String, com.wxxr.mobile.core.ui.api.IBindingDecoratorFactory)
	 */
	@Override
	public boolean unregisterDecorator(String name,
			IBindingDecoratorFactory factory) {
		synchronized(this.factories){
			IBindingDecoratorFactory old = this.factories.get(name);
			if(old == factory){
				this.factories.remove(name);
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#hasDecoratorRegistered(java.lang.String)
	 */
	@Override
	public boolean hasDecoratorRegistered(String name) {
		boolean val = false;
		synchronized(this.factories){
			if(this.factories.containsKey(name)){
				val = true;
			}
		}
		if(val){
			return true;
		}
		synchronized(this.classes){
			if(this.classes.containsKey(name)){
				val = true;
			}
		}
		return val;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry#createDecorator(java.lang.String, com.wxxr.mobile.core.ui.api.IBinding)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IUIComponent> IBinding<T> createDecorator(String name,
			IBinding<T> binding) {
		IBindingDecoratorFactory factory = null;
		synchronized(this.factories){
			factory = this.factories.get(name);
		}
		if(factory != null){
			return factory.createDecorator(binding);
		}
		Class<? extends IBinding<?>> clazz = null;
		synchronized(this.classes){
			clazz = this.classes.get(name);
		}
		if(clazz != null){
			try {
				@SuppressWarnings("rawtypes")
				IBinding b = clazz.getConstructor(IBinding.class).newInstance(binding);
				return (IBinding<T>)b;
			} catch (Exception e) {
				throw new RuntimeException("Unable to create binding decorator from class :"+clazz.getCanonicalName(),e);
			}	
		}
		return null;
	}

}
