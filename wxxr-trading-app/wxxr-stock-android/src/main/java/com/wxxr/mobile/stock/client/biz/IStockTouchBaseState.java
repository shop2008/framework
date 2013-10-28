package com.wxxr.mobile.stock.client.biz;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface IStockTouchBaseState {
	public void doDraw(Canvas canvas,float left,float right,float top,float botton,float stopX);
	public void doTouch(MotionEvent event);
}
