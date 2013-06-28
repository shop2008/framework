/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.Arrays;
import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.util.ArrayUtils;

/**
 * @author neillin
 *
 */
public class DataChangedEventImpl implements DataChangedEvent {

	private final IUIComponent source;
	private Long timestamp = System.currentTimeMillis();
	private List<AttributeKey<?>> keys;
	
	public DataChangedEventImpl(IUIComponent src,AttributeKey<?>...keys){
		this.source = src;
		this.keys = Arrays.asList(keys);
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getSource()
	 */
	public Object getSource() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#needSyncProcessed()
	 */
	public boolean needSyncProcessed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.event.api.IEventObject#getTimestamp()
	 */
	public Long getTimestamp() {
		return this.timestamp;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.DataChangedEvent#getComponent()
	 */
	public IUIComponent getComponent() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.DataChangedEvent#getChangedAttributes()
	 */
	public List<AttributeKey<?>> getChangedAttributes() {
		return this.keys;
	}

}
