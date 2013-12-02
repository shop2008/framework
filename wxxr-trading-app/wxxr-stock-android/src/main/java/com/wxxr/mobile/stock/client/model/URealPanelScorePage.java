package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="uRealPanelScorePage", withToolbar=true, description="实盘积分明细")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.actual_panel_integral_detail_layout")
public abstract class URealPanelScorePage extends PageBase{

	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	@Bean(type=BindingType.Pojo,express="${userService!=null?userService.myUserScoreInfo:null}")
	ScoreInfoBean scoreInfoBean;
	
	@Field(valueKey="options", binding="${scoreInfoBean!=null?scoreInfoBean.scores:null}",attributes = {@Attribute(name = "enablePullDownRefresh", value="true"),
			  @Attribute(name = "enablePullUpRefresh", value="false")})
	List<ScoreBean> actualScores;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	
	@Field(valueKey="text",binding="${scoreInfoBean!=null?scoreInfoBean.balance:null}")
	String scoreBalance;
	
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command
	String refreshTopData(InputEvent event) {
		return null;
	}
}
