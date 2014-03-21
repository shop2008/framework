package com.wxxr.mobile.stock.client.widget;

import java.util.IllegalFormatException;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.widget.wheel.OnWheelChangedListener;
import com.wxxr.mobile.stock.client.widget.wheel.OnWheelScrollListener;
import com.wxxr.mobile.stock.client.widget.wheel.WheelView;
import com.wxxr.mobile.stock.client.widget.wheel.adapters.ArrayWheelAdapter;

public class StockInputKeyboard extends FrameLayout implements OnClickListener, View.OnTouchListener {

	private static final Trace log = Trace.register(StockInputKeyboard.class);
	
	public interface OnStockKeyboardActionListener {
		void onStockKey(int primaryCode, String keyCodes);
	}
	private OnStockKeyboardActionListener mOnStockKeyListener;
	private Context context;
	private LinearLayout ll_stock_price_keyboard, ll_stock_number_keyboard, ll_sp_wheel_kb, ll_sn_wheel_kb;
	private String[][] data;
	private String[][] data2;
	private WheelView wv_left, wv_middle, wv_right, wv_sn_middle;
	
	private TextView tv_first, tv_second, tv_third, tv_stock_price, tv_sn_number, tv_fourth;
//	private Animation push_in, push_out;
	
	private boolean showSPWheel;
	private boolean showSNWheel;
	//外面传进来，放大1000倍的值
	private String marketPrice; //涨跌幅计算
	private int maxCountStock;
	//需要传出去，放大100倍的值
	private String toOrderprice;
	private String toCount;
	//区分买入卖出
	private TextView tv_typeDes;
	private String type;
	public StockInputKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public StockInputKeyboard(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
		if(toOrderprice == null)
			this.toOrderprice = marketPrice;
//		if(tv_stock_price != null)
//			tv_stock_price.setText("("+marketPrice+")");
	}
	
	public void setMaxCountStock(int maxCountStock) {
		this.maxCountStock = maxCountStock;
//		if(tv_sn_number != null)
//			tv_sn_number.setText(maxCountStock+"");
	}
	
	public void setKeyBoardType(String type) {
		this.type = type;
	}
	
	public void setOnKeyboardActionListener(OnStockKeyboardActionListener l) {
		this.mOnStockKeyListener = l;
	}
	
	public void showInitPriceKeyboard() {
		allKeyboardGone();
		isVisible_SP();
	}
	
	public void showInitCountKeyboard() {
		allKeyboardGone();
		isVisible_SN();
	}

	public void allKeyboardGone() {
		isGone_SP();
		isGone_SN();
		isGone_SP_Wheel();
		isGone_SN_Wheel();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		findView();
		initView();
//		requestLayout();
	}
	
	private void init() {
		FrameLayout.inflate(context, R.layout.stock_input_view, this);

		Resources resources = getResources();
		String[] first = resources.getStringArray(R.array.head1);
		String[] second = resources.getStringArray(R.array.head2);
		String[] third = resources.getStringArray(R.array.head3);
		String[] fourth = resources.getStringArray(R.array.head4);

		data = new String[][] { first, second, third };
		data2 = new String[][] { fourth };
	}

	public void initView() {
//		if (null == push_in)
//			push_in = AnimationUtils.loadAnimation(context, R.anim.push_in);
//		if (null == push_out)
//			push_out = AnimationUtils.loadAnimation(context, R.anim.push_out);

		getWheelSign();//%号滚轮
		getWheelScale();//比例滚轮
	}

	public void findView() {

		ll_stock_price_keyboard = (LinearLayout) findViewById(R.id.ll_stock_price_keyboard);
		ll_stock_number_keyboard = (LinearLayout) findViewById(R.id.ll_stock_number_keyboard);
		ll_sp_wheel_kb = (LinearLayout) findViewById(R.id.ll_sp_wheel_kb);
		ll_sn_wheel_kb = (LinearLayout) findViewById(R.id.ll_sn_wheel_kb);
		
		// 键盘按键
		findViewById(R.id.bt_sign).setOnClickListener(this);// %号
		findViewById(R.id.bt_scale).setOnClickListener(this);// 比例
		setButtonClickListener();
	}

	int layoutID[] = {R.id.left_layout, R.id.layout_0, R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4};
	private void setButtonClickListener() {
		LinearLayout layout;
		for(int i=0;i<layoutID.length;i++) {
			layout = (LinearLayout) ll_stock_price_keyboard.findViewById(layoutID[i]);
			if(layout != null)
				setOnClickListener(layout, layout.getChildCount());
			
			layout = (LinearLayout) ll_stock_number_keyboard.findViewById(layoutID[i]);
			if(layout != null)
				setOnClickListener(layout, layout.getChildCount());
		}
	}

	private void setOnClickListener(LinearLayout layout, int count) {
		for(int i=0;i<count;i++) {
			View v = layout.getChildAt(i);
			v.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
//		case R.id.input_btn_sp_r3_4:
//			isGone_SP();
//			break;
//		case R.id.input_btn_sn_r3_4:
//			isGone_SN();
//			break;
		case R.id.bt_sign:
			isVisible_SP_Wheel();
			isGone_SP();
			break;
		case R.id.bt_scale:
			isVisible_SN_Wheel();
			isGone_SN();
			break;
		case R.id.bt_123:
			isVisible_SP();
			isGone_SP_Wheel();
			break;
//		case R.id.bt_finish:
//			isGone_SP_Wheel();
//			break;
		case R.id.bt_sn_123:
			isVisible_SN();
			isGone_SN_Wheel();
			break;
//		case R.id.bt_sn_finish:
//			isGone_SN_Wheel();
//			break;
		case R.id.bt_finish:
			if(mOnStockKeyListener != null) {
				mOnStockKeyListener.onStockKey(R.id.bt_finish, toOrderprice);
				mOnStockKeyListener.onStockKey(R.id.input_btn_hide, "");
			}
			break;
		case R.id.bt_sn_finish:
			if(mOnStockKeyListener != null) {
				mOnStockKeyListener.onStockKey(R.id.bt_sn_finish, toCount);
				mOnStockKeyListener.onStockKey(R.id.input_btn_hide, "");
			}
			break;
		case R.id.input_btn_ok:
			if(mOnStockKeyListener != null) {
				mOnStockKeyListener.onStockKey(R.id.input_btn_hide, "");
			}
		break;
		default:
			if(mOnStockKeyListener != null) {
				mOnStockKeyListener.onStockKey(id, v instanceof Button?((Button)v).getText().toString():"");
			}
			break;
		}
	}

	/**
	 * 比例键盘
	 */
	private void getWheelScale() {
		// TODO Auto-generated method stub
		wv_sn_middle = (WheelView) findViewById(R.id.wv_sn_middle);
		
		tv_sn_number = (TextView) findViewById(R.id.tv_sn_number);
		tv_fourth = (TextView) findViewById(R.id.tv_fourth);
		findViewById(R.id.bt_sn_123).setOnClickListener(this);
		findViewById(R.id.bt_sn_finish).setOnClickListener(this);
		
		wv_sn_middle.setOnTouchListener(this);
		
		wv_sn_middle.setViewAdapter(new ArrayWheelAdapter<String>(context, (String[]) data2[0]));
		wv_sn_middle.addChangingListener(new MyOnWheelChangedListener());
		wv_sn_middle.addScrollingListener(new MyOnWheelScrollListener());
	}

	/**
	 * %号键盘
	 */
	private void getWheelSign() {
		// TODO Auto-generated method stub
		wv_left = (WheelView) findViewById(R.id.wv_left);
		wv_middle = (WheelView) findViewById(R.id.wv_middle);
		wv_right = (WheelView) findViewById(R.id.wv_right);

		wv_left.setOnTouchListener(this);
		wv_middle.setOnTouchListener(this);
		wv_right.setOnTouchListener(this);
		
		tv_first = (TextView) findViewById(R.id.tv_first);
		tv_second = (TextView) findViewById(R.id.tv_second);
		tv_third = (TextView) findViewById(R.id.tv_third);
		tv_stock_price = (TextView) findViewById(R.id.tv_stock_price);
		
		tv_typeDes = (TextView)findViewById(R.id.tv_buy_sell);
		findViewById(R.id.bt_123).setOnClickListener(this);
		findViewById(R.id.bt_finish).setOnClickListener(this);

		wv_left.setViewAdapter(new ArrayWheelAdapter<String>(context, (String[]) data[0]));
		wv_middle.setViewAdapter(new ArrayWheelAdapter<String>(context, (String[]) data[1]));
		wv_right.setViewAdapter(new ArrayWheelAdapter<String>(context, (String[]) data[2]));

		wv_left.addChangingListener(new MyOnWheelChangedListener());
		wv_middle.addChangingListener(new MyOnWheelChangedListener());
		wv_right.addChangingListener(new MyOnWheelChangedListener());

		wv_left.addScrollingListener(new MyOnWheelScrollListener());
		wv_middle.addScrollingListener(new MyOnWheelScrollListener());
		wv_right.addScrollingListener(new MyOnWheelScrollListener());

	}
	
	class MyOnWheelChangedListener implements OnWheelChangedListener {

		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {

//			setValue();
		}

	}

	class MyOnWheelScrollListener implements OnWheelScrollListener {

		@Override
		public void onScrollingStarted(WheelView wheel) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {

			if(wheel.equals(wv_sn_middle)) {
				setSnValue();
				if(mOnStockKeyListener != null) {
					mOnStockKeyListener.onStockKey(R.id.bt_sn_finish, toCount);
				}
			} else {
				setPriceValue();
				if(mOnStockKeyListener != null) {
					mOnStockKeyListener.onStockKey(R.id.bt_finish, toOrderprice);
				}
			}
		}

	}

	private void setPriceValue() {
		String str_value = "";
		int sign = 1;
		if (0 == wv_left.getCurrentItem()) {
			str_value = "按涨幅达";
			sign = 1;
		} else {
			str_value = "按跌幅达";
			sign = -1;
		}
		tv_first.setText(str_value);

		tv_second.setText(wv_middle.getCurrentItem() + ".");

		if (0 == wv_right.getCurrentItem())
			str_value = "00";
		else
			str_value = "50";

		tv_third.setText(str_value + "%");
		String value = wv_middle.getCurrentItem() + "." + str_value;
		try {
			float p = (1 + sign * Float.parseFloat(value)/100) * Float.parseFloat(marketPrice) / 10;
			toOrderprice = String.format("%.2f", p/100) + "";
			tv_stock_price.setText("("+String.format("%.2f", p/100)+")");
		} catch(NumberFormatException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
		} catch(IllegalFormatException e) {
			e.printStackTrace();
		}
		//区分买入卖出
		if("0".equals(type)) {
			
		} else if("1".equals(type)) {
			
		}
		log.debug("setPriceValue toOrderprice : "+ toOrderprice);
	}

	private void setSnValue() {
		String str_value;
		float multi = 100;
		if(0 == wv_sn_middle.getCurrentItem()) {
			str_value = ", 1/2";
			multi = 200f;
		}else if(1 == wv_sn_middle.getCurrentItem()) {
			str_value = ", 1/3";
			multi = 300f;
		}else if(2 == wv_sn_middle.getCurrentItem()) {
			str_value = ", 1/4";
			multi = 400f;
		}else {
			str_value = "";
			multi = 100f;
		}
		toCount = ((int)(maxCountStock / multi)*100)+"";
		tv_sn_number.setText(maxCountStock+"股");
		tv_fourth.setText(str_value);
		log.debug("setSnValue toCount : "+ toCount);
	}

	/**
	 * 显示股价键盘
	 */
	private void isVisible_SP(){
//		ll_stock_price_keyboard.startAnimation(push_in);
		ll_stock_price_keyboard.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏股价键盘
	 */
	private void isGone_SP(){
//		ll_stock_price_keyboard.startAnimation(push_out);
		ll_stock_price_keyboard.setVisibility(View.GONE);
	}
	
	/**
	 * 显示股数键盘
	 */
	private void isVisible_SN(){
//		ll_stock_number_keyboard.startAnimation(push_in);
		ll_stock_number_keyboard.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 隐藏股数键盘
	 */
	private void isGone_SN(){
//		ll_stock_number_keyboard.startAnimation(push_out);
		ll_stock_number_keyboard.setVisibility(View.GONE);
	}
	
	/**
	 * 显示股价滚轮
	 */
	private void isVisible_SP_Wheel(){
//		ll_sp_wheel_kb.startAnimation(push_in);
		ll_sp_wheel_kb.setVisibility(View.VISIBLE);
		wv_left.setCurrentItem(0);
		wv_middle.setCurrentItem(0);
		wv_right.setCurrentItem(0);
		setPriceValue();
		showSPWheel = true;
	}
	
	/**
	 * 隐藏股价滚轮
	 */
	private void isGone_SP_Wheel(){
//		ll_sp_wheel_kb.startAnimation(push_out);
		ll_sp_wheel_kb.setVisibility(View.GONE);
		showSPWheel = false;
	}
	
	/**
	 * 显示股数滚轮
	 */
	private void isVisible_SN_Wheel(){
//		ll_sn_wheel_kb.startAnimation(push_in);
		ll_sn_wheel_kb.setVisibility(View.VISIBLE);
		wv_sn_middle.setCurrentItem(0);
		setSnValue();
		if(mOnStockKeyListener != null) {
			mOnStockKeyListener.onStockKey(R.id.bt_sn_finish, toCount);
		}
		showSNWheel = true;
	}
	
	/**
	 * 隐藏股数滚轮
	 */
	private void isGone_SN_Wheel(){
//		ll_sn_wheel_kb.startAnimation(push_out);
		ll_sn_wheel_kb.setVisibility(View.GONE);
		showSNWheel = false;
	}

	public boolean isShowSPWheel() {
		return showSPWheel;
	}

	public boolean isShowSNWheel() {
		return showSNWheel;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.getParent().requestDisallowInterceptTouchEvent(true); 
		return false;
	}
}
