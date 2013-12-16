package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "userNewsPage", withToolbar = true, description = "消息")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_news_center_page_layout")
public abstract class UserNewsPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	
	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${usrService.remindMessageBean}")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;

	@Bean(type = BindingType.Pojo, express = "${usrService.getPullMessageBean(0,10)}")
	BindableListWrapper<PullMessageBean> infoNoticeListBean;

	/** 账户&交易-数据 */
	@Field(valueKey = "options", binding = "${accountTradeListBean!=null?accountTradeListBean.data:null}", visibleWhen = "${curItemId==0}")
	List<RemindMessageBean> accountTradeInfos;

	/** 当账户&交易数据为空时显示 */
	@Field(valueKey = "visible", binding = "${false}")
	boolean newsAccountNullVisible;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 0}") })
	boolean accountTrades;

	/** 更新RadioButton的选中状态 */
	@Field(valueKey = "checked", attributes = { @Attribute(name = "checked", value = "${curItemId == 1}") })
	boolean infoNotices;

	/** 资讯&公告-数据 */
	@Field(valueKey = "options", binding = "${infoNoticeListBean!=null?infoNoticeListBean.data:null}", visibleWhen = "${curItemId==1}")
	List<PullMessageBean> noticeInfos;

	@Field(valueKey = "visible", binding = "${false}")
	boolean noticeInfosNullVisible;

	@Bean
	int curItemId = 0;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "false") })
	String noticeRefreshView;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initData() {
		usrService.readAllUnremindMessage();
	}

	@Command
	String showInfoNotices(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			infoNoticeListBean.getData();
		}
		return null;
	}

	@Command
	String showAccountTrades(InputEvent event) {
		curItemId = 0;
		registerBean("curItemId", curItemId);
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			accountTradeListBean.getData();
		}
		return null;
	}

	@Command
	String handleNoticeTopRefresh(InputEvent event) {
		return null;
	}

	@Command(navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails",params={
					@Parameter(name = "add2BackStack", value = "false")
			}),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount")
			})
	CommandResult handleNewsItemClick(InputEvent event) {
		int position = (Integer) event.getProperty("position");
		RemindMessageBean messageBean = accountTradeListBean.getData().get(
				position);
		String accId = messageBean.getAcctId();
		TradingAccountBean accountBean = null;
		if(tradingService != null) {
			 accountBean = tradingService.getTradingAccountInfo(accId);
		}
		
		CommandResult result = null;
		if(accountBean != null) {
			
			boolean isVirtual = accountBean.getVirtual();
			String tradeStatus = accountBean.getOver();
			
			result = new CommandResult();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ACCOUNT_ID_FLAG, accId);
			map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
			if("CLOSED".equals(tradeStatus)){
				result.setPayload(map);
				result.setResult("operationDetails");
				updateSelection(new AccidSelection(accId, isVirtual));
			} 
			
			if("UNCLOSE".equals(tradeStatus)){
				result.setResult("sellTradingAccount");
				result.setPayload(map);
			}
			
			return result;
		}
		
		return null;
	}

	@Command(navigations = { @Navigation(on = "*", showPage = "webPage") })
	CommandResult handleNoticesItemClick(InputEvent event) {
		int position = (Integer) event.getProperty("position");
		PullMessageBean messageBean = infoNoticeListBean.getData()
				.get(position);
		if (usrService != null) {
			usrService.readPullMesage(messageBean.getId());
		}

		CommandResult result = new CommandResult();
		result.setPayload(messageBean.getArticleUrl());
		result.setResult("*");
		return result;
	}
}
