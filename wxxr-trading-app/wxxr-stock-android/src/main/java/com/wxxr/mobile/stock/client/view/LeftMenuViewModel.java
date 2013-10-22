/**
 * 
 */
package com.wxxr.mobile.stock.client.view;

import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 *
 */
public class LeftMenuViewModel extends ViewBase implements IModelUpdater {

	private String menuIcon;
	private String menuLabel;
	private DataField<String> menuIconField;
	private DataField<String> menuLabelField;
	
	@Override
	protected void init() {
		setName("leftMenu");
		menuIconField = new DataField<String>();
		menuIconField.setName("menuIcon");
		menuIconField.setValueKey(AttributeKeys.imageURI);
		add(menuIconField);
		
		menuLabelField = new DataField<String>();
		menuLabelField.setName("menuLabel");
		menuLabelField.setValueKey(AttributeKeys.text);
		add(menuLabelField);
	}

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
	 * @return the menuLabel
	 */
	protected String getMenuLabel() {
		return menuLabel;
	}

	/**
	 * @param menuIcon the menuIcon to set
	 */
	protected void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
		this.menuIconField.setValue(menuIcon);
	}

	/**
	 * @param menuLabel the menuLabel to set
	 */
	protected void setMenuLabel(String menuLabel) {
		this.menuLabel = menuLabel;
		this.menuLabelField.setValue(menuLabel);
	}
	

}
