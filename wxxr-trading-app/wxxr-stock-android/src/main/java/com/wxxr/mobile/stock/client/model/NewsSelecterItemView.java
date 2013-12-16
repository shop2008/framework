package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name="NewsSelecterItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.news_item_layout")
public abstract class NewsSelecterItemView extends ViewBase implements IModelUpdater{

	
	@Bean
	RemindMessageBean message;
	
	@Field(valueKey="text", binding="${message.attrs.get('time')}")
	String date;
	
	@Field(valueKey="text", binding="${message.title}")
	String title;
	
	@Field(valueKey="text", binding="${message.content}")
	String content;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof RemindMessageBean) {
			registerBean("message", value);
			message = (RemindMessageBean)value;
		}
	}
	
	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;
	
	@Command(navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails",params={
					@Parameter(name = "add2BackStack", value = "false")
			}),
			@Navigation(on="sellTradingAccount",showPage="sellTradingAccount")
			})
	CommandResult handleNewsItemClick(InputEvent event) {
		
		String accId = message.getAcctId();
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
}
