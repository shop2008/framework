/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.Properties;

import android.view.View;

/**
 * @author neillin
 *
 */
public interface IFieldBindingFactoryManager {
	/**
	 * return binding strategy for given android UI component
	 * @param clazz
	 * @return
	 */
	IFieldBindingFactory getBindingStrategy(String factoryName);
	
	void registerBindingFactory(IFieldBindingFactory factory);
	
	boolean unregisterBindingFactory(IFieldBindingFactory factory);
	
	Properties getTagProperties(Integer vid, View uiView);
}
