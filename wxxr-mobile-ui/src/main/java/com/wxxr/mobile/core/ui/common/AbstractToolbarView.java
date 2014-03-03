package com.wxxr.mobile.core.ui.common;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;

public abstract class AbstractToolbarView extends ViewBase implements IAppToolbar{

	private Stack<IView> viewStack = new Stack<IView>();
	
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
		int len = this.viewStack.size();
		if(len <= 0){
			return null;
		}
		for(int i=len-1;i >= 0 ; i--){
			IView page = this.viewStack.get(i);
			IMenu menu = page != null ? page.getChild("toolbar", IMenu.class) : null;
			if(menu != null){
				return menu;
			}
		}
		return null;
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
	public IView getCurrentAttachment() {
		return this.viewStack.peek();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IAppToolbar#setCurrentPage(com.wxxr.mobile.core.ui.api.IPage)
	 */
	@Override
	public void attach(IView page) {
		if(this.viewStack.contains(page)){
			this.viewStack.remove(page);
		}
		this.viewStack.push(page);
		KUtils.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				refreshUI();
			}
		}, 200, TimeUnit.MILLISECONDS);
	}

	@Override
	public void dettach(IView page) {
		this.viewStack.remove(page);
		KUtils.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				refreshUI();
			}
		}, 200, TimeUnit.MILLISECONDS);
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
	
	protected void refreshUI() {
		if(getBinding() != null){
			IBinding<IView> binding = getBinding();
			binding.deactivate();
			binding.activate(this);
			binding.doUpdate();
		}
	}

}