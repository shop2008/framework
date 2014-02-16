package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserSignBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


/**用户签到界面*/
@View(name="UserSignPage",withToolbar=true, description="我的签到")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.sign_in_layout")
public abstract class UserSignPage extends PageBase {

	
	/**签到奖励积分数量*/
	@Field(valueKey="text", binding="${userSignBean!=null?userSignBean.rewardVol:10}")
	String signAwardAmount;
	
	/**未签到奖励积分数量*/
	@Field(valueKey="text", binding="${userSignBean!=null?userSignBean.rewardVol:10}")
	String unSignAwardAmount;
	/**点击签到按钮，已签到--不可见，未签到--可见*/
	@Field(valueKey="visible", binding="${userSignBean!=null&&userSignBean.sign==false?true:false}")
	boolean unSigned;
	
	/**已签到文本，已签到---可见，未签到--不可见*/
	@Field(valueKey="visible", binding="${userSignBean!=null&&userSignBean.sign==true?true:false}")
	boolean signed;
	
	/**签到天数*/
	@Field(valueKey="text", binding="${userSignBean!=null?userSignBean.ongoingDays:0}")
	String signnedDays;
	
	@Field(valueKey="visible", binding="${userSignBean!=null&&userSignBean.sign==false?true:false}")
	boolean unsignBody;
	
	@Field(valueKey="visible", binding="${userSignBean!=null&&userSignBean.sign==true?true:false}")
	boolean signedBody;
	
	@Bean
	boolean signedFlag = false;
	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	@Bean(type=BindingType.Pojo, express="${userService!=null?userService.getUserSignBean():null}")
	UserSignBean userSignBean;
	
	@Menu(items = {"left"})
	IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "左菜单", icon = "resourceId:drawable/list_button_style")})
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	/**点击签到*/
	@Command(navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误")
	}) }
	)
	String clickToSign(InputEvent event) {
		if(userService != null) {
			userSignBean = userService.sign();
			registerBean("userSignBean", userSignBean);
		}
		return null;
	}
}
