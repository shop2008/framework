package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="uRealPanelScorePage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.actual_panel_integral_detail_layout")
public abstract class URealPanelScorePage extends PageBase implements IModelUpdater{

	@Field(valueKey="options")
	List<ScoreBean> actualScores;
	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	@Bean(type=BindingType.Pojo)
	ScoreInfoBean scoreInfoBean;
	
	@Field
	String userId;
	
	@Field(valueKey="text")
	String scoreBalance;
	
	DataField<String> scoreBalanceField;
	DataField<List> actualScoresField;
	
	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	@OnCreate
	void injectServices() {
		this.userService = AppUtils.getService(IUserManagementService.class);
		
	}

	@OnShow
	protected void initData() {
		userService = getUIContext().getKernelContext().getService(IUserManagementService.class);
		if(userId != null && !userId.equals(""))
			scoreInfoBean = userService.fetchUserScoreInfo(userId);
		
	}
	
	@OnDataChanged
	protected void dataChanged(ValueChangedEvent event) {
		if (scoreInfoBean != null) {
			String balance = scoreInfoBean.getBalance();
			this.scoreBalance = balance;
			this.scoreBalanceField.setValue(balance);
			List<ScoreBean> beans = scoreInfoBean.getScores();
			this.actualScores = beans;
			this.actualScoresField.setValue(beans);
		}
	}
	
	@Override
	public void updateModel(Object value) {
		Map<String, Object> map = (Map<String, Object>) value;
		userId = (String)map.get("userId");
	}
}
