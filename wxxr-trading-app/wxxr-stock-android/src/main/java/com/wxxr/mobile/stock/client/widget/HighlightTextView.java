package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wxxr.mobile.stock.client.R;

public class HighlightTextView extends TextView {

	public HighlightTextView(Context context) {
		super(context);
	}
	public HighlightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context, attrs);
	}
	public HighlightTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData(context, attrs);
	}
	
	private void initData(Context context, AttributeSet attrs){
		String defText = (String) this.getText();
		TypedArray arrs = context.obtainStyledAttributes(attrs,R.styleable.HighlightTextView);
		if(arrs!=null){
			String text = (String) arrs.getText(R.styleable.HighlightTextView_highLightText);
			if(text!=null && defText!=null){
				int start = defText.indexOf(text);
				int end = start + text.length();
				setColorSpan(defText, start, end);
			}
		}
	}
	private void setColorSpan(String val, int start, int end){
		SpannableStringBuilder style=new SpannableStringBuilder(val); 
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
		style.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		this.setText(style);
	}
}
