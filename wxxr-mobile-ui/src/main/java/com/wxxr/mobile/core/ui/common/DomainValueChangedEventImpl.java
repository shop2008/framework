/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Arrays;
import java.util.List;

import com.wxxr.mobile.core.ui.api.DomainValueChangedEvent;

/**
 * @author neillin
 *
 */
public class DomainValueChangedEventImpl implements DomainValueChangedEvent {

	private final Object source;
	private final String beanName;
	private Long timestamp = System.currentTimeMillis();
	private List<String> changedProperties;
	
	public DomainValueChangedEventImpl(Object bean, String beanName, String ... properties){
		this.source = bean;
		this.beanName = beanName;
		if(properties != null){
			this.changedProperties = Arrays.asList(properties);
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ValueChangedEvent#getSourceName()
	 */
	@Override
	public String getSourceName() {
		return this.beanName;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getSource()
	 */
	@Override
	public Object getSource() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#needSyncProcessed()
	 */
	@Override
	public boolean needSyncProcessed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getTimestamp()
	 */
	@Override
	public Long getTimestamp() {
		return this.timestamp;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.DomainValueChangedEvent#getChangedProperties()
	 */
	@Override
	public List<String> getChangedProperties() {
		return this.changedProperties;
	}

}
