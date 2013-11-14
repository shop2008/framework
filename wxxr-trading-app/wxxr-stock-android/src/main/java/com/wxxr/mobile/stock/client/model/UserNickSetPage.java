package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="userNickSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_nick_set_layout")
public abstract class UserNickSetPage extends PageBase {
	
	@Field(valueKey="text")
	String newNickName;
	
	DataField<String> newNickNameField;
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 返回上一层
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	/**
	 * 更改昵称业务
	 * @param event
	 * @return
	 */
	String done(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 更改昵称
			
		}
		return null;
	}
	
	/**
	 * 当文本框输入的内容不为空的时候，会调用此函数
	 * @param event InputEvent.EVENT_TYPE_TEXT_CHANGED
	 * @return null
	 */
	@Command(commandName="newNickChanged")
	String newNickChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String changedNick = (String)event.getProperty("changedText");
			this.newNickName = changedNick;
			this.newNickNameField.setValue(changedNick);
		}
		return null;
	}
}
