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
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name="SystemNewsPage", withToolbar=true, description="系统消息")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.system_news_layout")
public abstract class SystemNewsPage extends PageBase {

	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "false"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String refreshView;
	
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${usrService.getRemindMessageBean()}")
	BindableListWrapper<RemindMessageBean> accountTradeListBean;
	
	@Field(valueKey="options", binding="${accountTradeListBean!=null?accountTradeListBean.data:null}", visibleWhen = "${(curItemId==0)&&(accountTradeListBean.data!=null?(accountTradeListBean.data.size()>0?true:false):false)}")
	List<RemindMessageBean> systemNewsInfos;
	
	
	@Menu(items = {"left","right"})
	protected IMenu toolbar;
	
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(uiItems = { @UIItem(id = "right", label = "设置", icon = "resourceId:drawable/button_setting")},
			navigations = {@Navigation(on="*",showPage="PushMessageSetPage")})
	String toolbarClickedRight(InputEvent event) {
		return "*";
	}
	/**处理消息点击事*/
	@Command(navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult handleItemClick(InputEvent event) {
		int position = (Integer) event.getProperty("position");
		RemindMessageBean message = null;

		if (accountTradeListBean != null) {
			List<RemindMessageBean> remindMessageList = accountTradeListBean.getData();
			message =remindMessageList.get(position);
		}
		String accId = null;
		if(message != null)
			accId = message.getAttrs().get("acctID");
		TradingAccountBean accountBean = null;
		if (tradingService != null) {
			accountBean = tradingService.getTradingAccountInfo(accId);
		}

		CommandResult result = null;
		if (accountBean != null) {

			boolean isVirtual = accountBean.getVirtual();
			String tradeStatus = accountBean.getOver();

			result = new CommandResult();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ACCOUNT_ID_FLAG, accId);
			map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
			map.put("isSelf", true);
			result.setPayload(map);
			if ("CLOSED".equals(tradeStatus)) {
				// result.setPayload(map);
				result.setResult("operationDetails");
				// updateSelection(new AccidSelection(accId, isVirtual));
			}

			if ("UNCLOSE".equals(tradeStatus)) {

				int accountStatus = accountBean.getStatus();
				if (accountStatus == 0) {
					result.setResult("SellOut");
				} else if (accountStatus == 1) {
					result.setResult("BuyIn");
				}
				// result.setPayload(map);
			}
			updateSelection(new AccidSelection(String.valueOf(accId), isVirtual));
			return result;
		}
		return null;
	}
}
