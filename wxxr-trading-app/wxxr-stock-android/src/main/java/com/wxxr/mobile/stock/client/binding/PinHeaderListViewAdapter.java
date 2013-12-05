package com.wxxr.mobile.stock.client.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.CascadeAndroidBindingCtx;
import com.wxxr.mobile.android.ui.binding.ObserverDataChangedListerWrapper;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IObservableListDataProvider;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.UIContainer;
import com.wxxr.mobile.core.util.LRUList;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView.PinnedHeaderAdapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

public class PinHeaderListViewAdapter extends BaseAdapter implements OnScrollListener, SectionIndexer, PinnedHeaderAdapter {

	private IPinHeaderListProvider iListProvider;
	private IObservableListDataProvider observable;
	private IWorkbenchRTContext context;
	private Context uiContext;
	private String itemViewId;
	
	private IAndroidBindingContext bindingCtx;
	private List<ObserverDataChangedListerWrapper> listeners;
	private Map<String, LRUList<View>> viewPool = new HashMap<String, LRUList<View>>();
	private int mLocationPosition = -1;
	
	
	public PinHeaderListViewAdapter(IWorkbenchRTContext ctx, IAndroidBindingContext bCtx, IPinHeaderListProvider prov, String viewId) {
		if((ctx == null)||(bCtx == null)||(prov == null)||(viewId == null)){
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		
		this.context = ctx;
		this.iListProvider = prov;
		this.itemViewId = viewId;
		this.bindingCtx = bCtx;
		this.uiContext = bCtx.getUIContext();
		
		if(prov instanceof IObservableListDataProvider){
			this.observable = (IObservableListDataProvider)prov;
		}else{
			this.observable = null;
		}
		
	}
	
	@Override
	public int getCount() {
		return iListProvider.getItemCounts();
	}

	@Override
	public Object getItem(int position) {
		return iListProvider.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void destroy() {
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		String viewId = this.itemViewId;
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
		
		
		UIContainer<IUIComponent> container = vModel.getChild("headContainer", UIContainer.class);
		IUIComponent child = container.getChild("headTextContent", IUIComponent.class);
		int section = getSectionForPosition(position);
		
		if (getPositionForSection(section) == position) {
			
			if (container.hasAttribute(AttributeKeys.visible)) {
				container.setAttribute(AttributeKeys.visible, true);
			}
			
			if (child.hasAttribute(AttributeKeys.text)) {
				child.setAttribute(AttributeKeys.text, (String)iListProvider.getLabels().get(position));
			}
		} else {
			if (container.hasAttribute(AttributeKeys.visible)) {
				container.setAttribute(AttributeKeys.visible, false);
			}
		}
		vModel.getAdaptor(IModelUpdater.class).updateModel(getItem(position));
		vModel.setProperty("_item_position", position);
		binding.activate(vModel);
		localCtx.setReady(true);
		return view;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0
				|| (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		int realPosition = position;
		int section = getSectionForPosition(realPosition);
		String title = (String) getSections()[section];
		IView iModel = (IView) header.getTag();
		IUIComponent component = iModel.getChild("changedHeader", IUIComponent.class);
		if (component.hasAttribute(AttributeKeys.text)) {
			component.setAttribute(AttributeKeys.text,title);
		}
	}

	@Override
	public Object[] getSections() {
		return iListProvider.getLabels().toArray();
	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= iListProvider.getLabels().size()) {
			return -1;
		}
		return (Integer)iListProvider.getLabelPositions().get(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= getCount()) {
			return -1;
		}
		int index = Arrays.binarySearch(iListProvider.getLabelPositions().toArray(), position);
		return index >= 0 ? index : -index - 2;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}
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
		IViewDescriptor v = this.context.getWorkbenchManager().getViewDescriptor(viewId);
		IBindingDescriptor bDesc = v.getBindingDescriptor(TargetUISystem.ANDROID);
		IBinding<IView> binding = null;
		IViewBinder vBinder = this.context.getWorkbenchManager().getViewBinder();
		CascadeAndroidBindingCtx ctx = new CascadeAndroidBindingCtx(bindingCtx);
		binding = vBinder.createBinding(ctx, bDesc);
		binding.init(context);
		view = (View)binding.getUIControl();
		BindingBag bag = new BindingBag();
		bag.binding = binding;
		bag.ctx = ctx;
		bag.view = this.context.getWorkbenchManager().getWorkbench().createNInitializedView(viewId);
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
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		ObserverDataChangedListerWrapper l = findWrapper(observer);
		if((l != null)&&(observable != null)){
			this.listeners.remove(l);
			this.observable.unregisterDataChangedListener(l);
		}
		super.unregisterDataSetObserver(observer);
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return this.iListProvider.isItemEnabled(this.iListProvider.getItem(position));
	}
	
	private static class BindingBag {
		IBinding<IView> binding;
		IView view;
		CascadeAndroidBindingCtx ctx;
	}

}
