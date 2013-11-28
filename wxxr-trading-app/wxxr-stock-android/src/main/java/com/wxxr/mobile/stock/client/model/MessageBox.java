/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.R;

/**
 * @author neillin
 *
 */
@View(name="progressMonitor",alias="messageBox")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.message_box_layout")
public abstract class MessageBox extends ViewBase implements IModelUpdater{
	@Field(valueKey="text")
	String title;
	
	DataField<String> titleField;
	
	@Field(valueKey="text")
	String message;
	
	DataField<String> messageField;
	
	@Field
	int progress;
	
	DataField<Integer> progressField;
	
	@Field
	String icon;
	
	DataField<String> iconField;
	
	IUICommand left,right,middle;
	
	@Command(uiItems={
			@UIItem(label="resourceId:message/dummy_ok_label",id="left_button",icon="",visibleWhen="false"),
			@UIItem(label="resourceId:message/dummy_middle_label",id="mid_button",icon="",visibleWhen="false"),
			@UIItem(label="resourceId:message/dummy_canel_label",id="right_button",icon="",visibleWhen="false")
	})
	String dummyClick(InputEvent evt){
		hide();
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IModelUpdater#updateModel(java.lang.Object)
	 */
	@Override
	public void updateModel(Object data) {
		if(data instanceof Map){
			Map<String, Object> map = (Map<String, Object>)data;
			Object val = map.get(IDialog.DIALOG_ATTRIBUTE_TITLE);
			if(val instanceof String){
				this.titleField.setValue((String)val);
			}
			val = map.get(IDialog.DIALOG_ATTRIBUTE_MESSAGE);
			if(val instanceof String){
				this.messageField.setValue((String)val);
			}
			val = map.get(IDialog.DIALOG_ATTRIBUTE_ICON);
			if(val instanceof String){
				this.iconField.setValue((String)val);
			}
			val = map.get(IDialog.DIALOG_ATTRIBUTE_LEFT_BUTTON);
			if(val instanceof IUICommand){
				this.left = (IUICommand)val;
			}else if(map.containsKey(IDialog.DIALOG_ATTRIBUTE_LEFT_BUTTON)){
				super.getChild("left_button").setAttribute(AttributeKeys.visible, true);
			}else{
				super.getChild("left_button").setAttribute(AttributeKeys.visible, false);
			}
			val = map.get(IDialog.DIALOG_ATTRIBUTE_MID_BUTTON);
			if(val instanceof IUICommand){
				this.middle = (IUICommand)val;
			}else if(map.containsKey(IDialog.DIALOG_ATTRIBUTE_MID_BUTTON)){
				super.getChild("mid_button").setAttribute(AttributeKeys.visible, true);
			}else{
				super.getChild("mid_button").setAttribute(AttributeKeys.visible, false);
			}
			val = map.get(IDialog.DIALOG_ATTRIBUTE_RIGHT_BUTTON);
			if(val instanceof IUICommand){
				this.right = (IUICommand)val;
			}else if(map.containsKey(IDialog.DIALOG_ATTRIBUTE_RIGHT_BUTTON)){
				super.getChild("right_button").setAttribute(AttributeKeys.visible, true);
			}else{
				super.getChild("right_button").setAttribute(AttributeKeys.visible, false);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIContainer#getChild(java.lang.String)
	 */
	@Override
	public IUIComponent getChild(String name) {
		if(IDialog.DIALOG_ATTRIBUTE_LEFT_BUTTON.equals(name)&&(left != null)){
			return left;
		}else if(IDialog.DIALOG_ATTRIBUTE_MID_BUTTON.equals(name)&&(middle != null)){
			return middle;
		}else if(IDialog.DIALOG_ATTRIBUTE_RIGHT_BUTTON.equals(name)&&(right != null)){
			return right;
		}
		return super.getChild(name);
	}

}
