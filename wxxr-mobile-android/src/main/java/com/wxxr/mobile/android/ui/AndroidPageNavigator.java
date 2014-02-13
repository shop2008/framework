/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Parcelable;
import android.view.ViewGroup;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageDescriptor;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.IViewNavigationCallback;
import com.wxxr.mobile.core.ui.api.IViewNavigationListener;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.common.AbstractViewNavigationCallback;

/**
 * @author neillin
 *
 */
public class AndroidPageNavigator implements IAndroidPageNavigator {
	private static final Trace log = Trace.register(AndroidPageNavigator.class);
	
	private static final int PAGE_ACTIVITY_CREATE = 1;
	private static final int PAGE_ACTIVITY_SHOW = 2;
	private static final int PAGE_ACTIVITY_HIDE = 3;
	private static final int PAGE_ACTIVITY_DESTROY = 4;
	
	private Map<String, IBindableActivity> activeActivities = new HashMap<String, IBindableActivity>();
	private Map<String, List<IViewNavigationCallback>> callbacks = new HashMap<String, List<IViewNavigationCallback>>();
	private String currentPageId;
	private final IWorkbenchRTContext context;
	private List<IViewNavigationListener> listeners = new ArrayList<IViewNavigationListener>();
	
	public AndroidPageNavigator(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	
	protected Activity getCurrentActivity(){
		if(currentPageId != null){
			IBindableActivity activity = this.activeActivities.get(currentPageId);
			if(activity != null){
				return activity.getActivity();
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#showPage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void showPage(IPage page,IViewNavigationCallback cb) {
		Application app = AppUtils.getFramework().getAndroidApplication();
		Activity activity = getCurrentActivity();
		String pageId = page.getName();
		Class<?> activityClass = getActivityClass(pageId);
//		IModelUpdater updater = page.getAdaptor(IModelUpdater.class);
//		if(updater != null){
//			updater.updateModel(params);
//		}
		Intent intent;
		if(activity != null) {
			intent = new Intent(activity, activityClass);
		}else{
			intent = new Intent(app, activityClass);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		initShowPageIntent(page,intent);
		String[] params = page.getPropertyNames();
		if((params != null)&&(params.length > 0)){
			for (String key : params) {
				Object val = page.getProperty(key);
				if(PARAM_KEY_INTENT_FLAG.equals(key)){
					intent.addFlags(Integer.parseInt((String)val));
				}else if(key.startsWith(IAndroidPageNavigator.PARAM_KEY_INTENT_PREFIX)){
					add2Intent(intent,key.substring(IAndroidPageNavigator.PARAM_KEY_INTENT_PREFIX.length()),val);
				}
			}
		}
		if(cb != null){
			List<IViewNavigationCallback> cbs = this.callbacks.get(pageId);
			if(cbs == null){
				cbs = new LinkedList<IViewNavigationCallback>();
				this.callbacks.put(pageId, cbs);
			}
			if(!cbs.contains(cb)){
				cbs.add(cb);
			}
		}
		if(activity != null) {
			activity.startActivity(intent);
		} else {
			app.startActivity(intent);
		}
	}
	
	protected void add2Intent(Intent intent, String key, Object val){
		if(val.getClass().isArray()){
			Class<?> componentType = val.getClass().getComponentType();
			if(componentType == Byte.TYPE){
				intent.putExtra(key, (byte[])val);
			}else if(componentType == Character.TYPE){
				intent.putExtra(key, (char[])val);
			}else if(componentType == Short.TYPE){
				intent.putExtra(key, (short[])val);
			}else if(componentType == Integer.TYPE){
				intent.putExtra(key, (int[])val);
			}else if(componentType == Long.TYPE){
				intent.putExtra(key, (long[])val);
			}else if(componentType == Boolean.TYPE){
				intent.putExtra(key, (boolean[])val);
			}else if(componentType == Float.TYPE){
				intent.putExtra(key, (float[])val);
			}else if(componentType == Double.TYPE){
				intent.putExtra(key, (double[])val);
			}else if(Serializable.class.isAssignableFrom(componentType)){
				intent.putExtra(key, (Serializable[])val);
			}else if(Parcelable.class.isAssignableFrom(componentType)){
				intent.putExtra(key, (Parcelable[])val);
			}else{
				throw new IllegalArgumentException("Invalid navigation parameter, key :"+key+", value :"+val);
			}
		}else{
			if(val instanceof Byte){
				intent.putExtra(key, ((Byte)val).byteValue());
			}else if(val instanceof Short){
				intent.putExtra(key, ((Short)val).shortValue());
			}else if(val instanceof Integer){
				intent.putExtra(key, ((Integer)val).intValue());
			}else if(val instanceof Long){
				intent.putExtra(key, ((Long)val).longValue());
			}else if(val instanceof Float){
				intent.putExtra(key, ((Float)val).floatValue());
			}else if(val instanceof Double){
				intent.putExtra(key, ((Double)val).doubleValue());
			}else if(val instanceof String){
				intent.putExtra(key, (String)val);
			}else if(val instanceof Boolean){
				intent.putExtra(key, ((Boolean)val).booleanValue());
			}else if(val instanceof Serializable){
				intent.putExtra(key, (Serializable)val);
			}else if(val instanceof Parcelable){
				intent.putExtra(key, (Parcelable)val);
			}else{
				throw new IllegalArgumentException("Invalid navigation parameter, key :"+key+", value :"+val);
			}
		}
	}

	protected Class<?> getActivityClass(String pageName) {
		IPageDescriptor desc = getManager().getPageDescriptor(pageName);
		IAndroidBindingDescriptor bdesc = (IAndroidBindingDescriptor)desc.getBindingDescriptor(TargetUISystem.ANDROID);
		return bdesc.getTargetClass();
	}
	
	protected void initShowPageIntent(IPage page, Intent intent){

	}
	
	protected IWorkbenchManager getManager(){
		return this.context.getWorkbenchManager();
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#hidePage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void hidePage(IPage page) {
		IBindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			activity.getActivity().finish();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#getCurrentActivePage()
	 */
	@Override
	public IPage getCurrentActivePage() {
		return this.currentPageId != null ? getManager().getWorkbench().getPage(currentPageId): null;
	}


	protected void notifyNavigationCallbacks(IView view, int activity){
		List<IViewNavigationCallback> cbs = this.callbacks.get(view.getName());
		if(cbs != null){
			for (IViewNavigationCallback cb : cbs) {
				notifyCallback(view, activity, cb);
			}
		}
		IViewNavigationListener[] list = null;
		synchronized(this.listeners){
			list = this.listeners.toArray(new IViewNavigationListener[0]);
		}
		if(list != null){
			for (IViewNavigationListener l : list) {
				if(l.acceptable(view)){
					notifyCallback(view, activity, l);
				}
			}
		}
	}

	/**
	 * @param view
	 * @param activity
	 * @param cb
	 */
	protected void notifyCallback(IView view, int activity,IViewNavigationCallback cb) {
		switch(activity){
		case PAGE_ACTIVITY_CREATE:
			cb.onCreate(view);
			break;
		case PAGE_ACTIVITY_DESTROY:
			cb.onDestroy(view);
			break;
		case PAGE_ACTIVITY_HIDE:
			cb.onHide(view);
			break;
		case PAGE_ACTIVITY_SHOW:
			cb.onShow(view);
			break;
		}
	}
	
	protected void removePageCallback(String pageId,IViewNavigationCallback cb) {
		List<IViewNavigationCallback> cbs = this.callbacks.get(pageId);
		if(cbs != null){
			cbs.remove(cb);
		}
	}
	@Override
	public void onViewCreate(IView view, IBindableActivity activity) {
		if(log.isInfoEnabled()){
			log.info("View :"+view.getName()+" is created !");
		}
		if(activity != null){
			this.activeActivities.put(view.getName(), activity);
		}
		notifyNavigationCallbacks(view,PAGE_ACTIVITY_CREATE);
	}

	@Override
	public void onViewShow(IView view) {
		if(log.isInfoEnabled()){
			log.info("View :"+view.getName()+" is on show !");
		}
		if(view instanceof IPage){
			this.currentPageId = view.getName();
		}
		notifyNavigationCallbacks(view,PAGE_ACTIVITY_SHOW);
	}

	@Override
	public void onViewHide(IView view) {
		if(log.isInfoEnabled()){
			log.info("View :"+view.getName()+" is hidden !");
		}
		if(view.getName().equals(currentPageId)){
			this.currentPageId = null;
		}
		notifyNavigationCallbacks(view,PAGE_ACTIVITY_HIDE);

	}

	@Override
	public void onViewDetroy(IView view) {
		if(log.isInfoEnabled()){
			log.info("View :"+view.getName()+" is destroyed !");
		}
		this.activeActivities.remove(view.getName());
		notifyNavigationCallbacks(view,PAGE_ACTIVITY_DESTROY);
	}

	@Override
	public IBindableActivity getOnShowActivity() {
		return this.currentPageId != null ? this.activeActivities.get(currentPageId) : null;
	}

	@Override
	public void showView(final IView view,IViewNavigationCallback callback) {
		if(view instanceof IPage){
			showPage((IPage)view, callback);
			return;
		}
		IPage page = getPage(view);
		final IViewGroup vg = (IViewGroup)view.getParent();
		if((page == null)||(vg == null)){
			hideOrShowDialog(view, true);
			return;
		}
		IBindableActivity activity = this.activeActivities.get(page.getName());
		Object val = view.getProperty(IAndroidPageNavigator.PARAM_KEY_ADD2BACKSTACK);
		final boolean not2BackStack = "false".equalsIgnoreCase(String.valueOf(val));
		if(activity != null){
			hideOrShowView(view,(not2BackStack == false), true);
		}else{
			 IViewNavigationCallback cb = new AbstractViewNavigationCallback() {
				
				@Override
				public void onShow(IView page) {
					removePageCallback(page.getName(), this);
					AppUtils.getFramework().runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							hideOrShowView(view, (not2BackStack == false),true);							
						}
					});
					
				}
				
			};
			showPage(page, cb);
		}
	}

	@Override
	public void hideView(IView view) {
		if(view instanceof IPage){
			hidePage((IPage)view);
			return;
		}
		hideOrShowView(view,false,false);		
	}
	
	protected void hideOrShowDialog(IView view,boolean show){
		if(show){
			createDialog(view, null).show();
		}else if(view.isActive()){
			((IViewBinding)view.getBinding()).hide();
		}
	}

	/**
	 * @param view
	 */
	protected void hideOrShowView(IView view,boolean add2BackStack, boolean show) {
		IPage page = getPage(view);
		final IViewGroup vg = (IViewGroup)view.getParent();
		if((page == null)||(vg == null)){
			hideOrShowDialog(view, false);
			return;
		}
		IBindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			IViewDescriptor vDesc = context.getWorkbenchManager().getViewDescriptor(view.getName());
			IAndroidBindingDescriptor bDesc = (IAndroidBindingDescriptor)vDesc.getBindingDescriptor(TargetUISystem.ANDROID);
			IFieldBinding binding = activity.getViewBinding().getFieldBinding(vg.getName());
			if(binding == null){
				log.warn("Cannot found binding for view :"+view.getName());
				return;
			}
			ViewGroup vgControl = (ViewGroup)binding.getUIControl();
			if(bDesc.getBindingType() == AndroidBindingType.VIEW){
			}else if(bDesc.getBindingType() == AndroidBindingType.FRAGMENT){
				if(activity.getActivity() instanceof BindableFragmentActivity){
					if(show){
						activity.showFragment(vgControl,view.getName(),bDesc,add2BackStack);
					}else{
						activity.hideFragment(view.getName());
					}
				}
			}
		}
	}
	


	protected IPage getPage(IView view) {
		IUIContainer<?> v = view ; 
		while(v != null){
			if(v instanceof IPage){
				return (IPage)v;
			}
			v = v.getParent();
		}
		return null;
	}

	@Override
	public IBindableActivity getPageActivity(IPage page) {
		return this.activeActivities.get(page.getName());
	}

	@Override
	public IDialog createDialog(IView view,Object handback) {
		IBindableActivity act = getOnShowActivity();
		if(act != null){
			return act.createDialog(view,handback);
		}
		return null;
	}

	@Override
	public void registerNavigationListener(IViewNavigationListener listener) {
		synchronized(this.listeners){
			if(!this.listeners.contains(listener)){
				this.listeners.add(listener);
			}
		}
	}

	@Override
	public boolean unregisterNavigationListener(IViewNavigationListener listener) {
		synchronized(this.listeners){
			return this.listeners.remove(listener);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IAndroidPageNavigator#startActivity(android.content.Intent)
	 */
	@Override
	public void startActivity(Intent intent) {
		Application app = AppUtils.getFramework().getAndroidApplication();
		Activity activity = getCurrentActivity();
		if(activity != null) {
			activity.startActivity(intent);
		} else {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			app.startActivity(intent);
		}
	}

}
