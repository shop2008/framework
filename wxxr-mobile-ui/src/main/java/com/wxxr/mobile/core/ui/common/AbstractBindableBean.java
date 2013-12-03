/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.bean.api.BindableBeanPropertyChangedEvent;
import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.ObjectUtils;


/**
 * @author neillin
 *
 */
public class AbstractBindableBean implements IBindableBean {
	private List<IPropertyChangeListener> listeners;
	private BindableBeanPropertyChangedEvent firingEvent;

	public synchronized void addPropertyChangeListener(
			IPropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (listeners == null) {
			listeners = new ArrayList<IPropertyChangeListener>();
		}
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.bean.api.IBindableBean#removePropertyChangeListener(com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public synchronized void removePropertyChangeListener(
			IPropertyChangeListener listener) {
		if (listener == null) {
			return;
		}
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	private void firePropertyChange(BindableBeanPropertyChangedEvent evt) {
		if (listeners != null) {
			IPropertyChangeListener[] list = listeners.toArray(new IPropertyChangeListener[0]);
			for (int i = 0; i < list.length; i++) {
				IPropertyChangeListener target = list[i];
				target.propertyChange(evt);
			}
		}

	}

	
	protected synchronized void firePropertyChange(String propertyName, 
			Object oldValue, Object newValue) {
		if (ObjectUtils.isEquals(oldValue, newValue)) {
			return;
		}
		if(this.firingEvent != null){
			this.firingEvent.addChangedProperty(propertyName);
		}else{
			this.firingEvent = new BindableBeanPropertyChangedEvent(this, propertyName);
			KUtils.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					BindableBeanPropertyChangedEvent evt = null;
					synchronized(AbstractBindableBean.this){
						evt = firingEvent;
						firingEvent = null;
					}
					if(evt != null){
						firePropertyChange(evt);
					}
				}
			}, 300, TimeUnit.MILLISECONDS);
		}
	}

}
