package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "SystemNewsPage", withToolbar = true, description = "系统消息", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.system_news_layout")
public abstract class SystemNewsPage extends PageBase {

	@Field(valueKey = "text", attributes = {
			@Attribute(name="noMoreDataAlert", value="${newsNoMoreAlert}"),
			@Attribute(name = "enablePullDownRefresh", value = "false"),
			@Attribute(name = "enablePullUpRefresh", value = "${accountTradeListBean!=null&&accountTradeListBean.data!=null&&accountTradeListBean.data.size()>0?true:false}") })
	String refreshView;

	RemindMessageBean message;

	String accId = null;

	@Bean
	boolean newsNoMoreAlert = false;
	
	TradingAccountBean accountBean = null;
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${usrService.getRemindMessageBean()}", effectingFields="systemNewsInfos")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;
	
	private ELBeanValueEvaluator<BindableListWrapper> accountTradeListBeanUpdater;

	@Field(valueKey = "options", binding = "${accountTradeListBean!=null?accountTradeListBean.getData(true):null}", upateAsync = true)
	List<RemindMessageBean> systemNewsInfos;

	DataField<List> systemNewsInfosField;
	@Menu(items = { "left", "right" })
	protected IMenu toolbar;

	private int oldDataSize;

	private int newDataSize;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Command(uiItems = { @UIItem(id = "right", label = "设置", icon = "resourceId:drawable/button_setting", visibleWhen = "${true}") }, navigations = { @Navigation(on = "*", showPage = "PushMessageSetPage") })
	String toolbarClickedRight(InputEvent event) {
		return "*";
	}

	@OnHide
	void onHideOption() {
		newsNoMoreAlert = false;
		registerBean("newsNoMoreAlert", newsNoMoreAlert);
	}
	
	/** 处理消息点击事 */
	@Command(navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage"),
			@Navigation(on = "BuyInT3", showPage = "TBuyT3TradingPageView"),
			@Navigation(on = "BuyInTD", showPage = "TBuyTdTradingPageView"),
			@Navigation(on = "SellOutT3", showPage = "SellT3TradingPageView"),
			@Navigation(on = "SellOutTD", showPage = "SellTDTradingPageView") })
	CommandResult handleItemClick(ExecutionStep step, InputEvent event,
			Object result) {

		switch (step) {
		case PROCESS:
			int position = (Integer) event.getProperty("position");

			if (accountTradeListBean != null) {
				List<RemindMessageBean> remindMessageList = accountTradeListBean
						.getData();
				message = remindMessageList.get(position);
			}

			AsyncUtils.execRunnableAsyncInUI(new Runnable() {

				@Override
				public void run() {
					if (message != null) {
						accId = message.getAttrs().get("acctID");

						String messageId = message.getId();

						if (StringUtils.isNotBlank(messageId)) {
							if (usrService != null) {
								usrService.readRemindMessage(messageId);
							}
						}
					}
					if (tradingService != null) {
						if (StringUtils.isNotBlank(accId))
							accountBean = tradingService
									.getTradingAccountInfo(accId);
					}
				}
			});
			break;
		case NAVIGATION:
			CommandResult commResult = null;
			if (accountBean != null) {

				boolean isVirtual = accountBean.getVirtual();
				String tradeStatus = accountBean.getOver();

				//ASTOCK1、ASTOCK3，ASTOCKN
				String accType = accountBean.getDayType();
				commResult = new CommandResult();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(Constants.KEY_ACCOUNT_ID_FLAG, accId);
				map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
				map.put("isSelf", true);
				commResult.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					commResult.setResult("operationDetails");
				}

				if ("UNCLOSE".equals(tradeStatus)) {

					int accountStatus = accountBean.getStatus();
					if (accountStatus == 0) {
						//
						if(accType != null) {
							if(accType.equals("ASTOCKT1"))
								commResult.setResult("SellOut");
							else if(accType.equals("ASTOCKT3")) {
								commResult.setResult("SellOutT3");
							} else if(accType.equals("ASTOCKTN")) {
								commResult.setResult("SellOutTD");
							}
						}
						//commResult.setResult("SellOut");
					} else if (accountStatus == 1) {
						commResult.setResult("BuyIn");
						
						if(accType != null) {
							if(accType.equals("ASTOCKT1"))
								commResult.setResult("BuyIn");
							else if(accType.equals("ASTOCKT3")) {
								commResult.setResult("BuyInT3");
							} else if(accType.equals("ASTOCKTN")) {
								commResult.setResult("BuyInTD");
							}
						}
					}
				}
				updateSelection(new AccidSelection(String.valueOf(accId),
						isVirtual));
				
				accountBean = null;
				return commResult;
			}
			break;
		default:
			break;
		}

		return null;
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		accountTradeListBeanUpdater.doEvaluate();
		return null;
	}
	
	@Command
	String handleRefresh(ExecutionStep step, InputEvent event, Object result) {
		if(event.getEventType().equals("TopRefresh")) {
			if(usrService != null) {
				usrService.getRemindMessageBean();
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			newsNoMoreAlert = false;
			registerBean("newsNoMoreAlert", newsNoMoreAlert);
			if(accountTradeListBean != null) {

				switch (step) {
				case PROCESS:
					if(accountTradeListBean.getData() != null && accountTradeListBean.getData().size()>0)
						oldDataSize = accountTradeListBean.getData().size();
					
					accountTradeListBean.loadMoreData();
					break;
				case NAVIGATION:
					if(accountTradeListBean.getData() != null && accountTradeListBean.getData().size()>0) {
						newDataSize = accountTradeListBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						newsNoMoreAlert = true;
						registerBean("newsNoMoreAlert", newsNoMoreAlert);
					} else {
						return null;
					}
					
				default:
					break;
				}
			
			}
		}
		return null;
	}
}
