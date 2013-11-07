/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Arrays;
import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;


/**
 * @author neillin
 *
 */
public class ValueChangedEventImpl implements ValueChangedEvent {

	private final IUIComponent source;
	private Long timestamp = System.currentTimeMillis();
	private List<AttributeKey<?>> keys;
	
	public ValueChangedEventImpl(IUIComponent src,AttributeKey<?>...keys){
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
	 * @see com.wxxr.mobile.core.ui.api.ValueChangedEvent#getComponent()
	 */
	public IUIComponent getComponent() {
		return this.source;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ValueChangedEvent#getChangedAttributes()
	 */
	public List<AttributeKey<?>> getChangedAttributes() {
		return this.keys;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValueChangedEventImpl [source=" + source + ", timestamp="
				+ timestamp + ", keys=" + keys + "]";
	}

}
