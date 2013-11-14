package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
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
	
	@Bean
	MegagameRankBean msgRank;
	
	@Field(valueKey = "text")
	String maxStockCode;
	DataField<String> maxStockCodeField;

	@Field(valueKey = "text")
	String nickName;
	DataField<String> nickNameField;

	@Field(valueKey = "text")
	String gainRate;
	DataField<String> gainRateField;

	@Field(valueKey = "text")
	String rankSeq;
	DataField<String> rankSeqField;

	@Field(valueKey = "text")
	String maxStockMarket;
	DataField<String> maxStockMarketField;

	@Override
	public void updateModel(Object value) {
		if (value instanceof MegagameRankBean) {
//			MegagameRankBean msgRank = (MegagameRankBean) value;
			registerBean("msgRank",value);
			
//			this.rankSeq = String.valueOf(msgRank.getRankSeq());
//			this.rankSeqField.setValue(this.rankSeq);
//
//			this.nickName = msgRank.getNickName();
//			this.nickNameField.setValue(this.nickName);
//
//			this.maxStockCode = msgRank.getMaxStockCode();
//			this.maxStockCodeField.setValue(this.maxStockCode);
//
//			this.maxStockMarket = msgRank.getMaxStockMarket();
//			this.maxStockMarketField.setValue(this.maxStockMarket);
//
//			this.gainRate = msgRank.getGainRate();
//			this.gainRateField.setValue(this.gainRate);
		}
	}
}
