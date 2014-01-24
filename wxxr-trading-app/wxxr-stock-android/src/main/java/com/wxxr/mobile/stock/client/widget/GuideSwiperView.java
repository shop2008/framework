package com.wxxr.mobile.stock.client.widget;


import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.binding.IViewPagerSelCallBack;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class GuideSwiperView extends LinearLayout {

	
	private ViewPager mViewPager;
	
	
	/**引导图片适配器*/
	private ListAdapter mListAdapter;
	
	/**引导图片集合*/
	private List<View> mViewList;
	
	private Context mContext;
	private int mViewCount;
	
	private int mCurSelected;
	
	private IViewPagerSelCallBack iSelCallBack;

	public GuideSwiperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GuideSwiperView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		View view = View.inflate(context, R.layout.guide_swiper_view_layout, null);
		mViewPager = (ViewPager) view.findViewById(R.id.guide_view_pager);
		addView(view);
	}

	public void setAdapter(ListAdapter adapter) {
		if (adapter != null) {
			this.mListAdapter = adapter;
			loadImages(adapter);
		}
	}

	private void loadImages(ListAdapter adapter) {
		if (mListAdapter == null) {
			return;
		}
		
		/*if (dotsContainer.getChildCount() > 0) {
			dotsContainer.removeAllViews();
		}
		*/
		mViewCount = mListAdapter.getCount();
		
		if (mViewCount > 0) {
			mViewList = new ArrayList<View>();
			//dots = new View[mViewCount];
			for(int i=0;i<mViewCount;i++) {
				View view = mListAdapter.getView(i, null, null);
				
				if (view != null) {
					mViewList.add(view);
					//dots[i] = new View(mContext);
					//dots[i].setBackgroundResource(R.drawable.bg_guide_dot);
					/*if (i==0) {
						dots[i].setEnabled(true);
					} else {
						dots[i].setEnabled(false);
					}*/
					
					/*if (dotsContainer != null) {
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
						if (i != 0) {
							params.setMargins(10, 0, 0, 0);
						}
						dotsContainer.addView(dots[i],params);
						invalidate();
					}*/
				}
			}
			
			mViewPager.setAdapter(new ViewPagerAdapter(mViewList));
			mViewPager.setCurrentItem(0);
			mViewPager.setOnPageChangeListener(new ViewPagerChangedListener());
		}
	}
	
	private class ViewPagerAdapter extends PagerAdapter{

		private List<View> viewList;
		public ViewPagerAdapter(List<View> viewList) {
			this.viewList = viewList;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position), 0); 
			return viewList.get(position);
		}
		
		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position)); 
		}
	}
	
	private class ViewPagerChangedListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
		@Override
		public void onPageSelected(int position) {
			if (position < 0 || position > mViewCount - 1 || mCurSelected == position)
	    	{
	    		return ;
	    	}
	    	/*dots[mCurSelected].setEnabled(false);
	    	dots[position].setEnabled(true);*/
	    	mCurSelected = position;
	    	if (iSelCallBack != null) {
				iSelCallBack.selected(position);
			}
		}

	}
	
	public void setCurrentPage(int position) {
		if(mViewPager != null) {
			mViewPager.setCurrentItem(position);
		}
	}
	
	public void setIViewPageSelCallBack(IViewPagerSelCallBack iViewPagerSelCallBack) {
		this.iSelCallBack = iViewPagerSelCallBack;
	}
}
