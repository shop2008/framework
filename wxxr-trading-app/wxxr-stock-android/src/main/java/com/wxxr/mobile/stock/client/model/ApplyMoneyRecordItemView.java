package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name = "applyMoneyRecordsItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.apply_money_record_item_layout")
public abstract class ApplyMoneyRecordItemView extends ViewBase implements IModelUpdater {

	/**申请日期*/
	@Field(valueKey="text")
	String applyDate;
	
	/**申请时间*/
	@Field(valueKey="text")
	String applyTime;
	
	/**申请数量*/
	@Field(valueKey="text")
	String applyAmount;
	
	/**审请状态*/
	@Field(valueKey="text")
	String applyStatus;

	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
