/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.PropertyChangeListener;
import com.wxxr.mobile.core.bean.api.PropertyChangeSupport;

/**
 * @author neillin
 *
 */
public class BindableListWrapper<E> implements IBindableBean {
	private List<E> data;
	private Comparator<E> comparator;
	private IEntityFilter<E> filter;
	private PropertyChangeSupport pSupport = new PropertyChangeSupport(this);
	private final IBindableEntityCache<?, E> cache;
	
	public BindableListWrapper(IBindableEntityCache<?, E> cache,IEntityFilter<E> filter,Comparator<E> comparator){
		this.cache = cache;
		this.filter = filter;
		this.comparator = comparator;
		setupCacheCallback();
	}
	
	public BindableListWrapper(IBindableEntityCache<?, E> cache,IEntityFilter<E> filter){
		this.cache = cache;
		this.filter = filter;
		setupCacheCallback();
	}

	public BindableListWrapper(IBindableEntityCache<?, E> cache){
		this.cache = cache;
		setupCacheCallback();
	}
	
	public BindableListWrapper(IBindableEntityCache<?, E> cache,Comparator<E> comparator){
		this.cache = cache;
		this.comparator = comparator;
	}
	
	protected void setupCacheCallback() {
		this.cache.registerCallback(new ICacheUpdatedCallback() {
			
			@Override
			public void dataChanged(IBindableEntityCache<?, ?> cache) {
				notifyCacheChanged();
			}
		});
	}

	
	protected synchronized void notifyCacheChanged() {
		Object oldVal = this.data;
		this.data = null;
		this.pSupport.firePropertyChange("data", oldVal, null);
	}

	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#addPropertyChangeListener(com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pSupport.addPropertyChangeListener(listener);
	}
	/**
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#removePropertyChangeListener(com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pSupport.removePropertyChangeListener(listener);
	}
	/**
	 * @param propertyName
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pSupport.addPropertyChangeListener(propertyName, listener);
	}
	/**
	 * @param propertyName
	 * @param listener
	 * @see com.wxxr.mobile.core.bean.api.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, com.wxxr.mobile.core.bean.api.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pSupport.removePropertyChangeListener(propertyName, listener);
	}
	
	public synchronized List<E> getData() {
		if(this.data == null){
			final ArrayList<E> result = new ArrayList<E>();
			this.cache.acceptVisitor(new ICacheVisitor<E>() {

				@Override
				public boolean visit(Object key, E value) {
					if((filter == null)||filter.doFilter(value)){
						result.add(value);
					}
					return true;
				}
			});
			if(comparator != null){
				Collections.sort(result, comparator);
			}
			this.data = result;
		}
		return this.data;
	}
	
}
