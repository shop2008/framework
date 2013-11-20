package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.MessageInfoBean;
import com.wxxr.mobile.stock.app.bean.MessageInfoListBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userNewsPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_news_center_page_layout")
public abstract class UserNewsPage extends PageBase {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo, express="${usrService.myMessageInfo}")
	MessageInfoListBean messageList;
	
	@Field(valueKey="options", binding="${messageList!=null?messageList.messageInfos:null}")
	List<MessageInfoBean> accountTrades;

	@Command
	String showInfoNotices(InputEvent event) {
		
		return null;
	}
	
	@Command
	String showAccountTrades(InputEvent event) {
		
		return null;
	}
}
