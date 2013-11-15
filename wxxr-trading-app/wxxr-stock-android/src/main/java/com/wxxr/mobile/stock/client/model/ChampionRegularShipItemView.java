package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;

@View(name = "ChampionRegularShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_regular_ship_page_layout_item")
public abstract class ChampionRegularShipItemView extends ViewBase implements
		IModelUpdater {

	@Field(valueKey = "text")
	String rankSeq;
	DataField<String> rankSeqField;
	
	@Field(valueKey = "text")
	String nickName;
	DataField<String> nickNameField;

	@Field(valueKey = "text")
	String gainCount;
	DataField<String> gainCountField;

	@Field(valueKey = "text")
	String regularCount;
	DataField<String> regularCountField;

	@Override
	public void updateModel(Object value) {
		if (value instanceof RegularTicketBean) {
			RegularTicketBean msgRank = (RegularTicketBean) value;
			this.rankSeq = String.valueOf(msgRank.getRankSeq());
			this.rankSeqField.setValue(this.rankSeq);

			this.nickName = msgRank.getNickName();
			this.nickNameField.setValue(this.nickName);

			this.gainCount = msgRank.getGainCount()+"个正收益";
			this.gainCountField.setValue(this.gainCount);

			this.regularCount = msgRank.getRegular()+"";
			this.regularCountField.setValue(this.regularCount);
		}
	}
}
