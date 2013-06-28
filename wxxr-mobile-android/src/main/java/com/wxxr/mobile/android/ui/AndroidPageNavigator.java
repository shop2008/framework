/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Application;
import android.content.Intent;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public abstract class AndroidPageNavigator<T extends IAndroidAppContext> extends AbstractModule<T> implements
		IAndroidPageNavigator {

	private static final int PAGE_ACTIVITY_CREATE = 1;
	private static final int PAGE_ACTIVITY_SHOW = 2;
	private static final int PAGE_ACTIVITY_HIDE = 3;
	private static final int PAGE_ACTIVITY_DESTROY = 4;
	private Map<String, BindableActivity> activeActivities = new HashMap<String, BindableActivity>();
	private Map<String, List<IPageCallback>> callbacks = new HashMap<String, List<IPageCallback>>();
	private String currentPageId;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#showPage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void showPage(IPage page,Map<String, String> params,IPageCallback cb) {
		Application app = context.getApplication().getAndroidApplication();
		String pageId = page.getName();
		Class<?> activityClass = getActivityClass(pageId);
		Intent intent = new Intent(app, activityClass);
		Map<String, String> map = getPageShowParams(pageId);
		if(map != null){
			for (Entry<String, String> entry : map.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if(params != null){
			for (Entry<String, String> entry : params.entrySet()) {
				intent.putExtra(entry.getKey(), entry.getValue());
			}
		}
		if(cb != null){
			List<IPageCallback> cbs = this.callbacks.get(pageId);
			if(cbs == null){
				cbs = new LinkedList<IPageCallback>();
				this.callbacks.put(pageId, cbs);
			}
			if(!cbs.contains(cb)){
				cbs.add(cb);
			}
		}
		app.startActivity(intent);
	}

	protected abstract Class<?> getActivityClass(String pageName);
	
	protected abstract Map<String, String> getPageShowParams(String pageName);
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#hidePage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void hidePage(IPage page) {
		BindableActivity activity = this.activeActivities.get(page.getName());
		if(activity != null){
			activity.finish();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageNavigator#getCurrentActivePage()
	 */
	@Override
	public IPage getCurrentActivePage() {
		return this.currentPageId != null ? context.getService(IWorkbenchManager.class).getWorkbench().getPage(currentPageId): null;
	}

	@Override
	protected void initServiceDependency() {
		
	}

	@Override
	protected void startService() {
		context.registerService(IPageNavigator.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IPageNavigator.class, this);
	}

	protected void notifyPageCallbacks(String pageId, int activity){
		List<IPageCallback> cbs = this.callbacks.get(pageId);
		if(cbs != null){
			IPage page = context.getService(IWorkbenchManager.class).getWorkbench().getPage(currentPageId);
			for (IPageCallback cb : cbs) {
				switch(activity){
				case PAGE_ACTIVITY_CREATE:
					cb.onCreate(page);
					break;
				case PAGE_ACTIVITY_DESTROY:
					cb.onDestroy(page);
					break;
				case PAGE_ACTIVITY_HIDE:
					cb.onHide(page);
					break;
				case PAGE_ACTIVITY_SHOW:
					cb.onShow(page);
					break;
				}
			}
		}
	}
	@Override
	public void onPageCreate(String pageId, BindableActivity activity) {
		this.activeActivities.put(pageId, activity);
		notifyPageCallbacks(pageId,PAGE_ACTIVITY_CREATE);
	}

	@Override
	public void onPageShow(String pageId) {
		this.currentPageId = pageId;
		notifyPageCallbacks(pageId,PAGE_ACTIVITY_SHOW);
	}

	@Override
	public void onPageHide(String pageId) {
		notifyPageCallbacks(pageId,PAGE_ACTIVITY_HIDE);

	}

	@Override
	public void onPageDetroy(String pageId) {
		this.activeActivities.remove(pageId);
		notifyPageCallbacks(pageId,PAGE_ACTIVITY_DESTROY);
	}

	@Override
	public BindableActivity getOnShowActivity() {
		return this.currentPageId != null ? this.activeActivities.get(currentPageId) : null;
	}


}
