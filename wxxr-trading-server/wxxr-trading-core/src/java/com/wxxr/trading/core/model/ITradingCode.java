/**
 * 
 */
package com.wxxr.trading.core.model;

import com.wxxr.trading.core.storage.api.InheritableBizObject;

/**
 * @author wangyan
 *
 */
public interface ITradingCode extends InheritableBizObject<Integer> {
		Integer getCodeId();
		String getCode();
		String getDescription();
}
