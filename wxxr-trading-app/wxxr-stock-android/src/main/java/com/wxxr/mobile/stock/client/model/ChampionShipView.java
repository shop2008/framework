/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.MegagameRankBean;
import com.wxxr.mobile.stock.client.bean.RankListBean;
import com.wxxr.mobile.stock.client.bean.RegularTicketBean;
import com.wxxr.mobile.stock.client.bean.WeekRankBean;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;
import com.wxxr.mobile.stock.client.service.ITradingManagementService;

/**
 * @author neillin
 * 
 */
@View(name="championShip", description="大赛排行榜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.champion_ship_page_layout")
public abstract class ChampionShipView extends ViewBase {
	private static final Trace log = Trace.register(TradingMainView.class);

	@Bean(type=BindingType.Service)
	ITradingManagementService tradingMgr;
	
	@Bean(type=BindingType.Pojo,express="${tradingMgr.getTMegagameRank()}")
	RankListBean ChampionShipBean;
	//List
	@Field(valueKey = "options", binding="${ChampionShipBean != null ? ChampionShipBean.TRankBeans:null}", visibleWhen="${currentViewId == 1}")
	List<MegagameRankBean> ChampionShip;
	
	@Field(valueKey = "options", binding="${ChampionShipBean != null?ChampionShipBean.t1RankBeans:null}", visibleWhen="${currentViewId == 2}")
	List<MegagameRankBean> ChampionT1Ship;

	@Field(valueKey = "options", binding="${ChampionShipBean != null?ChampionShipBean.weekRanKBeans:null}", visibleWhen="${currentViewId == 3}")
	List<WeekRankBean> ChampionWeekShip;

	@Field(valueKey = "options", binding="${ChampionShipBean != null?ChampionShipBean.regularTicketBeans:null}", visibleWhen="${currentViewId == 4}")
	List<RegularTicketBean> ChampionRegularShip;
	//RadioButton
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 1}")
			})
	boolean tMegaBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 2}")
			})
	boolean t1MegaBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 3}")
			})
	boolean weekBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 4}")
			})
	boolean regularTicketBtn;
	
	int currentViewId = 1;
	@OnShow
	protected void updataMegagameRank() {
		registerBean("currentViewId", currentViewId);
	}

	
	@Command
	String handleTMegaTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		handleTMegaClick(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleTMega1TopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMega1TopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		handleTMega1Click(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleWeekTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleWeekTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		handleWeekClick(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleRegularTicketTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleRegularTicketTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		handleRegularTicketClick(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleTMegaClick(InputEvent event) {
		currentViewId = 1;
		registerBean("currentViewId", currentViewId);
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getTMegagameRank();
		} catch (StockAppBizException e) {

		}
		return null;
	}

	@Command
	String handleTMega1Click(InputEvent event) {
		currentViewId = 2;
		registerBean("currentViewId", currentViewId);
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getT1MegagameRank();
		} catch (StockAppBizException e) {

		}
		return null;
	}

	@Command
	String handleWeekClick(InputEvent event) {
		currentViewId = 3;
		registerBean("currentViewId", currentViewId);
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class).getWeekRank();
		} catch (StockAppBizException e) {

		}
		return null;
	}

	@Command
	String handleRegularTicketClick(InputEvent event) {
		currentViewId = 4;
		registerBean("currentViewId", currentViewId);
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getRegularTicketRank();
		} catch (StockAppBizException e) {

		}
		return null;
	}

}
