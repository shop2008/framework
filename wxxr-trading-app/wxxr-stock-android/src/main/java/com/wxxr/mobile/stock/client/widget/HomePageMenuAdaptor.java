/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import java.util.HashMap;
import java.util.Map;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.IMenuAdaptor;
import com.wxxr.mobile.core.ui.api.IMenuCallback;
import com.wxxr.mobile.core.ui.api.IMenuHandler;
import com.wxxr.mobile.stock.client.widget.SliderLayout.LayoutParams;

/**
 * @author neillin
 *
 */
public class HomePageMenuAdaptor implements IMenuAdaptor {

	private IAndroidBindingContext context;
	private SliderLayout widget;
	private Map<String, MenuHandler> handlers = new HashMap<String, MenuHandler>();
	private SliderLayout.DrawerListener listener = new SliderLayout.SimpleDrawerListener() {

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.stock.client.widget.SliderLayout.SimpleDrawerListener#onDrawerOpened(android.view.View)
		 */
		@Override
		public void onDrawerOpened(View drawerView) {
			String menuId = getDrawerViewGravity(drawerView);
			if(menuId != null){
				MenuHandler handler = getMenuHandler(menuId);
				if(handler.callback != null){
					handler.callback.onShow(menuId);
				}
			}
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.stock.client.widget.SliderLayout.SimpleDrawerListener#onDrawerClosed(android.view.View)
		 */
		@Override
		public void onDrawerClosed(View drawerView) {
			String menuId = getDrawerViewGravity(drawerView);
			if(menuId != null){
				MenuHandler handler = getMenuHandler(menuId);
				if(handler.callback != null){
					handler.callback.onHide(menuId);
				}
			}
		}

	};
	
    private String getDrawerViewGravity(View drawerView) {
        int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
//        gravity = GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(drawerView));
        if(gravity == GravityCompat.START){
        	return "leftMenu";
        }else if(gravity == GravityCompat.END){
        	return "rightMenu";
        }
        return null;
    }

	private class MenuHandler implements IMenuHandler {
		private int gravity;
		private IMenuCallback callback;
		
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
		
		public void setMenuCallback(IMenuCallback cb){
			this.callback = cb;
		}
	
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#destroy()
	 */
	@Override
	public void destroy() {
		if(this.widget != null){
			this.widget.setDrawerListener(null);
			this.widget = null;
		}
		this.context = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IMenuAdaptor#getMenuHandler(java.lang.String)
	 */
	@Override
	public MenuHandler getMenuHandler(String menuId) {
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
		this.widget.setDrawerListener(listener);
		this.handlers.put("leftMenu", new MenuHandler(GravityCompat.START));
		this.handlers.put("rightMenu", new MenuHandler(GravityCompat.END));
	}

}
