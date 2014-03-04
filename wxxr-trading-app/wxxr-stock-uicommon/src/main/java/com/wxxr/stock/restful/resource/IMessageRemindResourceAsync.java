/*
 * @(#)MessageRemindResource.java	 2012-3-28
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.notification.ejb.api.MsgQuery;
import com.wxxr.stock.restful.json.MessageVOs;

public interface IMessageRemindResourceAsync {
	public Async<MessageVOs> findById(MsgQuery vo);
}
