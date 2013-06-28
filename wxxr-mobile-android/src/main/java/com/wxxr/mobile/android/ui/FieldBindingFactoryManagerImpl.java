/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.util.SparseArray;
import android.view.View;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class FieldBindingFactoryManagerImpl<C extends IAndroidAppContext> extends AbstractModule<C> implements
		IFieldBindingFactoryManager {

	private SparseArray<Properties> tagProperties = new SparseArray<Properties>();
	private Map<String, IFieldBindingFactory> factories = new HashMap<String, IFieldBindingFactory>();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBindingFactoryManager#getBindingStrategy(java.lang.String)
	 */
	@Override
	public IFieldBindingFactory getBindingStrategy(String factoryName) {
		synchronized(factories){
			return factories.get(factoryName);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBindingFactoryManager#getTagProperties(java.lang.Integer, android.view.View)
	 */
	@Override
	public synchronized Properties getTagProperties(Integer vid, View uiView) {
		Properties p = this.tagProperties.get(vid);
		if(p == null){
			Object tag = uiView.getTag();
			String val = tag != null ? StringUtils.trimToNull(tag.toString()) : null;
			if(val != null){
				p = TagPropertiesParser.parse(val);
			}else{
				p = new Properties();
			}
			this.tagProperties.put(vid, p);
		}
		return p;
	}
	
	public void registerBindingFactory(IFieldBindingFactory factory){
		if(factory == null){
			throw new IllegalArgumentException("Invalid factory NULL !");
		}
		synchronized(factories){
			factories.put(factory.getName(), factory);
		}
	}
	
	public boolean unregisterBindingFactory(IFieldBindingFactory factory){
		if(factory != null){
			synchronized(factories){
				IFieldBindingFactory f = factories.get(factory.getName());
				if(f == factory){
					factories.remove(factory.getName());
					return true;
				}
			}
		}
		return false;
	}
	

	@Override
	protected void initServiceDependency() {		
	}

	@Override
	protected void startService() {
		registerBindingFactory(new ViewFrameBindingFactory());
		registerBindingFactory(new ViewBindingFactory());
		registerBindingFactory(new FieldBindingFactory());
		context.registerService(IFieldBindingFactoryManager.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IFieldBindingFactoryManager.class, this);
		synchronized(factories){
			factories.clear();
		}
	}

}
