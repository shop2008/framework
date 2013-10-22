/**
 * 
 */
package com.wxxr.mobile.stock.client.view;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AbstractUICommandHandler;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.MenuBase;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.ui.common.UICommand;

/**
 * @author neillin
 *
 */
public class HomePageModel extends PageBase {
	
	private static final Trace log = Trace.register(HomePageModel.class);

	@Override
	protected void init() {
		UICommand cmd = null;
		IUICommandHandler navCommand = new AbstractUICommandHandler() {
			
			@Override
			public String execute(InputEvent event) {
				if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
					String name = ((IUICommand)event.getProperty("ItemClicked")).getName();
					if(log.isDebugEnabled()){
						log.debug("Menu item :"+name+" was clicked !");
					}
					return name;
				}
				return null;
			}
		};
		addUICommand("home", navCommand);
		cmd = new UICommand();
		cmd.setName("home");
		cmd.setAttribute(AttributeKeys.icon, "resourceId:drawable/home");
		cmd.setAttribute(AttributeKeys.title, "首页");
		add(cmd);
		
		addUICommand("page2", navCommand);
		cmd = new UICommand();
		cmd.setName("page2");
		cmd.setAttribute(AttributeKeys.icon, "resourceId:drawable/zpb");
		cmd.setAttribute(AttributeKeys.title, "赚钱榜");
		add(cmd);
		
		addUICommand("page3", navCommand);
		cmd = new UICommand();
		cmd.setName("page3");
		cmd.setAttribute(AttributeKeys.icon, "resourceId:drawable/hqzx");
		cmd.setAttribute(AttributeKeys.title, "行情中心");
		add(cmd);

		addUICommand("page4", navCommand);
		cmd = new UICommand();
		cmd.setName("page4");
		cmd.setAttribute(AttributeKeys.icon, "resourceId:drawable/dsphb");
		cmd.setAttribute(AttributeKeys.title, "大赛排行榜");
		add(cmd);
		
		addUICommand("page5", navCommand);
		cmd = new UICommand();
		cmd.setName("page5");
		cmd.setAttribute(AttributeKeys.icon, "resourceId:drawable/help");
		cmd.setAttribute(AttributeKeys.title, "帮助中心");
		add(cmd);

		MenuBase menu = new MenuBase(new String[]{"page1","page2","page3","page4","page5"});
		menu.setName("leftMenu");
		add(menu);
	}

}
