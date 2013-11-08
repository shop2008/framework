package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="appSetPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.setting_page_layout")
public abstract class AppSetPage extends PageBase {

	@Field(valueKey="text")
	String pushEnabled;
	
	
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
		
		}
		return null;
	}

	
	/**
	 * 联系我们
	 * @param event
	 * @return
	 */
	@Command(commandName = "contractUs", description = "Back To Last UI")
	String contractUs(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 联系我们
		}
		return null;
	}

	/**
	 * 新手指引
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "useInstruction", description = "Back To Last UI")
	String useInstruction(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 新手指引
		}
		return null;
	}
	
	/**
	 * 给软件打分
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "playScore", description = "Back To Last UI")
	String playScore(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 新手指引
		}
		return null;
	}
	
	/**
	 * 是否推送消息
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "setPushMsgEnabled", description = "Back To Last UI")
	String setPushMsgEnabled(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 是否推送消息
		}
		return null;
	}
}
