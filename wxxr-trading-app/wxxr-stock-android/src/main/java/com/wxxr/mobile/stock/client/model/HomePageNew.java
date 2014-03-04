/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IViewActivationListener;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IClientInfoService;

/**
 * @author dz
 */
@View(name = "home", withToolbar = true, description = "短线放大镜", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.home_page_new")
public abstract class HomePageNew extends PageBase {
	private static final Trace log = Trace.register(HomePageNew.class);
	private static final String[] VIEWIDs = new String[] { "MainHomeView",
			"TradingPageView", "helpCenter", "infoCenter", "AppManageView" };

	@Bean(type = BindingType.Service)
	IUserManagementService usrMgr;

	@Bean(type = BindingType.Service)
	IUserIdentityManager usridentityMgr;

	@Bean(type = BindingType.Pojo, express = "${usrMgr.getRemindMessageBean()}")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrMgr.clientInfo}")
	ClientInfoBean vertionInfoBean;

	/*
	 * @Bean(type = BindingType.Pojo, express = "${usrMgr.clientInfo}")
	 * ClientInfoBean vertionInfoBean;
	 */

	@ViewGroup(viewIds = { "MainHomeView", "TradingPageView", "helpCenter",
			"infoCenter", "AppManageView" })
	IViewGroup contents;

	String curVertion;

	String remoteVertion;

	IViewActivationListener listener;

	boolean alertUpdateEnabled = true;
	boolean isLastestVertion = false;
	boolean alertDialogShown = false;
	@Field(valueKey = "selectedIdx")
	int onShowViewIndex = 0;
	DataField<Integer> onShowViewIndexField;

	// private ClientInfoBean vertionInfoBean;

	@OnUICreate
	void onUICreateOption() {
		isLastestVertion = false;
		//AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(false);
	}

	@OnCreate
	void handleOnCreateAction() {
		curVertion = AppUtils.getFramework().getApplicationVersion();
		final Runnable[] tasks = new Runnable[1];

		tasks[0] = new Runnable() {

			@Override
			public void run() {
				if (isLastestVertion) {
					return;
				}

				if (log.isDebugEnabled()) {
					log.debug("HomePageNew : checkVertion");
				}
				checkVertion();
				AppUtils.runOnUIThread(tasks[0], 15, TimeUnit.SECONDS);
			}
		};

		AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
	}

	private void checkVertion() {
		if (vertionInfoBean == null) {
			if (log.isDebugEnabled()) {
				log.debug("HomePageNew : vertionInfoBean is null");
			}
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("HomePageNew : vertionInfoBean not null");
		}

		remoteVertion = vertionInfoBean.getVersion();

		if (remoteVertion == null) {
			if (log.isDebugEnabled()) {
				log.debug("HomePageNew : remoteVertion is null");
			}
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("HomePageNew : remoteVertion:" + remoteVertion);
		}

		boolean isLastest = curVertion.compareTo(remoteVertion) >= 0 ? true
				: false;
		if (isLastest) {
			if (log.isDebugEnabled()) {
				log.debug("HomePageNew : currentVertion is lastest!");
			}
			isLastestVertion = true;
			return;
		} else {

			alertUpdateEnabled = AppUtils.getFramework()
					.getService(IClientInfoService.class).alertUpdateEnabled();
			alertDialogShown = AppUtils.getFramework().getService(IClientInfoService.class).alertUpdateDialogShown();
			if (log.isDebugEnabled()) {
				log.debug("HomePageNew : alertUpdateEnabled:"
						+ alertUpdateEnabled);
			}
			if (alertUpdateEnabled && !alertDialogShown) {
				updateSelection(new VertionUpdateSelection(
						vertionInfoBean.getUrl(), vertionInfoBean.getDescription()));
				KUtils.invokeLater(new Runnable() {

					@Override
					public void run() {

						AppUtils.runOnUIThread(new Runnable() {

							@Override
							public void run() {

								IDialog dialog = getUIContext().getWorkbenchManager().getWorkbench().createDialog("AlertVertionUpdate", new HashMap<String, Object>());
                            	if(dialog != null) {
                            		dialog.show();
                            		AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(true);
                            	}
								//AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(true);
							}
						});

					}
				});
			}

		}
	}

	/**
	 * 首页点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showView = "MainHomeView") })
	// , closeCurrentView=true, params = { @Parameter(name="add2BackStack",
	// type=ValueType.STRING, value="false")}) })
	String homeMainClick(InputEvent event) {
		return "";
	}

	/**
	 * 交易点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showView = "TradingPageView") })
	String tradingClick(InputEvent event) {
		return "";
	}

	/**
	 * 帮助点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showView = "helpCenter") })
	String helpClick(InputEvent event) {
		return "";
	}

	/**
	 * 行情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showView = "infoCenter") })
	String infoClick(InputEvent event) {
		return "";
	}

	/**
	 * 管理点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showView = "AppManageView") })
	String manageClick(InputEvent event) {
		return "";
	}

	@OnCreate
	void addListener() {
		this.listener = new IViewActivationListener() {

			@Override
			public void viewActivated(String viewId) {
				for (int i = 0; i < VIEWIDs.length; i++) {
					if (VIEWIDs[i].equals(viewId)) {
						onShowViewIndexField.setValue(RUtils.getInstance()
								.getResourceIdByURI(
										"resourceId:id/radio_btn_" + i));
						break;
					}
				}
			}
		};
		this.contents.addViewActivationListner(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#onBackPressed()
	 */
	@Override
	public boolean onBackPressed() {
		return true;
	}

}
