package com.wxxr.mobile.stock.client.widget;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Utils;
import com.wxxr.mobile.stock.client.widget.StockInputKeyboard.OnStockKeyboardActionListener;

public class BuyStockDetailInputView extends RelativeLayout implements
		OnStockKeyboardActionListener, OnTouchListener,
		OnCheckedChangeListener, View.OnKeyListener, ScrollViewListener {

	private static final Trace log = Trace
			.register(BuyStockDetailInputView.class);

	private Context mContext;
	private EditText priceEditText;
	private EditText countEditText;
	private NewScrollView scrollView;
	private RadioGroup radioGroup;
	private StockInputKeyboard keyboardView;

	private boolean isPrice;
	private boolean priceKeyboardautoShow;
	// attribute
	private String orderPrice; //计算最大购买数
	// private String marketPrice;
	private String fund;

	private int maxCountStock;
	//拥有的股票数
	private String maxSellCount;
	//键盘类型 0：买入，1：卖出
	private String type = "0";
	public BuyStockDetailInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public BuyStockDetailInputView(Context context) {
		super(context);
		this.mContext = context;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
		setCountHint();
		if (keyboardView != null)
			keyboardView.setMaxCountStock(maxCountStock);
	}

	public void setMarketPrice(String marketPrice) {
		// this.marketPrice = marketPrice;
		if (keyboardView != null)
			keyboardView.setMarketPrice(marketPrice);
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public void setType(String type) {
		this.type = type;
		if (keyboardView != null)
			keyboardView.setKeyBoardType(type);
	}
	
	public void setMaxSellCount(String maxSellCount) {
		this.maxSellCount = maxSellCount;
		int count = 0;
		try {
			count = Integer.parseInt(maxSellCount);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		setSellCountHint(count);
		if (keyboardView != null)
			keyboardView.setMaxCountStock(count);
	}
	
	private void initEditView() {
		priceEditText = (EditText) findViewById(R.id.price);
		countEditText = (EditText) findViewById(R.id.count);
		scrollView = (NewScrollView) findViewById(R.id.scroll_view);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		keyboardView = (StockInputKeyboard) findViewById(R.id.keyboard_view);
		initView();
		priceEditText.setOnKeyListener(this);
		countEditText.setOnKeyListener(this);
		scrollView.setScrollViewListener(this);
	}

	private void setCountHint() {
		if (StringUtils.isEmpty(orderPrice) || StringUtils.isEmpty(fund))
			return;
		try {
			// fund放大100倍,price放大1000倍；交易数最小值100
			float price = Float.parseFloat(this.orderPrice) / 10;
			float fund = Float.parseFloat(this.fund);
			
			if(price == 0) {
				countEditText.setHint("输入最大可买股数");
			} else {
				maxCountStock = ((int) (fund / (price * 100)) * 100);
				countEditText.setHint("输入最大可买股数: " + maxCountStock + "股");
			}
			countEditText.setText("");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	private void setSellCountHint(int count) {
//		if (StringUtils.isEmpty(orderPrice) || StringUtils.isEmpty(fund))
//			return;
//		try {
//			// fund放大100倍,price放大1000倍；交易数最小值100
//			float price = Float.parseFloat(this.orderPrice) / 10;
//			float fund = Float.parseFloat(this.fund);
//			
//			if(price == 0) {
//				countEditText.setHint("输入最大可买股数");
//			} else {
//				maxCountStock = ((int) (fund / (price * 100)) * 100);
//				countEditText.setHint("输入最大可买股数: " + maxCountStock + "股");
//			}
//			countEditText.setText("");
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}
		countEditText.setHint("输入最大可卖股数: " + count + "股");
	}
	
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
				e.printStackTrace();
			}
		} else {
			et.setInputType(InputType.TYPE_NULL);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
				return true;
			case R.id.count:
				isPrice = false;
				showInitCountKeyboard();
				setKeyboardShow(true);
				return true;
			default:
				break;
			}
		}
		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.order_price:
			priceEditText.setTextColor(Color.WHITE);
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
			priceEditText.setTextColor(Color.GRAY);
			if (isPrice) {
				if(keyboardView.isShown())
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
					if (priceEditable instanceof Spannable) {
						Spannable spanText = (Spannable) priceEditable;
						Selection.setSelection(spanText, priceStart - 1);
					}
				}
			} else {
				if (null != countEditable && countEditable.length() > 0
						&& countStart > 0) {
					countEditable.delete(countStart - 1, countStart);
					if (countEditable instanceof Spannable) {
						Spannable spanText = (Spannable) countEditable;
						Selection.setSelection(spanText, countStart - 1);
					}
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
			if ("0".equals(type)) { // 买入
				countEditable.clear();
				countEditable.append(maxCountStock + "");
			} else if ("1".equals(type)) { // 卖出
				countEditable.clear();
				countEditable.append(maxSellCount);
			}
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
				if ("0".equals(type)) { //买入
					if (countInt > maxCountStock) {
						Toast.makeText(mContext,
								"买入数量最大不能超过" + maxCountStock + "股",
								Toast.LENGTH_SHORT).show();
					} else {
						countEditable.insert(countStart, keyCodes);
					}
				} else if ("1".equals(type)) { //卖出
					int count = 0;
					try {
						count = Integer.parseInt(maxSellCount);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					if (countInt > count) {
						Toast.makeText(mContext,
								"卖出数量最大不能超过" + count + "股",
								Toast.LENGTH_SHORT).show();
					} else {
						countEditable.insert(countStart, keyCodes);
					}
				}
			}

			break;
		}
	}

	/** 输入数据前先判断是否越界 */
	private int getInputCountValue(String keyCodes, Editable countEditable,
			int countStart) {
		SpannableStringBuilder editable = new SpannableStringBuilder(
				countEditable);
		editable.insert(countStart, keyCodes);
		String count = editable.toString();
		int countInt = 0;
		try {
			countInt = Integer.parseInt(count);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return countInt;
	}
	float scraleY = 0;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				scraleY = scrollView.getScrollY();
				int[] location = Utils.getViewRBPoint(keyboardView);
				scrollView.smoothScrollTo(0, location[1]);
			} else {
				keyboardView.setVisibility(View.GONE);
				keyboardView.allKeyboardGone();
			}
		}
	};

	/** 键盘延迟，做个动画 */
	protected void setKeyboardShow(boolean show) {
		if (show) {
			keyboardView.setVisibility(View.VISIBLE);
			handler.sendEmptyMessageDelayed(0, 100);
		} else {
			scrollView.smoothScrollTo(0, (int)scraleY);
			handler.sendEmptyMessageDelayed(1, 100);
		}
	}

	/** 不是滚轮，则显示键盘 */
	protected void showInitPriceKeyboard() {
		if (keyboardView != null && !keyboardView.isShowSPWheel())
			keyboardView.showInitPriceKeyboard();
	}

	/** 不是滚轮，则显示键盘 */
	protected void showInitCountKeyboard() {
		if (keyboardView != null && !keyboardView.isShowSNWheel())
			keyboardView.showInitCountKeyboard();
	}

	/** 清除输入框焦点 */
	void clearEditTextFocus() {
		radioGroup.setFocusableInTouchMode(true);
		radioGroup.setFocusable(true);
		radioGroup.requestFocus();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			if (keyboardView.isShown()) {
				setKeyboardShow(false);
				clearEditTextFocus();
				return true;
			}
		}
		return false;
	}

	@Override
	public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx,
			int oldy) {
		if (keyboardView.isShown() && y < oldy) {
			setKeyboardShow(false);
			clearEditTextFocus();
		}
	}
}
