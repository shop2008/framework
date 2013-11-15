package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ScoreBean;
import com.wxxr.mobile.stock.client.utils.ColorUtils;

@View(name="actualScoreItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_panel_integral_detail_item_layout")
public abstract class URealPanelScoreItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text")
	String scoreCatagory;
	
	@Field(valueKey="text")
	String gainDate;
	
	@Field(valueKey="text")
	String gainNum;
	
	DataField<String> scoreCatagoryField;
	DataField<String> gainDateField;
	DataField<String> gainNumField;
	
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof ScoreBean) {
			ScoreBean score = (ScoreBean) value;
			
			this.scoreCatagory = score.getCatagory();
			this.scoreCatagoryField.setValue(score.getCatagory());
			
			this.gainDate = score.getDate();
			this.gainDateField.setValue(score.getDate());
			
			
			
			this.gainNum = score.getAmount();
			if (Integer.parseInt(score.getAmount()) > 0) {
				this.gainNumField.setValue("+"+score.getAmount());
				this.gainNumField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_RED);
			} else {
				this.gainNumField.setValue(score.getAmount());
				this.gainNumField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GREEN);
			}
		}
	}
}
