/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.ObjectUtils;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

/**
 * @author neillin
 * 
 */
@View(name = "championShip", withToolbar=true, description = "大赛排行榜", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.champion_ship_page_layout")
public abstract class ChampionShipPage extends PageBase {
	private static final Trace log = Trace.register(ChampionShipPage.class);

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingMgr;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getTMegagameRank()}")
	BindableListWrapper<MegagameRankBean> tRankListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getT1MegagameRank()}")
	BindableListWrapper<MegagameRankBean> t1RankListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getWeekRank()}")
	BindableListWrapper<WeekRankBean> weekRankListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getRegularTicketRank()}")
	BindableListWrapper<RegularTicketBean> rtRankListBean;

	// List
	@Field(valueKey = "options", binding = "${tRankListBean.data}")
	List<MegagameRankBean> ChampionShip;

	@Field(valueKey = "options", binding = "${t1RankListBean.data}")
	List<MegagameRankBean> ChampionT1Ship;

	@Field(valueKey = "options", binding = "${weekRankListBean.data}", visibleWhen = "${weekRankListBean != null && weekRankListBean.data!=null && weekRankListBean.data.size()>0?true:false}")
	List<WeekRankBean> ChampionWeekShip;

	@Field(valueKey = "options", binding = "${rtRankListBean.data}")
	List<RegularTicketBean> ChampionRegularShip;

	@Field(valueKey = "text", visibleWhen = "${currentViewId == 1}", attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${false&&tRankListBean!=null&&tRankListBean.data!=null&&tRankListBean.data.size()>0?true:false}") })
	String TRefreshView;
	@Field(valueKey = "text", visibleWhen = "${currentViewId == 2}", attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${false&&t1RankListBean!=null&&t1RankListBean.data!=null&&t1RankListBean.data.size()>0?true:false}") })
	String T1RefreshView;
	@Field(valueKey = "text", visibleWhen = "${currentViewId == 3}", attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${false&&weekRankListBean!=null&&weekRankListBean.data!=null&&weekRankListBean.data.size()>0?true:false}") })
	String WeekRefreshView;
	@Field(valueKey = "text", visibleWhen = "${currentViewId == 4}", attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${false&&rtRankListBean!=null&&rtRankListBean.data!=null&&rtRankListBean.data.size()>0?true:false}") })
	String RegularRefreshView;

	// RadioButton
	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${currentViewId == 1}"),
			@Attribute(name = "textColor", value = "${currentViewId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean tMegaBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${currentViewId == 2}"),
			@Attribute(name = "textColor", value = "${currentViewId == 2?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean t1MegaBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${currentViewId == 3}"),
			@Attribute(name = "textColor", value = "${currentViewId == 3?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean weekBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${currentViewId == 4}"),
			@Attribute(name = "textColor", value = "${currentViewId == 4?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean regularTicketBtn;
	// week Title
	// @Field(valueKey = "text",
	// binding="${weekRankListBean.data.size() > 0 ? weekRankListBean.data.get(0).dates : ''}"
	// ,visibleWhen="${(currentViewId == 3)&&(weekRankListBean.data.size() > 0)}")
	// String weekDate;

	@Bean
	int currentViewId = 1;

	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String reloadTRank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : reloadTRank");
		}
		this.currentViewId = 1;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadTMegagameRank(true);
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	// @ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 1,
	// cancellable = true)
	String reloadRefreshTRank(InputEvent event) {
		if ("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("ChampionShipView : reloadRefreshTRank");
			}
			this.currentViewId = 1;
			registerBean("currentViewId", this.currentViewId);
			tradingMgr.reloadTMegagameRank(true);
		}
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String reloadT1Rank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : reloadT1Rank");
		}
		this.currentViewId = 2;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadT1MegagameRank(true);
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	// @ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 1,
	// cancellable = true)
	String reloadRefreshT1Rank(InputEvent event) {
		if ("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("ChampionShipView : reloadRefreshT1Rank");
			}
			this.currentViewId = 2;
			registerBean("currentViewId", this.currentViewId);
			tradingMgr.reloadT1MegagameRank(true);
		}
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String reloadWeekRank(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : reloadWeekRank");
		}
		this.currentViewId = 3;
		registerBean("currentViewId", this.currentViewId);
		tradingMgr.reloadWeekRank(true);
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	// @ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 1,
	// cancellable = true)
	String reloadRefreshWeekRank(InputEvent event) {
		if ("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("ChampionShipView : reloadRefreshWeekRank");
			}
			this.currentViewId = 3;
			registerBean("currentViewId", this.currentViewId);
			tradingMgr.reloadWeekRank(true);
		}
		return null;
	}

	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m", params = { @Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2") }) })
	// @ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 1,
	// cancellable = true)
	String reloadRegularTicketRank(InputEvent event) {
		if ("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("ChampionShipView : handleRegularTicketTopRefresh");
			}
			this.currentViewId = 4;
			registerBean("currentViewId", this.currentViewId);
			tradingMgr.reloadRegularTicketRank(true);
		}
		return null;
	}

	/**
	 * T日列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "TBuyTradingPage") })
	CommandResult handleChampionShipItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<MegagameRankBean> championShip = (tRankListBean != null ? tRankListBean
					.getData() : null);
			if (championShip != null && championShip.size() > 0
					&& position < championShip.size()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String acctId = championShip.get(position).getAcctID() + "";
				String userId = championShip.get(position).getUserId();
				String user = AppUtils.getService(IUserIdentityManager.class)
						.getUserId();

				map.put(Constants.KEY_ACCOUNT_ID_FLAG, acctId);
				map.put(Constants.KEY_VIRTUAL_FLAG, true);
				map.put(Constants.KEY_SELF_FLAG,
						ObjectUtils.isEquals(userId, user));
				result.setPayload(map);
				updateSelection(new AccidSelection(acctId, true));
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
	@Command(navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails", params = { @Parameter(name = "add2BackStack", value = "false") }),
			@Navigation(on = "sellTradingAccount", showPage = "sellTradingAccount") })
	CommandResult handleChampionShipT1ItemClick(InputEvent event) {
		CommandResult result = new CommandResult();

		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<MegagameRankBean> championShip = (t1RankListBean != null ? t1RankListBean
					.getData() : null);
			if (championShip != null && championShip.size() > 0
					&& position < championShip.size()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String acctId = championShip.get(position).getAcctID() + "";
				String userId = championShip.get(position).getUserId();
				String user = AppUtils.getService(IUserIdentityManager.class)
						.getUserId();

				map.put(Constants.KEY_ACCOUNT_ID_FLAG, acctId);
				map.put(Constants.KEY_VIRTUAL_FLAG, true);
				map.put(Constants.KEY_SELF_FLAG,
						ObjectUtils.isEquals(userId, user));
				String over = championShip.get(position).getOver();
				if ("CLOSED".equals(over)) {
					result.setPayload(map);
					result.setResult("operationDetails");
				} else if ("UNCLOSE".equals(over)) {
					result.setResult("sellTradingAccount");
					result.setPayload(map);
				}
				updateSelection(new AccidSelection(acctId, true));
			}
		}
		return result;
	}

	/**
	 * Week列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = {
			@Navigation(on = "otherUserPage", showPage = "otherUserPage"),
			@Navigation(on = "userPage", showPage = "userPage") })
	CommandResult handleChampionWeekShipItemClick(InputEvent event) {
		CommandResult result = new CommandResult();
		boolean isSelf = false;
		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			List<WeekRankBean> weekShip = (weekRankListBean != null ? weekRankListBean
					.getData() : null);
			if (weekShip != null && weekShip.size() > 0
					&& position < weekShip.size()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String userId = weekShip.get(position).getUserId();
				String nickName = weekShip.get(position).getNickName();
				String user = AppUtils.getService(IUserIdentityManager.class)
						.getUserId();
				isSelf = ObjectUtils.isEquals(userId, user);
				map.put(Constants.KEY_USER_ID_FLAG, userId);
				map.put(Constants.KEY_USER_NAME_FLAG, nickName);
				map.put(Constants.KEY_SELF_FLAG, isSelf);
				result.setPayload(map);
			}
		}
		if (isSelf) {
			result.setResult("userPage");
		} else {
			result.setResult("otherUserPage");
		}
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
			if (rtShip != null && rtShip.size() > 0 && position < rtShip.size()) {
				// String userId = rtShip.get(position).getUserId();
				// updateSelection(userId);
				// result.setPayload(userId);
			}
		}
		result.setResult("");
		return result;
	}
}
