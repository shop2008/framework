/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class MessageMenuItem extends BaseMenuItem {
	private int num;
	private String message;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "MessageMenuItem [num=" + num + ", message=" + message
				+ ", title=" + title + ", date=" + date + ", type=" + type
				+ "]";
	}
	
}
