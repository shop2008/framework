package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wxxr.mobile.stock.client.R;

public class TextSpinnerView extends EditText implements OnClickListener,OnItemClickListener {

	private EditText tv;
	private ListView popListView;
	private ListAdapter adapter;
	private PopupWindow popupWindow;
	private Context mContext;
	private int position = -1;
	
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
		if(adapter!=null && adapter.getCount()>0){
			showPopUpWindowsList();
		}else{
			Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
		}
	}

	public int getPosition(){
		return this.position;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		this.position = position;
		this.setTag(position);
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
			popupWindow = new PopupWindow(view,w , WindowManager.LayoutParams.WRAP_CONTENT);
			popListView = (ListView) view.findViewById(R.id.lv_down_amount);
			popListView.setAdapter(adapter);
			popupWindow.setFocusable(true);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true); 
			popupWindow.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						if(popupWindow!=null && popupWindow.isShowing()){
							popupWindow.dismiss();
						}
						return true;
					}
					return false;
				}
			});
			popListView.setOnItemClickListener(this);
		}
	}
	
	private void showPopUpWindowsList(){
		createPopUpWindows();
		if(adapter!=null && adapter.getCount()>0 && popupWindow!=null && !popupWindow.isShowing()){
//			if(adapter.getCount()>4){
//				popupWindow.setWindowLayoutMode(this.getWidth(),80);
//			}else{
//				popupWindow.setWindowLayoutMode(this.getWidth(),WindowManager.LayoutParams.WRAP_CONTENT);
//			}
			popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_window));
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
