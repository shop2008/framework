package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserPicBean;

@View(name="imageItemForHomeBack")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_home_set_item_layout")
public abstract class UserHomeBackSetItemView extends ViewBase implements IModelUpdater{



	@Field(valueKey="imageURI")
	String userHomeBack;
	
	@Field(valueKey="visible")
	boolean picChecked;
	
	DataField<String> userHomeBackField;
	DataField<Boolean> picCheckedField;
	
	

	@Override
	public void updateModel(Object value) {
		if (value instanceof UserPicBean) {
			UserPicBean userPic = (UserPicBean) value;
			this.userHomeBack = userPic.getImageURI();
			this.userHomeBackField.setValue(userPic.getImageURI());
			this.picChecked = userPic.getIsPicChecked();
			this.picCheckedField.setValue(userPic.getIsPicChecked());
		}
	}

}
