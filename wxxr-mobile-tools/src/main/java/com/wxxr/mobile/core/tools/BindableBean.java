/**
 * 
 */
package com.wxxr.mobile.core.tools;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * @author neillin
 *
 */
public class BindableBean implements IBindableBean {
	
	private final PropertyChangeSupport emitter = new PropertyChangeSupport(this);
	
	private String name;
		

	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#addPropertyChangeListener(com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#removePropertyChangeListener(com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.addPropertyChangeListener(propertyName, listener);
	}

	/**
	 * @param propertyName
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		emitter.removePropertyChangeListener(propertyName, listener);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
