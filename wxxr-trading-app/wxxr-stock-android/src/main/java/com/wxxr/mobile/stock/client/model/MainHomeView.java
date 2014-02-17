/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;



import android.os.SystemClock;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.AdStatusBean;
import com.wxxr.mobile.stock.app.bean.HomePageMenu;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

/**
 * @author dz
 *
 */
@View(name="MainHomeView", withToolbar=false,provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.main_home_view_layout")
public abstract class MainHomeView extends ViewBase{
	private static final Trace log = Trace.register(MainHomeView.class);
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager idManager;
	
	@Field(valueKey="visible", binding="${adStatusBean.off==true}")
	boolean leftIcon;
	
	@OnCreate
	void initData() {
		AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().getPageToolbar().hide();
	}
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	/**获取文章*/
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
	
	/**获取首页数据*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getHomeMenuList()}")
	HomePageMenu homeMenuBean;
	
	
	@Bean(type=BindingType.Pojo, express="${articleService.getAdStatusBean()}")
	AdStatusBean adStatusBean;
	
	
	@Field(valueKey="options",binding="${homeMenuBean!=null?homeMenuBean.menuItems:null}")
	List<BaseMenuItem> homeView;
	
	@Bean
	boolean headerVisible = true;
	DataField<List> homeViewField;
	
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String refreshView;
	
	/*@Menu(items = { "left", "right" })
	IMenu toolbar;*/
	
	/*@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "左菜单", icon = "resourceId:drawable/list_button_style",visibleWhen="${adStatusBean.off==true}")})
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		if(adStatusBean != null) {
			adStatusBean.setOff(false);
			
			//registerBean("adStatusBean", adStatusBean);
		}
		SystemClock.sleep(500);
		AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().getPageToolbar().getBinding().doUpdate();
		return null;
	}*/

	/*@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "搜索", icon = "resourceId:drawable/find_button_style",visibleWhen="${false}") })
	String toolbarClickedSearch(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :search was clicked !");
		}
		return "";
	}*/
	
	@Command
	String handleRefresh(InputEvent event) {
		if("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("MainHomeView : handleRefresh");
			}
//			this.articleService.getHomeArticles(0, 4);
//			this.tradingService.getHomeMenuList();
		}
		return null;
	}	
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		homeViewField.getDomainModel().doEvaluate();
		return null;
	}
	
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	int type=0;
	/**状态 0-未结算 ； 1-已结算*/
	int status=0;
	
	
	@Command(navigations={
			@Navigation(on="ChampionShipPage",showPage="championShip"),
			@Navigation(on="TBuyTradingPage",showPage="TBuyTradingPage"),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount"),
			@Navigation(on="operationDetails",showPage="OperationDetails",params={@Parameter(name = "add2BackStack", value = "false")}),
			@Navigation(on="60",showPage="SystemNewsPage"),
			@Navigation(on="61",showPage="InfoNoticesPage"),
			@Navigation(on="62",showPage="userNickSet"),
			@Navigation(on="63",showPage="userAuthPage"),
			@Navigation(on="TBuyT3TradingPageView",showPage="TBuyT3TradingPageView"),
			@Navigation(on="TBuyTdTradingPageView",showPage="TBuyTdTradingPageView"),
			@Navigation(on="SellT3TradingPageView",showPage="SellT3TradingPageView"),
			@Navigation(on="SellTDTradingPageView",showPage="SellTDTradingPageView"),
	})
	CommandResult homeMessageClick(InputEvent event){
			CommandResult resutl = new CommandResult();
			if(event.getProperty("position") instanceof Integer){
				int position = (Integer) event.getProperty("position");
				List<BaseMenuItem> menuList = null;
				if(homeMenuBean != null)
					menuList = homeMenuBean.getMenuItems();
				if(menuList!=null && menuList.size()>0){
					BaseMenuItem menu = menuList.get(position);
					if(menu instanceof ChampionShipMessageMenuItem) {
						resutl.setResult("ChampionShipPage");
					} else if(menu instanceof TradingAccountMenuItem) {
						TradingAccountMenuItem m = (TradingAccountMenuItem)menu;
						
						String acctId = m.getAcctId();
						boolean isVirtual = m.getType()!=null&&m.getType().startsWith("0")?true:false;
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(Constants.KEY_ACCOUNT_ID_FLAG, acctId);
						map.put(Constants.KEY_SELF_FLAG, true);
						map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
						resutl.setPayload(map);
						if("0".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								resutl.setResult("TBuyT3TradingPageView");
							} else if("1d".equals(m.getType())) {
								resutl.setResult("TBuyTdTradingPageView");
							} else {
								resutl.setResult("TBuyTradingPage");
							}
						} else if("1".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								resutl.setResult("SellT3TradingPageView");
							} else if("1d".equals(m.getType())) {
								resutl.setResult("SellTDTradingPageView");
							} else {
								resutl.setResult("sellTradingAccount");
							}
						} else if("2".equals(m.getStatus())) {
							if("13".equals(m.getType())) {
								resutl.setResult("operationDetails");
							} else if("1d".equals(m.getType())) {
								resutl.setResult("operationDetails");
							} else {
								resutl.setResult("operationDetails");
							}
						} else if ("60".equals(m.getStatus()) || "61".equals(m.getStatus())) {
							//系统消息
							usrService.readAllUnremindMessage();
						}
						updateSelection(new AccidSelection(acctId, isVirtual));
					} else if(menu instanceof SignInMessageMenuItem) {
						resutl.setResult("ChampionShipPage");
					} else if(menu instanceof MessageMenuItem) {
						MessageMenuItem m = (MessageMenuItem)menu;
						resutl.setResult(m.getType());
					}
					return resutl;
				}
			}		
		return null;
	}	
	
	@Command
	String handleLeftClick(InputEvent event) {
		
		if(adStatusBean != null) {
			adStatusBean.setOff(false);
		}
		return null;
	}
	
	@Command(navigations={@Navigation(on="*", showPage="stockSearchPage")})
	String handleRightClick(InputEvent event) {
		
		
	return "";
	}
	
	
	
	/*@Command
	String closeBanner(InputEvent event) {
		headerVisible = false;
		registerBean("headerVisible", headerVisible);
		return null;
	}*/
}
