/**
 * 
 */
package com.wxxr.trading.core.storage.api;

import com.wxxr.smbiz.bizobject.INumberKeyObject;


/**
 * @author neillin
 *
 */
public interface IBizObjectConverter<K extends Number,EB extends InheritableBizObject<K>,BD extends INumberKeyObject<K>,ED extends INumberKeyObject<K>> {
	boolean updateExtDO(ED extData, EB bizObject);	
	ED createExtDO(EB bizObject);
	boolean updateExtBO(EB bizObject,ED extData);	

}
