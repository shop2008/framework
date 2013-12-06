/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.util.LRUList;

/**
 * @author neillin
 *
 */
public class GenericListAdapter extends BaseAdapter implements IRefreshableListAdapter {

	private final IListDataProvider provider;
	private final IObservableListDataProvider observable;
	private final IAndroidBindingContext bindingCtx;
	private final Context uiContext;
	private final String itemViewId;
	private final ItemViewSelector viewSelector;
	private List<ObserverDataChangedListerWrapper> listeners;
	private Map<String, LRUList<View>> viewPool = new HashMap<String, LRUList<View>>();
	
	public GenericListAdapter(IAndroidBindingContext bCtx, IListDataProvider prov, String viewId){
		if((bCtx == null)||(prov == null)||(viewId == null)){
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.provider = prov;
		this.itemViewId = viewId;
		this.bindingCtx = bCtx;
		this.uiContext = bCtx.getUIContext();
		if(prov instanceof IObservableListDataProvider){
			this.observable = (IObservableListDataProvider)prov;
		}else{
			this.observable = null;
		}
		IView v = bCtx.getWorkbenchManager().getWorkbench().createNInitializedView(viewId);
		if(v instanceof ItemViewSelector){
			this.viewSelector = (ItemViewSelector)v;
		}else{
			this.viewSelector = null;
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.provider.getItemCounts();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return this.provider.getItem(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
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

	
	protected View createUI(String viewId){
		LRUList<View> pool = getViewPool(viewId, false);
		View view = pool != null ? pool.get() : null;
		if(view != null){
			return view;
		}
		IViewDescriptor v = this.bindingCtx.getWorkbenchManager().getViewDescriptor(viewId);
		IBindingDescriptor bDesc = v.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = this.bindingCtx.getWorkbenchManager().getViewBinder();
		CascadeAndroidBindingCtx ctx = new CascadeAndroidBindingCtx(bindingCtx);
		binding = vBinder.createBinding(ctx, bDesc);
		view = (View)binding.getUIControl();
		BindingBag bag = new BindingBag();
		bag.binding = binding;
		bag.ctx = ctx;
		bag.view = this.bindingCtx.getWorkbenchManager().getWorkbench().createNInitializedView(viewId);
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
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String viewId = this.itemViewId;
		if(this.viewSelector != null){
			Object data = getItem(position);
			viewId = this.viewSelector.getItemViewId(data);
		}
		boolean existing = false;
		View view = null;
		BindingBag bag = null;
		if(convertView == null){
			view = createUI(viewId);
			bag = (BindingBag)view.getTag();
		}else{
			view = convertView;
			bag = (BindingBag)view.getTag();
			if((bag.view == null)||(! bag.view.getName().equals(viewId))){
				poolView(view);
				view = createUI(viewId);
				bag = (BindingBag)view.getTag();
			}else{
				existing = true;
			}
		}
		IBinding<IView> binding = bag.binding;
		CascadeAndroidBindingCtx localCtx = bag.ctx;
		IView vModel = bag.view;
		if(existing){
			binding.deactivate();
			if(vModel instanceof IReusableUIModel){
				((IReusableUIModel)vModel).reset();
			}
			localCtx.setReady(false);
		}
		vModel.getAdaptor(IModelUpdater.class).updateModel(getItem(position));
		vModel.setProperty("_item_position", position);
		binding.activate(vModel);
		binding.doUpdate();
		localCtx.setReady(true);
		return view;
	}
	
	
	public void destroy() {
		
	}
	
	protected ObserverDataChangedListerWrapper findWrapper(DataSetObserver observer){
		if(this.listeners != null){
			for (ObserverDataChangedListerWrapper l : this.listeners) {
				if(l.getObserver() == observer){
					return l;
				}
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#registerDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
		if(observable != null){
			ObserverDataChangedListerWrapper l = findWrapper(observer);
			if(l == null){
				if(listeners == null){
					this.listeners = new ArrayList<ObserverDataChangedListerWrapper>();
				}
				l = new ObserverDataChangedListerWrapper(observer);
				this.listeners.add(l);
				observable.registerDataChangedListener(l);
			}
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#unregisterDataSetObserver(android.database.DataSetObserver)
	 */
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		ObserverDataChangedListerWrapper l = findWrapper(observer);
		if((l != null)&&(observable != null)){
			this.listeners.remove(l);
			this.observable.unregisterDataChangedListener(l);
		}
		super.unregisterDataSetObserver(observer);
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#areAllItemsEnabled()
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return this.provider.isItemEnabled(this.provider.getItem(position));
	}

	private static class BindingBag {
		IBinding<IView> binding;
		IView view;
		CascadeAndroidBindingCtx ctx;
	}

	@Override
	public boolean refresh() {
		if(this.provider.updateDataIfNeccessary()){
			notifyDataSetChanged();
			return true;
		}
		return false;
	}
	
	public static GenericListAdapter createAdapter(IUIComponent comp,IAndroidBindingContext bCtx, String itemViewId){
		IListDataProvider provider = comp.getAdaptor(IListDataProvider.class);
		if (provider == null) {
			provider = createAdaptorFromValue(comp);
		}
		return new GenericListAdapter(bCtx, provider,itemViewId);
	}
	
	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	public static IListDataProvider createAdaptorFromValue(final IUIComponent comp) {
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
				return data[i];
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

	public static Object[] getListData(IUIComponent comp){
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



	
}
