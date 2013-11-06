/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author neillin
 *
 */
public class GenericListAdapter extends BaseAdapter {

	private final IListDataProvider provider;
	private final IObservableListDataProvider observable;
	private final IWorkbenchRTContext context;
	private final Context uiContext;
	private final String itemViewId,headerViewId, footerViewId;
	private List<ObserverDataChangedListerWrapper> listeners;
	
	public GenericListAdapter(IWorkbenchRTContext ctx, Context uiCtx, IListDataProvider prov, String viewId,String headerViewId,String footerViewId){
		if((ctx == null)||(uiCtx == null)||(prov == null)||(viewId == null)){
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.context = ctx;
		this.provider = prov;
		this.itemViewId = viewId;
		this.uiContext = uiCtx;
		this.footerViewId = footerViewId;
		this.headerViewId = headerViewId;
		if(prov instanceof IObservableListDataProvider){
			this.observable = (IObservableListDataProvider)prov;
		}else{
			this.observable = null;
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

	protected View createUI(IViewDescriptor v){
		IBindingDescriptor bDesc = v.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = this.context.getWorkbenchManager().getViewBinder();
		binding = vBinder.createBinding(new IAndroidBindingContext() {
			
			@Override
			public Context getUIContext() {
				return uiContext;
			}
			
			@Override
			public View getBindingControl() {
				return null;
			}
			@Override
			public IWorkbenchManager getWorkbenchManager() {
				return context.getWorkbenchManager();
			}
		}, bDesc);
		binding.init(context);
		View view = (View)binding.getUIControl();
		view.setTag(binding);
		return view;

	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IViewDescriptor v = this.context.getWorkbenchManager().getViewDescriptor(itemViewId);
		boolean existing = false;
		View view = null;
		if(convertView == null){
			view = createUI(v);
		}else{
			view = convertView;
			existing = true;
		}
		IBinding<IView> binding = (IBinding<IView>)view.getTag();
		if(existing){
			binding.deactivate();
		}
		IView vModel = v.createPresentationModel(context);
		vModel.getAdaptor(IModelUpdater.class).updateModel(getItem(position));
		binding.activate(vModel);
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

}
