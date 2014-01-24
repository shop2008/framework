package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;

@View(name = "ApplyMoneyRecordPage" ,withToolbar=true, description="提现记录")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.apply_money_record_layout")
public abstract class ApplyMoneyRecordPage extends PageBase {

	
	@Field(valueKey="text")
	String refreshView;
	
	

	@Field(valueKey="options")
	List<GainPayDetailBean> applyMoneyRecordsItemView;
	
	@Command
	String handleRefresh(InputEvent event) {
		
		if(event.getEventType().equals("TopRefresh")) {
			//下拉刷新 
		} else if(event.getEventType().equals("BottomRefresh")) {
			//上拉加载更多
		}
		return null;
	}
}
