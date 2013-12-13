package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wxxr.mobile.stock.client.binding.AbstractPinnedHeaderListAdapter;
import com.wxxr.mobile.stock.client.utils.Utils;

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
	public String[] getAllViewIds() {
		return new String[] {"NewsTitleItemView","NewsSelecterItemView"};
	}

	@Override
	public IRefreshableListAdapter buildListAdapter(IUIComponent field,
			IAndroidBindingContext bContext, String itemViewId) {
		
		AbstractPinnedHeaderListAdapter adapter = new AbstractPinnedHeaderListAdapter(bContext,createAdaptorFromValue(field), this) {
			
			@Override
			protected String getHeaderViewId() {
				return "NewsTitleItemView";
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
		return adapter;
	}
	
	public static IListDataProvider createAdaptorFromValue(IUIComponent comp) {
		final IUIComponent c = comp;
		return new IListDataProvider() {
			Object[] data = null;
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
				return data.length>0?data[i]:null;
			}

			@Override
			public boolean isItemEnabled(Object item) {
				return true;
			}

			@Override
			public boolean updateDataIfNeccessary() {
				data = getListData(c);
				return true;
			}
		};
	}

	public static Object[] getListData(IUIComponent comp){
		//List<Object>  beans = new ArrayList<Object>();
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			//List<Object>  beans = new ArrayList<Object>();
			
			List<Object>  beans = new ArrayList<Object>();
			beans.add("2010-11-10");
			List<RemindMessageBean> messageBeans = new ArrayList<RemindMessageBean>();
			RemindMessageBean bean = new RemindMessageBean();
			Map<String, String> map;
			bean.setContent("11111");
			bean.setTitle("22222");
			bean.setCreatedDate("2012-11-10");
			map = new HashMap<String, String>();
			map.put("time", ""+System.currentTimeMillis());
			bean.setAttrs(map);
			messageBeans.add(bean);
			
			bean = new RemindMessageBean();
			bean.setContent("11111");
			bean.setTitle("22222");
			bean.setCreatedDate("2012-11-10");
			map = new HashMap<String, String>();
			map.put("time", ""+System.currentTimeMillis());
			bean.setAttrs(map);
			messageBeans.add(bean);
			
			bean = new RemindMessageBean();
			bean.setContent("11111");
			bean.setTitle("22222");
			bean.setCreatedDate("2012-11-10");
			map = new HashMap<String, String>();
			map.put("time", ""+System.currentTimeMillis());
			bean.setAttrs(map);
			messageBeans.add(bean);
			bean = new RemindMessageBean();
			bean.setContent("11111");
			bean.setTitle("22222");
			bean.setCreatedDate("2012-11-10");
			map = new HashMap<String, String>();
			map.put("time", ""+System.currentTimeMillis());
			bean.setAttrs(map);
			messageBeans.add(bean);
			beans.addAll(messageBeans);
			
			beans.add("2012-12-11");
			
			beans.addAll(messageBeans);
			beans.add("2012-12-12");
			beans.addAll(messageBeans);
			
			beans.add("2012-12-13");
			beans.addAll(messageBeans);
			
			beans.add("2012-12-15");
			beans.addAll(messageBeans);
			
			beans.add("2012-12-15");
			beans.addAll(messageBeans);
			beans.addAll(messageBeans);
			return beans.toArray();
			//return result != null ? Utils.getSortedGroupData(result, "getCreatedDate"): null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				List<Object> result = (List<Object>) val;
				/*beans = new ArrayList<Object>();
				beans.add("2010-11-10");
				List<RemindMessageBean> messageBeans = new ArrayList<RemindMessageBean>();
				RemindMessageBean bean = new RemindMessageBean();
				Map<String, String> map;
				bean.setContent("11111");
				bean.setTitle("22222");
				bean.setCreatedDate("2012-11-10");
				map = new HashMap<String, String>();
				map.put("time", ""+System.currentTimeMillis());
				bean.setAttrs(map);
				messageBeans.add(bean);
				
				bean = new RemindMessageBean();
				bean.setContent("11111");
				bean.setTitle("22222");
				bean.setCreatedDate("2012-11-10");
				map = new HashMap<String, String>();
				map.put("time", ""+System.currentTimeMillis());
				bean.setAttrs(map);
				messageBeans.add(bean);
				
				bean = new RemindMessageBean();
				bean.setContent("11111");
				bean.setTitle("22222");
				bean.setCreatedDate("2012-11-10");
				map = new HashMap<String, String>();
				map.put("time", ""+System.currentTimeMillis());
				bean.setAttrs(map);
				messageBeans.add(bean);
				bean = new RemindMessageBean();
				bean.setContent("11111");
				bean.setTitle("22222");
				bean.setCreatedDate("2012-11-10");
				map = new HashMap<String, String>();
				map.put("time", ""+System.currentTimeMillis());
				bean.setAttrs(map);
				messageBeans.add(bean);
				beans.addAll(messageBeans);
				
				beans.add("2012-12-11");
				
				beans.addAll(messageBeans);
				beans.add("2012-12-12");
				beans.addAll(messageBeans);
				
				beans.add("2012-12-13");
				beans.addAll(messageBeans);
				
				beans.add("2012-12-15");
				beans.addAll(messageBeans);
				
				beans.add("2012-12-15");
				beans.addAll(messageBeans);
				beans.addAll(messageBeans);
				return beans.toArray();*/
				return Utils.getSortedGroupData(result, "getCreatedDate");
			}else if((val != null)&&val.getClass().isArray()){
				List<Object>  result = Arrays.asList(val);
				return Utils.getSortedGroupData(result, "getCreatedDate");
			}
		}
		return null;
	}

}
