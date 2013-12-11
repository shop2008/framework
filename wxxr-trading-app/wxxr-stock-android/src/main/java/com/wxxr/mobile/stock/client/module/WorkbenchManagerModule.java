/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import android.support.v4.view.ViewPager;
import android.widget.Spinner;

import com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule;
import com.wxxr.mobile.core.log.api.Trace;
//import com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.UIComponent;
import com.wxxr.mobile.core.ui.common.ViewGroupBase;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.client.binding.ArticleBodyFieldBinder;
import com.wxxr.mobile.stock.client.binding.BackgroundAttributeUpdater;
import com.wxxr.mobile.stock.client.binding.BuyStockViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.GuideSwiperViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.HideProgressEventBinder;
import com.wxxr.mobile.stock.client.binding.IPinHeadItemClickBinder;
import com.wxxr.mobile.stock.client.binding.IViewPagerSelEventBinder;
import com.wxxr.mobile.stock.client.binding.InfoNoticesViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.KlineFieldBinder;
import com.wxxr.mobile.stock.client.binding.MinuteLineViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.NewsAccountViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.PageChangeEventBinder;
import com.wxxr.mobile.stock.client.binding.PageSwiperViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.PinHeaderListViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.RefreshEventBinder;
import com.wxxr.mobile.stock.client.binding.RefreshListViewAdapterBinder;
import com.wxxr.mobile.stock.client.binding.RefreshViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.SellFiveDayMinuteLineViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.SpinnerItemClickEventBinder;
import com.wxxr.mobile.stock.client.binding.SpinnerViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.TextChangedEventBinder;
import com.wxxr.mobile.stock.client.binding.TextSpinnerViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.ToolbarTextAttributeUpdater;
import com.wxxr.mobile.stock.client.binding.ViewPagerAdapterViewFieldBinder;
import com.wxxr.mobile.stock.client.binding.ViewPagerIndexGroupFieldBinder;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView;
import com.wxxr.mobile.stock.client.widget.ArticleBodyViewKeys;
import com.wxxr.mobile.stock.client.widget.BuyStockDetailInputView;
import com.wxxr.mobile.stock.client.widget.BuyStockViewKeys;
import com.wxxr.mobile.stock.client.widget.GuideSwiperView;
import com.wxxr.mobile.stock.client.widget.InfoNoticesView;
import com.wxxr.mobile.stock.client.widget.KLineView;
import com.wxxr.mobile.stock.client.widget.MinuteLineView;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;
import com.wxxr.mobile.stock.client.widget.NewsAccountView;
import com.wxxr.mobile.stock.client.widget.PageSwiperView;
import com.wxxr.mobile.stock.client.widget.Pull2RefreshViewKeys;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView;
import com.wxxr.mobile.stock.client.widget.PullToRefreshView;
import com.wxxr.mobile.stock.client.widget.SellFiveDayMinuteLine;
import com.wxxr.mobile.stock.client.widget.TextSpinnerView;
import com.wxxr.mobile.stock.client.widget.ViewPagerIndexGroup;
import com.wxxr.mobile.stock.client.widget.wheel.PinnedHeaderListView;

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
		mgr.registerFieldBinder(UIComponent.class, PullToRefreshListView.class, new RefreshListViewAdapterBinder());
		mgr.registerFieldBinder(UIComponent.class, MinuteLineView.class, new MinuteLineViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, SellFiveDayMinuteLine.class, new SellFiveDayMinuteLineViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, ArticleBodyView.class, new ArticleBodyFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, BuyStockDetailInputView.class, new BuyStockViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, NewsAccountView.class, new NewsAccountViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, InfoNoticesView.class, new InfoNoticesViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, GuideSwiperView.class, new GuideSwiperViewFieldBinder());
		mgr.registerFieldBinder(ViewGroupBase.class,ViewPager.class, new ViewPagerAdapterViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, Spinner.class, new SpinnerViewFieldBinder());
		mgr.registerFieldBinder(UIComponent.class, ViewPagerIndexGroup.class, new ViewPagerIndexGroupFieldBinder());
	}

	@Override
	protected void initEventBinders(IEventBinderManager mgr) {
		mgr.registerFieldBinder("TopRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder("BottomRefresh", new RefreshEventBinder());
		mgr.registerFieldBinder(InputEvent.EVENT_TYPE_TEXT_CHANGED, new TextChangedEventBinder());
		mgr.registerFieldBinder("HideDialog", new HideProgressEventBinder());
		mgr.registerFieldBinder("PinItemClick", new IPinHeadItemClickBinder());
		mgr.registerFieldBinder("SelCallBack", new IViewPagerSelEventBinder());
		mgr.registerFieldBinder("SpinnerItemSelected", new SpinnerItemClickEventBinder());
		mgr.registerFieldBinder("PageChange", new PageChangeEventBinder());
	}

	@Override
	protected void initAttributeUpdaters(IFieldAttributeManager mgr) {
		Pull2RefreshViewKeys.registerKeys(mgr);
		MinuteLineViewKeys.registerKeys(mgr);
		ArticleBodyViewKeys.registerKeys(mgr);
		BuyStockViewKeys.registerKeys(mgr);
//		StockInfoViewKeys.registerKeys(mgr);
		mgr.registerAttributeUpdater("text", new ToolbarTextAttributeUpdater());
		mgr.registerAttributeUpdater("background", new BackgroundAttributeUpdater());
		//mgr.registerAttributeUpdater("label", new EditTextAttributeUpdater());
	}

	@Override
	protected void initPresentationModels(IWorkbenchRTContext context) {
		try {
			Class.forName("com.wxxr.mobile.stock.client.view.DeclarativePModelProvider").getMethod("updatePModel", new Class[]{IWorkbenchRTContext.class}).invoke(null,context);
		}catch(Throwable t){
			Trace.getLogger(WorkbenchManagerModule.class).fatal("Failed to load in presentation model !!!",t);
		}
	}

	/*@Override
	protected void initBindingDecorators(IBindingDecoratorRegistry arg0) {
		
	}*/

}
