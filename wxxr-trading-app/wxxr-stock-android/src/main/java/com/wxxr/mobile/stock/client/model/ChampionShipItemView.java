package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.MegagameRankBean;

@View(name = "ChampionShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_ship_page_layout_item")
public abstract class ChampionShipItemView extends ViewBase implements
		IModelUpdater {
	
	MegagameRankBean msgRank;
	
	@Field(valueKey = "text", binding="${msgRank!=null?msgRank.maxStockCode:'--'}")
	String maxStockCode;
	DataField<String> maxStockCodeField;

	@Field(valueKey = "text", binding="${msgRank!=null?msgRank.nickName:'--'}")
	String nickName;
	DataField<String> nickNameField;

	@Field(valueKey = "text", binding="${msgRank!=null?msgRank.gainRate:'--'}")
	String gainRate;
	DataField<String> gainRateField;

	@Field(valueKey = "text", binding="${msgRank!=null?msgRank.rankSeq:'--'}")
	String rankSeq;
	DataField<String> rankSeqField;

	@Field(valueKey = "text", binding="${msgRank!=null?msgRank.maxStockMarket:'--'}")
	String maxStockMarket;
	DataField<String> maxStockMarketField;

	@Override
	public void updateModel(Object value) {
		if (value instanceof MegagameRankBean) {
			registerBean("msgRank",value);
		}
	}
}
