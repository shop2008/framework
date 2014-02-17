package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.stock.client.R;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TextSpinnerView extends EditText implements OnClickListener,OnItemClickListener {

	private EditText tv;
	private ListView popListView;
	private ListAdapter adapter;
	private PopupWindow popupWindow;
	private Context mContext;
	
	public TextSpinnerView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	public TextSpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	public TextSpinnerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}	
	
	private void init(){
		this.setCursorVisible(false);
		this.setFocusable(false);
		this.setFocusableInTouchMode(false);
		this.setInputType(InputType.TYPE_NULL);	
		this.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View arg0) {
		showPopUpWindowsList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		TextView tx = (TextView) view.findViewById(R.id.money_text_id);
		this.setText(tx.getText());
		dismissPopupWindow();
	}
	
	public void setAdapter(ListAdapter adapter){
		this.adapter = adapter;
		this.setText(null);
	}
	
	public void cleanText(String val){
		this.setText(null);
	}
	
	private void createPopUpWindows(){
		if(adapter!=null && popupWindow==null){
			int w = this.getWidth();
			View view = LayoutInflater.from(mContext).inflate(R.layout.money_drop_down_menu, null);
			popupWindow = new PopupWindow(view,w , LayoutParams.WRAP_CONTENT);
			popListView = (ListView) view.findViewById(R.id.lv_down_amount);
			popListView.setAdapter(adapter);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true); 
			popListView.setOnItemClickListener(this);
		}
	}
	
	private void showPopUpWindowsList(){
		createPopUpWindows();
		if(adapter!=null && adapter.getCount()>0 && popupWindow!=null && !popupWindow.isShowing()){
//			popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_activity_top_title));
			popupWindow.showAsDropDown(this, 0, 0);
			
		}
	}
	
	private void dismissPopupWindow(){
		if(popupWindow!=null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch(action){
			case MotionEvent.ACTION_DOWN:
				break;
		}
		return super.onTouchEvent(event);
	}
}
