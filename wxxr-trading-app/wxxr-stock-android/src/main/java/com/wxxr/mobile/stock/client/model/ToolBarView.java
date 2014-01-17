/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.event.api.IBroadcastEvent;
import com.wxxr.mobile.core.event.api.IEventListener;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.event.NewRemindingMessagesEvent;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.ui.StockAppToolbar;
import com.wxxr.mobile.stock.client.utils.Utils;

/**
 * @author neillin
 * 
 */
@View(name="toolbarView",singleton=true)
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.layout_animation_tool_bar_view")
public abstract class ToolBarView extends StockAppToolbar implements IEventListener {


	@Field(valueKey="imageURI")
	String leftIcon;
	
	DataField<String> leftIconField;
	
	@Field(valueKey="imageURI")
	String searchIcon;

	DataField<String> rightIconField;

	@Field(valueKey="imageURI")
	String rightIcon;

	DataField<String> searchIconField;
	
	@Field(valueKey="text")
	String message;
	
	DataField<String> messageField;
	
	@Field(valueKey="text")
	String title;
	DataField<String> titleField;

	@Field(valueKey="text")
	String status;
	
	@Field(valueKey="visible")
	boolean pushMessage;
	
	DataField<Boolean> pushMessageField;
	@Command
	String handleClick(InputEvent event){
		String name = event.getEventSource().getName();
		if("leftIcon".equals(name)){
			name = "left";
		} else if("rightIcon".equals(name)){
			name = "right";
		} else if("searchIcon".equals(name)){
			name = "search";
		}
		IUICommand cmd = getMenuItem(name);
		if(cmd != null){
			cmd.invokeCommand(null, event);
		}
		return null;
	}
	
	@Override
	public void setTitle(String message, Map<String, String> parameters) {
		this.titleField.setValue(message);
	}

	@Override
	public IUIComponent getChild(String name) {
		if("leftIcon".equals(name)){
			IMenu menu = getToolbarMenu();
			if(menu != null){
				IUICommand cmd  = menu.getCommand("left");
				if(cmd != null){
					this.leftIconField.setValue(cmd.getAttribute(AttributeKeys.icon));
					this.leftIconField.setAttribute(AttributeKeys.visible,true);
				}else{
					this.leftIconField.setAttribute(AttributeKeys.visible,false);
					this.leftIconField.setAttribute(AttributeKeys.takeSpaceWhenInvisible,true);
				}
			}
		}else if("rightIcon".equals(name)){
			IMenu menu = getToolbarMenu();
			if(menu != null){
				IUICommand cmd  = menu.getCommand("right");
				if(cmd != null){
					this.rightIconField.setValue(cmd.getAttribute(AttributeKeys.icon));
					this.rightIconField.setAttribute(AttributeKeys.visible,true);
				}else{
					this.rightIconField.setAttribute(AttributeKeys.visible,false);
					this.rightIconField.setAttribute(AttributeKeys.takeSpaceWhenInvisible,true);
				}
			}
		}else if("searchIcon".equals(name)){
			IMenu menu = getToolbarMenu();
			if(menu != null){
				IUICommand cmd  = menu.getCommand("search");
				if(cmd != null){
					this.searchIconField.setValue(cmd.getAttribute(AttributeKeys.icon));
					this.searchIconField.setAttribute(AttributeKeys.visible,true);
				}else{
					this.searchIconField.setAttribute(AttributeKeys.visible,false);
					this.searchIconField.setAttribute(AttributeKeys.takeSpaceWhenInvisible,true);
				}
			}
		}
		return super.getChild(name);
	}

	/*********************消息推送********************/
	@OnShow
	void registerEventListener() {
		AppUtils.getService(IEventRouter.class).registerEventListener(NewRemindingMessagesEvent.class, this);
	}
	
	@Override
	public void onEvent(IBroadcastEvent event) {
		if(!AppUtils.getService(IUserManagementService.class).getPushMessageSetting())
			return;
		NewRemindingMessagesEvent e = (NewRemindingMessagesEvent) event;
		final RemindMessageBean[] messages = e.getReceivedMessages();

		final int[] count = new int[1];
		count[0] = 0;
		final Runnable[] tasks = new Runnable[1];
		tasks[0] = new Runnable() {

			@Override
			public void run() {
				if (messages != null && count[0] < messages.length) {
					RemindMessageBean msg = messages[count[0]++];
					String time = Utils.getCurrentTime("HH:mm");
					pushMessageField.setValue(!pushMessageField.getValue());
					pushMessageField.setValue(true);
					showNotification(time + ", "
							+ msg.getTitle() + ", " + msg.getContent(), "", null);
//					messageField.setValue(time + ", "
//							+ msg.getTitle() + ", " + msg.getContent());
					AppUtils.runOnUIThread(tasks[0], 6, TimeUnit.SECONDS);
				}
			}
		};
		if (messages != null)
			AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
	}
	
	@OnHide
	void unRegisterEventListener() {
		pushMessageField.setValue(false);
		AppUtils.getService(IEventRouter.class).unregisterEventListener(NewRemindingMessagesEvent.class, this);
	}
}
