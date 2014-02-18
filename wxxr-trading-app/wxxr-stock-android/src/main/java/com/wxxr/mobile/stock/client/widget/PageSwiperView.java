package com.wxxr.mobile.stock.client.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.stock.client.R;

public class PageSwiperView extends LinearLayout {
	private static final Trace log = Trace.register(PageSwiperView.class);

	private LinearLayout paginationLayout; // 分页布局
	private View[] paginationImgView; // 分页图片
	private Context mContext;
	private ViewPager swiperPager;
	private int mViewCount = 0;
	private List<View> viewList = null;
	private boolean showPagination = true;

	private final static int START_SWIPE_MSG = 0x11;

	private int curPageIndex = 0;
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_SWIPE_MSG:

				swiperPager.setCurrentItem(curPageIndex);
				curPageIndex++;
				if (curPageIndex > swiperPager.getChildCount()) {
					curPageIndex = 0;
				}
				handler.sendEmptyMessageDelayed(START_SWIPE_MSG, 5000);
				break;
			}
		};
	};

	private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			if (log.isDebugEnabled()) {
				log.debug("Received datset changed event, going to reload all pages");
			}
			/*handler.removeMessages(START_SWIPE_MSG);
			curPageIndex = 0;*/
			//stopPageSwipe();
			handler.removeMessages(START_SWIPE_MSG);
			bindImageList();
		}

		@Override
		public void onInvalidated() {
			if (log.isDebugEnabled()) {
				log.debug("Received datset invalidated event, going to reload all pages");
			}

			handler.removeMessages(START_SWIPE_MSG);
			//curPageIndex = 0;
			bindImageList();
		}

	};

	private static final String TAG = "PageSwiperView";
	private ListAdapter mAdapter;

	public PageSwiperView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public PageSwiperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * 初始化组件
	 * */
	private void init() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.page_swiper_view_layout, null);
		if (view != null) {
			paginationLayout = (LinearLayout) view.findViewById(R.id.swiper_pagination_layout);
			swiperPager = (ViewPager) view.findViewById(R.id.swiper_view_pager);
			swiperPager.requestDisallowInterceptTouchEvent(true);
		}
		addView(view);
	}

	public void setAdapter(ListAdapter adaptor) {
		if (this.mAdapter == adaptor) {
			return;
		}
		if (this.mAdapter != null) {
			this.mAdapter.unregisterDataSetObserver(observer);
		}
		this.mAdapter = adaptor;
		if (mAdapter != null) {
			bindImageList();
			this.mAdapter.registerDataSetObserver(observer);
		}
	}

	public ListAdapter getAdapter() {
		return mAdapter;
	}

	public void setShowPagination(boolean val) {
		this.showPagination = val;
		if (showPagination) {
			paginationLayout.setVisibility(View.VISIBLE);
		} else {
			paginationLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// this.requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}

	private void bindImageList() {
		if (getAdapter() == null)
			return;
		
		if (paginationLayout.getChildCount() > 0) {
			paginationLayout.removeAllViews();
		}
		mViewCount = getAdapter().getCount();
		if (mViewCount > 0) {
			viewList = new ArrayList<View>();
			paginationImgView = new View[mViewCount];
			for (int i = 0; i < mViewCount; i++) {
				View image = getAdapter().getView(i, null, null);
				if (image != null) {
					viewList.add(image);
					paginationImgView[i] = new View(mContext);
					paginationImgView[i].setPadding(8, 8, 8, 8);
					paginationImgView[i].setBackgroundResource(0);
					paginationImgView[i].setEnabled(true);
					paginationImgView[i]
							.setBackgroundResource(R.drawable.guide_round);
					paginationImgView[i].setTag(i);
					if (i == 0) {
						paginationImgView[0].setEnabled(false);
					}
					if (paginationLayout != null ) {
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								14, 14);
						params.setMargins(8, 0, 0, 0);
						paginationLayout.addView(paginationImgView[i], params);
					}
				}
			}
			swiperPager.setAdapter(new ViewPagerAdapter(viewList));
			swiperPager.setCurrentItem(0);
			curPageIndex = 0;
			swiperPager.setOnPageChangeListener(new ViewOnPageChangeListener());
			startPageSwipe();
		}
	}

	private void startPageSwipe() {
		Message msg = handler.obtainMessage();
		msg.what = START_SWIPE_MSG;
		handler.sendMessageDelayed(msg, 5000);
	}
	
	private void stopPageSwipe() {
		handler.removeMessages(START_SWIPE_MSG);
		startPageSwipe();
	}

	private class ViewPagerAdapter extends PagerAdapter {
		private List<View> viewlist;

		public ViewPagerAdapter(List<View> list) {
			this.viewlist = list;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewlist.get(position), 0);
			return viewlist.get(position);
		}

		@Override
		public int getCount() {
			return viewlist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewlist.get(position));
		}
	}

	public class ViewOnPageChangeListener implements OnPageChangeListener {
		private int mCurSel = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			//handler.removeMessages(START_SWIPE_MSG);
			stopPageSwipe();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			stopPageSwipe();
		}

		@Override
		public void onPageSelected(int position) {
			if (position < 0 || position > mViewCount - 1
					|| this.mCurSel == position) {
				return;
			}
			paginationImgView[this.mCurSel].setEnabled(true);
			paginationImgView[position].setEnabled(false);
			this.mCurSel = position;
			curPageIndex = position;
			stopPageSwipe();
			//startPageSwipe();
		}
	}
}
