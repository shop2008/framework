package com.wxxr.mobile.stock.client.widget;

import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wxxr.mobile.core.util.DateUtil;
import com.wxxr.mobile.stock.client.R;

public class TitleBarLayout extends RelativeLayout implements OnClickListener {

	public interface OnActionClickListener {
		public void onActionClick(ActionClickType type);
	}

	public enum ActionClickType {
		LEFT_NAV, RIGHT_NAV, SEARCH
	}

	public enum TitleBarType {
		MAIN_NAV, NAV_BACK
	}

	private Context mContext;
	private TextView mTitle;
	private ImageButton mLeftNavButton;
	private ImageButton mRightNavButton;
	private ImageButton mSearchNavButton;

	private TextView mUpdateTime;
	private Rotate3DViewSwitcher mSwitcher;

	private OnActionClickListener mActionClickListener;

	public TitleBarLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public TitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public void initView() {
		mTitle = (TextView) findViewById(R.id.tv_title_content);
		mLeftNavButton = (ImageButton) findViewById(R.id.ib_toggle_left_menu);
		mRightNavButton = (ImageButton) findViewById(R.id.ib_toggle_right_menu);
		mSearchNavButton = (ImageButton) findViewById(R.id.ib_toggle_search);

		mUpdateTime = (TextView) findViewById(R.id.time);

		mSwitcher = (Rotate3DViewSwitcher) findViewById(R.id.view_switch);
//		mSwitcher.setDisplayedChild(0);
//		handler.sendEmptyMessageDelayed(0, 1000);
	}

	public void initActionBar(TitleBarType titleType, String title,
			OnActionClickListener l) {
		if (titleType == TitleBarType.MAIN_NAV) {
			mLeftNavButton.setImageResource(R.drawable.list_button);
			mRightNavButton.setImageResource(R.drawable.message_button);
			mSearchNavButton.setImageResource(R.drawable.finds);
			mLeftNavButton.setVisibility(View.VISIBLE);
			mRightNavButton.setVisibility(View.VISIBLE);
			mSearchNavButton.setVisibility(View.VISIBLE);
		} else if (titleType == TitleBarType.NAV_BACK) {
			mLeftNavButton.setImageResource(R.drawable.back_button);
			mLeftNavButton.setVisibility(View.VISIBLE);
			mRightNavButton.setVisibility(View.INVISIBLE);
			mSearchNavButton.setVisibility(View.INVISIBLE);
		} else {
			mLeftNavButton.setImageResource(R.drawable.back_button);
			mLeftNavButton.setVisibility(View.VISIBLE);
			mRightNavButton.setVisibility(View.INVISIBLE);
			mSearchNavButton.setVisibility(View.INVISIBLE);
		}
		
		mTitle.setText(title);
		mActionClickListener = l;
	}

	public void refreshComplete() {
		mUpdateTime.setText("最后更新：" + DateUtil.formatDate(new Date()));
		mSwitcher.setRorateUp();
		mSwitcher.showNext();
		handler.sendEmptyMessageDelayed(0, 1500);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			mSwitcher.setRorateUp();
			mSwitcher.setDisplayedChild(0);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.ib_toggle_left_menu:
			if (mActionClickListener != null)
				mActionClickListener.onActionClick(ActionClickType.LEFT_NAV);
			break;
		case R.id.ib_toggle_right_menu:
			if (mActionClickListener != null)
				mActionClickListener.onActionClick(ActionClickType.RIGHT_NAV);
			break;
		case R.id.ib_toggle_search:
			if (mActionClickListener != null)
				mActionClickListener.onActionClick(ActionClickType.SEARCH);
			break;
		default:
			break;
		}
	}
}
