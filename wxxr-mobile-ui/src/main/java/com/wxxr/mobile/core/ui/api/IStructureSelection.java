/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IStructureSelection extends ISelection,Iterable<Object> {
	int size();
	Object[] toArray();
	List<Object> toList();
}
