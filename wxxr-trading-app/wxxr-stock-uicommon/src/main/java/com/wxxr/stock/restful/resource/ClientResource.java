/*
 * @(#)ClientResource.java	 Mar 23, 2012
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.stock.restful.json.ClientInfoVO;

@Path("/rest/client")
public interface ClientResource{

    @GET
    @Path("/ClientInfo")
    @Produces({ "application/json;charset=utf-8"})
	@Consumes
    public ClientInfoVO getClientInfo();
}
