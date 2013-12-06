package com.wxxr.mobile.stock.client.binding;

import java.util.List;
import java.util.Map;

import android.widget.Adapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.android.ui.binding.GenericListAdapter;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;

public class SpinnerViewFieldBinding extends BasicFieldBinding {

	public static final String SPINNER_ITEM_VIEW_ID = "itemViewId";
	private IListDataProvider provider;
	private GenericListAdapter listAdapter;
	private static final int DEFAULT_POSITION = 0;
	public SpinnerViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}
	
	@Override
	public void activate(IView model) {
		super.activate(model);
		String itemViewId = getBindingAttrs().get(SPINNER_ITEM_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		provider = comp.getAdaptor(IListDataProvider.class);
		if (provider == null) {
			provider = createAdaptorFromValue(comp);
		}
		this.listAdapter = new GenericListAdapter(getWorkbenchContext(),
				getAndroidBindingContext(), provider,
				itemViewId);
		this.provider.updateDataIfNeccessary();
		setupAdapter(listAdapter);
	}
	protected void setupAdapter(Adapter adapter) {
		if(getUIControl() instanceof Spinner){
			((Spinner) getUIControl()).setAdapter((SpinnerAdapter)adapter);
		}
	}
	
	protected Object[] getListData(IUIComponent comp){
		if(comp.hasAttribute(AttributeKeys.options)){
			List<Object> result = comp.getAttribute(AttributeKeys.options);
			return result != null ? result.toArray() : null;
		}
		if (comp instanceof IDataField) {
			Object val = ((IDataField<?>) comp).getValue();
			if (val instanceof List){
				return ((List<Object>)val).toArray();
			}else if((val != null)&&val.getClass().isArray()){
				return (Object[])val;
			}
		}
		return null;
	}	
	
	protected IListDataProvider createAdaptorFromValue(final IUIComponent comp) {
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
				return data[i]!=null ? data[i] : null;
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

	@Override
	public void deactivate() {
		if (this.listAdapter != null) {
			setupAdapter(null);
			this.listAdapter.destroy();
			this.listAdapter = null;
		}
		super.deactivate();
	}
	
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.listAdapter != null){
			this.listAdapter.notifyDataSetChanged();
		}
		super.notifyDataChanged(events);
	}
	
	@Override
	protected void updateUI(boolean recursive) {
		if((this.provider != null)&&this.provider.updateDataIfNeccessary()&&(this.listAdapter != null)){
			this.listAdapter.notifyDataSetChanged();
		}
		IUIComponent comp = getField();
		Spinner view = (Spinner)getUIControl();
		Integer position = comp.getAttribute(MinuteLineViewKeys.position);
		if(position!=null){
			view.setSelection(position, true);
		}
		super.updateUI(recursive);
	}
	@Override
	public void destroy() {
		super.destroy();
	}	
	
	@Override
	public void doUpdate() {
		if(this.listAdapter != null){
			this.listAdapter.notifyDataSetChanged();
		}
		super.doUpdate();
	}
}
