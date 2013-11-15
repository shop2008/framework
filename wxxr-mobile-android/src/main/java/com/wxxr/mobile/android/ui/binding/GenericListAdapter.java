/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.app.AppUtils;
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
import com.wxxr.mobile.core.ui.common.UIComponent;

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
	private final IAndroidBindingContext bindingCtx;
	private final Context uiContext;
	private final String itemViewId;
	private List<ObserverDataChangedListerWrapper> listeners;
	private Integer lastItemCount;
	
	public GenericListAdapter(IWorkbenchRTContext ctx, IAndroidBindingContext bCtx, IListDataProvider prov, String viewId){
		if((ctx == null)||(bCtx == null)||(prov == null)||(viewId == null)){
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.context = ctx;
		this.provider = prov;
		this.itemViewId = viewId;
		this.bindingCtx = bCtx;
		this.uiContext = bCtx.getUIContext();
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
		int cnt = this.provider.getItemCounts();
		if(this.lastItemCount == null){
			this.lastItemCount = cnt;
			return cnt;
		}else if(cnt != this.lastItemCount.intValue()){
			int oldcnt = this.lastItemCount.intValue();
			this.lastItemCount = cnt;
			AppUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					notifyDataSetChanged();
					
				}
			}, 0, null);
			return oldcnt;
		}
		return cnt;
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

			@Override
			public boolean isOnShow() {
				return bindingCtx.isOnShow();
			}
		}, bDesc);
		binding.init(context);
		View view = (View)binding.getUIControl();
		BindingBag bag = new BindingBag();
		bag.binding = binding;
		view.setTag(bag);
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
		BindingBag bag = (BindingBag)view.getTag();
		IBinding<IView> binding = bag.binding;
		if(existing){
			binding.deactivate();
		}
		IView vModel = bag.view;
		if(vModel == null){
			vModel = v.createPresentationModel(context);
			vModel.init(context);
			bag.view = vModel;
		}
		boolean bool = UIComponent.disableEvents();
		try {
			vModel.getAdaptor(IModelUpdater.class).updateModel(getItem(position));
			binding.activate(vModel);
			return view;
		}finally{
			if(bool){
				UIComponent.enableEvents();
			}
		}
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
	}
}
