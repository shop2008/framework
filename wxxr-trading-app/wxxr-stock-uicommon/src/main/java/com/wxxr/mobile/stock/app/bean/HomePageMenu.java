/**
 * 
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.List;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;
import com.wxxr.mobile.core.ui.common.ListDecorator;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;

/**
 * @author wangxuyang
 * 
 */
public class HomePageMenu implements IBindableBean {

	private final BindableBeanSupport emitter = new BindableBeanSupport(this);
	private List<BaseMenuItem> menuItems;

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		emitter.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		emitter.removePropertyChangeListener(listener);
	}

	public List<BaseMenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<BaseMenuItem> menuItems) {
		List<BaseMenuItem> old = this.menuItems;
		this.menuItems = menuItems;
		if (this.menuItems != null) {
			this.menuItems = new ListDecorator<BaseMenuItem>("menuItems",
					this.emitter, this.menuItems);
		}
		this.emitter.firePropertyChange("menuItems", old, this.menuItems);
		this.menuItems = menuItems;
	}

	@Override
	public String toString() {
		return "HomePageMenu [menuItems=" + menuItems + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((menuItems == null) ? 0 : menuItems.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HomePageMenu other = (HomePageMenu) obj;
		if (menuItems == null) {
			if (other.menuItems != null)
				return false;
		} else if (!menuItems.equals(other.menuItems)){
			return false;
		} else if (menuItems.size() != other.menuItems.size()) {
			return false;
		}
		return true;
	}

}
