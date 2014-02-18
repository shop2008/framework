package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="ApplyMoneyAuthConfirmDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.apply_money_auth_confirm_dialog")
public abstract class ApplyMoneyAuthConfirmDialog extends ViewBase implements IModelUpdater {

	@Bean(type = BindingType.Service)
	IUserManagementService userService;
	
	@Field(valueKey = "text",binding="${accountName}")
	String accountName;

	
	@Field(valueKey = "text",binding="${bankName}")
	String bankName;
	
	@Field(valueKey = "text",binding="${bankAddr}")
	String bankAddr;
	
	
	@Field(valueKey = "text",binding="${bankNum}")
	String bankNum;
	
	String accountNameStr;
	String bankNameStr;
	String bankAddrStr;
	String bankNumStr;
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		if(value instanceof Map) {
			Map map = (Map)value;
			if (map.containsKey("accountName")) {
				registerBean("accountName", map.get("accountName"));
				accountNameStr = (String)map.get("accountName");
			}
			
			if (map.containsKey("bankName")) {
				registerBean("bankName", map.get("bankName"));
				bankNameStr = (String)map.get("bankName");
			}
			
			if (map.containsKey("bankAddr")) {
				registerBean("bankAddr", map.get("bankAddr"));
				bankAddrStr = (String)map.get("bankAddr");
			}
			
			if (map.containsKey("bankNum")) {
				registerBean("bankNum", map.get("bankNum"));
				bankNumStr = (String)map.get("bankNum");
			}
		}
	}
	
	@Command(navigations={ @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }),
			@Navigation(on="OK", showDialog="CardAuthSuccessDialogView")})
	String done(InputEvent event) {
		
		hide();
		userService.withDrawCashAuth(accountNameStr, bankNameStr, bankAddrStr, bankNumStr);
		
		//throw new StockAppBizException("认证成功");
		return "OK";
		//return null;
	}
	
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
}
