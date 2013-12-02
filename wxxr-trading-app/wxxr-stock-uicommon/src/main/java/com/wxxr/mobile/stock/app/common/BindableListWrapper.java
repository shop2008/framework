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
import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author neillin
 *
 */
public class BindableListWrapper<E> implements IBindableBean {
	private Trace xlog;
	
	private List<E> data;
	private Comparator<E> comparator;
	/**
	 * @return the comparator
	 */
	public Comparator<E> getComparator() {
		return comparator;
	}
	/**
	 * @return the filter
	 */
	public IEntityFilter<E> getFilter() {
		return filter;
	}

	private IEntityFilter<E> filter;
	private PropertyChangeSupport pSupport = new PropertyChangeSupport(this);
	private final IBindableEntityCache<?, E> cache;
	private ICacheUpdatedCallback callback = new ICacheUpdatedCallback() {
		
		@Override
		public void dataChanged(IBindableEntityCache<?, ?> cache) {
			notifyCacheChanged();
		}
	};
	
	protected Trace getLog(){
		if(xlog == null){
			xlog = Trace.getLogger(getClass().getPackage().getName()+".Cache_list_"+cache.getEntityTypeName());
		}
		return xlog;
	}
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
		this.cache.registerCallback(callback);
	}

	
	protected synchronized void notifyCacheChanged() {
		Object oldVal = this.data;
		this.data = null;
		if(getLog().isDebugEnabled()){
			getLog().debug("Received cache changed, Clear list data and fire property changed event. ");
		}
		this.pSupport.firePropertyChange("data", oldVal, null);
	}
	
	public synchronized void clear() {
		if(getLog().isDebugEnabled()){
			getLog().debug("Clear list data ");
		}
		this.data = null;
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
			if(getLog().isDebugEnabled()){
				getLog().debug("data is null, going to refill list data ...");
			}
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
			if(getLog().isDebugEnabled()){
				getLog().debug("data refilling is done, new size of list data : "+this.data.size());
			}
		}
		return this.data;
	}
	
}
