package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.model.UserApplyCashCallBack;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.String2StringConvertor;

/**
 * 提现金界面
 * @author renwenjie
 *
 */
@View(name="userWithDrawCashPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.withdraw_cath_page_layout")
public abstract class UserWithDrawCashPage extends PageBase{

	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Bean(type=BindingType.Pojo, express="${usrService.userAssetBean}")
	UserAssetBean userAssetBean;
	
	@Bean(type=BindingType.Pojo, express="${usrService.userAuthInfo}")
	AuthInfo authInfoBean;
	
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.usableBal:''}",converter="stockL2StrConvertor")
	String avaliCashAmount;
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Field(
			valueKey="text", 
			attributes={@Attribute(name="enabled", value="${checked}")}
			)
	String commitBtn;
	
	
	@Convertor(
			params={
				@Parameter(name="replace",value="x")
				}
			)
	String2StringConvertor s2sConvertor;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%.2f"),
					@Parameter(name="formatUnit", value="元"),
					@Parameter(name="multiple", value="100.0f"),
					@Parameter(name="nullString",value="0.00元")
			})
	StockLong2StringConvertor stockL2StrConvertor;
	
	@Convertor(params={@Parameter(name="format", value="%.0f")})
	String2StringConvertor s2sConvertorBanNum;
	
	@Field(valueKey="text", binding="${callBack.applyAmount}")
	String availCashAmountET;
	
	@Field(valueKey="checked", binding="${checked}")
	boolean readChecked;
	
	@Bean
	boolean checked = true;
	
	@Bean
	UserApplyCashCallBack callBack = new UserApplyCashCallBack();
	
	@Field(valueKey="text", binding="${authInfoBean!=null?authInfoBean.bankNum:''}", converter="s2sConvertorBanNum")
	String bankNum;
	
	@Field(valueKey="text", binding="${authInfoBean!=null?authInfoBean.bankAddr:'--'}")
	String bankAddr;
	
	@Field(valueKey="text", binding="${authInfoBean!=null?authInfoBean.bankName:'--'}")
	String bankName;
	
	@Field(valueKey="text", binding="${authInfoBean!=null?authInfoBean.accountName:''}", converter="s2sConvertor")
	String accountName;
	/**
	 * 返回到上一个界面
	 * @param event
	 * @return
	 */
	@Command
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}

	@Command(
			navigations = { 
					@Navigation(
							on = "StockAppBizException", 
							message = "%m%n", 
							params = {
									@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
									@Parameter(name = "title", value = "错误")
									}
							) 
					}
			)
	@ExeGuard(title = "提取现金", message = "正在处理，请稍候...", silentPeriod = 1)
	String commit(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
		
			if (tradingService != null) {
				
				if (!StringUtils.isBlank(callBack.getApplyAmount())) {
					tradingService.applyDrawMoney(Long.parseLong(callBack.getApplyAmount())*100);
				} else {
					tradingService.applyDrawMoney(-1);
				}
			}
			hide();
		}
		return null;
	}
	

	
	@Command
	String setReadChecked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {

			checked = !checked;
			registerBean("checked", checked);
		}
		return null;
	}
	
	/*@OnShow
	protected void initData() {
		this.readChecked = true;
		this.readCheckedField.setValue(true);
	}*/
	
	@Command(
			commandName="withDrawCashRules",
			navigations={@Navigation(on="OK", showPage="articleBodyPage")}
			)
	
	CommandResult withDrawCashRules(InputEvent event) { 
		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("loadUrl", "http://m.hao123.com");
		result.setPayload(map);
		result.setResult("OK");
		return result;
	}
	
	@OnUIDestroy
	protected  void clearData() {
		callBack.setApplyAmount("");
	}
}
