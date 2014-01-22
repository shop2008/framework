/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class BaseMenuItem {
	protected String title;//菜单项标题
	protected String date;//菜单项日期
	protected String type;//菜单项类型

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "BaseMenuItem [title=" + title + ", date=" + date + ", type="
				+ type + "]";
	}
	
	

}
