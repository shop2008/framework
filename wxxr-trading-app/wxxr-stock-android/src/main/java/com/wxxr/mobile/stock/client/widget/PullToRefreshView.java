package com.wxxr.mobile.stock.client.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.stock.app.IStockAppToolbar;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.SpUtil;

/**
 * 自定义下拉刷新控件
 * @author renwenjie
 */
public class PullToRefreshView extends LinearLayout {
	private static final String TAG = "PullToRefreshView";
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	
	
	private boolean isFooterInvisible = true;
	private boolean isHeaderInvisible = true;
	/**
	 * 最终y点坐标，用于计算划过距离
	 */
	private int mLastMotionY;
	private int mLastMotionX;
	
	/**
	 * 头部下拉刷新控件
	 */
	private View mHeaderView;

	/**
	 * 底部上拉刷新控件
	 */
	private View mFooterView;

	/**
	 * 用于控件ListView、ExpandableListView或GridView
	 */
	private AdapterView<?> mAdapterView;

	/**
	 * 用于控件ScrollView
	 */
	private ScrollView mScrollView;
	
	/**
	 * 头部上拉刷新控件高度
	 */
	private int mHeaderViewHeight;
	
	/**
	 * 底部下拉刷新控件高度
	 */
	private int mFooterViewHeight;
	
	/**
	 * 头部箭头
	 */
	private ImageView mHeaderImageView;
	
	/**
	 * 底部箭头 
	 */
	private ImageView mFooterImageView;
	
	/**
	 * 头部提示文本
	 */
	private TextView mHeaderTextView;

	/**
	 * 底部提示文本
	 */
	private TextView mFooterTextView;
	/**
	 * 头部刷新时间
	 */
	private TextView mHeaderUpdateTextView;
	
	/**
	 * 头部进度条
	 */
	private ProgressBar mHeaderProgressBar;
	
	/**
	 * 底部进度条
	 */
	private ProgressBar mFooterProgressBar;
	
	/**
	 * 布局填充器
	 */
	private LayoutInflater mInflater;
	
	/**
	 * 头部上拉刷新控件状态
	 */
	private int mHeaderState;
	
	/**
	 * 底部下拉刷新控件状态
	 */
	private int mFooterState;
	
	/**
	 * 拉的状态,上拉(pull up) 或 下拉(pull down)或上拉刷新(PULL_UP_STATE)或下拉刷新(PULL_DOWN_STATE)
	 */
	private int mPullState;
	
	/**
	 * 底部刷新时间
	 */
	private TextView mFooterUpdateTextView;
	
	/**
	 * 底部---变为向下的箭头,改变箭头方向
	 */
	private RotateAnimation mFlipAnimationForFooter;
	
	/**
	 * 底部---变为逆向的箭头,旋转
	 */
	private RotateAnimation mReverseFlipAnimationForFooter;
	
	
	/**
	 * 头部---变为向下的箭头,改变箭头方向
	 */
	private RotateAnimation mFlipAnimationForHeader;
	
	/**
	 * 头部---变为逆向的箭头,旋转
	 */
	private RotateAnimation mReverseFlipAnimationForHeader;
	/**
	 * 底部上拉刷新监听器
	 */
	private OnFooterRefreshListener mOnFooterRefreshListener;
	
	/**
	 * 头部下拉刷新监听器
	 */
	private OnHeaderRefreshListener mOnHeaderRefreshListener;

	private boolean isback = false;
	boolean isfootBack= false;
	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

    

	private void init() {
		mFlipAnimationForFooter = new RotateAnimation(0, 180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mFlipAnimationForFooter.setInterpolator(new LinearInterpolator());
		mFlipAnimationForFooter.setDuration(250);
		mFlipAnimationForFooter.setFillAfter(true);
		mReverseFlipAnimationForFooter = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimationForFooter.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimationForFooter.setDuration(250);
		mReverseFlipAnimationForFooter.setFillAfter(true);

		
		mFlipAnimationForHeader = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mFlipAnimationForHeader.setInterpolator(new LinearInterpolator());
		mFlipAnimationForHeader.setDuration(250);
		mFlipAnimationForHeader.setFillAfter(true);
		mReverseFlipAnimationForHeader = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimationForHeader.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimationForHeader.setDuration(250);
		mReverseFlipAnimationForHeader.setFillAfter(true);
		mInflater = LayoutInflater.from(getContext());
		addHeaderView();
	}

	private void addHeaderView() {
		mHeaderView = mInflater.inflate(R.layout.header, this, false);

		mHeaderImageView = (ImageView) mHeaderView
				.findViewById(R.id.head_arrow);
		mHeaderTextView = (TextView) mHeaderView
				.findViewById(R.id.head_title);
		mHeaderUpdateTextView = (TextView) mHeaderView
				.findViewById(R.id.head_lastUpdate);
		mHeaderProgressBar = (ProgressBar) mHeaderView
				.findViewById(R.id.head_progressBar);
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mHeaderViewHeight);
		params.topMargin = -(mHeaderViewHeight);
		addView(mHeaderView, params);
	}

	
	private void addFooterView() {
		mFooterView = mInflater.inflate(R.layout.footer, this, false);
		mFooterImageView = (ImageView) mFooterView
				.findViewById(R.id.footer_load_image);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.footer_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView
				.findViewById(R.id.footer_load_progress);
		
		mFooterUpdateTextView = (TextView) mFooterView.findViewById(R.id.footer_lastUpdate);
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				mFooterViewHeight);
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addFooterView();
		initContentAdapterView();
	}

	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException(
					"this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException(
					"must contain a AdapterView or ScrollView in this layout!");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		int x = (int) e.getRawX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaY = y - mLastMotionY;
			int deltaX = x - mLastMotionX;
			if (isRefreshViewScroll(deltaY) && Math.abs(deltaY) > Math.abs(deltaX)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
	 * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE && isHeaderInvisible) {
				// PullToRefreshView执行下拉
				headerPrepareToRefresh(deltaY);
				// setHeaderPadding(-mHeaderViewHeight);
			} else if (mPullState == PULL_UP_STATE && isFooterInvisible) {
				// PullToRefreshView执行上拉
				Log.i(TAG, "pull up!parent view move!");
				footerPrepareToRefresh(deltaY);
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE && isHeaderInvisible) {
				if (topMargin >= 0) {
					// 开始刷新
					headerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE && isFooterInvisible) {
				if (Math.abs(topMargin) >= mHeaderViewHeight
						+ mFooterViewHeight) {
					// 开始执行footer 刷新
					footerRefreshing();
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}
		case MotionEvent.ACTION_CANCEL:

			break;
		}
		return super.onTouchEvent(event);
	}


	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 * 
	 * @param deltaY
	 *            , deltaY>0 是向下运动,<0是向上运动
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		
		int absDeltay = Math.abs(deltaY);
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0 && absDeltay > 10 && isHeaderInvisible) {

				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 8) {// 这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < 0 && absDeltay > 10 && isFooterInvisible) {
				View lastChild = mAdapterView.getChildAt(mAdapterView
						.getChildCount() - 1);
				if (lastChild == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView
								.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& child.getMeasuredHeight() <= getHeight()
							+ mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 * 
	 * @param deltaY 手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderTextView.setText("松开即可更新…");
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimationForHeader);
			mHeaderState = RELEASE_TO_REFRESH;
			isback = true;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
			if (isback) {
				mHeaderImageView.clearAnimation();
				mHeaderImageView.startAnimation(mReverseFlipAnimationForHeader);
				isback = false;
			}
			mHeaderTextView.setText("下拉即可更新…");
			mHeaderState = PULL_TO_REFRESH;
			String date = SpUtil.getInstance(getContext()).find(Constants.KEY_DOWN_REFRESH_DATE);
			if (TextUtils.isEmpty(date)) {
				mHeaderUpdateTextView.setText("最后更新:无更新记录");
			} else {
				mHeaderUpdateTextView.setText("最后更新:"+date);
			}
		}
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 * 
	 * @param deltaY 手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTextView
					.setText("松开即可加载…");
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimationForFooter);
			mFooterState = RELEASE_TO_REFRESH;
			isfootBack = true;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			if (isfootBack) {
				mFooterImageView.startAnimation(mReverseFlipAnimationForFooter);
				isfootBack = false;
			}
			mFooterTextView.setText("上拉即可加载…");
			mFooterState = PULL_TO_REFRESH;
			String date = SpUtil.getInstance(getContext()).find(Constants.KEY_UP_REFRESH_DATE);
			if (TextUtils.isEmpty(date)) {
				mFooterUpdateTextView.setText("最后加载:无更新记录");
			} else {
				mFooterUpdateTextView.setText("最后加载:"+date);
			}
		}
	}

	/**
	 * 修改Header view top margin的值
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,感谢网友yufengzungzhe的指出
		// 表示如果是在上拉后一段距离,然后直接下拉
		if (deltaY > 0 && mPullState == PULL_UP_STATE
				&& Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE
				&& Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * 头部正在刷新
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		mHeaderTextView.setText("加载中...");
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
	}

	/**
	 * 底部正在刷新
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		mFooterTextView
				.setText("加载中...");
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 设置header view 的topMargin的值
	 * @param topMargin，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	void notifyToolBar() {
		final SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		IWorkbench bench = AppUtils.getService(IWorkbenchManager.class).getWorkbench();
		String id = bench.getActivePageId();
		IPage page = bench.getPage(id);
		IStockAppToolbar tool = ((IStockAppToolbar) page.getPageToolbar());
		if (tool != null) {
			tool.showNotification("最后更新:" + fmt.format(new Date()), null);
		}
	}
	
	/**
	 * header view 完成更新后恢复初始状态
	 */
	@SuppressWarnings("deprecation")
	public void onHeaderRefreshComplete(boolean success) {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(R.drawable.arrow_down);
		mHeaderTextView.setText("下拉即可更新…");
		mHeaderProgressBar.setVisibility(View.GONE);
		String date = new Date().toLocaleString();
		mHeaderUpdateTextView.setText("最后更新：" + date);
		SpUtil.getInstance(getContext()).save(Constants.KEY_DOWN_REFRESH_DATE, date);
		mHeaderState = PULL_TO_REFRESH;
		if(success)
			notifyToolBar();
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	@SuppressWarnings("deprecation")
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(R.drawable.arrow_up);
		mFooterTextView.setText("上拉即可加载…");
		mFooterProgressBar.setVisibility(View.GONE);
		String date = new Date().toLocaleString();
		mFooterUpdateTextView.setText("最后加载：" + date);
		SpUtil.getInstance(getContext()).save(Constants.KEY_UP_REFRESH_DATE, date);
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * 获取当前header view 的topMargin
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}


	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}


	public interface OnFooterRefreshListener {
		public void onFooterRefresh(PullToRefreshView view);
	}

	public interface OnHeaderRefreshListener {
		public void onHeaderRefresh(PullToRefreshView view);
	}
	
	public void setFooterView(boolean show) {
		mFooterView.setVisibility(show?View.VISIBLE:View.GONE);
		isFooterInvisible = show;
	}
	
	public void setHeaderView(boolean show) {
		mHeaderView.setVisibility(show?View.VISIBLE:View.GONE);
		isHeaderInvisible = show;
	}
}
