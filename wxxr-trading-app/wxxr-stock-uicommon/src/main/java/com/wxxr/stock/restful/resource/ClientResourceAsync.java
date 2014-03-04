/*
 * @(#)ClientResource.java	 Mar 23, 2012
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.restful.json.ClientInfoVO;

public interface ClientResourceAsync{
    public Async<ClientInfoVO> getClientInfo();
}
