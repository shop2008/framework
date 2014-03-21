package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;

@View(name="todayHotRankView")
@AndroidBinding(type=AndroidBindingType.FRAGMENT, layoutId="R.layout.today_hot_rank_layout")
public abstract class TodayHotRankView extends ViewBase {

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 0}"),
			@Attribute(name = "textColor", value = "${curItemId == 0?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean actualRankBtn;
	
	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean virtualRankBtn;

	@Bean
	int curItemId = 0;
	@Field(valueKey="options")
	List<GainBean> actualRankList;
	
	@Field(valueKey="options")
	List<GainBean> virtualRankList;
	
	@Field(valueKey="visible")
	boolean rankListEmpty;
	
	@Command
	String virtualRankItemClick(InputEvent event) {
		
		return null;
	}
	
	@Command
	String actualRankItemClick(InputEvent event) {
		return null;
	}
}
