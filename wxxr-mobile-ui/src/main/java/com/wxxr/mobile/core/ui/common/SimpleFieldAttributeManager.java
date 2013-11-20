/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

/**
 * @author neillin
 *
 */
public class SimpleFieldAttributeManager implements IFieldAttributeManager {

	private Map<String, AttributeKey<?>> attrKeys = new HashMap<String, AttributeKey<?>>();
	private Map<String, List<IAttributeUpdater<?>>> updaters = new HashMap<String, List<IAttributeUpdater<?>>>();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#registerAttribute(java.lang.String, java.lang.Class)
	 */
	public <T> IFieldAttributeManager registerAttribute(final String name,
			final Class<T> valueType) {
		this.attrKeys.put(name, new AttributeKey<T>(valueType,name));
		return this;
	}
	
	public <T> IFieldAttributeManager registerAttribute(AttributeKey<T> key) {
		this.attrKeys.put(key.getName(),key);
		return this;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#unregisterAttribute(java.lang.String)
	 */
	public IFieldAttributeManager unregisterAttribute(String name) {
		this.attrKeys.remove(name);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#getAttributeKey(java.lang.String)
	 */
	public AttributeKey<?> getAttributeKey(String name) {
		AttributeKey<?> key = this.attrKeys.get(name);
		if(key == null){
			for (AttributeKey<?> k : AttributeKeys.keys) {
				if(k.getName().equals(name)){
					key = k;
					break;
				}
			}
		}
		return key;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#registerAttributeUpdater(java.lang.String, com.wxxr.mobile.core.ui.api.IAttributeUpdater)
	 */
	public <T> IFieldAttributeManager registerAttributeUpdater(String name,
			IAttributeUpdater<T> updater) {
		List<IAttributeUpdater<?>> list = this.updaters.get(name);
		if(list == null){
			list = new LinkedList<IAttributeUpdater<?>>();
			this.updaters.put(name, list);
		}
		if(!list.contains(updater)){
			list.add(updater);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#unregisterAttributeUpdater(java.lang.String, com.wxxr.mobile.core.ui.api.IAttributeUpdater)
	 */
	public <T> IFieldAttributeManager unregisterAttributeUpdater(String name,
			IAttributeUpdater<T> updater) {
		List<IAttributeUpdater<?>> list = this.updaters.get(name);
		if(list != null){
			list.remove(updater);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IFieldAttributeManager#getAttributeUpdaters(java.lang.String)
	 */
	public IAttributeUpdater<?>[] getAttributeUpdaters(String name) {
		List<IAttributeUpdater<?>> list = this.updaters.get(name);
		return list != null ? list.toArray(new IAttributeUpdater<?>[list.size()]) : new IAttributeUpdater<?>[0];
	}

}
