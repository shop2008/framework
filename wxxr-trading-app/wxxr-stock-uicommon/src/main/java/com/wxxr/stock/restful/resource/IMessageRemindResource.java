/*
 * @(#)MessageRemindResource.java	 2012-3-28
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.notification.ejb.api.MsgQuery;

@Path("/secure/remind")
public interface IMessageRemindResource {
	@POST
	@Path("/findById")
	@Produces( { "application/json;charset=utf-8" })
	@Consumes
	public List<MessageVO> findById(MsgQuery vo) throws Exception ;
}
