/**
 * 
 */
package com.wxxr.mobile.tools.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxxr.mobile.core.tools.generator.UIViewModelGenerator;

/**
 * @author neillin
 *
 */
public class PModeProviderClass extends AbstractClassModel {
	private static final Logger log = LoggerFactory.getLogger(UIViewModelGenerator.class); 
	private LinkedList<String> descriptors = new LinkedList<String>();
	private Map<String, String> alias;
	private String workbenchDescriptor;
	
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

	/**
	 * @return the workbenchDescriptor
	 */
	public String getWorkbenchDescriptor() {
		return workbenchDescriptor;
	}

	/**
	 * @param workbenchDescriptor the workbenchDescriptor to set
	 */
	public void setWorkbenchDescriptor(String workbenchDescriptor) {
		log.info("set workbench description class :"+workbenchDescriptor);
		this.workbenchDescriptor = addImport(workbenchDescriptor);
	}
}
