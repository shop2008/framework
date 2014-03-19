package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="Copy2ClipBoardDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.copy_to_clipboard_layout")
public abstract class Copy2ClipBoardDialogView extends ViewBase implements IModelUpdater{

	@Field(valueKey="text")
	String message;
	DataField<String> messageField;
	
	@Command
	String done(InputEvent event) {
		hide();
		//AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().hide();
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "message".equals(key)) {
					if (tempt instanceof String) {
						messageField.setValue((String)tempt);
					}
				}
			}
		}
	}
}
