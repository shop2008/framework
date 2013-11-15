package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;
/**
 * 实盘券排行榜项
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="RegularTicketBean")
public class RegularTicket{
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 实盘券数
	 */
	private long regular;
	/**
	 * 正收益个数
	 */
	private int gainCount;
	/**
	 * 排行
	 */
	private int rankSeq;
}
