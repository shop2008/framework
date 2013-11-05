/**
 * 
 */
package com.wxxr.mobile.core.model;

import java.util.LinkedList;

/**
 * @author neillin
 *
 */
public class PModeProviderClass extends AbstractClassModel {
	private LinkedList<String> descriptors = new LinkedList<String>();
	
	public void addDescriptor(String descriptor){
		descriptors.add(addImport(descriptor));
	}
	
	public String[] getDescriptors() {
		return descriptors.isEmpty() ? new String[0] : descriptors.toArray(new String[0]);
	}
}
