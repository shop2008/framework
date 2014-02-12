package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;


/**用户签到界面*/
@View(name="UserSignPage",withToolbar=true, description="我的签到")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.sign_in_layout")
public abstract class UserSignPage extends PageBase {

	/**签到可见，不签到透明 */
	@Field(valueKey="imageURI", binding="${signedFlag==true?'resourceId:drawable/icon_signed':'resourceId:drawable/icon_unsigned'}")
	String dotSignOrUnSign;
	
	/**签到奖励积分数量*/
	@Field(valueKey="text")
	String signAwardAmount;
	
	/**点击签到按钮，已签到--不可见，未签到--可见*/
	@Field(valueKey="text")
	String unSigned;
	
	/**已签到文本，已签到---可见，未签到--不可见*/
	@Field(valueKey="visible")
	boolean signed;
	
	/**签到天数*/
	@Field(valueKey="text")
	String signnedDays;
	
	@Bean
	boolean signedFlag = false;
	
	@Menu(items = {"left"})
	IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "左菜单", icon = "resourceId:drawable/list_button_style")})
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	/**点击签到*/
	@Command
	String clickToSign(InputEvent event) {
		return null;
	}
}
