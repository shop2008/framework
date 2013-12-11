package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IListAdapterBuilder;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.client.widget.wheel.AbstractPinnedHeaderListAdapter;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView;

@View(name="NewsContentItem")
public abstract class NewsItemView extends ViewBase implements ItemViewSelector, IListAdapterBuilder {
	
	
	@Override
	public String getItemViewId(Object itemData) {
		if (itemData instanceof String) {
			return "NewsTitleItemView";
		} else if (itemData instanceof RemindMessageBean) {
			return "NewsSelecterItemView";
		}
		return null;
	}
	
	@Override
	public IRefreshableListAdapter buildListAdapter(final IUIComponent field,
			IAndroidBindingContext bContext, String itemViewId) {
		
		AbstractPinnedHeaderListAdapter adapter = new AbstractPinnedHeaderListAdapter(bContext,createAdaptorFromValue(field), this) {
			
			@Override
			protected String getHeaderViewId() {
				return "NewsPHeadItemView";
			}
			
			@Override
			protected boolean isHeaderData(Object data) {
				if (data instanceof String) {
					return true;
				} else if (data instanceof RemindMessageBean) {
					return false;
				} 
				
				return false;
			}
		};
		((PinnedHeaderListView)bContext.getBindingControl()).setOnScrollListener(adapter);
		return adapter;
	}
	
	public static IListDataProvider createAdaptorFromValue(final IUIComponent comp) {
		return new IListDataProvider() {
			Object[]  data = null;
			@Override
			public Object getItemId(Object item) {
				return null;
			}

			@Override
			public int getItemCounts() {
				return data != null ? data.length : 0;
			}

			@Override
			public Object getItem(int i) {
				return data[i];
			}

			@Override
			public boolean isItemEnabled(Object item) {
				return true;
			}

			@Override
			public boolean updateDataIfNeccessary() {
				data = getListData(comp);
				return true;
			}
		};
	}

	public static Object[] getListData(final IUIComponent comp){
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			
			List<Object> labels = new ArrayList<Object>();
			labels.add("2013-11-20");
			List<RemindMessageBean> data = new ArrayList<RemindMessageBean>();
			RemindMessageBean bean = new RemindMessageBean();
			bean.setContent("11111");
			bean.setTitle("2222");
			data.add(bean);
			
			bean = new RemindMessageBean();
			bean.setContent("11122");
			bean.setTitle("33333");
			data.add(bean);
			

			bean = new RemindMessageBean();
			bean.setContent("3332");
			bean.setTitle("44444");
			data.add(bean);
			
			labels.addAll(data);
			
			labels.add("2013-11-21");
			labels.addAll(data);
			
			labels.add("2013-11-23");
			labels.addAll(data);
			labels.add("2013-11-24");
			labels.addAll(data);
			labels.add("2013-11-21");
	        labels.addAll(data);
	        labels.add("2013-11-23");
	        labels.addAll(data);
	        labels.add("2013-11-24");
	        labels.addAll(data);
	        labels.add("2013-11-21");
	        labels.addAll(data);
	        labels.add("2013-11-23");
	        labels.addAll(data);
	        labels.add("2013-11-24");
	        labels.addAll(data);
	        labels.add("2013-11-21");
	        labels.addAll(data);
	        labels.add("2013-11-23");
	        labels.addAll(data);
	        labels.add("2013-11-24");
	        labels.addAll(data);
	        
	        labels.add("2013-11-21");
	        labels.addAll(data);
	        labels.add("2013-11-23");
	        labels.addAll(data);
	        labels.add("2013-11-24");
	        labels.addAll(data);
			return labels.toArray();
			//return result != null ? result.toArray() : null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				List<Object> labels = new ArrayList<Object>();
				labels.add("2013-11-20");
				List<RemindMessageBean> data = new ArrayList<RemindMessageBean>();
				RemindMessageBean bean = new RemindMessageBean();
				bean.setContent("11111");
				bean.setTitle("2222");
				data.add(bean);
				
				bean = new RemindMessageBean();
				bean.setContent("11122");
				bean.setTitle("33333");
				data.add(bean);
				

				bean = new RemindMessageBean();
				bean.setContent("3332");
				bean.setTitle("44444");
				data.add(bean);
				
				labels.addAll(data);
				
				labels.add("2013-11-21");
				labels.addAll(data);
				
				labels.add("2013-11-23");
				labels.addAll(data);
				labels.add("2013-11-24");
				labels.addAll(data);
				return labels.toArray();
			}else if((val != null)&&val.getClass().isArray()){
				return (Object[])val;
			}
		}
		return null;
	}

}
