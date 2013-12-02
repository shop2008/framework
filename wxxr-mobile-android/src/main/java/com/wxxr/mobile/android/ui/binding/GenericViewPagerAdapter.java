/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IReusableUIModel;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IViewPagerDataProvider;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.util.LRUList;

/**
 * @author dz
 *
 */
public class GenericViewPagerAdapter extends PagerAdapter {

	private final IViewPagerDataProvider viewPagerProvider;
	private final IWorkbenchRTContext context;
	private final IAndroidBindingContext bindingCtx;
	private final Context uiContext;
	private Map<String, LRUList<View>> viewPool = new HashMap<String, LRUList<View>>();
	
	public GenericViewPagerAdapter(IWorkbenchRTContext ctx, IAndroidBindingContext bCtx, IViewPagerDataProvider prov) {
		if((ctx == null)||(bCtx == null)||(prov == null) ) {
			throw new IllegalArgumentException("All arguments cannot be NULL");
		}
		this.context = ctx;
		this.viewPagerProvider = prov;
		this.bindingCtx = bCtx;
		this.uiContext = bCtx.getUIContext();
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
	
	public void destroy() {
		
	}
	public void active() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			IView v = (IView) viewPagerProvider.getItem(i);
			View view = createUI(v.getName());

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();

			IBinding<IView> binding = bag.binding;
			IView vModel = bag.view;
			binding.activate(vModel);
		}
	}

	private static class BindingBag {
		IBinding<IView> binding;
		IView view;
		CascadeAndroidBindingCtx ctx;
	}

/******************************View Pager Adapter ********************************************/
	 @Override  
     public void destroyItem(ViewGroup container, int position, Object object)   {   
		 IView v = (IView)viewPagerProvider.getItem(position);
		 View view = createUI(v.getName());
         container.removeView(view);//删除页卡  
     } 
	
	 @Override  
     public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
		 IView v = (IView)viewPagerProvider.getItem(position);
		 View view = createUI(v.getName());
		 container.addView(view, 0);//添加页卡  
          
		 BindingBag bag = null;
		 bag = (BindingBag)view.getTag();
		 
			IBinding<IView> binding = bag.binding;
//			CascadeAndroidBindingCtx localCtx = bag.ctx;
			IView vModel = bag.view;
//			if(existing){
//				binding.deactivate();
//				if(vModel instanceof IReusableUIModel){
//					((IReusableUIModel)vModel).reset();
//				}
//				localCtx.setReady(false);
//			}
//			vModel.getAdaptor(IModelUpdater.class).updateModel(null);
			binding.activate(vModel);
//			vModel.setProperty("_item_position", position);
//			localCtx.setReady(true);
		 
          return view;  
     }
	 
	@Override  
    public int getCount() {           
        return  viewPagerProvider.getItemCounts();//返回页卡的数量  
    } 
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			IView v = (IView) viewPagerProvider.getItem(i);
			View view = createUI(v.getName());

			BindingBag bag = null;
			bag = (BindingBag) view.getTag();

			IBinding<IView> binding = bag.binding;
//			IView vModel = bag.view;
			binding.refresh();
//			binding.activate(vModel);
		}
		super.notifyDataSetChanged();
	}
	
}
