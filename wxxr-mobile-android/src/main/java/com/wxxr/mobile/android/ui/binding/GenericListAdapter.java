/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IRefreshableListAdapter;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

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
	private ListViewPool viewPool;
	
	
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
	
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(this.viewPool == null){
			this.viewPool = new ListViewPool(bindingCtx, viewSelector);
		}
		return this.viewPool.getView(this.itemViewId, convertView, getItem(position), position);
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
				Object[] val = getListData(comp);
				if(Arrays.equals(data, val)) {
					return false;
				} else {
					data = val;
					return true;
				}
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
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		if(this.viewSelector != null){
			Object val = getItem(position);
			String vid = this.viewSelector.getItemViewId(val);
			String[] ids = this.viewSelector.getAllViewIds();
			for (int i = 0; i < ids.length; i++) {
				if(ids[i].equals(vid)){
					return i;
				}			
			}
		}
		return super.getItemViewType(position);
	}
	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		if(this.viewSelector != null){
			return this.viewSelector.getAllViewIds().length;
		}
		return super.getViewTypeCount();
	}	
}
