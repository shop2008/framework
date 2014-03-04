/**
 * 
 */
package com.wxxr.trading.core.storage.api;

import com.wxxr.smbiz.bizobject.INumberKeyObject;


/**
 * @author neillin
 *
 */
public interface InheritableBizObject<K extends Number> extends INumberKeyObject<K> {
	String getType();
}
