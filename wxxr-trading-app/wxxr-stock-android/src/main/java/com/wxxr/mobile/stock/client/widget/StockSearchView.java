package com.wxxr.mobile.stock.client.widget;

import java.util.List;

import com.wxxr.mobile.stock.client.R;


import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;

public class StockSearchView extends RelativeLayout implements
		OnKeyboardActionListener, OnTouchListener, TextWatcher,
		OnScrollListener {

	/**
	 * 上下文环境
	 */
	private Context context;

	/**
	 * 编辑框
	 */
	private EditText editText;

	/**
	 * 位于上方的编辑框(左边搜索图标和编辑框)
	 */
	private RelativeLayout editBox;

	/**
	 * 整个键盘布局控件
	 */
	private KeyboardView keyboardView;

	/**
	 * 英文键盘
	 */
	private Keyboard eng;

	/**
	 * 股票键盘
	 */
	private Keyboard search_num;

	/**
	 * 英文-股票键盘切换标记位
	 */
	private boolean isEng;

	private ListView stock_result_list;
	private OnTextChangedListener listener;

	private Animation keyBoardAnim;

	public StockSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public StockSearchView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		addEditBox(context);
	}

	private void progressLogic(Context context) {
		eng = new Keyboard(context, R.xml.eng);
		search_num = new Keyboard(context, R.xml.search_num);
		isEng = false;
		keyboardView.setKeyboard(search_num);
		keyboardView.setOnKeyboardActionListener(this);
		keyboardView.setPreviewEnabled(false);
		editText.setOnTouchListener(this);
		editText.addTextChangedListener(this);
		editText.setInputType(InputType.TYPE_NULL);
	}


	/**
	 * 添加文本框
	 * 
	 * @param context
	 */
	private void addEditBox(Context context) {
		editBox = (RelativeLayout) View.inflate(context, R.layout.edit_box,
				null);
		editText = (EditText) editBox.findViewById(R.id.et_stock_search);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, dip2px(context, 60));
		params.topMargin = dip2px(context, 5);

		// 项部显示
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(editBox, params);
	}

	/**
	 * 添加软键盘
	 * 
	 * @param context
	 */
	private void addKeyBoardView(Context context) {
		View view = View.inflate(getContext(), R.layout.keybord, null);
		keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, dip2px(context, 60));
		params.topMargin = dip2px(context, 5);
		// 项部显示
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

		addView(view, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// initEditTextView();

		int childCount = getChildCount();

		addKeyBoardView(context);
		progressLogic(context);
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			if (view instanceof ListView) {
				stock_result_list = (ListView) view;
				stock_result_list.setOnScrollListener(this);
			}
		}
	}

	@Override
	public void onKey(int primaryCode, int[] keyCodes) {
		// TODO Auto-generated method stub
		Editable editable = editText.getText();
		int start = editText.getSelectionStart();

		switch (primaryCode) {
		case 54600:
			editable.insert(start, "600");
			break;
		case 54002:
			editable.insert(start, "002");
			break;
		case 54300:
			editable.insert(start, "300");
			break;
		case 5400:
			editable.insert(start, "00");
			break;
		case 54000:
			// 搜索键
			/*
			 * if (null != cursor && cursor.getCount() == 1) { Stock stock =
			 * getStock(cursor, 0); Intent i = new Intent();
			 * i.setClass(SearchActivity.this,
			 * StockChangeQuotaionActivity.class);
			 * i.putExtra(Constant.KEY_STOCK, stock);
			 * i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			 * startActivity(i); SearchActivity.this.finish(); } else {
			 * Tools.showToast(SearchActivity.this, "可搜索，只有一条数据的情况"); return; }
			 */
			break;

		case Keyboard.KEYCODE_MODE_CHANGE:// 数字英文切换键
			if (isEng) {
				keyboardView.setKeyboard(search_num);
				isEng = false;
			} else {
				keyboardView.setKeyboard(eng);
				isEng = true;
			}
			break;
		case Keyboard.KEYCODE_DELETE:// 删除键
			if (null != editable && editable.length() > 0 && start > 0) {
				editable.delete(start - 1, start);
			}
			break;
		case Keyboard.KEYCODE_CANCEL:// 隐藏键
			if (keyboardView.getVisibility() == View.VISIBLE) {
				keyboardView.setVisibility(View.GONE);
			}
			break;
		case Keyboard.KEYCODE_SHIFT:// 大小写切换键
			changeKey();
			keyboardView.setKeyboard(eng);
			break;
		case 55555:// 清空键
			editable.clear();
			break;
		default:
			// 打印键盘上内容
			editable.insert(start, String.valueOf((char) primaryCode));
			break;
		}
	}

	private void changeKey() {
		List<Key> keylist = eng.getKeys();
		if (isEng) {// 切换到小写
			isEng = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 切换到大写
			isEng = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	private boolean isword(String string) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(string.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

	@Override
	public void onPress(int primaryCode) {

	}

	@Override
	public void onRelease(int primaryCode) {

	}

	@Override
	public void onText(CharSequence text) {

	}

	@Override
	public void swipeDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeRight() {
		// TODO Auto-generated method stub

	}

	@Override
	public void swipeUp() {
		// TODO Auto-generated method stub

	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 供外部使用
	 * 
	 * @param listener
	 */
	public void setOnTextChangedListener(OnTextChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {

			if (keyboardView.getVisibility() != View.VISIBLE) {
				keyboardView.setVisibility(View.VISIBLE);
			} else {
				keyboardView.setVisibility(View.GONE);
			}
		}
		return false;
	}

	public interface OnTextChangedListener {

		/**
		 * 将改变后的文本传递给调用者
		 * 
		 * @param changedText
		 */
		void onTextChanged(String changedText);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (!TextUtils.isEmpty(s)) {
			if (listener != null) {
				listener.onTextChanged(s.toString());
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (keyboardView != null) {
			keyboardView.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
}
