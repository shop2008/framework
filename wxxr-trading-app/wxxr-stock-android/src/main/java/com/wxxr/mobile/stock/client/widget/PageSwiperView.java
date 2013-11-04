package com.wxxr.mobile.stock.client.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.wxxr.mobile.stock.client.R;

public class PageSwiperView extends RelativeLayout {

	private LinearLayout paginationLayout; //分页布局
	private View[] paginationImgView; //分页图片
	private Context mContext;
	private ImageSwiperViewGroup swiperViewGroup;
	private int mViewCount = 0;
	private int mCurSel = 0;
	
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
	
	/**
	 * 初始化组件
	 * */
	private void init(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

		if(swiperViewGroup==null){
			swiperViewGroup = new ImageSwiperViewGroup(mContext);
			swiperViewGroup.SetOnImageViewChangeListener(new ImageSwiperViewGroup.OnImageViewChangeListener() {
				@Override
				public void OnViewChange(int position) {
					setCurrentIndex(position);
				}
			});
			this.addView(swiperViewGroup, params);
		}
		
		//分页布局
		if(paginationLayout==null){
			paginationLayout = new LinearLayout(mContext);
			paginationLayout.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
			paginationLayout.setPadding(8, 8, 8, 8);
			paginationLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			this.addView(paginationLayout,params);
		}		
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
		if(swiperViewGroup.getChildCount()>0){
			swiperViewGroup.removeAllViews();
		}
		mViewCount = getAdapter().getCount();
		if(mViewCount>0){
			paginationImgView = new View[mViewCount];
			for(int i=0; i<mViewCount;i++){
				View image = getAdapter().getView(i, null, null);
				if(image!=null){
					swiperViewGroup.addView(image);
					paginationImgView[i] = new View(mContext);
					paginationImgView[i].setBackgroundResource(R.drawable.guide_round);
					paginationImgView[i].setTag(i);
					if(i==0){
						paginationImgView[i].setEnabled(false);
					}else{
						paginationImgView[i].setEnabled(true);
					}
					if(paginationLayout!=null){
						paginationLayout.addView(paginationImgView[i]);
					}
				}
			}
		}
	}
	
	
    private void setCurrentIndex(int position)
    {
    	if (position < 0 || position > mViewCount - 1 || mCurSel == position)
    	{
    		return ;
    	}
    	paginationImgView[mCurSel].setEnabled(true);
    	paginationImgView[position].setEnabled(false);
    	mCurSel = position;
    }
}
