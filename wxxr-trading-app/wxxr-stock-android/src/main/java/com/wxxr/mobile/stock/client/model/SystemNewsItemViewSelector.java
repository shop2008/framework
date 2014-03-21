package com.wxxr.mobile.stock.client.model;

import java.util.Arrays;
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
import com.wxxr.mobile.stock.client.binding.AbstractPinnedHeaderListAdapter;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name="SystemNewsItemViewSelector")
public abstract class SystemNewsItemViewSelector extends ViewBase implements ItemViewSelector, IListAdapterBuilder {

	@Override
	public String getItemViewId(Object itemData) {
		if(itemData instanceof String) {
			return "SystemNewsTitleView";
		} else {
			return "SystemNewsItemView";
		}
	}
	
	@Override
	public String[] getAllViewIds() {
		return new String[]{"SystemNewsTitleView", "SystemNewsItemView"};
	}

	
	@Override
	public IRefreshableListAdapter buildListAdapter(IUIComponent field,
			IAndroidBindingContext bContext, String itemViewId) {
		AbstractPinnedHeaderListAdapter adapter = new AbstractPinnedHeaderListAdapter(bContext,createAdaptorFromValue(field), this) {
			
			@Override
			protected String getHeaderViewId() {
				return "SystemNewsTitleView";
			}
			
			@Override
			protected boolean isHeaderData(Object data) {
				if (data != null) {
					if (data instanceof String) {
						return true;
					}
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
				return (data!=null)&&(data.length>0)?data[i]:null;
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
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			return result != null ? Utils.getSortedGroupData(result, "getCreatedDate"): null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				List<Object> result = (List<Object>) val;
				return Utils.getSortedGroupData(result, "getCreatedDate");
			}else if((val != null)&&val.getClass().isArray()){
				List<Object>  result = Arrays.asList(val);
				return Utils.getSortedGroupData(result, "getCreatedDate");
			}
		}
		return null;
	}
}
