package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
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
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name="userScorePage", withToolbar=true, description="我的积分")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_score_layout")
public abstract class UserScorePage extends PageBase {

	private static final Trace log = Trace.register(UserScorePage.class);
	@Bean(type=BindingType.Service)
	ITradingManagementService  tradingService;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getGainPayDetailDetails(0,20)}", effectingFields="realScoreDetails")
	BindableListWrapper<GainPayDetailBean> voucherDetailsBean;
	private ELBeanValueEvaluator<BindableListWrapper> voucherDetailsBeanUpdater;
	
	@Field(valueKey="options", binding="${voucherDetailsBean!=null?voucherDetailsBean.getData():null}")
	List<GainPayDetailBean> realScoreDetails;

	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${voucherDetailsBean!=null&&voucherDetailsBean.data!=null&&voucherDetailsBean.data.size()>0?true:false}"),
			@Attribute(name="noMoreDataAlert", value="${noMoreDataAlert}")})
	String refreshView;
	
	@Menu(items = { "left" })
	protected IMenu toolbar;
	
	@Bean
	boolean noMoreDataAlert = false;
	
	int loadSize = 0;
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	int newDataSize = 0;
	List<GainPayDetailBean> newLoadData = null;
	int oldDataSize = 0;
	
	@OnHide
	void onHideOption() {
		noMoreDataAlert = false;
		registerBean("noMoreDataAlert", noMoreDataAlert);
	}
	
	@OnUIDestroy
	void onUIDestryOption() {
		voucherDetailsBean = null;
		registerBean("voucherDetailsBean", voucherDetailsBean);
	}
	
	@Command
	String handleRefresh(ExecutionStep step, InputEvent event, Object result) {
		
		if(event.getEventType().equals("TopRefresh")) {
			if(tradingService != null) {
				tradingService.getGainPayDetailDetails(0, voucherDetailsBean.getData().size());
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			noMoreDataAlert = false;
	    	registerBean("noMoreDataAlert", noMoreDataAlert);
			if(voucherDetailsBean != null) {
				switch (step) {
				case PROCESS:
					if(voucherDetailsBean.getData() != null && voucherDetailsBean.getData().size()>0)
						oldDataSize = voucherDetailsBean.getData().size();
					
					voucherDetailsBean.loadMoreData();
					break;
				case NAVIGATION:
					if(voucherDetailsBean.getData() != null && voucherDetailsBean.getData().size()>0) {
						newDataSize = voucherDetailsBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						noMoreDataAlert = true;
						registerBean("noMoreDataAlert", noMoreDataAlert);
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
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		if(tradingService != null) {
			//int completeSize = voucherDetailsBean.getData().size();
//			tradingService.getGainPayDetailDetails(0, 20);
		}
		voucherDetailsBeanUpdater.doEvaluate();
		return null;
	}
}
