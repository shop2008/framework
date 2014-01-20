package com.wxxr.mobile.stock.client.model;


import android.os.SystemClock;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.model.UserApplyCashCallBack;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.String2StringConvertor;

/**
 * 提现金界面
 * @author renwenjie
 */
@View(name="userWithDrawCashPage", withToolbar=true, description="提现金")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.withdraw_cath_page_layout")
public abstract class UserWithDrawCashPage extends PageBase{

	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	
	@Bean(type = BindingType.Service)
	IArticleManagementService articleService;
	
	@Bean(type = BindingType.Pojo, express = "${articleService.withdrawalNoticeArticle}")
	BindableListWrapper<ArticleBean> articleBean;
	
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
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
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
	@ExeGuard(title = "提取现金", message = "正在处理，请稍候...", silentPeriod = 200)
	String commit(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
		
			SystemClock.sleep(500);
			if (tradingService != null) {
				
				
				if (!StringUtils.isBlank(callBack.getApplyAmount())) {
					
					if(userAssetBean!=null) {
						long useableBalance = userAssetBean.getUsableBal();
						if(useableBalance < Long.parseLong(callBack.getApplyAmount())*100) {
							throw new StockAppBizException("余额不足!");
						}
					}
					tradingService.applyDrawMoney(Long.parseLong(callBack.getApplyAmount())*100);
				} else {
					tradingService.applyDrawMoney(-1l);
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
			navigations={@Navigation(on="OK", showPage="webPage")}
			)
	
	CommandResult withDrawCashRules(InputEvent event) { 
		CommandResult result = new CommandResult();
		if(articleBean != null)
			result.setPayload((articleBean.getData()!=null)&&(articleBean.getData().size()>0)?articleBean.getData().get(0).getArticleUrl():null);
		result.setResult("OK");
		
		return result;
	}
	
	@OnUIDestroy
	protected  void clearData() {
		callBack.setApplyAmount("");
	}
}
