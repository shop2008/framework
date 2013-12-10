package com.wxxr.mobile.stock.client.widget;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.wxxr.mobile.stock.client.R;

public class StockSearchView extends RelativeLayout implements
		OnKeyboardActionListener, OnTouchListener, OnClickListener, OnScrollListener {

	private Context context;
	private EditText editText;
	private ImageView deleteImage;
	private ListView listView;
	private KeyboardView keyboardView;

	private Keyboard eng;
	private Keyboard search_num;
	private boolean isEng;
	private boolean isupper;

	public StockSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public StockSearchView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 初始化文本框
	 * 
	 * @param context
	 */
	private void initEditBox() {
		editText = (EditText) findViewById(R.id.et_stock_search);
		deleteImage = (ImageView) findViewById(R.id.bt_delete);
		listView = (ListView) findViewById(R.id.lv_stock_list);
		keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
		progressLogic();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		progressLogic(context);
		initEditBox();
	}

	private void progressLogic() {
		eng = new Keyboard(context, R.xml.eng);
		search_num = new Keyboard(context, R.xml.search_num);
		isEng = false;
		isupper = false;
		keyboardView.setKeyboard(search_num);
		keyboardView.setOnKeyboardActionListener(this);
		keyboardView.setPreviewEnabled(false);
		editText.setOnTouchListener(this);
		listView.setOnTouchListener(this);
		setEditTextInputNull(editText);
		deleteImage.setOnClickListener(this);
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
			try {
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod(
						"setSoftInputShownOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(et, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			et.setInputType(InputType.TYPE_NULL);
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
		if (isupper) {// 切换到小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 切换到大写
			isupper = true;
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_UP) {
			switch (v.getId()) {
			case R.id.et_stock_search:
				keyboardView.setVisibility(View.VISIBLE);
				break;
			case R.id.lv_stock_list:
				keyboardView.setVisibility(View.GONE);
				break;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.bt_delete:
			editText.setText(null);
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
