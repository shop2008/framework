/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import android.os.SystemClock;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IClientInfoService;

/**
 * @author neillin
 */
@View(name = "home", withToolbar = true, description = "短线放大镜", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.home_page")
public abstract class HomePage extends PageBase {
	static Trace log;

	@Bean(type = BindingType.Service)
	IUserManagementService usrMgr;

	@Bean(type = BindingType.Pojo, express = "${usrMgr.clientInfo}")
	ClientInfoBean vertionInfoBean;

	@Bean(type = BindingType.Pojo, express = "${usrMgr.myUserInfo}")
	UserBean userInfo;
	// @Menu(items={"home","page1","page2","page3","page4","page5","page6"})
	@Menu(items = { "home", "page1", "page2", "page3", "page6" })
	IMenu leftMenu;

	@Menu(items = { "left", "right", "search" })
	IMenu toolbar;
	// @ViewGroup(viewIds={"tradingMain","tradingWinner","infoCenter","championShip","todayHotRankView","masterRankView","helpCenter"})
	@ViewGroup(viewIds = { "tradingMain", "tradingWinner", "infoCenter",
			"championShip", "helpCenter" })
	IViewGroup contents;

	String curVertion;

	String remoteVertion;
	
	boolean alertUpdateEnabled = true;
	
	@OnShow
	void initData() {

		curVertion = AppUtils.getFramework().getApplicationVersion();
		if (log.isDebugEnabled()) {
			log.debug("CurVertion:" + curVertion);
		}
		IUIComponent vertionItem = getChild("rpage3");
		vertionItem.setAttribute(AttributeKeys.title, "版本:" + curVertion);
		if (vertionInfoBean == null) {
			return;
		}

		remoteVertion = vertionInfoBean.getVersion();

		if (remoteVertion == null) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("RemoteVertion:" + remoteVertion);
		}
		boolean isLastest = curVertion.equals(remoteVertion) ? true : false;
		if (isLastest) {
			return;
		} else {
			vertionItem.setAttribute(AttributeKeys.icon,
					"resourceId:drawable/v_update");
			alertUpdateEnabled = AppUtils.getFramework().getService(IClientInfoService.class).alertUpdateEnabled();
			if (alertUpdateEnabled) {
				updateSelection(new VertionUpdateSelection(
						vertionInfoBean.getUrl()));
				KUtils.executeTask(new Runnable() {

					@Override
					public void run() {

						SystemClock.sleep(10000);
						AppUtils.runOnUIThread(new Runnable() {

							@Override
							public void run() {
								getUIContext()
										.getWorkbenchManager()
										.getWorkbench()
										.createDialog("AlertVertionUpdate",
												new HashMap<String, Object>())
										.show();
							}
						});
					}
				});
			}
			
		}

	}

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "左菜单", icon = "resourceId:drawable/list_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		rightMenu.hide();
		if (leftMenu.isOnShow()) {
			leftMenu.hide();
		} else {
			leftMenu.show();
		}
		return null;
	}

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "右菜单", icon = "resourceId:drawable/user_button_style") })
	String toolbarClickedRight(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :right was clicked !");
		}
		leftMenu.hide();
		if (rightMenu.isOnShow()) {
			rightMenu.hide();
		} else {
			rightMenu.show();
		}
		return null;
	}

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "search", label = "搜索", icon = "resourceId:drawable/find_button_style") }, navigations = { @Navigation(on = "*", showPage = "GeGuStockPage") })
	String toolbarClickedSearch(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :search was clicked !");
		}
		updateSelection(new StockSelection());
		return "";
	}

	@Command(description = "Invoke when a menu item was clicked", commandName = "doNavigation", uiItems = {
			@UIItem(id = "home", label = "首页", icon = "resourceId:drawable/home"),
			@UIItem(id = "page1", label = "赚钱榜", icon = "resourceId:drawable/zpb"),
			@UIItem(id = "page2", label = "行情中心", icon = "resourceId:drawable/hqzx"),
			@UIItem(id = "page3", label = "大赛排行榜", icon = "resourceId:drawable/dsphb"),
			// @UIItem(id="page4",label="今日热股榜",icon="resourceId:drawable/rgb"),
			// @UIItem(id="page5",label="高手榜",icon="resourceId:drawable/gsb"),
			@UIItem(id = "page6", label = "帮助中心", icon = "resourceId:drawable/help") }, navigations = {
			@Navigation(on = "home", showView = "tradingMain"),
			@Navigation(on = "page1", showView = "tradingWinner"),
			@Navigation(on = "page2", showView = "infoCenter"),
			@Navigation(on = "page3", showView = "championShip"),
			// @Navigation(on="page4",showView="todayHotRankView"),
			// @Navigation(on="page5",showView="masterRankView"),
			@Navigation(on = "page6", showView = "helpCenter") })
	String menuClicked(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			String name = ((IUICommand) event.getProperty("ItemClicked"))
					.getName();
			if (log.isDebugEnabled()) {
				log.debug("Menu item :" + name + " was clicked !");
			}
			// if("page6".equals(name)){
			// try {
			// Thread.sleep(5000L);
			// } catch (InterruptedException e) {
			// }
			// }
			return name;
		}
		return null;
	}

	@Menu(items = { "rhome", "rpage1", "rpage2", "rpage3" })
	private IMenu rightMenu;

	@Command(description = "Invoke when a menu item was clicked", commandName = "doNavigationRight", uiItems = {
			@UIItem(id = "rhome", label = "我的主页", icon = "resourceId:drawable/rz", visibleWhen = "${userInfo != null}"),
			@UIItem(id = "rpage1", label = "交易记录", icon = "resourceId:drawable/jyjl", visibleWhen = "${userInfo != null}"),
			@UIItem(id = "rpage2", label = "设置", icon = "resourceId:drawable/seting"),
			@UIItem(id = "rpage3", label = "版本:1.0.0", icon = "resourceId:drawable/v_default"/*
																							 * ,
																							 * enableWhen
																							 * =
																							 * "${(remoteVertion!=null&&curVertion!=null)?(curVertion!=remoteVertion?true:false):false}"
																							 */) }, navigations = {
			@Navigation(on = "rhome", showPage = "userPage", keepMenuOpen = true),
			@Navigation(on = "rpage1", showPage = "userTradeRecordPage", keepMenuOpen = true),
			@Navigation(on = "rpage2", showPage = "appSetPage", keepMenuOpen = true),
			@Navigation(on = "*", showDialog = "noVerUpdateDialog", keepMenuOpen = true),
			@Navigation(on = "+", showDialog = "updateVertionDialog", keepMenuOpen = true) })
	String menuRightClicked(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			String name = ((IUICommand) event.getProperty("ItemClicked"))
					.getName();
			if (log.isDebugEnabled()) {
				log.debug("Menu item :" + name + " was clicked !");
			}
			if (name != null && name.equals("rpage3")) {
				curVertion = AppUtils.getFramework().getApplicationVersion();

				if (vertionInfoBean == null) {
					return "";
				}

				remoteVertion = vertionInfoBean.getVersion();

				if (remoteVertion == null) {
					return "";
				}

				boolean isLastest = curVertion.equals(remoteVertion) ? true
						: false;
				if (isLastest) {
					return "";
				} else {
					updateSelection(new VertionUpdateSelection(
							vertionInfoBean.getUrl()));
					return "+";

				}
			}

			return name;
		}
		return null;
	}
}
