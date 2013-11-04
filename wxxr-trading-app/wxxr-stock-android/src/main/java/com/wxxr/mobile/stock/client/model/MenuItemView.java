/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 *
 */
@View(name="mainMenuItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_left_navi_item")
public abstract class MenuItemView extends ViewBase implements IModelUpdater{
	
	@Field(valueKey="imageURI")
	String menuIcon;
	
	@Field(valueKey="text")
	String menuLabel;
	
	DataField<String> menuIconField;
	
	DataField<String> menuLabelField;

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IModelUpdater#updateModel(java.lang.Object)
	 */
	@Override
	public void updateModel(Object val) {
		if(val instanceof IUICommand){
			IUICommand cmd = (IUICommand)val;
			String value = cmd.getAttribute(AttributeKeys.icon);
			setMenuIcon(value);
			value = cmd.getAttribute(AttributeKeys.title);
			setMenuLabel(value);
			if(cmd.hasAttribute(AttributeKeys.visible)){
				setAttribute(AttributeKeys.visible, cmd.getAttribute(AttributeKeys.visible));
			}
			if(cmd.hasAttribute(AttributeKeys.enabled)){
				setAttribute(AttributeKeys.enabled, cmd.getAttribute(AttributeKeys.enabled));
			}
		}
		
	}

	/**
	 * @return the menuIcon
	 */
	protected String getMenuIcon() {
		return menuIcon;
	}

	/**
	 * @param menuIcon the menuIcon to set
	 */
	protected void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
		this.menuIconField.setValue(menuIcon);
	}

	/**
	 * @return the menuLabel
	 */
	protected String getMenuLabel() {
		return menuLabel;
	}

	/**
	 * @param menuLabel the menuLabel to set
	 */
	protected void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
		this.menuLabelField.setValue(menuLabel);
	}

}
