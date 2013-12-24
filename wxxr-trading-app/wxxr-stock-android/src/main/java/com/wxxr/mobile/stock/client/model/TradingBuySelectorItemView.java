package com.wxxr.mobile.stock.client.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.client.binding.AbstractPinnedHeaderListAdapter;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name = "TradingBuySelectorItemView")
public abstract class TradingBuySelectorItemView extends ViewBase implements ItemViewSelector, IListAdapterBuilder {

	@Override
	public String getItemViewId(Object itemData) {
		if (itemData instanceof String) {
			return "NewsTitleItemView";
		} else if (itemData instanceof TradingAccInfoBean) {
			return "TradingBuyInfoItemView";
		}
		return null;
	}

	@Override
	public String[] getAllViewIds() {
		return new String[] { "NewsTitleItemView", "TradingBuyInfoItemView" };
	}

	@Override
	public IRefreshableListAdapter buildListAdapter(IUIComponent field,
			IAndroidBindingContext bContext, String itemViewId) {

		AbstractPinnedHeaderListAdapter adapter = new AbstractPinnedHeaderListAdapter(
				bContext, createAdaptorFromValue(field), this) {

			@Override
			protected String getHeaderViewId() {
				return "NewsTitleItemView";
			}

			@Override
			protected boolean isHeaderData(Object data) {
				if (data instanceof String) {
					return true;
				} else if (data instanceof TradingRecordBean) {
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
				if(data==null)
					return null;
				return data.length > 0 ? data[i] : null;
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

	public static Object[] getListData(IUIComponent comp) {
		if (comp.hasAttribute(AttributeKeys.options)) {
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			return result != null ? getSortedGroupDataLongTime(result, "getStatus") : null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List) {
				List<Object> result = (List<Object>) val;
				return Utils.getSortedGroupDataLongTime(result, "getStatus");
			} else if ((val != null) && val.getClass().isArray()) {
				List<Object> result = Arrays.asList(val);
				return Utils.getSortedGroupDataLongTime(result, "getStatus");
			}
		}
		return null;
	}
	public static Object[] getSortedGroupDataLongTime(List<Object> data, String method) {
		if (data == null) {
			return null;
		}
		if (data.size() <= 0) {
			return data.toArray();
		}
		List<Object> retList = new ArrayList<Object>();
		Object curLabelObj = null;
		for (int i = 0; i < data.size(); i++) {
			Object obj = data.get(i);
			try {
				Method m = obj.getClass().getMethod(method, null);
				m.setAccessible(true);
				Object labelObj = m.invoke(obj, null);
				if(labelObj instanceof Integer) {
					if((Integer)labelObj==0){
						labelObj = "T+1日";
					}else if((Integer)labelObj==1){
						labelObj = "T日";
					}else{
						labelObj = "T+3日";
					}
				}
				if (i == 0) {
					curLabelObj = labelObj;
					retList.add(curLabelObj);
					retList.add(obj);
				} else {
					if (labelObj.equals(curLabelObj)) {
						retList.add(obj);
					} else {
						curLabelObj = labelObj;
						retList.add(curLabelObj);
						retList.add(obj);
					}
				}
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return retList.toArray();		
	}
}
