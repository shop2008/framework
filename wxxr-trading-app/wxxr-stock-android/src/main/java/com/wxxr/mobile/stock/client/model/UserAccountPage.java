package com.wxxr.mobile.stock.client.model;




import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;

import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 用户账户
 * @author renwenjie
 *
 */
@View(name="userAccountPage", withToolbar=true, description="我的帐户")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_account_page_layout")
public abstract class UserAccountPage extends PageBase {
	
	@Field(valueKey = "options", upateAsync=true,binding = "${gainPayDetailListBean!=null?gainPayDetailListBean.getData(true):null}")
	List<GainPayDetailBean> accountDetailDetails;
	
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type = BindingType.Pojo, express = "${usrService.getGPDetails(0,20)}")
	BindableListWrapper<GainPayDetailBean> gainPayDetailListBean;
	
	
	
	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${gainPayDetailListBean!=null&&gainPayDetailListBean.data!=null&&gainPayDetailListBean.data.size()>0?true:false}")})
	String refreshView;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		
		if(event.getEventType().equals("TopRefresh")) {
			//下拉刷新 
			if (usrService != null) {
				usrService.getGPDetails(0, gainPayDetailListBean.getData()
						.size());
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			//上拉加载更多
			if (usrService != null) {
				usrService.getGPDetails(gainPayDetailListBean.getData().size(), 20);
			}
		}
		return null;
	}
}
