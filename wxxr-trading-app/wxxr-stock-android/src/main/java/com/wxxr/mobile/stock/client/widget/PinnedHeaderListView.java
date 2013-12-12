package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.stock.client.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PinnedHeaderListView extends ListView {

	public interface PinnedHeaderAdapter {
		public static final int PINNED_HEADER_GONE = 0;
		public static final int PINNED_HEADER_VISIBLE = 1;
		public static final int PINNED_HEADER_PUSHED_UP = 2;

		int getPinnedHeaderState(int position);

		void configurePinnedHeader(View header, int position, int alpha);
		
		View getPinnedHeaderView();
	}

	private static final int MAX_ALPHA = 255;

	protected PinnedHeaderAdapter mAdapter;
	protected View mHeaderView;
	protected boolean mHeaderViewVisible;
	protected int mHeaderViewWidth;
	protected int mHeaderViewHeight;

	public PinnedHeaderListView(Context context) {
		super(context);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }
    
    /**
     * 此方法供外部使用，用于HeaderView的配置，并且重新ListView的onLayout方法
     * @param view 标题头部的View
     */
	public void setPinnedHeaderView(View view) {
		mHeaderView = view;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//.generateLayoutParams(mHeaderView.);
		mHeaderView.setLayoutParams(params);
		if (mHeaderView != null) {
			setFadingEdgeLength(0);
		}
		/**
		 * 重新执行onLayout方法，将列表的第一个标题显示出来
		 */
		requestLayout();
	}
	
	
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (PinnedHeaderAdapter)adapter;
        if(mAdapter instanceof OnScrollListener){
        	setOnScrollListener((OnScrollListener)mAdapter);
        }
        if(mAdapter != null)
        	setPinnedHeaderView(mAdapter.getPinnedHeaderView());
    }

    /**
     * 根据不同位置返回的不同状态，来控制ListView指定条目(position)标题的显示，隐藏和上推下拉效果
     * @param position
     */
    synchronized public void configureHeaderView(int position) {
		if (mHeaderView == null) {
			return;
		}
		int state = mAdapter.getPinnedHeaderState(position);
		
		switch (state) {
		case PinnedHeaderAdapter.PINNED_HEADER_GONE: {
			mHeaderViewVisible = false;
			break;
		}

		case PinnedHeaderAdapter.PINNED_HEADER_VISIBLE: {
			mAdapter.configurePinnedHeader(mHeaderView, position, MAX_ALPHA);
			if (mHeaderView.getTop() != 0) {
				mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
			}
			mHeaderViewVisible = true;
			break;
		}

		case PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP: {
			View firstView = getChildAt(0);
			int bottom = firstView.getBottom();
			int headerHeight = mHeaderView.getHeight();
			int y;
			int alpha;
			if (bottom < headerHeight) {
				y = (bottom - headerHeight);
				alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
			} else {
				y = 0;
				alpha = MAX_ALPHA;
			}
			mAdapter.configurePinnedHeader(mHeaderView, position, alpha);
			if (mHeaderView.getTop() != y) {
				mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
						+ y);
			}
			mHeaderViewVisible = true;
			break;
		}
		}
	}
    
	/**
	 * 绘制每个条目的标题
	 */
	protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}