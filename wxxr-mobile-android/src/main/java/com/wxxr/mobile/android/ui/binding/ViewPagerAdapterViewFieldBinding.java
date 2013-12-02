/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.IViewPagerDataProvider;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;

/**
 * @author dz
 * 
 */
public class ViewPagerAdapterViewFieldBinding extends BasicFieldBinding {
	private GenericViewPagerAdapter viewPagerAdapter;
	private IViewPagerDataProvider viewPagerProvider;
	
	public ViewPagerAdapterViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
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
		IUIComponent comp = model.getChild(getFieldName());
		viewPagerProvider = comp.getAdaptor(IViewPagerDataProvider.class);
		if (viewPagerProvider == null) {
			viewPagerProvider = createAdaptorFromValue(comp);
		}
		if(viewPagerAdapter == null) {
			this.viewPagerAdapter = new GenericViewPagerAdapter(getWorkbenchContext(),
					getAndroidBindingContext(), viewPagerProvider);
			this.viewPagerProvider.updateDataIfNeccessary();
			setupAdapter(viewPagerAdapter);
		} else {
			viewPagerAdapter.active();
		}
	}

	protected void setupAdapter(PagerAdapter adapter) {
		((ViewPager) getUIControl()).setAdapter(adapter);
	}
	
	protected IViewGroup GetViewGroup(IUIComponent comp){
		IViewGroup vg = (IViewGroup)getField();
		boolean backable =true;
		String activeViewId = vg.getActiveViewId();
		if(activeViewId == null){
			activeViewId = vg.getDefaultViewId();
			backable = false;
		}
		if(activeViewId != null){
			vg.activateView(activeViewId,backable);
		}
		return vg;
	}

	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IViewPagerDataProvider createAdaptorFromValue(final IUIComponent comp) {
		return new IViewPagerDataProvider() {
			IViewGroup viewGroup = null;
			String[]  viewIDs = null;
//			@Override
//			public Object getItemId(Object item) {
//				return null;
//			}

			@Override
			public int getItemCounts() {
				return viewIDs != null ? viewIDs.length : 0;
			}

			@Override
			public Object getItem(int i) {
				return viewGroup.getView(viewIDs[i]);
			}

//			@Override
//			public boolean isItemEnabled(Object item) {
//				return true;
//			}

			@Override
			public boolean updateDataIfNeccessary() {
				viewGroup = GetViewGroup(comp);
				if(viewGroup != null)
					viewIDs = viewGroup.getViewIds();
				return true;
			}
		};
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
	public void refresh() {
		if(this.viewPagerAdapter != null){
			this.viewPagerAdapter.notifyDataSetChanged();
		}
		super.refresh();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#handleValueChangedCallback(com.wxxr.mobile.core.ui.common.UIComponent, com.wxxr.mobile.core.ui.api.AttributeKey<?>[])
	 */
	@Override
	protected void updateUI(boolean recursive) {
		if((this.viewPagerProvider != null) &&this.viewPagerProvider.updateDataIfNeccessary()&&(this.viewPagerAdapter != null)){
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
			setupAdapter(null);
			this.viewPagerAdapter.destroy();
			this.viewPagerAdapter = null;
		}
		super.destroy();
	}

}
