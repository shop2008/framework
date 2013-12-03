/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;
import java.util.List;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;

/**
 * @author neillin
 *
 */
public abstract class AbstractUICommandHandler implements IUICommandHandler {

	private LinkedList<INavigationDescriptor> navs;
	private IProgressGuard progressGuard;
	private List<ConstraintLiteral> constraints;
	
	public AbstractUICommandHandler addConstraint(ConstraintLiteral constraint){
		if(constraint == null){
			throw new IllegalArgumentException("Invalid constraint : NULL !");
		}
		if(this.constraints == null){
			this.constraints = new LinkedList<ConstraintLiteral>();
		}
		if(!this.constraints.contains(constraint)){
			this.constraints.add(constraint);
		}
		return this;
	}
	
	public AbstractUICommandHandler removeConstraint(ConstraintLiteral constraint){
		if(this.constraints != null){
			this.constraints.remove(constraint);
		}
		return this;
	}
	
	
	public ConstraintLiteral[] getConstraints() {
		return this.constraints != null ? this.constraints.toArray(new ConstraintLiteral[0]) : null;
	}

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

	/**
	 * @return the progressGuard
	 */
	public IProgressGuard getProgressGuard() {
		return progressGuard;
	}

	/**
	 * @param progressGuard the progressGuard to set
	 */
	public void setProgressGuard(IProgressGuard progressGuard) {
		this.progressGuard = progressGuard;
	}

}
