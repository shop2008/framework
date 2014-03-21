/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author neillin
 *
 */
public class PullableAdapterView extends AbstractPullableView {

	private final AdapterView<?> mAdapterView;
	
	
	public PullableAdapterView( AdapterView<?> view){
		if(view == null){
			throw new IllegalArgumentException("Invalid adapter view  : NULL");
		}
		this.mAdapterView = view;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#getView()
	 */
	@Override
	public View getView() {
		return this.mAdapterView;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#isPullable(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean isPullable(View view, int deltaY) {
		int absDeltay = Math.abs(deltaY);

		if(deltaY > 0 && absDeltay > 10) {
			View child = mAdapterView.getChildAt(0);
			if (child == null) {
				// 如果mAdapterView中没有数据,不拦截
				return false;
			}
			if (mAdapterView.getFirstVisiblePosition() == 0
					&& child.getTop() == 0) {
				return true;
			}
			int top = child.getTop();
			int padding = mAdapterView.getPaddingTop();
			if (mAdapterView.getFirstVisiblePosition() == 0
					&& Math.abs(top - padding) <= 8) {// 这里之前用3可以判断,但现在不行,还没找到原因
				return true;
			}
		} else if (deltaY < 0 && absDeltay > 10) {
			View lastChild = mAdapterView.getChildAt(mAdapterView
					.getChildCount() - 1);
			if (lastChild == null) {
				// 如果mAdapterView中没有数据,不拦截
				return false;
			}
			// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
			// 等于父View的高度说明mAdapterView已经滑动到最后
			if (lastChild.getBottom() <= ((View)mAdapterView.getParent()).getHeight()
					&& mAdapterView.getLastVisiblePosition() == mAdapterView
							.getCount() - 1) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.IPullableView#getTouchView(float, float)
	 */
	@Override
	public View getTouchView(float x, float y) {
		if(isPointInsideView(x,y,this.mAdapterView)){
			return mAdapterView;
		}
		return null;
	}

}
