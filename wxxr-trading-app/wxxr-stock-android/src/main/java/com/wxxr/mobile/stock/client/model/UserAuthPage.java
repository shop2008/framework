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
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
//import com.wxxr.mobile.stock.client.bean.AuthInfoBean;
import com.wxxr.mobile.stock.client.utils.String2StringConvertor;

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
	
	@Bean(type=BindingType.Pojo,express="${usrMgr.userAuthInfo}")
	AuthInfo authBean;
	
	/**手机未认证部分*/
	@Field(valueKey="visible", binding="${false}")
	boolean mobileUnAuthBody;
	
	/**手机已认证部分*/
	@Field(valueKey="visible", binding="${true}")
	boolean mobileAuthedBody;
	
	/**提现认证未认证部分*/
	@Field(valueKey="visible", binding="${authBean!=null&&authBean.bankNum!=null&&authBean.bankNum.length()>0?false:true}")
	boolean bankCardUnAuthBody;
	
	/**提现认证已认证部分*/
	@Field(valueKey="visible",binding="${authBean!=null&&authBean.bankNum!=null&&authBean.bankNum.length()>0?true:false}")
	boolean bankCardAuthedBody;
	/**
	 * 认证手机号
	 */
	@Field(valueKey="text", binding="${user!=null&&user.phoneNumber!=null?user.phoneNumber:'--'}")
	String authMobileNum;
	
	@Convertor(
			params={
				@Parameter(name="replace", value="x")
			}
			)
	String2StringConvertor s2sConvertor;
	
	
	@Convertor(params={@Parameter(name="format", value="%.0f")})
	String2StringConvertor l2StrConvertor;
	
	/**
	 * 银行卡用户名
	 */
	
	@Field(valueKey="text", binding="${authBean!=null?authBean.accountName:null}", converter="s2sConvertor")
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
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
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
					@Navigation(on="InputPswDialog",showDialog="InputPswDialog")
					}
			)
	CommandResult switchBankCard(InputEvent event) {
		
		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "switchBankCard");
		result.setPayload(map);
		result.setResult("InputPswDialog");
		return result;
	}
	
	/**手机认证*/
	@Command
	String mobileNumAuth(InputEvent event) {
		return null;
	}
	
	
	/**银行卡认证*/
	@Command(navigations = { 
			@Navigation(
					on = "*", 
					showPage = "withDrawCashAuthPage"
					) 
			})
	String bankCardAuth(InputEvent event) {
		return "*";
	}
	
	
}
