package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;

@View(name = "ChampionRegularShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_regular_ship_page_layout_item")
public abstract class ChampionRegularShipItemView extends ViewBase implements
		IModelUpdater {

	@Bean
	RegularTicketBean regularTicket;
	
	@Field(valueKey = "text", binding="${regularTicket!=null?regularTicket.rankSeq:'--'}")
	String rankSeq;
	
	@Field(valueKey = "text", binding="${regularTicket!=null?regularTicket.nickName:'--'}")
	String nickName;

	@Field(valueKey = "text", binding="${regularTicket!=null?regularTicket.gainCount:'--'}${'个正收益'}")
	String gainCount;

	@Field(valueKey = "text", binding="${regularTicket!=null?regularTicket.regular:'--'}")
	String regularCount;

	@Override
	public void updateModel(Object value) {
		if (value instanceof RegularTicketBean) {
			registerBean("regularTicket",value);
		}
	}
}
