/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.android.ui.binding.CascadeAndroidBindingCtx;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.ViewGroupBase;
import com.wxxr.mobile.core.util.LRUList;

/**
 * @author dz
 * 
 */
public class ViewPagerAdapterViewFieldBinding extends BasicFieldBinding {
	
	private GenericViewPagerAdapter viewPagerAdapter;
	private IListDataProvider viewPagerProvider = new IListDataProvider() {

		View[] androidViewGroup;
		String[] viewIDs = null;

		private ViewGroupBase getViewGroup() {
			return (ViewGroupBase) getField();
		}

		@Override
		public int getItemCounts() {
			return viewIDs != null ? viewIDs.length : 0;
		}

		@Override
		public Object getItem(int i) {
			// TODO Auto-generated method stub
			if (androidViewGroup != null)
				return androidViewGroup[i];
			return null;
		}

		@Override
		public Object getItemId(Object item) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isItemEnabled(Object item) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean updateDataIfNeccessary() {
			// TODO Auto-generated method stub
			if (getViewGroup() != null && androidViewGroup != null
					&& viewIDs != null)
				return true;
			if (getViewGroup() != null)
				viewIDs = getViewGroup().getViewIds();
			int length = viewIDs.length;
			androidViewGroup = new View[length];
			for (int i = 0; i < length; i++) {
				androidViewGroup[i] = createUI(getViewGroup(), viewIDs[i]);
			}
			return true;
		}
	};
	
	private final IAndroidBindingContext bindingCtx;
	private Map<String, LRUList<View>> viewPool = new HashMap<String, LRUList<View>>();
	
	public ViewPagerAdapterViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		this.bindingCtx = ctx;
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
		if (viewPagerAdapter == null) {
			viewPagerProvider.updateDataIfNeccessary();
			viewPagerAdapter = new GenericViewPagerAdapter(
					getWorkbenchContext(), getAndroidBindingContext(),
					viewPagerProvider);
			setupAdapter(viewPagerAdapter);
		} else {
//			viewPagerAdapter.active();
		}
	}

	protected void setupAdapter(PagerAdapter adapter) {
		((ViewPager) getUIControl()).setAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
//		if (this.viewPagerAdapter != null) {
//			setupAdapter(null);
//			this.viewPagerAdapter.destroy();
//			this.viewPagerAdapter = null;
//		}
		super.deactivate();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.ValueChangedEvent[])
	 */
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.viewPagerAdapter != null){
			this.viewPagerAdapter.notifyDataSetChanged();
		}
		super.notifyDataChanged(events);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#refresh()
	 */
	@Override
	public void doUpdate() {
		if(this.viewPagerAdapter != null){
			this.viewPagerAdapter.notifyDataSetChanged();
		}
		super.doUpdate();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#handleValueChangedCallback(com.wxxr.mobile.core.ui.common.UIComponent, com.wxxr.mobile.core.ui.api.AttributeKey<?>[])
	 */
	@Override
	protected void updateUI(boolean recursive) {
		if((this.viewPagerProvider != null) && (this.viewPagerAdapter != null)){
			this.viewPagerAdapter.notifyDataSetChanged();
		}
		super.updateUI(recursive);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#destroy()
	 */
	@Override
	public void destroy() {
		if (this.viewPagerAdapter != null) {
//			this.viewPagerAdapter.destroy();
			this.viewPagerAdapter = null;
			setupAdapter(null);
		}
		super.destroy();
	}
	protected LRUList<View> getViewPool(String viewItemId, boolean create){
		LRUList<View> pool = null;
		synchronized(this.viewPool){
			pool = this.viewPool.get(viewItemId);
			if((pool == null)&&create){
				pool = new LRUList<View>(100);
				pool.setTimeoutInSeconds(2*60);
			}
			this.viewPool.put(viewItemId, pool);
		}
		return pool;
	}

	
	protected View createUI(ViewGroupBase viewGroup, String viewId){
		LRUList<View> pool = getViewPool(viewId, false);
		View view = pool != null ? pool.get() : null;
		if(view != null){
			return view;
		}
		IView v = viewGroup.getView(viewId);
//		IViewDescriptor v = getWorkbenchContext().getWorkbenchManager().getViewDescriptor(viewId);
		IBindingDescriptor bDesc = getWorkbenchContext().getWorkbenchManager().getViewDescriptor(viewId).getBindingDescriptor(TargetUISystem.ANDROID);
		CascadeAndroidBindingCtx ctx = new CascadeAndroidBindingCtx(bindingCtx);
		IBinding<IView> binding = v.getBinding();
		if(binding == null) {
			IViewBinder vBinder = getWorkbenchContext().getWorkbenchManager().getViewBinder();
			binding = vBinder.createBinding(ctx, bDesc);
			binding.init(getWorkbenchContext());
			v.doBinding(binding);
		}
		
		view = (View)binding.getUIControl();
		BindingBag bag = new BindingBag();
		bag.binding = binding;
		bag.ctx = ctx;
		bag.view = v;//getWorkbenchContext().getWorkbenchManager().getWorkbench().createNInitializedView(viewId);
		view.setTag(bag);
		return view;

	}
	
	protected void poolView(View v){
		BindingBag bag = (BindingBag)v.getTag();
		IBinding<IView> binding = bag.binding;
		CascadeAndroidBindingCtx localCtx = bag.ctx;
		if(binding != null){
			binding.deactivate();
		}
		IView vModel = bag.view;
		if(vModel == null){
			return;
		}
		if(vModel instanceof IReusableUIModel){
			((IReusableUIModel)vModel).reset();
		}
		localCtx.setReady(false);
		LRUList<View> pool = getViewPool(vModel.getName(), true);
		pool.put(v);

	}
	
	public static class BindingBag {
		IBinding<IView> binding;
		IView view;
		CascadeAndroidBindingCtx ctx;
	}
}
