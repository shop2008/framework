/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

/**
 * @author neillin
 *
 */
public class UserLoginCallback {
	private String userName,password;
	private boolean cancelled;
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public void clear() {
		this.userName = null;
		this.password = null;
	}
	
	public synchronized void done(boolean cancelled){
		this.cancelled = cancelled;
		notifyAll();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserLoginCallback [userName=" + userName + ", password="
				+ password + ", cancelled=" + cancelled + "]";
	}
}
