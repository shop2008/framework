package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.Adapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.android.ui.binding.GenericListAdapter;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;

public class SpinnerViewFieldBinding extends BasicFieldBinding {

	public static final String SPINNER_ITEM_VIEW_ID = "itemViewId";
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
		this.listAdapter = GenericListAdapter.createAdapter(comp, (IAndroidBindingContext)getBindingContext(), itemViewId);
		setupAdapter(listAdapter);
	}
	
	protected void setupAdapter(Adapter adapter) {
		if(getUIControl() instanceof Spinner){
			((Spinner) getUIControl()).setAdapter((SpinnerAdapter)adapter);
		}
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
		if(this.listAdapter!=null){
			this.listAdapter.refresh();
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
