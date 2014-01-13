package com.wxxr.mobile.stock.client.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wxxr.mobile.android.ui.ImageUtils;
import com.wxxr.mobile.core.ui.api.IDataChangedListener;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;

public class imageViewPager extends ViewPager implements IDataChangedListener {

	private IObservableListDataProvider dataProvider;
	private View view = null;
	private List<View> viewList = null;
	public imageViewPager(Context context) {
		super(context);
	}
	public imageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * @return the dataProvider
	 */
	public IObservableListDataProvider getDataProvider() {
		return dataProvider;
	}

	/** 
	 * @param dataProvider the dataProvider to set
	 */
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
		updataUI();
		invalidate();
	}
	@Override
	public void dataItemChanged() {
		updataUI();
		invalidate();
	}
	
	private void updataUI(){
		if(dataProvider==null || dataProvider.getItemCounts()==0)
			return;
		int size = dataProvider.getItemCounts();
		viewList = new ArrayList<View>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,140);
		for(int i=0;i<size;i++){
			if(dataProvider.getItem(i) instanceof String){
				String imgUrl = (String) dataProvider.getItem(i);
				if(imgUrl!=null){
					view = new View(getContext());
					view.setLayoutParams(params);
					ImageUtils.updateViewBackgroupImage(imgUrl, view);
					viewList.add(view);
				}
			}
		}
		if(viewList!=null && viewList.size()>0){
			ViewPagerAdapter imgPage = new ViewPagerAdapter(viewList);
			imgPage.notifyDataSetChanged();
			setAdapter(imgPage);
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
}
