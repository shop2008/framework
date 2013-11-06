package com.wxxr.mobile.stock.client.widget;


import com.wxxr.mobile.stock.client.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TListView extends ListView {

	private String title="T日";
	private Context mContext;
	private AttributeSet attrs;
	private View view = null;
	public TListView(Context context) {
		super(context);
		this.mContext = context;
	}
	public TListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		this.attrs = attrs;
		init(attrs,context);
	}
	public TListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		this.attrs = attrs;
		init(attrs,context);
	}	
	private void init(AttributeSet attrs,Context context){
		TypedArray arrs = context.obtainStyledAttributes(attrs, R.styleable.TListView);
		String temp = (String) arrs.getText(R.styleable.TListView_tHeaderTitle);
		if(temp!=null){
			title = temp;
		}
		if(title!=null){
			LayoutInflater inflater = LayoutInflater.from(context);
			if(inflater!=null){
				view = (View)inflater.inflate(R.layout.t_listview_header_view, null);
				if(view!=null){
					TextView mTextView = (TextView) view.findViewById(R.id.listview_header_title);
					if(mTextView!=null){
						mTextView.setText(title);
						addHeaderView(view);
					}
				}
			}
		}
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		if(adapter.getCount()==0 && view!=null){
			removeHeaderView(view);
			super.setAdapter(null);
			return;
		}
		super.setAdapter(adapter);
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	}
}
