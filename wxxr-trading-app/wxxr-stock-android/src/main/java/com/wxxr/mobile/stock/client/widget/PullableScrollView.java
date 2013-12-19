/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

/**
 * @author neillin
 *
 */
public class PullableScrollView extends AbstractPullableView {

	private final ScrollView scrollView;
	
	
	public PullableScrollView(ScrollView view){
		if(view == null){
			throw new IllegalArgumentException("Invalid adapter view  : NULL");
		}
		this.scrollView = view;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#getView()
	 */
	@Override
	public View getView() {
		return this.scrollView;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#isPullable(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean isPullable(View view, int deltaY) {
		View child = scrollView.getChildAt(0);
		if (deltaY > 0 && scrollView.getScrollY() == 0) {
			return true;
		}else if((deltaY < 0) && (child.getMeasuredHeight() <= ((View)scrollView.getParent()).getHeight()
				+ scrollView.getScrollY())) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#getTouchView(float, float)
	 */
	@Override
	public View getTouchView(float x, float y) {
		if(isPointInsideView(x,y,this.scrollView)){
			return scrollView;
		}
		return null;
	}

}
