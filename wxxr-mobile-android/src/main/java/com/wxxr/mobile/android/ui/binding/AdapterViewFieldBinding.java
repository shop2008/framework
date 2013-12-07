/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IListAdapterBuilder;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
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

/**
 * @author neillin
 * 
 */
public class AdapterViewFieldBinding extends BasicFieldBinding {
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String LIST_FOOTER_VIEW_ID = "footerViewId";
	public static final String LIST_HEADER_VIEW_ID = "headerViewId";
	private IRefreshableListAdapter listAdapter;
	private IViewBinding headerBinding;
	private IViewBinding footerBinding;
	private IView headerView,footerView;
	private View headItemView, footerItemView;
	private IListAdapterBuilder adapterBuilder;
	
	public AdapterViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		String footerViewId = getBindingAttrs().get(LIST_FOOTER_VIEW_ID);
		String headerViewId = getBindingAttrs().get(LIST_HEADER_VIEW_ID);
		ListView list = getUIControl() instanceof ListView ? (ListView)getUIControl() : null;
		if(list != null){
			if (headerViewId != null
					&& list.getHeaderViewsCount() == 0) {
				IViewDescriptor v = ctx.getWorkbenchManager().getViewDescriptor(headerViewId);
				View view = createUI(v,ctx.getWorkbenchManager());
				headerBinding = (IViewBinding) view.getTag();
				list.addHeaderView(view);
				headItemView = view;
				headerView = ctx.getWorkbenchManager().getWorkbench().createNInitializedView(headerBinding.getBindingViewId());
			}
			if (footerViewId != null
					&& list.getFooterViewsCount() == 0) {
				IViewDescriptor v = ctx.getWorkbenchManager().getViewDescriptor(footerViewId);
				View view = createUI(v,ctx.getWorkbenchManager());
				footerBinding = (IViewBinding) view.getTag();
				list.addFooterView(view);
				footerItemView = view;
				footerView = ctx.getWorkbenchManager().getWorkbench().createNInitializedView(footerBinding.getBindingViewId());
			}
		}
		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		this.adapterBuilder = ctx.getWorkbenchManager().getWorkbench().createNInitializedView(itemViewId).getAdaptor(IListAdapterBuilder.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr
	 * .mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void activate(IView model) {
		super.activate(model);
		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		
		this.listAdapter = this.adapterBuilder != null ? this.adapterBuilder.buildListAdapter(comp, getAndroidBindingContext(),itemViewId) : GenericListAdapter.createAdapter(comp, context, itemViewId);
		if((headerBinding != null)&&(headerView != null)){
			model.addChild(headerView);
			headerBinding.activate(headerView);
		}
		if((footerBinding != null)&&(footerView != null)){
			model.addChild(footerView);
			footerBinding.activate(footerView);
		}
		this.listAdapter.refresh();
		setupAdapter(listAdapter);
	}

	protected View createUI(IViewDescriptor v,final IWorkbenchManager mgr) {
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

	protected void setupAdapter(ListAdapter adapter) {
		((AbsListView) getUIControl()).setAdapter(adapter);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if (this.listAdapter != null) {
			setupAdapter(null);
			this.listAdapter.destroy();
			this.listAdapter = null;
		}
		super.deactivate();
		if(headerBinding != null) {
			headerBinding.deactivate();
			if(headerView.getParent() != null){
				headerView.getParent().removeChild(headerView);
			}
		}
		if(footerBinding != null) {
			footerBinding.deactivate();
			if(footerView.getParent() != null){
				footerView.getParent().removeChild(footerView);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.ValueChangedEvent[])
	 */
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.listAdapter != null){
			this.listAdapter.refresh();
		}
		super.notifyDataChanged(events);
		if(headerBinding != null) {
			headerBinding.notifyDataChanged(events);
		}
		if(footerBinding != null) {
			footerBinding.notifyDataChanged(events);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#refresh()
	 */
	@Override
	public void doUpdate() {
		if(this.listAdapter != null){
			this.listAdapter.refresh();
		}
		super.doUpdate();
		if(headerBinding != null) {
			headerBinding.doUpdate();
		}
		if(footerBinding != null) {
			footerBinding.doUpdate();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#handleValueChangedCallback(com.wxxr.mobile.core.ui.common.UIComponent, com.wxxr.mobile.core.ui.api.AttributeKey<?>[])
	 */
	@Override
	protected void updateUI(boolean recursive) {
		if(this.listAdapter != null){
			this.listAdapter.refresh();
		}
		super.updateUI(recursive);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#destroy()
	 */
	@Override
	public void destroy() {
		ListView list = getUIControl() instanceof ListView ? (ListView)getUIControl() : null;
		if((list != null)&&(headItemView != null)){
			list.removeHeaderView(headItemView);
			headItemView = null;
		}
		if(headerBinding != null){
			headerBinding.destroy();
			headerBinding = null;
		}
		if(headerView != null){
			bContext.getWorkbenchManager().getWorkbench().destroyComponent(headerView);
			headerView = null;
		}
		if((list != null)&&(footerItemView != null)){
			list.removeFooterView(footerItemView);
			footerItemView = null;
		}
		if(footerBinding != null){
			footerBinding.destroy();
			footerBinding = null;
		}
		if(footerView != null){
			bContext.getWorkbenchManager().getWorkbench().destroyComponent(footerView);
			footerView = null;
		}
		super.destroy();
	}

}
