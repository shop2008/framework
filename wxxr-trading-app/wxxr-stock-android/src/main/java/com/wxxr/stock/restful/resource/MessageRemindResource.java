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

import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.SimpleVO;

/*
 * 消息提醒
 */
@Path("/remind")
public interface MessageRemindResource  {

	/**
	 * 根据用户id获取相应的记录
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/findById")
	@Produces( { "application/json" })
	//@GZIP
	public List<MessageVO> findById(MessageVO vo) throws Exception ;
	
	@POST
	@Path("/findByIdNCode")
	@Produces( { "application/json" })
	//@GZIP
	public List<MessageVO> findByIdNCode(MessageVO messageVO) throws Exception ;
	
	/**
	 * 查询前30天的提醒记录
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/findDateInMonth")
	@Produces( { "application/json" })
	//@GZIP
	public List<SimpleVO> findDateOfRemindsInMonth() throws Exception;
	
	/**
	 * 根据日期查看记录
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/findRemindsByDate")
	@Produces( { "application/json" })
	//@GZIP
	public List<MessageVO> findRemindsByCreateDate(SimpleVO vo) throws Exception;
}
