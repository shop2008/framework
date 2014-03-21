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
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="InfoNoticesPage",withToolbar=true,description="操盘资讯")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.info_notices_layout")
public abstract class InfoNoticesPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type = BindingType.Pojo, express = "${usrService.getPullMessageBean(0,20)}",effectingFields="infoNoticesInfos")
	BindableListWrapper<PullMessageBean> infoNoticeListBean;
	
	private ELBeanValueEvaluator<BindableListWrapper> infoNoticeListBeanUpdater;
	@Menu(items = {"left"})
	protected IMenu toolbar;
	
	@Field(valueKey = "text",attributes= {
			@Attribute(name="noMoreDataAlert", value="${infosNoMoreAlert}"),
			@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${infoNoticeListBean!=null&&infoNoticeListBean.data!=null&&infoNoticeListBean.data.size()>0?true:false}")})
	String refreshView;
	
	@Bean
	boolean infosNoMoreAlert = false;
	
	@Field(valueKey="options",binding="${infoNoticeListBean!=null?infoNoticeListBean.getData():null}")
	List<PullMessageBean> infoNoticesInfos;
	
	//DataField<List> infoNoticesInfosField;
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style",visibleWhen="${true}")})
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@OnHide
	void onUIDestroyOption() {
		infosNoMoreAlert = false;
		registerBean("infosNoMoreAlert", infosNoMoreAlert);
	}
	
	PullMessageBean message = null;

	private int oldDataSize;

	private int newDataSize;
	/**处理消息点击事件*/
	@Command(navigations = { @Navigation(on = "*", showPage = "webPage") })
	CommandResult handleItemClick(ExecutionStep step, InputEvent event, Object result) {
		
		switch (step) {
		case PROCESS:
			int position = (Integer) event.getProperty("position");
			
			
			if (infoNoticeListBean != null) {
				List<PullMessageBean> infoNoticesList = infoNoticeListBean
						.getData();
				message = infoNoticesList.get(position);

				if (usrService != null && message != null) {
					Long messageId = message.getId();
					if(messageId != null) {
						usrService.readPullMesage(messageId.longValue());
					}
				}
			}
			break;
		case NAVIGATION:
			CommandResult commResult = new CommandResult();
			commResult.setPayload(message.getArticleUrl());
			commResult.setResult("*");
			return commResult;

		default:
			break;
		}
		
		return null;
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		
		if (event.getEventType().equals("TopRefresh")) {
			if (infoNoticeListBean != null) {
				infoNoticeListBean.getData(true);
			}
		} else if (event.getEventType().equals("BottomRefresh")) {
			if (infoNoticeListBean != null) {
				infoNoticeListBean.loadMoreData();
			}
		}
		return null;
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		infoNoticeListBeanUpdater.doEvaluate();
		return null;
	}
	
	@Command
	String handleRefresh(ExecutionStep step, InputEvent event, Object result) {
		if(event.getEventType().equals("TopRefresh")) {
			if(usrService != null) {
				usrService.getPullMessageBean(0, infoNoticeListBean!=null&&infoNoticeListBean.getData()!=null?infoNoticeListBean.getData().size():20);
			}
		} else if(event.getEventType().equals("BottomRefresh"))  {
			infosNoMoreAlert = false;
			registerBean("infosNoMoreAlert", infosNoMoreAlert);
			
			if(infoNoticeListBean != null) {


				switch (step) {
				case PROCESS:
					if(infoNoticeListBean.getData() != null && infoNoticeListBean.getData().size()>0)
						oldDataSize = infoNoticeListBean.getData().size();
					
					infoNoticeListBean.loadMoreData();
					break;
				case NAVIGATION:
					if(infoNoticeListBean.getData() != null && infoNoticeListBean.getData().size()>0) {
						newDataSize = infoNoticeListBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						infosNoMoreAlert = true;
						registerBean("infosNoMoreAlert", infosNoMoreAlert);
					} else {
						return null;
					}
				default:
					break;
				}
			}
		}
		return null;
	}
}
