/**
 * 
 */
package com.wxxr.trading.core.storage.api;

import java.util.Map;

import com.wxxr.smbiz.bizobject.INumberKeyObject;

/**
 * @author neillin
 *
 */
public interface IExtStrategy<K extends Number,BB extends InheritableBizObject<K>,EB extends BB,BD extends INumberKeyObject<K>,ED extends INumberKeyObject<K>> {
	IDataAccessObject<K,ED> getDAO();
	IBizObjectConverter<K,EB, BD, ED> getConverter();
	EB createBizObject(Map<String, Object> params);
	Class<EB> getBizObjectClass();
	Class<ED> getExtDataObjectClass();
}
