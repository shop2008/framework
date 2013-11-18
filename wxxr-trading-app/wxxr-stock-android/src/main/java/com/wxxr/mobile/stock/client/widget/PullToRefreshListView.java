package com.wxxr.mobile.stock.client.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.stock.app.IStockAppToolbar;
import com.wxxr.mobile.stock.client.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {
//    private static final String TAG = "XListView";

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IRefreshViewListener mListViewListener;

    // -- header view
    private RefreshHeaderView mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.
    // -- footer view
    private RefreshFooterView mFooterView;
    private boolean mEnablePullLoad = true;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >= 50px
    // at bottom, trigger
    // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
    // feature.

    private LinearLayout mFooterLayout;

    public PullToRefreshListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new RefreshHeaderView(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.header_hint_time);
        addHeaderView(mHeaderView);

        // init footer view
        mFooterView = new RefreshFooterView(context);
        mFooterLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mFooterLayout.addView(mFooterView, params);

        // init header height
        ViewTreeObserver observer = mHeaderView.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mHeaderViewHeight = mHeaderViewContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
//                    	if(Build.VERSION.SDK_INT>=16){  
//                    		observer.removeOnGlobalLayoutListener(this);  
//                        }else{  
                        	observer.removeGlobalOnLayoutListener(this);  
//                        }
                    }
                }
            });
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        // make sure XFooterView is the last footer view, and only add once.
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(mFooterLayout);
        }
        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.setBottomMargin(0);
            mFooterView.hide();
            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterView.show();
            mFooterView.setState(RefreshFooterView.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
            notifyToolBar();
        }
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
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(RefreshFooterView.STATE_NORMAL);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(RefreshHeaderView.STATE_READY);
            } else {
                mHeaderView.setState(RefreshHeaderView.STATE_NORMAL);
            }
        }

        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // not visible.
            return;
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
                // more.
                mFooterView.setState(RefreshFooterView.STATE_READY);
            } else {
                mFooterView.setState(RefreshFooterView.STATE_NORMAL);
            }
        }
        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(RefreshFooterView.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 ||
                        deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView
                        .getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            default:
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(RefreshHeaderView.STATE_REFRESHING);
                        if (mListViewListener != null) {
                            mListViewListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public void setRefreshViewListener(IRefreshViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IRefreshViewListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
