package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;

@View(name="masterRankItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.master_rank_item_layout")
public abstract class MasterRankItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text")
	String rankNum;
	
	
	@Field(valueKey="text")
	String nickName;
	
	@Field(valueKey="text")
	String profitNum;
	
	@Field(valueKey="text")
	String totalProfitAmount;
	
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainBean) {
			registerBean("accountBean", value);
		}
	}
}
