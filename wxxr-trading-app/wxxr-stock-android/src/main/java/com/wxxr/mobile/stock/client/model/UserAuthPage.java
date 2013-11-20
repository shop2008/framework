/**
 * 
 */
package com.wxxr.mobile.stock.client.model;


import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
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
import com.wxxr.mobile.stock.app.bean.AuthInfoBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
//import com.wxxr.mobile.stock.client.bean.AuthInfoBean;

/**
 * @author neillin
 *
 */
@View(name="userAuthPage", withToolbar=true, description="我的认证")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.user_auth_page_layout")
public abstract class UserAuthPage extends PageBase {

	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;
	
	@Bean(type=BindingType.Pojo,express="${usrMgr.myUserInfo}")
	UserBean user;
	
	@Bean(type=BindingType.Pojo,express="${user!=null?user.bankInfo!=null?user.bankInfo:null:null}")
	AuthInfoBean authBean;
	
	/**
	 * 认证手机号
	 */
	@Field(valueKey="text", binding="${user!=null?user.phoneNumber:'--'}")
	String authMobileNum;
	
	
	/**
	 * 银行卡认证，用于控制已认证布局的显示及隐藏
	 */
	@Field(valueKey="visible", binding="${authBean!=null?true:false}")
	boolean authedBodyVisible;
	
	
	/**
	 * 银行卡认证，用于控制未认证布局的显示及隐藏
	 */
	@Field(valueKey="visible", binding="${authBean!=null?false:true}")
	boolean notAuthBodyVisible;
	
	
	/**
	 * 银行卡用户名
	 */
	
	@Field(valueKey="text", binding="${authBean!=null?authBean.accountName:null}")
	String accountName;
	
	/**
	 * 开户银行名称
	 */
	@Field(valueKey="text", binding="${authBean!=null?authBean.bankName:null}")
	String bankName;
	
	/**
	 * 银行卡号
	 */
	@Field(valueKey="text", binding="${authBean!=null?authBean.bankNum:null}")
	String bankNum;
	
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	/**
	 * 提现认证
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "bankAuth", 
			description = "To WithDrawCashAuth UI", 
			navigations = { 
					@Navigation(
							on = "SUCCESS", 
							showPage = "withDrawCashAuthPage"
							) 
					}
			)
	String bankAuth(InputEvent event) {
		return "SUCCESS";
	}
	
	/**
	 * 更换银行卡
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "switchBankCard", 
			description = "To SwitchBanCard UI", 
			navigations = { 
					@Navigation(
							on = "SUCCESS", 
							showPage = "userSwitchCardPage"
							) 
					}
			)
	CommandResult switchBankCard(InputEvent event) {
		
		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("accountName", authBean.getAccountName());
		result.setPayload(map);
		result.setResult("SUCCESS");
		return result;
	}
}
