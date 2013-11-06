package com.wxxr.mobile.stock.client.widget;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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

import com.wxxr.mobile.stock.client.R;

public class PageSwiperView extends LinearLayout {

	private LinearLayout paginationLayout; //分页布局
	private View[] paginationImgView; //分页图片
	private Context mContext;
//	private ImageSwiperViewGroup swiperViewGroup;
	private ViewPager swiperPager;
	private int mViewCount = 0;
	private int mCurSel = 0;
	private List<View> viewList = null;
	
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
		initStyle(attrs);
	}

	/**
	 * 初始化样式
	 * */
	private void initStyle(AttributeSet attrs){
//		TypedArray arrs = mContext.obtainStyledAttributes(attrs, R.styleable.ImageSwiper);
//		titleTextColor = arrs.getColor(R.styleable.ImageSwiper_swiperTitleTextColor, Color.parseColor("#FFFFFF"));
//		titleBackgroundColor = arrs.getColor(R.styleable.ImageSwiper_swiperTitleBackgroundColor, Color.parseColor("#000000"));
//		textSize = arrs.getFloat(R.styleable.ImageSwiper_swiperTitleFontSize, 12.0f);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
	/**
	 * 初始化组件
	 * */
	private void init(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.page_swiper_view_layout, null);
		if(view!=null){
			paginationLayout = (LinearLayout) view.findViewById(R.id.swiper_pagination_layout);
			swiperPager = (ViewPager) view.findViewById(R.id.swiper_view_pager);
			swiperPager.requestDisallowInterceptTouchEvent(true);
		}
		addView(view);
	}
	public void setAdapter(ListAdapter dapter){
		this.mAdapter = dapter;
		bindImageList();
	}
	public ListAdapter getAdapter(){
		return mAdapter;
	}
	
	
	private void bindImageList(){
		if(getAdapter()==null)
			return;
		if(paginationLayout.getChildCount()>0){
			paginationLayout.removeAllViews();
		}
		mViewCount = getAdapter().getCount();
		if(mViewCount>0){
			viewList = new ArrayList<View>();
			paginationImgView = new View[mViewCount];
			for(int i=0; i<mViewCount;i++){
				View image = getAdapter().getView(i, null, null);
				if(image!=null){
					viewList.add(image);
					paginationImgView[i] = new View(mContext);
					paginationImgView[i].setPadding(8, 8, 8, 8);
					paginationImgView[i].setBackgroundResource(R.drawable.guide_round);
					paginationImgView[i].setTag(i);
					if(i==0){
						paginationImgView[i].setEnabled(false);
					}else{
						paginationImgView[i].setEnabled(true);
					}
					if(paginationLayout!=null){
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14,14);
						params.setMargins(8, 0, 0, 0);
						paginationLayout.addView(paginationImgView[i],params);
					}
				}
			}
			swiperPager.setAdapter(new ViewPagerAdapter(viewList));
			swiperPager.setCurrentItem(0);
			swiperPager.setOnPageChangeListener(new ViewOnPageChangeListener());
		}
	}

	private class ViewPagerAdapter extends PagerAdapter{

		private List<View> viewlist;
		public ViewPagerAdapter(List<View> list){
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
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewlist.get(position)); 
		}
	}	
	public class ViewOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			if (position < 0 || position > mViewCount - 1 || mCurSel == position)
	    	{
	    		return ;
	    	}
	    	paginationImgView[mCurSel].setEnabled(true);
	    	paginationImgView[position].setEnabled(false);
	    	mCurSel = position;			
		}
	}	
	
}
