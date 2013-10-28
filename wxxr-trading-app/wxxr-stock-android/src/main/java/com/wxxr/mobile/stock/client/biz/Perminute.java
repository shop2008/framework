/*
 * @(#)Perminute.java	 2012-4-23
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.client.biz;

/**
 * @class desc A Perminute.分时线的每分钟数据
 */
public class Perminute {
	public double newprice;// (最新)成交价（元）
	public double averageprice;// 均价（元）
	public long secuvolume;// 成交量（股）
	public double secuamount;// 成交金额（元）
	public double zhangdiexian;
	public String fiveDayTime; 
}
