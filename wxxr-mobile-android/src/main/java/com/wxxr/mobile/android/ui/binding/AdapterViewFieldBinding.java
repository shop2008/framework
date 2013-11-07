/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 * 
 */
public class AdapterViewFieldBinding extends BasicFieldBinding {
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String LIST_FOOTER_VIEW_ID = "footerViewId";
	public static final String LIST_HEADER_VIEW_ID = "headerViewId";
	private GenericListAdapter listAdapter;

	public AdapterViewFieldBinding(IAndroidBindingContext ctx,
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
		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		String footerViewId = getBindingAttrs().get(LIST_FOOTER_VIEW_ID);
		String headerViewId = getBindingAttrs().get(LIST_HEADER_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		IListDataProvider provider = comp.getAdaptor(IListDataProvider.class);
		if (provider == null) {
			if (comp instanceof IDataField) {
				Object val = ((IDataField<?>) comp).getValue();
				provider = createAdaptorFromValue(val);
			}
		}
		if (provider == null) {
			provider = createAdaptorFromValue(comp
					.getAttribute(AttributeKeys.options));
		}
		this.listAdapter = new GenericListAdapter(getWorkbenchContext(),
				getAndroidBindingContext().getUIContext(), provider,
				itemViewId, headerViewId, footerViewId);
		Object list = getUIControl();
		if (list instanceof ListView) {
			IWorkbenchRTContext c = getWorkbenchContext();
			IWorkbenchManager mgr = c.getWorkbenchManager();
			if (headerViewId != null
					&& ((ListView) list).getHeaderViewsCount() == 0) {
				IViewDescriptor v = mgr.getViewDescriptor(headerViewId);
				View view = createUI(v);
				@SuppressWarnings("unchecked")
				IBinding<IView> binding = (IBinding<IView>) view.getTag();
				IView vModel = v.createPresentationModel(c);
				vModel.init(c);
				binding.activate(vModel);
				((ListView) getUIControl()).addHeaderView(view);
			}
			if (footerViewId != null
					&& ((ListView) list).getFooterViewsCount() == 0) {
				IViewDescriptor v = mgr.getViewDescriptor(footerViewId);
				View view = createUI(v);
				@SuppressWarnings("unchecked")
				IBinding<IView> binding = (IBinding<IView>) view.getTag();
				IView vModel = v.createPresentationModel(c);
				vModel.init(c);
				binding.activate(vModel);
				((ListView) getUIControl()).addFooterView(view);
			}
		}
		setupAdapter(listAdapter);
	}

	protected View createUI(IViewDescriptor v) {
		IBindingDescriptor bDesc = v
				.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = getWorkbenchContext().getWorkbenchManager()
				.getViewBinder();
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
				return getWorkbenchContext().getWorkbenchManager();
			}
		}, bDesc);
		binding.init(getWorkbenchContext());
		View view = (View) binding.getUIControl();
		view.setTag(binding);
		return view;

	}

	protected void setupAdapter(ListAdapter adapter) {
		((AbsListView) getUIControl()).setAdapter(adapter);
	}

	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IListDataProvider createAdaptorFromValue(Object val) {
		IListDataProvider provider = null;
		if (val instanceof List) {
			final List data = (List) val;
			provider = new IListDataProvider() {

				@Override
				public Object getItemId(Object item) {
					return null;
				}

				@Override
				public int getItemCounts() {
					return data.size();
				}

				@Override
				public Object getItem(int i) {
					return data.get(i);
				}

				@Override
				public boolean isItemEnabled(Object item) {
					return true;
				}
			};
		} else if ((val != null) && val.getClass().isArray()) {
			final Object[] data = (Object[]) val;
			provider = new IListDataProvider() {

				@Override
				public Object getItemId(Object item) {
					return null;
				}

				@Override
				public int getItemCounts() {
					return data.length;
				}

				@Override
				public Object getItem(int i) {
					return data[i];
				}

				@Override
				public boolean isItemEnabled(Object item) {
					return true;
				}
			};
		}
		return provider;
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
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.ValueChangedEvent[])
	 */
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		if(this.listAdapter != null){
			this.listAdapter.notifyDataSetChanged();
		}
		super.notifyDataChanged(events);
	}

}
