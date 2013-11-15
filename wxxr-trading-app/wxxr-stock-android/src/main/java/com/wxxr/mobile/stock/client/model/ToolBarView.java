/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.stock.client.ui.StockAppToolbar;

/**
 * @author neillin
 * 
 */
@View(name="toolbarView",singleton=true)
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.layout_animation_tool_bar_view")
public abstract class ToolBarView extends StockAppToolbar {


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


}
