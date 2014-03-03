/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public class SimpleInputEvent implements InputEvent {
	private final String eventType;
	private final IUIComponent source;
	private Map<String, Object> properties;
	private String targetCommand;
	
	public SimpleInputEvent(String type, IUIComponent src){
		this.eventType = type;
		this.source = src;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @return the source
	 */
	public IUIComponent getEventSource() {
		return source;
	}

	public SimpleInputEvent addProperty(String key, Object value){
		if(this.properties == null){
			this.properties = new HashMap<String, Object>();
		}
		this.properties.put(key, value);
		return this;
	}
	
	public SimpleInputEvent removeProperty(String key){
		if(this.properties != null){
			this.properties.remove(key);
		}
		return this;
	}

	public String[] getPropertyKeys() {
		return (this.properties != null) ? this.properties.keySet().toArray(new String[this.properties.size()]) : new String[0];
	}

	public Object getProperty(String key) {
		return (this.properties != null) ? this.properties.get(key) : null;
	}

	public boolean hasProperty(String key) {
		return (this.properties != null) ? this.properties.containsKey(key) : false;
	}

	/**
	 * @return the targetCommand
	 */
	public String getTargetCommand() {
		return targetCommand;
	}

	/**
	 * @param targetCommand the targetCommand to set
	 */
	public void setTargetCommand(String targetCommand) {
		this.targetCommand = targetCommand;
	}


}
