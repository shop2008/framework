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
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

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
	
	@Field(valueKey="visible", binding="${userInfo != null ? true : false}")
	boolean userRegistered;
	
	@Field(valueKey="imageURI", binding="${userInfo != null ? userInfo.userPic : 'resourceId:drawable/default_user_icons'}")
	String headIcon;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.nickName : '登录账号'}")
	String nickName;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.phoneNumber : '赶快登录赚实盘积分吧'}")
	String userNum;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.unReadMsg : 0}")
	String unreadNews;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.score : 0}")
	String integralBalance;
	
	@Field(valueKey="text", binding="${userInfo != null ? userInfo.balance : '0.00'}")
	String accountBalance;
	
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
	
	@Command(commandName="handleClickImage",
			navigations={
			@Navigation(on="*",showPage="userLoginPage")
		})
	String handleClickUnread(InputEvent event){
		log.info("User click on Unread acticles !");
		return "";
	}
	
	@Command(commandName="handleClickImage",
			navigations={
			@Navigation(on="*",showPage="userLoginPage")
		})
	String handleClickBalance(InputEvent event){
		log.info("User click on Account balance !");
		return "";
	}
	
	@Command(commandName="handleClickImage",
			navigations={
			@Navigation(on="*",showPage="userLoginPage")
		})
	String handleClickCash(InputEvent event){
		log.info("User click on cash icon !");
		return "";
	}
}
