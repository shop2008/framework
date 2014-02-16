package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="InfoNoticesPage",withToolbar=true,description="咨询公告")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.info_notices_layout")
public abstract class InfoNoticesPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type = BindingType.Pojo, express = "${usrService.getPullMessageBean(0,20)}")
	BindableListWrapper<PullMessageBean> infoNoticeListBean;
	
	@Menu(items = {"left"})
	protected IMenu toolbar;
	
	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${false}")})
	String refreshView;
	
	@Field(valueKey="options",binding="${infoNoticeListBean!=null?infoNoticeListBean.data:null}")
	List<PullMessageBean> infoNoticesInfos;
	
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	/**处理消息点击事件*/
	@Command(navigations = { @Navigation(on = "*", showPage = "webPage") })
	CommandResult handleItemClick(InputEvent event) {
		
		int position = (Integer) event.getProperty("position");
		PullMessageBean message = null;
		if (infoNoticeListBean != null) {
			List<PullMessageBean> infoNoticesList = infoNoticeListBean
					.getData();
			message = infoNoticesList.get(position);

			if (usrService != null && message != null) {
				usrService.readPullMesage(message.getId());
			}
			CommandResult result = new CommandResult();
			result.setPayload(message.getArticleUrl());
			result.setResult("*");
			return result;
		}
		return null;
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		
		if (event.getEventType().equals("TopRefresh")) {
			if (usrService != null) {
				usrService.getPullMessageBean(0, infoNoticeListBean.getData()
						.size(), true);
			}
		} else if (event.getEventType().equals("BottomRefresh")) {
			int completeLoadSize = 0;
			if (infoNoticeListBean != null)
				completeLoadSize += infoNoticeListBean.getData().size();
			if (usrService != null) {
				usrService.getPullMessageBean(completeLoadSize, 20, true);
			}
		}
		return null;
	}
}
