package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.ICommandHandler;

public abstract class UICommand implements ICommandHandler{
	private LinkedList<INavigationDescriptor> navs;
	
	public INavigationDescriptor[] getNavigations() {
		return this.navs != null && this.navs.isEmpty() == false ? 
				this.navs.toArray(new INavigationDescriptor[this.navs.size()]) :
				new INavigationDescriptor[0];
	}
	
	protected UICommand addNavigation(INavigationDescriptor nav){
		if(this.navs == null){
			this.navs = new LinkedList<INavigationDescriptor>();
		}
		if(!this.navs.contains(nav)){
			this.navs.addLast(nav);
		}
		return this;
	}
	
	protected UICommand removeNavigation(INavigationDescriptor nav){
		if(this.navs != null){
			this.navs.remove(nav);
		}
		return this;
	}

}
