/*
 * @(#)IBaseState.java	 2012-4-28
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.client.biz;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @class desc A IBaseState.
 * 
 */
public interface IStockBaseState {
	/**
	 * 画图
	 * */
	public void doDraw(Canvas canvas);
	/**
	 * 手势
	 * */
	public void doTouch(MotionEvent event);
	/**
	 * 5日
	 * @param num
	 */
	public void setFiveDay(int num);
	/**
	 * 边框颜色
	 * */
	public void setBorderColor(int sbColor);
	/**
	 * 坐标虚线颜色
	 * */
	public void setStrokeColor(int xbColor);
	
	/**
	 * 上涨颜色 默认红色
	 * */
	public void setStockUpColor(int upColor);
	/**
	 * 下跌颜色 默认绿色
	 * */
	public void setStockDownColor(int downColor);
	/**
	 * 平均线颜色 默认黄色
	 * */
	public void setStockAverageLineColor(int averagColor);
	/**
	 *  昨收价 默认白 or 黑
	 * */
	public void setStockCloseColor(int closeColor);
		
	
}
