package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NewScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;
	private float xDistance, yDistance, xLast, yLast;

	public NewScrollView(Context context) {
		super(context);
	}

	public NewScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NewScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
            xDistance = yDistance = 0f;
            xLast = e.getX();
            yLast = e.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            final float curX = e.getX();
            final float curY = e.getY();
            
            xDistance += Math.abs(curX - xLast);
            yDistance += Math.abs(curY - yLast);
            xLast = curX;
            yLast = curY;
            
            if(xDistance > yDistance){
                return false;
            } else {
            	return true;
            }
    }
		return false;
	}
}
