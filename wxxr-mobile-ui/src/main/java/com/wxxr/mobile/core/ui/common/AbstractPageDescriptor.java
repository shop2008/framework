/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IPageDescriptor;


/**
 * @author neillin
 *
 */
public abstract class AbstractPageDescriptor<T extends PageBase> extends AbstractViewDescriptor<T> implements
		IPageDescriptor {
	
	private boolean hasToolbar;
	
	public AbstractPageDescriptor(){
		setSingleton(true);
	}

	/**
	 * @return the hasToolbar
	 */
	public boolean isHasToolbar() {
		return hasToolbar;
	}

	/**
	 * @param hasToolbar the hasToolbar to set
	 */
	public void setHasToolbar(boolean hasToolbar) {
		this.hasToolbar = hasToolbar;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IPageDescriptor#withToolbar()
	 */
	@Override
	public boolean withToolbar() {
		return isHasToolbar();
	}
	
}
