package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 用户账户
 * @author renwenjie
 *
 */
@View(name="userAccountPage", withToolbar=true, description="我的账户")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_account_page_layout")
public abstract class UserAccountPage extends PageBase {
	
	@Field(valueKey = "options", binding = "${gainPayDetailListBean!=null?gainPayDetailListBean.getData():null}")
	List<GainPayDetailBean> accountDetailDetails;
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type = BindingType.Pojo, express = "${usrService.getGPDetails(0,20)}", effectingFields="accountDetailDetails")
	BindableListWrapper<GainPayDetailBean> gainPayDetailListBean;
	private ELBeanValueEvaluator<BindableListWrapper> gainPayDetailListBeanUpdater;
	
	@Field(valueKey = "text",attributes= {
			@Attribute(name="noMoreDataAlert", value="${noMoreDataAlert}"),
			@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${gainPayDetailListBean!=null&&gainPayDetailListBean.data!=null&&gainPayDetailListBean.data.size()>0?true:false}")})
	String refreshView;
	
	@Bean
	boolean noMoreDataAlert = false;
	@Menu(items = { "left" })
	private IMenu toolbar;

	private int oldDataSize = 0;

	private int newDataSize = 0;
	
	@OnHide
	void onHideOption() {
		noMoreDataAlert = false;
		registerBean("noMoreDataAlert", noMoreDataAlert);
	}
	
	@OnUIDestroy
	void onUIDestroyOption() {
		
		gainPayDetailListBean = null;
		registerBean("gainPayDetailListBean", gainPayDetailListBean);
	}
	
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String handleRefresh(ExecutionStep step, InputEvent event, Object result) {
		
		if(event.getEventType().equals("TopRefresh")) {
			//下拉刷新 
			if (usrService != null) {
				usrService.getGPDetails(0, gainPayDetailListBean.getData()
						.size());
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			//上拉加载更多
			if (gainPayDetailListBean != null) {
				noMoreDataAlert = false;
		    	registerBean("noMoreDataAlert", noMoreDataAlert);
				switch (step) {
				case PROCESS:
					if(gainPayDetailListBean.getData() != null && gainPayDetailListBean.getData().size()>0)
						oldDataSize = gainPayDetailListBean.getData().size();
					
					gainPayDetailListBean.loadMoreData();
					break;
				case NAVIGATION:
					if(gainPayDetailListBean.getData() != null && gainPayDetailListBean.getData().size()>0) {
						newDataSize = gainPayDetailListBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						noMoreDataAlert = true;
						registerBean("noMoreDataAlert", noMoreDataAlert);
					} else {
						return null;
					}
					break;
				default:
					break;
				}
			}
		}
		return null;
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		if(usrService != null) {
//			usrService.getGPDetails(0, 20);
		}
		gainPayDetailListBeanUpdater.doEvaluate();
		return null;
	}
}
