package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name="helpCenterItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_center_page_layout_item")
public abstract class HelpCenterItemView extends ViewBase  implements IModelUpdater  {

	@Field(valueKey="text")
	String title;
	@Field(valueKey="text")
	String description;
	
	DataField<String> titleField;
	DataField<String> descriptionField;
	
	@Override
	public void updateModel(Object value) {
		
	}


}
