/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * @author neillin
 *
 */
@View(name="headerMenuItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_right_navi_content")
public abstract class HeaderMenuItemView extends ViewBase {
	private static Trace log;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;

	@Bean(type=BindingType.Pojo, express="${usrMgr.myUserInfo}")
	UserBean userInfo;
	
	@Bean(type=BindingType.Pojo, express="${usrMgr.voucherBean}")
	VoucherBean voucherBean;
	
	@Bean(type=BindingType.Pojo, express="${usrMgr.userAssetBean}")
	UserAssetBean assetBean;
	@Field(valueKey="visible", binding="${userInfo != null ? true : false}")
	boolean userRegistered;
	
	@Field(valueKey="imageURI", binding="${userInfo != null ? userInfo.userPic : 'resourceId:drawable/default_user_icons'}")
	String headIcon;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.nickName : '登录账号'}")
	String nickName;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.phoneNumber : '赶快登录赚实盘积分吧'}")
	String userNum;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.unReadMsg : '0'}")
	String unreadNews;
	
	@Field(valueKey="text", binding="${voucherBean != null ? voucherBean.balance : '0'}")
	String integralBalance;
	
	@Field(valueKey="text", binding="${assetBean != null ? assetBean.balance : '0.00'}", converter="stockL2StrConvertor")
	String accountBalance;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%10.2f"),
					@Parameter(name="formatUnit", value="元"),
					@Parameter(name="multiple", value="100.0f")
			})
	StockLong2StringConvertor stockL2StrConvertor;
	
	@Command(commandName="handleClickImage",
		navigations={
			@Navigation(on="userLoginPage",showPage="userLoginPage"),
			@Navigation(on="userPage",showPage="userPage", params={
					@Parameter(name="phone",value="${userInfo.phoneNumber}")
		})
	})
	String handleClickImage(InputEvent event) {
		log.info("User click on user image !");
		if(this.userInfo != null){
			return "userPage";
		} else {
			return "userLoginPage";
		}
	}
	
	@Command(navigations={
			@Navigation(on="*",showPage="userNewsPage")
		})
	String handleClickUnread(InputEvent event){
		log.info("User click on Unread acticles !");
		return "";
	}
	
	@Command(navigations={
			@Navigation(on="*",showPage="userAccountPage")
		})
	String handleClickBalance(InputEvent event){
		log.info("User click on Account balance !");
		return "";
	}
	
	@Command(navigations={
			@Navigation(on="*",showPage="userAccountPage")
		})
	String handleClickCash(InputEvent event){
		log.info("User click on cash icon !");
		return "";
	}
}
