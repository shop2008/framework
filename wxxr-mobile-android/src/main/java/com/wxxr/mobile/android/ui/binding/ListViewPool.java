/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.HashMap;
import java.util.Map;

import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.util.LRUList;

/**
 * @author neillin
 *
 */
public class ListViewPool {
	private Map<String, LRUList<View>> viewPool = new HashMap<String, LRUList<View>>();
	private final IAndroidBindingContext bindingCtx;
	private final ItemViewSelector viewSelector;
	
	public ListViewPool(IAndroidBindingContext ctx, ItemViewSelector selector){
		this.bindingCtx = ctx;
		this.viewSelector = selector;
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

	

	
	public View createUI(String viewId){
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
	public View getView(String viewId, View convertView,Object data, int position) {
		if(this.viewSelector != null){
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
		vModel.getAdaptor(IModelUpdater.class).updateModel(data);
		vModel.setProperty("_item_position", position);
		binding.activate(vModel);
		binding.doUpdate();
		localCtx.setReady(true);
		return view;
	}
	
	public void updateView(View view, Object data, int position){
		BindingBag bag = (BindingBag)view.getTag();
		IBinding<IView> binding = bag.binding;
		CascadeAndroidBindingCtx localCtx = bag.ctx;
		IView vModel = bag.view;
		binding.deactivate();
		if(vModel instanceof IReusableUIModel){
			((IReusableUIModel)vModel).reset();
		}
		localCtx.setReady(false);
		vModel.getAdaptor(IModelUpdater.class).updateModel(data);
		vModel.setProperty("_item_position", position);
		binding.activate(vModel);
		binding.doUpdate();
		localCtx.setReady(true);
	}
	
	public void destroy() {
		this.viewPool.clear();
	}
	
	private static class BindingBag {
		IBinding<IView> binding;
		IView view;
		CascadeAndroidBindingCtx ctx;
	}


}
