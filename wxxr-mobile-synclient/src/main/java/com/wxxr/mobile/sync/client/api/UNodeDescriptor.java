/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

import java.util.Arrays;

/**
 * @author neillin
 *
 */
public class UNodeDescriptor extends MNodeDescriptor {
	private static final long serialVersionUID = -7387014261776309123L;
	private MNodeDescriptor[] children;		// the sequence of children is significant !!!
	/**
	 * @return the children
	 */
	public MNodeDescriptor[] getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(MNodeDescriptor[] children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UNodeDescriptor [children=" + Arrays.toString(children) +","+ super.toString()+"]";
	}
	
	
	
}
