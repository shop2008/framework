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
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * @author neillin
 * 
 */
@View(name="championShip", description="大赛排行榜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.champion_ship_page_layout")
public abstract class ChampionShipView extends ViewBase {
	private static final Trace log = Trace.register(ChampionShipView.class);

	@Bean(type=BindingType.Service)
	ITradingManagementService tradingMgr;
	
	@Bean(type=BindingType.Pojo,express="${tradingMgr.getTMegagameRank()}")
	BindableListWrapper<MegagameRankBean> tRankListBean;
	
	@Bean(type=BindingType.Pojo,express="${tradingMgr.getT1MegagameRank()}")
	BindableListWrapper<MegagameRankBean> t1RankListBean;

	@Bean(type=BindingType.Pojo,express="${tradingMgr.getWeekRank()}")
	BindableListWrapper<WeekRankBean> weekRankListBean;

	@Bean(type=BindingType.Pojo,express="${tradingMgr.getRegularTicketRank()}")
	BindableListWrapper<RegularTicketBean> rtRankListBean;

	//List
	@Field(valueKey = "options", binding="${tRankListBean.data}", visibleWhen="${currentViewId == 1}",
			attributes = {@Attribute(name = "enablePullDownRefresh", value="true"),
						  @Attribute(name = "enablePullUpRefresh", value="true")})
	List<MegagameRankBean> ChampionShip;
	
	@Field(valueKey = "options", binding="${t1RankListBean.data}", visibleWhen="${currentViewId == 2}",
			attributes = {@Attribute(name = "enablePullDownRefresh", value="true"),
				  @Attribute(name = "enablePullUpRefresh", value="true")})
	List<MegagameRankBean> ChampionT1Ship;

	@Field(valueKey = "options", binding="${weekRankListBean.data}", visibleWhen="${currentViewId == 3}",
			attributes = {@Attribute(name = "enablePullDownRefresh", value="false"),
			  @Attribute(name = "enablePullUpRefresh", value="false")})
	List<WeekRankBean> ChampionWeekShip;

	@Field(valueKey = "options", binding="${rtRankListBean.data}", visibleWhen="${currentViewId == 4}",
			attributes = {@Attribute(name = "enablePullDownRefresh", value="false"),
			  @Attribute(name = "enablePullUpRefresh", value="true")})
	List<RegularTicketBean> ChampionRegularShip;
	//RadioButton
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 1}"),
			@Attribute(name = "textColor", value = "${currentViewId == 1?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean tMegaBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 2}"),
			@Attribute(name = "textColor", value = "${currentViewId == 2?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean t1MegaBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 3}"),
			@Attribute(name = "textColor", value = "${currentViewId == 3?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean weekBtn;
	
	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${currentViewId == 4}"),
			@Attribute(name = "textColor", value = "${currentViewId == 4?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean regularTicketBtn;
	//week Title
	@Field(valueKey = "text", binding="${weekRankListBean.data.size() > 0 ? weekRankListBean.data.get(0).dates : ''}"
			,visibleWhen="${(currentViewId == 3)&&(weekRankListBean.data.size() > 0)}")
	String weekDate;
	
	@Bean
	int currentViewId = 1;
//	@OnShow
//	protected void updataMegagameRank() {
//		registerBean("currentViewId", currentViewId);
//	}

	
	@Command
	@ExeGuard(title="提示",message="正在获取数据，请稍后...",silentPeriod=1,cancellable=true)
	String reloadTRank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		this.currentViewId = 1;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadTMegagameRank(true);
		return null;
	}

	@Command
	@ExeGuard(title="提示",message="正在获取数据，请稍后...",silentPeriod=1,cancellable=true)
	String reloadT1Rank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMega1TopRefresh");
		}
		this.currentViewId = 2;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadT1MegagameRank(true);
		return null;
	}

	@Command
	@ExeGuard(title="提示",message="正在获取数据，请稍后...",silentPeriod=1,cancellable=true)
	String reloadWeekRank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleWeekTopRefresh");
		}
		this.currentViewId = 3;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadWeekRank(true);
		return null;
	}

	@Command
	@ExeGuard(title="提示",message="正在获取数据，请稍后...",silentPeriod=1,cancellable=true)
	String reloadRegularTicketRank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleRegularTicketTopRefresh");
		}
		this.currentViewId = 4;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadRegularTicketRank(true);
		return null;
	}
	
	/**
	 * T日列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
	CommandResult handleChampionShipItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<MegagameRankBean> championShip = (tRankListBean != null ? tRankListBean
					.getData() : null);
			if (championShip != null && championShip.size() > 0
					&& position < championShip.size()) {
				String userId = championShip.get(position).getUserId();
//				updateSelection(userId);
				result.setPayload(userId);
			}
		}
		result.setResult("");
		return result;
	}
	
	/**
	 * T+1日列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
	CommandResult handleChampionShipT1ItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<MegagameRankBean> championShip = (t1RankListBean != null ? t1RankListBean
					.getData() : null);
			if (championShip != null && championShip.size() > 0
					&& position < championShip.size()) {
				String userId = championShip.get(position).getUserId();
//				updateSelection(userId);
				result.setPayload(userId);
			}
		}
		result.setResult("");
		return result;
	}
	
	/**
	 * Week列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
	CommandResult handleChampionWeekShipItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<WeekRankBean> weekShip = (weekRankListBean != null ? weekRankListBean
					.getData() : null);
			if (weekShip != null && weekShip.size() > 0
					&& position < weekShip.size()) {
				String userId = weekShip.get(position).getUserId();
//				updateSelection(userId);
				result.setPayload(userId);
			}
		}
		result.setResult("");
		return result;
	}
	
	/**
	 * Regular列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
	CommandResult handleChampionRegularShipItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<RegularTicketBean> rtShip = (rtRankListBean != null ? rtRankListBean
					.getData() : null);
			if (rtShip != null && rtShip.size() > 0
					&& position < rtShip.size()) {
//				String userId = rtShip.get(position).getUserId();
//				updateSelection(userId);
//				result.setPayload(userId);
			}
		}
		result.setResult("");
		return result;
	}
}
