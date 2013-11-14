/**
 * 
 */
package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
//import com.wxxr.mobile.stock.client.bean.AuthInfoBean;
import com.wxxr.mobile.stock.client.bean.AuthInfoBean;
import com.wxxr.mobile.stock.client.bean.UserBean;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * @author neillin
 *
 */
@View(name="userAuthPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.user_auth_page_layout")
public abstract class UserAuthPage extends PageBase {

	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;
	
	@Bean(type=BindingType.Pojo,express="${usrMgr.userAuthInfo}")
	AuthInfoBean authBean;
	
	/**
	 * 认证手机号
	 */
	@Field(valueKey="text", binding="${usrMgr.getUserAuthMobileNum('123')}")
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
							showPage = "userSwitchCardPage", 
							params={@Parameter(name="accountName", value="${authBean!=null?authBean.accountName:null}", type=ValueType.STRING)}
							) 
					}
			)
	String switchBankCard(InputEvent event) {
		
		return "SUCCESS";
	}
	
	/**
	 * 返回到上一界面
	 * @param event
	 * @return
	 */
	@Command(commandName="back")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
}
