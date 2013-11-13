/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IProgressGuard;

/**
 * @author neillin
 *
 */
public class SimpleProgressGuard implements IProgressGuard{
	
	private String title, message, icon;
	private boolean cancellable;
	private Map<String, Object> extras;
	private int silentPeriod;
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @return the cancellable
	 */
	public boolean isCancellable() {
		return cancellable;
	}
	/**
	 * @return the extras
	 */
	public Map<String, Object> getExtras() {
		return extras;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @param cancellable the cancellable to set
	 */
	public void setCancellable(boolean cancellable) {
		this.cancellable = cancellable;
	}
	/**
	 * @param extras the extras to set
	 */
	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}
	/**
	 * @return the silentPeriod
	 */
	public int getSilentPeriod() {
		return silentPeriod;
	}
	/**
	 * @param silentPeriod the silentPeriod to set
	 */
	public void setSilentPeriod(int silentPeriod) {
		this.silentPeriod = silentPeriod;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleProgressGuard [title=" + title + ", message=" + message
				+ ", icon=" + icon + ", cancellable=" + cancellable
				+ ", extras=" + extras + ", silentPeriod=" + silentPeriod + "]";
	}
	
	
}
