package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;

@View(name = "ChampionShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_ship_page_layout_item")
public abstract class ChampionShipItemView extends ViewBase implements
		IModelUpdater {
	
	@Bean
	MegagameRankBean msgRank;
	
	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.rankSeq:'--'}")
	String rankSeq;
	
	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.maxStockCode:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String maxStockCode;

	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.nickName:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String nickName;

	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.gainRate:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.status==0?'resourceId:color/gray':'resourceId:color/red'}")
			})
	String gainRate;

	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.maxStockMarket:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String maxStockMarket;

	@Override
	public void updateModel(Object value) {
		if (value instanceof MegagameRankBean) {
			registerBean("msgRank",value);
		}
	}
}
