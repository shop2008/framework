/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IPageDescriptor;


/**
 * @author neillin
 *
 */
public abstract class AbstractPageDescriptor extends AbstractViewDescriptor implements
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
	
}
