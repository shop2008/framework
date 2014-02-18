package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;


import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


@View(name="guidePage", withToolbar=true, description="新手指引")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.guide_page_layout")
public abstract class GuidePage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	
	@Field(valueKey="options", binding="${imageUris}", attributes={@Attribute(name="position", value="${nextPosition}")})
	List<String> guideImages;
	
	
	int selectPos;
	
	
	@Field(valueKey="text", binding="${10}")
	String totalPage;
	
	@Field(valueKey="text", binding="${currentPosition+1}")
	String currentPage;
	
	
	
	@Bean
	int nextPosition = 0;
	
	@Bean
	int currentPosition = 0;
	@Field(valueKey="text", visibleWhen="${currentPosition<9}")
	String bottomRightBtn;
	
	@Field(valueKey="text", visibleWhen="${currentPosition>0}")
	String bottomLeftBtn;
	
	@Field(valueKey="text", enableWhen="${currentPosition==9}")
	String centerBtn;
	
	@OnShow
	protected void initGuideImages() {
		List<String> guideImages = new ArrayList<String>();
		for(int i=0;i<10;i++) {
			guideImages.add("resourceId:drawable/guide_"+(i+1));
		}
		registerBean("imageUris", guideImages);
	}
	


	@Command
	String selected(InputEvent event) {
		
		Integer position = (Integer)event.getProperty("selectPos");
		
		if (position != null) {
			selectPos = position;
			registerBean("currentPosition", selectPos);
		}
		return null;
	}
	
	@Command
	String bottomLeftClick(InputEvent event) {
		
		registerBean("nextPosition", selectPos -1);
		return null;
	}
	
	@Command
	String bottomRightClick(InputEvent event) {
		
		registerBean("nextPosition", selectPos+1);
		return null;
	}
	
	@Command(navigations={
			@Navigation(on = "+",showDialog="UnLoginDialogView"),
			@Navigation(on = "-",showDialog="GainedAwardDialogView"), 
			@Navigation(on = "OK",showDialog="GainAwardSucDialogView"),
			@Navigation(on="StockAppBizException", message = "%m%n", params = {
					@Parameter(name = "autoClosed", type =

					ValueType.INETGER, value = "2"),
					@Parameter(name = "title", value = "错误") })
			})
	String centerBtnClick(InputEvent event) {
		
		if(!AppUtils.getService(IUserIdentityManager.class).isUserAuthenticated()) {
			return "+";
		}
		
		boolean guideGainned = true;
		if(user != null) {
			guideGainned = user.getAllowGuideGain();
			if (!guideGainned) {
				return "-";
			} else {
				usrService.getGuideGain();
			}
		}
		
		hide();
		return "OK";
	}
	
	@OnUIDestroy
	void clearData() {
		selectPos = 0;
		registerBean("nextPosition", 0);
		registerBean("currentPosition", 0);
	}
	
}
