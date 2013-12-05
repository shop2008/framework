package com.wxxr.mobile.stock.client.binding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.ListAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.app.bean.MessageInfoBean;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView;
public class PinHeaderListViewFieldBinding extends BasicFieldBinding {

	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String PIN_HEAD_VIEW_ID = "pinHeadViewId";
	private PinHeaderListViewAdapter adapter;
	private IPinHeaderListProvider provider;
	private IViewBinding pinHeadViewBinding;
	private View pinHeaderView;
	private IView pinHeader;
	public PinHeaderListViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		PinnedHeaderListView list = getUIControl() instanceof PinnedHeaderListView ? (PinnedHeaderListView) getUIControl()
				: null;
		String pinHeaderViewId = getBindingAttrs().get(PIN_HEAD_VIEW_ID);
		if (list != null) {
			if (pinHeaderViewId != null) {
				IViewDescriptor v = ctx.getWorkbenchManager()
						.getViewDescriptor(pinHeaderViewId);
				View view = createUI(v, ctx.getWorkbenchManager());
				pinHeadViewBinding = (IViewBinding) view.getTag();
				pinHeaderView = view;
				IView iView = ctx
						.getWorkbenchManager()
						.getWorkbench()
						.createNInitializedView(
								pinHeadViewBinding.getBindingViewId());
				pinHeaderView.setTag(iView);
				pinHeader = iView;
			}
		}
	}

	@Override
	public void activate(IView model) {
		super.activate(model);

		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		provider = comp.getAdaptor(IPinHeaderListProvider.class);
		if (provider == null) {
			provider = createAdaptorFromValue(comp);
		}
		
		this.adapter = new PinHeaderListViewAdapter(getWorkbenchContext(),
				getAndroidBindingContext(), provider,
				itemViewId);
		
		if (pinHeadViewBinding !=null && pinHeader != null) {
			model.addChild(pinHeader);
			pinHeadViewBinding.activate(pinHeader);
		}
		
		this.provider.updateDataIfNeccessary();
		setupAdapter(adapter);
		((PinnedHeaderListView)getUIControl()).setOnScrollListener(adapter);
		((PinnedHeaderListView)getUIControl()).setPinnedHeaderView(pinHeaderView);
	}
	
	protected void setupAdapter(ListAdapter adapter) {
		((PinnedHeaderListView) getUIControl()).setAdapter(adapter);
	}

	protected IPinHeaderListProvider createAdaptorFromValue(
			final IUIComponent comp) {
		return new IPinHeaderListProvider() {

			Map<String, List<Object>> data = null;

			@Override
			public boolean updateDataIfNeccessary() {
				data = getListData(comp);
				return true;
			}

			@Override
			public boolean isItemEnabled(Object item) {
				
				return true;
			}

			@Override
			public Object getItemId(Object item) {
				
				return null;
			}

			@Override
			public int getItemCounts() {
				
				return data!=null?data.get("data").size():0;
			}

			@Override
			public Object getItem(int i) {
				
				return data!=null?data.get("data").get(i):null;
			}

			@Override
			public List<Object> getLabels() {
				
				return data!=null?data.get("labels"):null;
			}

			@Override
			public List<Object> getLabelPositions() {
				
				return data!=null? data.get("labelPostions"):null;
			}
		};
	}

	protected Map<String, List<Object>> getListData(IUIComponent comp) {

		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		if (comp.hasAttribute(AttributeKeys.options)) {
			List<Object> data = comp.getAttribute(AttributeKeys.options);
			map.put("data", data);

			List<Object> labels;
			List<Object> labelPositions;
			if (data != null && data.size() > 0) {
				labels = new ArrayList<Object>();
				labelPositions = new ArrayList<Object>();
				MessageInfoBean curInfoBean = null;
				int index = -1;
				for (int i = 0; i < data.size(); i++) {
					Object obj = data.get(i);
					if (obj instanceof MessageInfoBean) {
						
						MessageInfoBean infoBean = (MessageInfoBean) obj;
						if (i==0) {
							labelPositions.add(i);
							curInfoBean = infoBean;
							labels.add(dateFormatToYMD(infoBean.getDate()));
						} else {
							if (!isSameDate(curInfoBean.getDate(), infoBean.getDate())) {
								labels.add(dateFormatToYMD(infoBean.getDate()));
								curInfoBean = infoBean;
								labelPositions.add(i);
							}
						}
					}
				}
				
				map.put("labels", labels);
				map.put("labelPostions", labelPositions);
			}
			
			return map;
		}

		return null;
	}

	/** 格式化--年-月-日 */
	private String dateFormatToYMD(Long time) {
		if (time == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
		Date date = new Date(time);
		return format.format(date);
	}

	private boolean isSameDate(Long time1, Long time2) {

		String timeStr1 = dateFormatToYMD(time1);
		String timeStr2 = dateFormatToYMD(time2);
		if (timeStr1 == null || timeStr2 == null) {
			return false;
		}

		if (timeStr1.equals(timeStr2)) {
			return true;
		} else {
			return false;
		}
	}

	protected View createUI(IViewDescriptor v, final IWorkbenchManager mgr) {
		IBindingDescriptor bDesc = v
				.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = mgr.getViewBinder();
		binding = vBinder.createBinding(new IAndroidBindingContext() {

			@Override
			public Context getUIContext() {
				return getAndroidBindingContext().getUIContext();
			}

			@Override
			public View getBindingControl() {
				//return (PinnedHeaderListView)getUIControlT();
				return null;
			}

			@Override
			public IWorkbenchManager getWorkbenchManager() {
				return mgr;
			}

			@Override
			public boolean isOnShow() {
				return getAndroidBindingContext().isOnShow();
			}

			@Override
			public void hideView() {
				getAndroidBindingContext().hideView();

			}
		}, bDesc);
		View view = (View) binding.getUIControl();
		view.setTag(binding);
		return view;

	}

	@Override
	public void deactivate() {
		if (this.adapter != null) {
			setupAdapter(null);
			this.adapter.destroy();
			this.adapter = null;
		}
		super.deactivate();
		if(pinHeadViewBinding != null) {
			pinHeadViewBinding.deactivate();
			if(pinHeader.getParent() != null){
				pinHeader.getParent().removeChild(pinHeader);
			}
		}
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.adapter != null){
			this.adapter.notifyDataSetChanged();
		}
		super.notifyDataChanged(events);
		if(pinHeadViewBinding != null) {
			pinHeadViewBinding.notifyDataChanged(events);
		}
		if(pinHeadViewBinding != null) {
			pinHeadViewBinding.notifyDataChanged(events);
		}
	}

	@Override
	public void refresh() {
		if(this.adapter != null){
			this.adapter.notifyDataSetChanged();
		}
		super.refresh();
		if(pinHeadViewBinding != null) {
			pinHeadViewBinding.refresh();
		}
		
	}
	
	@Override
	protected void updateUI(boolean recursive) {
		if((this.provider != null)&&this.provider.updateDataIfNeccessary()&&(this.adapter != null)){
			this.adapter.notifyDataSetChanged();
		}
		super.updateUI(recursive);
	}
	
	@Override
	public void destroy() {
		PinnedHeaderListView list = getUIControl() instanceof PinnedHeaderListView ? (PinnedHeaderListView)getUIControl() : null;
		if((list != null)&&(pinHeaderView != null)){
			list.setPinnedHeaderView(null);
			pinHeaderView = null;
		}
		
		if(pinHeadViewBinding != null){
			pinHeadViewBinding.destroy();
			pinHeadViewBinding = null;
		}
	
		if(pinHeader != null){
			pinHeader.destroy();
			pinHeader = null;
		}
		super.destroy();
	}

}
