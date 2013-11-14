/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author neillin
 *
 */
public class PModeProviderClass extends AbstractClassModel {
	private LinkedList<String> descriptors = new LinkedList<String>();
	private Map<String, String> alias;
	
	public void addDescriptor(String descriptor){
		descriptors.add(addImport(descriptor));
	}
	
	public String[] getDescriptors() {
		return descriptors.isEmpty() ? new String[0] : descriptors.toArray(new String[0]);
	}
	
	public void addAlias(String name, String className){
		if(this.alias == null){
			this.alias = new HashMap<String, String>();
		}
		this.alias.put(name, addImport(className));
	}
	
	public Entry<String, String>[] getAllAlias() 
	{
		return (this.alias != null)&& (this.alias.size() > 0) ? this.alias.entrySet().toArray(new Entry[0]) : null;
	}
}
