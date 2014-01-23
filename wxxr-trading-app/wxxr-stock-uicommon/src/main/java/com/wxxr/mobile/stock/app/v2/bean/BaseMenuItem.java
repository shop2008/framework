/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class BaseMenuItem {
	//type类型0-5交易6-9消息a-f预留
	//0   模拟盘
	//01  模拟盘T+1
	//02  模拟盘T+2
	//03  模拟盘T+3
	//1   实盘
	//11  实盘T+1
	//12  实盘T+2
	//13  实盘T+3
	//60  系统消息
	//61  操盘咨询
	//62  未设置昵称
	//63  未认证手机号
	//70  参赛排行榜
	//80  签到
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
