/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.view.GravityCompat;
import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IMenuAdaptor;
import com.wxxr.mobile.core.ui.api.IMenuHandler;

/**
 * @author neillin
 *
 */
public class HomePageMenuAdaptor implements IMenuAdaptor {

	private IAndroidBindingContext context;
	private SliderLayout widget;
	private Map<String, IMenuHandler> handlers = new HashMap<String, IMenuHandler>();
	
	private class MenuHandler implements IMenuHandler {
		private int gravity;
		
		MenuHandler(int direction){
			this.gravity = direction;
		}
		
		@Override
		public void showMenu() {
			widget.openDrawer(gravity);
		}
		
		@Override
		public boolean isMenuOnShow() {
			return widget.isDrawerOpen(gravity);
		}
		
		@Override
		public void hideMenu() {
			widget.closeDrawer(gravity);
		}
	
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#destroy()
	 */
	@Override
	public void destroy() {
		this.context = null;
		this.widget = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#getMenuHandler(java.lang.String)
	 */
	@Override
	public IMenuHandler getMenuHandler(String menuId) {
		return this.handlers.get(menuId);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#getMenuIds()
	 */
	@Override
	public String[] getMenuIds() {
		return this.handlers.keySet().toArray(new String[this.handlers.size()]);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#init(com.wxxr.mobile.android.ui.IAndroidBindingContext, android.view.View)
	 */
	@Override
	public void init(IAndroidBindingContext ctx, View view) {
		this.context = ctx;
		this.widget = (SliderLayout)view;
		this.handlers.put("leftMenu", new MenuHandler(GravityCompat.START));
		this.handlers.put("rightMenu", new MenuHandler(GravityCompat.END));
	}

}
