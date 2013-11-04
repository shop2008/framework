/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author neillin
 *
 */
public class GenericListAdapter extends BaseAdapter {

	private final IListDataProvider provider;
	private final IWorkbenchRTContext context;
	private final Context uiContext;
	private final String itemViewId,headerViewId, footerViewId;
	
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
		Object obj = getItem(position);
		return (Long)this.provider.getItemId(obj);
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

}
