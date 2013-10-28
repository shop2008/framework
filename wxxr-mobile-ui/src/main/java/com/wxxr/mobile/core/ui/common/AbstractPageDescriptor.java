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
	
	public AbstractPageDescriptor(){
		setSingleton(true);
	}
	
}
