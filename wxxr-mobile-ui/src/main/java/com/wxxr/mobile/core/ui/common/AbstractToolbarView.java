package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;

public abstract class AbstractToolbarView extends ViewBase implements IAppToolbar{

	private IPage activePage;
	
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
		IPage page = getCurrentPage();
		return page != null ? page.getChild("toolbar", IMenu.class) : null;
	}



	protected IUICommand getMenuItem(String name) {
		IUICommand cmd = null;
		IMenu menu = getToolbarMenu();
		if(menu != null){
			 cmd = menu.getCommand(name);
		}
//		if(cmd == null){
//			cmd = new UICommand(name);
//			cmd.setAttribute(AttributeKeys.visible, false);
//			cmd.setAttribute(AttributeKeys.takeSpaceWhenInvisible, true);
//			cmd.init(getUIContext());
//		}
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IAppToolbar#getCurrentPage()
	 */
	@Override
	public IPage getCurrentPage() {
		return this.activePage;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IAppToolbar#setCurrentPage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void attachPage(IPage page) {
		this.activePage = page;
	}

	@Override
	public void dettachPage(IPage page) {
		if(this.activePage == page){
			this.activePage = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#hide()
	 */
	@Override
	public void hide() {
		setAttribute(AttributeKeys.visible, false);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#show()
	 */
	@Override
	public void show() {
		setAttribute(AttributeKeys.visible, true);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#show(boolean)
	 */
	@Override
	public void show(boolean backable) {
		this.show();
	}

}