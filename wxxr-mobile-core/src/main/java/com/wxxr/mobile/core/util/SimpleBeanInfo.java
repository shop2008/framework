/**
 * 
 */
package com.wxxr.mobile.core.util;

/**
 * @author neillin
 *
 */
public class SimpleBeanInfo implements BeanInfo {
	private final PropertyDescriptor[] descriptors;
	
	public SimpleBeanInfo(PropertyDescriptor[] infos){
		this.descriptors = infos;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.util.BeanInfo#getPropertyDescriptors()
	 */
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		return this.descriptors;
	}

}
