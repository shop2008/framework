package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

import com.wxxr.mobile.stock.client.utils.Constants;

@View(name = "otherUserPage", withToolbar=true, description="---的个人主页",provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.other_user_page_layout")
public abstract class OtherUserPage extends PageBase implements IModelUpdater {

	
	@OnShow
	void initData() {
		getAppToolbar().setTitle(userName+"的主页", null);
	}
	
	
	/*@Bean(type=BindingType.Service)
	IMockDataService service;
	
	
	@Field(valueKey="options", binding="${service!=null?service.data:null}")
	List<PersonalHomeBean> successTradeRecords;*/


	private String userId;


	private String userName;
	
	
	@Command
	String handleTradeRecordItemClick(InputEvent event) {
		
		int position = (Integer) event.getProperty("position");
		System.out.println("+++position+++"+position);
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && Constants.KEY_USER_ID_FLAG.equals(key)) {
					if (tempt instanceof String) {
						userId = (String) tempt;
					}
					registerBean("userId", userId);
				}
				
				if (tempt != null && Constants.KEY_USER_NAME_FLAG.equals(key)) {
					if (tempt instanceof String) {
						userName = (String) tempt;
						registerBean("userName", userName);
					}
				}
			}
		}
	}
}
