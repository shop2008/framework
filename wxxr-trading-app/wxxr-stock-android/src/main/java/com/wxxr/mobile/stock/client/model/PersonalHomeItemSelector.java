package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IListAdapterBuilder;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.client.binding.AbstractPinnedHeaderListAdapter;
import com.wxxr.mobile.stock.client.biz.NoShareRecordBean;
import com.wxxr.mobile.stock.client.biz.ShareCountBean;
import com.wxxr.mobile.stock.client.biz.ViewMoreBean;
import com.wxxr.mobile.stock.client.widget.ImageRefreshViewKeys;

@View(name="PersonalHomeItemSelector")
public abstract class PersonalHomeItemSelector extends ViewBase implements
		ItemViewSelector, IListAdapterBuilder {

	/**记录'查看更多'显示个数*/
	private int count = 0;
	private AbstractPinnedHeaderListAdapter adapter;
	@Override
	public String getItemViewId(Object itemData) {
		if(itemData instanceof GainBean) {
			return "TradeRecordItemView";
		} else if(itemData instanceof NoShareRecordBean ) {
			return "NoShareRecordView";
		} else if(itemData instanceof ViewMoreBean) {
			ViewMoreBean viewMoreBean = (ViewMoreBean) itemData;
			boolean isVirtual = viewMoreBean.isVirtual();
			if(isVirtual) {
				return "VirtualViewMoreView";
			} else {
				return "ActualViewMoreView";
			}
		} else if(itemData instanceof ShareCountBean) {
			
			
			return "ShareCountView";
		}
		return null;
	}

	@Override
	public String[] getAllViewIds() {
		return new String[]{"TradeRecordItemView","NoShareRecordView","VirtualViewMoreView","ActualViewMoreView","ShareCountView"};
	}

	@Override
	public IRefreshableListAdapter buildListAdapter(IUIComponent field,
			IAndroidBindingContext bContext, String itemViewId) {
		adapter = new AbstractPinnedHeaderListAdapter(bContext,createAdaptorFromValue(field), this) {
			
			@Override
			protected String getHeaderViewId() {
				
				return "ShareCountView";
			}
			
			@Override
			protected boolean isHeaderData(Object data) {
				if(data instanceof ShareCountBean)
					return true;
				return false;
			}
		};
		return adapter;
	}

	private IListDataProvider createAdaptorFromValue(IUIComponent comp) {
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

	@SuppressWarnings("null")
	protected Object[] getListData(IUIComponent comp) {
		if(comp.hasAttribute(AttributeKeys.options)){
			
			Long joinShareCount = 0l;
			Long challengeShareCount = 0l;
			if(comp.hasAttribute(ImageRefreshViewKeys.joinShareCount)) {
				joinShareCount = comp.getAttribute(ImageRefreshViewKeys.joinShareCount);
			}
			
			if(comp.hasAttribute(ImageRefreshViewKeys.challengeShareCount)) {
				challengeShareCount = comp.getAttribute(ImageRefreshViewKeys.challengeShareCount);
			}
			@SuppressWarnings("unchecked")
			List<Object> data = comp.getAttribute(AttributeKeys.options);
			List<Object> result = new ArrayList<Object>();
			
			List<GainBean> virtualList = null;
			List<GainBean> actualList = null;
			ShareCountBean shareCountBean = null;
			if(data!=null&&data.size() <=0) {
				shareCountBean = new ShareCountBean();
				shareCountBean.setVirtual(true);
				shareCountBean.setShareCount(joinShareCount.intValue());
				result.add(shareCountBean);
				result.add(new NoShareRecordBean());
				shareCountBean = new ShareCountBean();
				shareCountBean.setVirtual(false);
				shareCountBean.setShareCount(challengeShareCount.intValue());
				result.add(shareCountBean);
				result.add(new NoShareRecordBean());
			} else if(data != null){
				shareCountBean = new ShareCountBean();
				shareCountBean.setVirtual(true);
				shareCountBean.setShareCount(joinShareCount.intValue());
				result.add(shareCountBean);
				virtualList = new ArrayList<GainBean>();
				actualList = new ArrayList<GainBean>();
				
				for(Object  obj : data) {
					if(obj instanceof GainBean) {
						GainBean personalHomeBean = (GainBean)obj;
						if(personalHomeBean.getVirtual()) {
							virtualList.add(personalHomeBean);
						} else {
							actualList.add(personalHomeBean);
						}
					}
				}
				
				if(virtualList.size() <= 0) {
					result.add(new NoShareRecordBean());
				} else {
					result.addAll(virtualList);
					ViewMoreBean viewMoreBean = new ViewMoreBean();
					viewMoreBean.setVirtual(true);
					result.add(viewMoreBean);
					//adapter.setUnItemCount(1);
					if(adapter != null)
						adapter.setUnItemPosition(virtualList.size());
				}
				
				shareCountBean = new ShareCountBean();
				shareCountBean.setVirtual(false);
				shareCountBean.setShareCount(challengeShareCount.intValue());
				result.add(shareCountBean);
				
				if(actualList.size() <= 0) {
					result.add(new NoShareRecordBean());
				} else {
					result.addAll(actualList);
					ViewMoreBean viewMoreBean = new ViewMoreBean();
					viewMoreBean.setVirtual(false);
					result.add(viewMoreBean);
				}
			}
			return result != null?result.toArray():null;
		}
		return null;
	}

}
