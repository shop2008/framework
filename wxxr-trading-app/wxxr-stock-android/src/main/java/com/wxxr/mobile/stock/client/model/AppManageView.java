package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.app.AppUtils;
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
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IGenericContentService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="AppManageView", withToolbar=true, description="管理")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.app_manage_page")
public abstract class AppManageView extends ViewBase {

	@Bean(type=BindingType.Service)
	IUserLoginManagementService loginMgr;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;
	
	@Bean(type=BindingType.Pojo, express="${usrMgr.voucherBean}")
	VoucherBean voucherBean;
	
	@Bean(type=BindingType.Pojo, express="${usrMgr.userAssetBean}")
	UserAssetBean assetBean;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getVoucherDetails(0,1)}")
	VoucherDetailsBean voucherDetailBean;
	
	
	@Bean(type = BindingType.Pojo, express = "${usrMgr.clientInfo}")
	ClientInfoBean vertionInfoBean;
	
	
	@Bean(type=BindingType.Pojo, express="${loginMgr.myUserInfo}")
	UserBean userInfo;
	
	@Menu(items={"right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="right",label="搜索",icon="resourceId:drawable/find_button_style")
			},navigations = { @Navigation(on = "*", showPage = "GeGuStockPage")}
	)
	String toolbarClickedLeft(InputEvent event) {
		return "";
	}
	
	/**未登录布局的显示及隐藏*/
	@Field(valueKey="visible", visibleWhen="${userInfo==null?true:false}")
	boolean unLoginBody;

	
	/**登录后布局的显示及隐藏*/
	@Field(valueKey="visible", visibleWhen="${userInfo!=null?true:false}")
	boolean loginedBody;
	
	/**用户头像*/
	@Field(valueKey="imageURI", binding="${userInfo!=null&&userInfo.userPic!=null?userInfo.userPic:'resourceId:drawable/head4'}")
	String userPic;
	
	/**用户昵称*/
	@Field(valueKey="text", binding="${userInfo!=null&&userInfo.nickName!=null?userInfo.nickName:'设置昵称'}", enableWhen="${userInfo!=null&&userInfo.nickName!=null?false:true}")
	String userNickName;
	
	/**用户手机号码*/
	@Field(valueKey="text", binding="${userInfo!=null&&userInfo.phoneNumber!=null?userInfo.phoneNumber:'--'}")
	String userPhoneNum;
	
	/**积分余额*/
	@Field(valueKey="text",binding="${(voucherBean!=null&&voucherBean.balance>0)?voucherBean.balance:null}")
	String scroeBalance;
	
	/**帐户余额*/
	@Field(valueKey="text",binding="${(assetBean!=null&&assetBean.balance>0)?assetBean.balance:null}", converter="profitConvertor")
	String userBalance;
	
	/**设置是否接收推送消息*/
	@Field(valueKey="checked",binding="${user!=null?user.messagePushSettingOn:false}")
	boolean pushMsgEnabled;
	
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	
	/**版本*/
	@Field(valueKey="text")
	String vertionField;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%.0f"),
					@Parameter(name="nullString", value="0")
			})
	StockLong2StringConvertor scoreConvertor;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%10.2f"),
					@Parameter(name="formatUnit", value="元"),
					@Parameter(name="multiple", value="100.0f"),
					@Parameter(name="nullString", value="0")
			})
	StockLong2StringConvertor profitConvertor;
	/**跳转到登录界面*/
	@Command(navigations={@Navigation(on="*", showPage="userLoginPage")})
	String login(InputEvent event) {
		//TODO 跳转到登录界面
		return "*";
	}
	
	/**跳转到我的帐号*/
	@Command(navigations={@Navigation(on="*", showPage="AccountManagePage")})
	String enterAccountManagePage(InputEvent event) {
		//TODO 跳转到我的帐号
		return "*";
	}
	
	/**跳转到用户积分界面*/
	@Command(navigations={@Navigation(on="*", showPage="userScorePage")})
	String enterUserScorePage(InputEvent event) {
		//TODO 跳转到用户积分界面
		return "*";
	}
	
	/**跳转到用户帐户界面*/
	@Command(navigations={@Navigation(on="*", showPage="userAccountPage")})
	String enterUserAccountPage(InputEvent event) {
		//TODO 跳转到用户帐户界面
		return "*";
	}
	
	/**提取现金*/
	@Command(navigations={@Navigation(on="userWithDrawCashPage",showPage="userWithDrawCashPage"),@Navigation(on="NoBindCardDialog",showDialog="NoBindCardDialog")})
	String applyMoney(InputEvent event) {
		boolean isBandCard = user.getBindCard();
		if(isBandCard) {
			return "userWithDrawCashPage";
		} else {
			return "NoBindCardDialog";
		}
	}
	
	/**进入我的主页*/
	@Command(navigations={@Navigation(on="*", showPage="userPage")})
	String enterUserPage(InputEvent event) {
		return "*";
	}
	
	/**进入我的认证页面*/
	@Command(navigations={@Navigation(on="*", showPage="userAuthPage")})
	String enterUserAuthPage(InputEvent event) {
		return "*";
	}
	
	/**进入用户交易记录界面*/
	@Command(navigations={@Navigation(on="*", showPage="UserTradeRecordPage")})
	String enterUserTradeRecordPage(InputEvent event) {
		return "*";
	}
	
	/**当切换时会回调此方法*/
	@Command
	String pushMsgStatusChanged(InputEvent event) {
		
		Object obj = event.getProperty("isChecked");
		Boolean isChecked = null;
		if (obj instanceof Boolean) {
			isChecked = (Boolean) obj;
		}
		
		if (isChecked != null) {
			usrService.pushMessageSetting(isChecked.booleanValue());
			
			if(user != null) {
				user.setMessagePushSettingOn(isChecked);
				registerBean("user", user);
			}
		}
		return null;
	}
	
	/**给短信放大镜打分*/
	@Command
	String playScoreForApp(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			AppUtils.getService(IGenericContentService.class).showMarket(AppUtils.getFramework().getAndroidApplication().getPackageName());			
		}
		return null;
	}
	
	/**新手指引*/
	@Command(navigations={@Navigation(on="*", showPage="guidePage")})
	String playerInstruction(InputEvent event) {
		return "*";
	}
	
	/**联系我们*/
	@Command(navigations={@Navigation(on="*", showPage="constructUsPage")})
	String constructUs(InputEvent event) {
		return "";
	}
	
	/**版本*/
	@Command(navigations={@Navigation(on = "*", showDialog = "noVerUpdateDialog", keepMenuOpen = true),
			@Navigation(on = "+", showDialog = "updateVertionDialog", keepMenuOpen = true)})
	String handleVertionClick(InputEvent event) {
		
		String curVertion = AppUtils.getFramework().getApplicationVersion();
		if (vertionInfoBean == null) {
			return "";
		}
		
		String remoteVertion = vertionInfoBean.getVersion();
		
		boolean isLastest = curVertion.compareTo(remoteVertion) >= 0 ? true
				: false;
		
		if (isLastest) {
			return "";
		} else {
			updateSelection(new VertionUpdateSelection(
					vertionInfoBean.getUrl()));
			return "+";
		}
		
	}
	
	@Command(navigations={@Navigation(on="*", showPage="userNickSet")})
	String toSetNickNamePage(InputEvent event) {
		return "";
	}
	
}
