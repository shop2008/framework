package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class DelLineTextView extends TextView {
	public DelLineTextView(Context context) {
		super(context);
	}
	public DelLineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public DelLineTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	public void setIsDelLine(boolean flag){
		if(flag){
			this.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			this.getPaint().setFlags(0);
		}
	}
}
