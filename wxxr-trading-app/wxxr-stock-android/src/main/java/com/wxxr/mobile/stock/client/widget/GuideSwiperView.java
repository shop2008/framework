package com.wxxr.mobile.stock.client.widget;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.binding.IViewPagerSelCallBack;

public class GuideSwiperView extends ViewPager implements IDataChangedListener{

	private IObservableListDataProvider dataProvider;
	/**引导图片集合*/
	private List<View> mViewList = null;
	private ImageView view = null;
	private Context mContext;
	private IViewPagerSelCallBack iSelCallBack;
	private int mViewCount;
	private int mCurSelected;
	private ViewPagerChangedListener listener;
	public GuideSwiperView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(IObservableListDataProvider dataProvider) {
		IObservableListDataProvider oldProv = this.dataProvider;
		this.dataProvider = dataProvider;
		if(this.dataProvider != null){
			this.dataProvider.registerDataChangedListener(this);
		}else if(oldProv != null){
			oldProv.unregisterDataChangedListener(this);
		}
	}	
	
	@Override
	public void dataSetChanged() {
		initData();
	}

	@Override
	public void dataItemChanged() {
		initData();
	}
	
	private void initData(){
		if(dataProvider==null) return;
		int size = dataProvider.getItemCounts();
		if(size==0) return; 
		mViewList = new ArrayList<View>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mViewCount = size;
		for(int i=0; i<size;i++){
			String img = dataProvider.getItem(i).toString();
			if(img!=null && !StringUtils.isBlank(img)){
				int temp = getImage(img);
				view = new ImageView(mContext);
				view.setLayoutParams(params);
				Bitmap bitmap = readBitMap(mContext, temp);
				Drawable drawable =new BitmapDrawable(bitmap);
				view.setBackgroundDrawable(drawable);
				mViewList.add(view);
			}
		}
		if(mViewList!=null && mViewList.size()>0){
			this.setAdapter(new ViewPagerAdapter(mViewList));
			this.setCurrentItem(0);
			listener = new ViewPagerChangedListener();
			this.setOnPageChangeListener(listener);
		}
	}
	
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
//		opt.inSampleSize = 2;
		opt.inInputShareable = true;
		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
		}
	
	private int getImage(String val){
		if(val!=null && !StringUtils.isBlank(val)){
			int img = RUtils.getInstance().getResourceIdByURI(val);
			return img;
		}
		return 0;
	}
	
	private class ViewPagerAdapter extends PagerAdapter {
		private List<View> provider;

		public ViewPagerAdapter(List<View> provider) {
			this.provider = provider;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(provider.get(position), 0);
			return provider.get(position);
		}

		@Override
		public int getCount() {
			return provider.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(provider.get(position));
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
	    	mCurSelected = position;
	    	if (iSelCallBack != null) {
				iSelCallBack.selected(position);
			}
		}

	}	
	
	public void setCurrentPage(int position) {
		this.setCurrentItem(position);
	}
	
	public void setIViewPageSelCallBack(IViewPagerSelCallBack iViewPagerSelCallBack) {
		this.iSelCallBack = iViewPagerSelCallBack;
	}
}
