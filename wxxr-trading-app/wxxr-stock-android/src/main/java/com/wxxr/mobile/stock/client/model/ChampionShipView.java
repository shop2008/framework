/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
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
	
	@Field(valueKey = "options", binding="${ChampionShipBean!=null?ChampionShipBean.tRankBeans:null}",visibleWhen="${currentViewId == 1}")
	List<MegagameRankBean> ChampionShip;
	
	@Field(valueKey = "options", binding="${ChampionShipBean!=null?ChampionShipBean.t1RankBeans:null}",visibleWhen="${currentViewId == 2}")
	List<MegagameRankBean> ChampionT1Ship;

	@Field(valueKey = "options", binding="${ChampionShipBean!=null?ChampionShipBean.weekRanKBeans:null}",visibleWhen="${currentViewId == 3}")
	List<WeekRankBean> ChampionWeekShip;

	@Field(valueKey = "options", binding="${ChampionShipBean!=null?ChampionShipBean.regularTicketBeans:null}",visibleWhen="${currentViewId == 4}")
	List<RegularTicketBean> ChampionRegularShip;
	
//	@Field
//	int currentViewId;
//	
	DataField<List> ChampionShipField;
	DataField<List> ChampionT1ShipField;
	DataField<List> ChampionWeekShipField;
	DataField<List> ChampionRegularShipField;
	@OnShow
	protected void updataMegagameRank() {
		registerBean("currentViewId", 1);
//		try {
//			ChampionShip = getUIContext().getKernelContext()
//					.getService(ITradingManagementService.class)
//					.getTMegagameRank();
//			ChampionT1Ship = new ArrayList<MegagameRankBean>();
//			ChampionWeekShip = new ArrayList<WeekRankBean>();
//			ChampionRegularShip = new ArrayList<RegularTicketBean>();
//		} catch (StockAppBizException e) {
//
//		}
//		ChampionShipField.setValue(this.ChampionShip);
//		ChampionT1ShipField.setValue(this.ChampionT1Ship);
//		ChampionWeekShipField.setValue(this.ChampionWeekShip);
//		ChampionRegularShipField.setValue(this.ChampionRegularShip);
//		
//		ChampionShipField.setAttribute(AttributeKeys.visible, true);
//		ChampionT1ShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionWeekShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionRegularShipField.setAttribute(AttributeKeys.visible, false);
	}

	@Command
	String handleTMegaTopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback)event.getProperty("callback");
		ChampionShip.clear();
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getTMegagameRank();
		} catch (StockAppBizException e) {

		}
		if(cb!=null)
			cb.refreshSuccess();
		return null;
		
	}
	
	@Command
	String handleTMega1TopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMega1TopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback)event.getProperty("callback");
		ChampionShip.clear();
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getT1MegagameRank();
		} catch (StockAppBizException e) {

		}
		if(cb!=null)
			cb.refreshSuccess();
		return null;
		
	}
	
	@Command
	String handleWeekTopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleWeekTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback)event.getProperty("callback");
		ChampionShip.clear();
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getWeekRank();
		} catch (StockAppBizException e) {

		}
		if(cb!=null)
			cb.refreshSuccess();
		return null;
		
	}
	
	@Command
	String handleRegularTicketTopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleRegularTicketTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback)event.getProperty("callback");
		ChampionShip.clear();
		try {
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getRegularTicketRank();
		} catch (StockAppBizException e) {

		}
		if(cb!=null)
			cb.refreshSuccess();
		return null;
		
	}
	
	@Command
	String handleTMegaClick(InputEvent event) {
		registerBean("currentViewId", 1);
		return null;

	}

	@Command
	String handleTMega1Click(InputEvent event) {
		registerBean("currentViewId", 2);
//		if(ChampionT1Ship.size() == 0) {
//			try {
//				getUIContext().getKernelContext()
//						.getService(ITradingManagementService.class).getT1MegagameRank();
////						.getT1MegagameRank();
////				ChampionT1Ship.addAll(ship);
//			} catch (StockAppBizException e) {
//
//			}
//		}
//		ChampionT1ShipField.setValue(this.ChampionT1Ship);
//		ChampionShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionT1ShipField.setAttribute(AttributeKeys.visible, true);
//		ChampionWeekShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionRegularShipField.setAttribute(AttributeKeys.visible, false);
		return null;

	}

	@Command
	String handleWeekClick(InputEvent event) {
		registerBean("currentViewId", 3);
//		if(ChampionWeekShip.size() == 0) {
////			try {
////				List<WeekRankBean> ship = getUIContext().getKernelContext()
////						.getService(ITradingManagementService.class).getWeekRank();
////				ChampionWeekShip.addAll(ship);
////			} catch (StockAppBizException e) {
////
////			}
//		}
//		ChampionWeekShipField.setValue(this.ChampionWeekShip);
////		ChampionShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionT1ShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionWeekShipField.setAttribute(AttributeKeys.visible, true);
//		ChampionRegularShipField.setAttribute(AttributeKeys.visible, false);
		return null;

	}

	@Command
	String handleRegularTicketClick(InputEvent event) {
		registerBean("currentViewId", 4);
//		if(ChampionRegularShip.size() == 0)	 {
////			try {
////				List<RegularTicketBean> ship = getUIContext().getKernelContext()
////						.getService(ITradingManagementService.class)
////						.getRegularTicketRank();
////				ChampionRegularShip.addAll(ship);
////			} catch (StockAppBizException e) {
////
////			}
//		}
//		ChampionRegularShipField.setValue(this.ChampionRegularShip);
////		ChampionShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionT1ShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionWeekShipField.setAttribute(AttributeKeys.visible, false);
//		ChampionRegularShipField.setAttribute(AttributeKeys.visible, true);
		return null;

	}
	

}
