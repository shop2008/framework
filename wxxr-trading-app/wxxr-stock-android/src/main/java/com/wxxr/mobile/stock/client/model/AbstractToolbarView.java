package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.ViewBase;

public abstract class AbstractToolbarView extends ViewBase {

	public AbstractToolbarView() {
		super();
	}

	public AbstractToolbarView(String name) {
		super(name);
	}

	@Override
	public <T extends IUIComponent> T getChild(String name, Class<T> clazz) {
		if("toolbar".equals(name)&&(clazz == IMenu.class)){
			return clazz.cast(getToolbarMenu());
		}
		return super.getChild(name, clazz);
	}

	protected IMenu getToolbarMenu() {		
		IPage page = getActivePage();
		return page != null ? page.getChild("toolbar", IMenu.class) : null;
	}

	protected IPage getActivePage() {
		IWorkbenchManager mgr = AppUtils.getService(IWorkbenchManager.class);
		String activePageId = mgr.getWorkbench().getActivePageId();
		if(activePageId != null){
			return mgr.getWorkbench().getPage(activePageId);
		}
		return null;
		
	}


	protected IUICommand getMenuItem(String name) {
		IMenu menu = getToolbarMenu();
		if(menu != null){
			 return menu.getCommand(name);
		}
		return null;
	}

}