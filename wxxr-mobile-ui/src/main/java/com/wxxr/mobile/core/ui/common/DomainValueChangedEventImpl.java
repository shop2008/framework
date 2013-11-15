/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.wxxr.mobile.core.ui.api.DomainValueChangedEvent;

/**
 * @author neillin
 *
 */
public class DomainValueChangedEventImpl implements DomainValueChangedEvent {
	private static SimpleDateFormat fmt = new SimpleDateFormat("yy/MM/dd HH:mm:ss sss");
	private final Object source;
	private final String beanName;
	private long timestamp = System.currentTimeMillis();
	private String date;
	private List<String> changedProperties;
	
	public DomainValueChangedEventImpl(Object bean, String beanName, String ... properties){
		this.source = bean;
		this.beanName = beanName;
		synchronized(fmt){
			this.date = fmt.format(new Date(this.timestamp));
		}
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		return "DomainValueChangedEventImpl [source=" + source + ", beanName="
				+ beanName + ", timestamp=" + date
				+ ", changedProperties=" + changedProperties + "]";
	}

}
