package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NewScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;

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
}
