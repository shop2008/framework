/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.HashMap;

import android.content.Intent;

import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.UIComponent;
import com.wxxr.mobile.core.ui.common.WorkbenchBase;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.client.binding.KlineFieldBinder;
import com.wxxr.mobile.stock.client.binding.PageSwiperViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.RefreshEventBinder;
import com.wxxr.mobile.stock.client.binding.RefreshViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.TextChangedEventBinder;
import com.wxxr.mobile.stock.client.binding.TextSpinnerViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.ToolbarTextAttributeUpdater;
import com.wxxr.mobile.stock.client.view.DeclarativePModelProvider;
import com.wxxr.mobile.stock.client.widget.KLineView;
import com.wxxr.mobile.stock.client.widget.PageSwiperView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView;
import com.wxxr.mobile.stock.client.widget.TextSpinnerView;

/**
 * @author neillin
 *
 */
public class WorkbenchManagerModule extends AbstractWorkbenchManagerModule<IStockAppContext> {

	@Override
	protected void initFieldBinders(IFieldBinderManager mgr) {
		mgr.registerFieldBinder(UIComponent.class,PageSwiperView.class, new PageSwiperViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, KLineView.class, new KlineFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, TextSpinnerView.class, new TextSpinnerViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, PullToRefreshView.class, new RefreshViewFieldBinder());
	}

	@Override
	protected void initEventBinders(IEventBinderManager mgr) {
		mgr.registerFieldBinder("TopRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder("BottomRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder(InputEvent.EVENT_TYPE_TEXT_CHANGED, new TextChangedEventBinder());
	}

	@Override
	protected void initAttributeUpdaters(IFieldAttributeManager mgr) {
		mgr.registerAttributeUpdater("text", new ToolbarTextAttributeUpdater());
	}

	@Override
	protected void initPresentationModels(IWorkbenchRTContext context) {
		DeclarativePModelProvider.updatePModel(context);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule#doCreateWorkbench()
	 */
	@Override
	protected IWorkbench doCreateWorkbench() {
		return new WorkbenchBase(uiContext) {
			
			@Override
			public String[] getPageIds() {
				return manager.getAllRegisteredPageIds();
			}

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.ui.common.WorkbenchBase#showHomePage()
			 */
			@Override
			public void showHomePage() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(IAndroidPageNavigator.PARAM_KEY_INTENT_FLAG, String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				super.showPage(HOME_PAGE_ID,map,null);
			}
		};
	}
}
