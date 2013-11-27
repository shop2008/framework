package com.wxxr.mobile.stock.client.widget;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.model.BuyStockDetailPage;
import com.wxxr.mobile.stock.client.utils.Utils;
import com.wxxr.mobile.stock.client.widget.StockInputKeyboard.OnStockKeyboardActionListener;

public class BuyStockDetailInputView extends RelativeLayout implements
		OnStockKeyboardActionListener, OnTouchListener, OnCheckedChangeListener {
	
	private static final Trace log = Trace.register(BuyStockDetailInputView.class);
	
	private Context context;
	private EditText priceEditText;
	private EditText countEditText;
	private ScrollView srcollView;
	private RadioGroup radioGroup;
	private StockInputKeyboard keyboardView;
	private GestureDetector gestureDetector;

	private boolean isPrice;
	private boolean priceKeyboardautoShow;
	// attribute
	private String orderPrice;
	private String marketPrice;
	private String fund;

	private int maxCountStock;
	public BuyStockDetailInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public BuyStockDetailInputView(Context context) {
		super(context);
		this.context = context;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
		setCountHint();
		if(keyboardView != null)
			keyboardView.setMaxCountStock(maxCountStock);
	}
	
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
		if(keyboardView != null)
			keyboardView.setMarketPrice(marketPrice);
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	private void initEditView() {
		priceEditText = (EditText) findViewById(R.id.price);
		countEditText = (EditText) findViewById(R.id.count);
		srcollView = (ScrollView) findViewById(R.id.scroll_view);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		keyboardView = (StockInputKeyboard) findViewById(R.id.keyboard_view);
		initView();
	}

	private void setCountHint() {
		if(StringUtils.isEmpty(orderPrice)||StringUtils.isEmpty(fund))
			return;
		try {
			float price = Float.parseFloat(this.orderPrice);
			float fund = Float.parseFloat(this.fund);
			maxCountStock = ((int)(fund/(price*100))*100);
			countEditText.setHint("输入最大可买股数: "+maxCountStock+"股");
			countEditText.setText("");
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		switch (keyCode) {
//		case KeyEvent.KEYCODE_BACK:
//			if (keyboardView.isShown()) {
//				setKeyboardShow(false);
//				return true;
//			}
//
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initEditView();
	}

	private void initView() {
		keyboardView.setOnKeyboardActionListener(this);
		priceEditText.setOnTouchListener(this);
		setEditTextInputNull(priceEditText);

		countEditText.setOnTouchListener(this);
		setEditTextInputNull(countEditText);

		radioGroup.setOnCheckedChangeListener(this);

//		gestureDetector = new GestureDetector(context,
//				new KeyboardGestureDetector());
		srcollView.setOnTouchListener(this);

	}

	void setEditTextInputNull(EditText et) {
		int sdkInt = Build.VERSION.SDK_INT;
		if (sdkInt >= 11) {
			Class<EditText> cls = EditText.class;
			try {
				Method setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(false);
				setShowSoftInputOnFocus.invoke(et, false);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			et.setInputType(InputType.TYPE_NULL);
		}
	}

	int nx = 0;
	int ny = 0;
	int fromY = 0;
	int toY = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		if (v.getId()== R.id.keyboard_view&&gestureDetector.onTouchEvent(event))
//			return true;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.price:
				if (radioGroup.getCheckedRadioButtonId() == R.id.market_price) {
					setKeyboardShow(false);
					clearEditTextFocus();
					return true;
				}
				isPrice = true;
				showInitPriceKeyboard();
				setKeyboardShow(true);
				break;
			case R.id.count:
				isPrice = false;
				showInitCountKeyboard();
				setKeyboardShow(true);
				break;
			case R.id.scroll_view:
				setKeyboardShow(false);
				clearEditTextFocus();
				break;
			default:
				setKeyboardShow(false);
				break;
			}
		}
//		else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			nx = (int) event.getX();
//			ny = (int) event.getY();
//			fromY = (int) event.getY();
//		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			toY = (int) event.getY();
//			if (toY - fromY > 100) {
//				setKeyboardShow(false);
//				clearEditTextFocus();
//			}
//		}
		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.order_price:
			priceEditText.setEnabled(true);
			if (priceKeyboardautoShow) {
				priceKeyboardautoShow = false;
				isPrice = true;
				setKeyboardShow(true);
				priceEditText.requestFocus();
			}
			if (keyboardView.isShown() && isPrice) {
				showInitPriceKeyboard();
				// 光标置为最后
				CharSequence text = priceEditText.getText();
				if (text instanceof Spannable) {
					Spannable spanText = (Spannable) text;
					Selection.setSelection(spanText, text.length());
				}
			}
			break;
		case R.id.market_price:
			priceEditText.setEnabled(false);
			priceEditText.setText(marketPrice + "");
			if (isPrice) {
				setKeyboardShow(false);
				priceKeyboardautoShow = true;
				clearEditTextFocus();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onStockKey(int primaryCode, String keyCodes) {
		Editable priceEditable = priceEditText.getText();
		int priceStart = priceEditText.getSelectionStart();

		Editable countEditable = countEditText.getText();
		int countStart = countEditText.getSelectionStart();

		switch (primaryCode) {
		case R.id.input_btn_del:// 删除键
			if (isPrice) {
				if (null != priceEditable && priceEditable.length() > 0
						&& priceStart > 0) {
					priceEditable.delete(priceStart - 1, priceStart);
				}
			} else {
				if (null != countEditable && countEditable.length() > 0
						&& countStart > 0) {
					countEditable.delete(countStart - 1, countStart);
				}
			}
			break;
		case R.id.input_btn_hide:// 隐藏键
			if (keyboardView.getVisibility() == View.VISIBLE) {
				setKeyboardShow(false);
			}
			clearEditTextFocus();
			break;
		case R.id.input_btn_clean:// 清空键
			if (isPrice) {
				priceEditable.clear();
			} else {
				countEditable.clear();
			}
			break;
		case R.id.bt_market_price:// 市价
			radioGroup.findViewById(R.id.market_price).performClick();
			break;
		case R.id.bt_all:// 全部
			countEditable.clear();
			countEditable.append(maxCountStock+"");
			break;
			
		case R.id.bt_finish:// 价格输完-完成
			priceEditable.clear();
			priceEditable.append(keyCodes);
			break;
			
		case R.id.bt_sn_finish:// 数量输完
			countEditable.clear();
			countEditable.append(keyCodes);
			break;
		default:
			// 打印键盘上内容
			if (isPrice) {
				priceEditable.insert(priceStart, keyCodes);
			} else {
				int countInt = getInputCountValue(keyCodes, countEditable,
						countStart);
				if(countInt > maxCountStock) {
					Toast.makeText(context, "买入数量最大不能超过"+maxCountStock+"股", Toast.LENGTH_SHORT).show();
				} else {
					countEditable.insert(countStart, keyCodes);
				}
			}

			break;
		}
	}
	/** 输入数据前先判断是否越界*/
	private int getInputCountValue(String keyCodes, Editable countEditable,
			int countStart) {
		SpannableStringBuilder editable = new SpannableStringBuilder(countEditable);
		editable.insert(countStart, keyCodes);
		String count = editable.toString();
		int countInt = 0;
		try {
			countInt = Integer.parseInt(count);
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return countInt;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				int[] location = Utils.getViewRBPoint(keyboardView);
				srcollView.smoothScrollTo(0, location[1]);
			} else {
				keyboardView.setVisibility(View.GONE);
				keyboardView.allKeyboardGone();
			}
		}
	};
	/** 键盘延迟，做个动画*/
	protected void setKeyboardShow(boolean show) {
		if (show) {
			keyboardView.setVisibility(View.VISIBLE);
			handler.sendEmptyMessageDelayed(0, 100);
		} else {
			srcollView.smoothScrollTo(0, 0);
			handler.sendEmptyMessageDelayed(1, 100);

		}
	}
	/** 不是滚轮，则显示键盘*/
	protected void showInitPriceKeyboard() {
		if (keyboardView != null && !keyboardView.isShowSPWheel())
			keyboardView.showInitPriceKeyboard();
	}
	/** 不是滚轮，则显示键盘*/
	protected void showInitCountKeyboard() {
		if (keyboardView != null && !keyboardView.isShowSNWheel())
			keyboardView.showInitCountKeyboard();
	}
	/** 清除输入框焦点*/
	void clearEditTextFocus() {
		radioGroup.setFocusableInTouchMode(true);
		radioGroup.setFocusable(true);
		radioGroup.requestFocus();
	}

//	class KeyboardGestureDetector extends SimpleOnGestureListener {
//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			if (e2.getY() > e1.getY()) {
//				return true;
//			}
//			return false;
//		}
//	}
}
