/**
 * 
 */
package com.wxxr.mobile.stock.app;

import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;

/**
 * @author neillin
 *
 */
public interface IStockAppFramework extends IAndroidFramework<IStockAppContext, AbstractModule<IStockAppContext>> {
	String getUserAgentString();
}
