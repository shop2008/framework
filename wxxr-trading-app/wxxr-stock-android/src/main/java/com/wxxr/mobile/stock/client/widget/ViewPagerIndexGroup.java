package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Utils;

public class ViewPagerIndexGroup extends RadioGroup {

	private int size;
	private int position;
	private Context mContext;

	public ViewPagerIndexGroup(Context context) {
		super(context);
		this.mContext = context;
		setOrientation(HORIZONTAL);
	}

	public ViewPagerIndexGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setOrientation(HORIZONTAL);
	}

	public void setSize(int size) {
		this.size = size;
		addSizePoint(size);
		setPosition(position);
	}

	public void setPosition(int position) {
		this.position = position;
		if (getChildCount() > 0 && position < getChildCount()) {
			for (int i = 0; i < getChildCount(); i++) {
				RadioButton bt = (RadioButton) getChildAt(i);
				if (bt != null) {
					if (i == position) {
						bt.setChecked(true);
					} else {
						bt.setChecked(false);
					}
				}
			}
		}
	}

	private void addSizePoint(int size) {
		removeAllViews();
		RadioButton bt;
		for (int i = 0; i < size; i++) {
			bt = new RadioButton(mContext);
			bt.setBackgroundResource(R.drawable.viewpager_index_radiobuttn_bg);
			bt.setButtonDrawable(android.R.color.transparent);
			LayoutParams params = new LayoutParams(Utils.dip2px(mContext, 10),Utils.dip2px(mContext, 10));
			if(i != 0)
				params.leftMargin = Utils.dip2px(mContext, 12);
			addView(bt, params);
		}
	}
}
