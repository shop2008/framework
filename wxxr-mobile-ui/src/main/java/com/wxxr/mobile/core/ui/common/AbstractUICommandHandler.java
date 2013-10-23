/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public abstract class AbstractUICommandHandler implements IUICommandHandler {

	private LinkedList<INavigationDescriptor> navs;
	
	public INavigationDescriptor[] getNavigations() {
		return this.navs != null && this.navs.isEmpty() == false ? 
				this.navs.toArray(new INavigationDescriptor[this.navs.size()]) :
				new INavigationDescriptor[0];
	}
	
	public AbstractUICommandHandler addNavigation(INavigationDescriptor nav){
		if(this.navs == null){
			this.navs = new LinkedList<INavigationDescriptor>();
		}
		if(!this.navs.contains(nav)){
			this.navs.addLast(nav);
		}
		return this;
	}
	
	public AbstractUICommandHandler removeNavigation(INavigationDescriptor nav){
		if(this.navs != null){
			this.navs.remove(nav);
		}
		return this;
	}

}
