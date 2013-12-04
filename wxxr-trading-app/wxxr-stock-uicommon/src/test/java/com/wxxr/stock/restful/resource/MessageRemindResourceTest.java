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

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.json.NewsQueryBO;


public class MessageRemindResourceTest extends TestCase{

	private IRestProxyService restService=new ResteasyRestClientService();

	public void testFindById()throws Exception{
		MessageVO vo = new MessageVO();
		vo.setId("whatever");//这个接口其实不需要传参
		List<MessageVO> a = restService.getRestService(IMessageRemindResource.class).findById(vo);
	}
}
