/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.core.rpc.api.IServerUrlLocator;

/**
 * @author wangxuyang
 *
 */
public interface IURLLocatorManagementService extends IServerUrlLocator{
	String getMagnoliaURL();
}
