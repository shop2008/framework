package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.FieldUpdating;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.model.UserDrawCachAuthCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "withDrawCashAuthPage", withToolbar=true, description="提现认证")
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.withdraw_cash_auth_layout")
public abstract class UserWithDrawCashAuthPage extends PageBase {

	/**
	 * 户名
	 */
	@Field(valueKey = "text", binding = "${callBack.accountName}")
	String accountName;

	/**
	 * 银行名称
	 */
	@Field(valueKey = "text", binding = "${callBack.bankName}")
	String bankName;

	/**
	 * 开户行所在地
	 */
	@Field(valueKey = "text", binding = "${callBack.bankAddr}")
	String bankAddr;

	/**
	 * 银行卡号
	 */
	@Field(valueKey = "text", binding = "${callBack.bankNum}")
	String bankNum;

	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Bean(type = BindingType.Service)
	IUserManagementService userService;

	@Bean
	UserDrawCachAuthCallBack callBack = new UserDrawCachAuthCallBack();

	
	/**
	 * 提现认证处理
	 * @param event
	 * @return
	 */
	@Command(commandName = "cashAuth", description = "Back To Last UI", 
			updateFields = {
				@FieldUpdating(fields={"accountName", "bankName", "bankAddr", "bankNum"},message="请输入正确的提现认证信息")
			},navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }),
			@Navigation(on="ApplyMoneyAuthConfirmDialog", showDialog="ApplyMoneyAuthConfirmDialog")
			
	})
	@ExeGuard(title = "提现认证", message = "正在处理，请稍候...", silentPeriod = 200)
	CommandResult cashAuth(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			if(StringUtils.isBlank(callBack.getAccountName())) {
				throw new StockAppBizException("帐户名不能为空");
			}
			
			if(StringUtils.isBlank(callBack.getBankName())) {
				throw new StockAppBizException("开户行不能为空");
			}
			
			if(StringUtils.isBlank(callBack.getBankAddr())) {
				throw new StockAppBizException("开户行所在地不能为空");
			}
			
			if(StringUtils.isBlank(callBack.getBankNum())) {
				throw new StockAppBizException("银行卡号不能为空");
			}
			
			CommandResult result = new CommandResult();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("accountName", callBack.getAccountName());
			map.put("bankName", callBack.getBankName());
			map.put("bankAddr", callBack.getBankAddr());
			map.put("bankNum", callBack.getBankNum());
			result.setPayload(map);
			result.setResult("ApplyMoneyAuthConfirmDialog");
			return result;
			
		}
		return null;
	}

	@OnUIDestroy
	protected void clearData() {
		callBack.setAccountName("");
		callBack.setBankAddr("");
		callBack.setBankName("");
		callBack.setBankNum("");
	}
}
