package com.wxxr.stock.restful.resource;

import java.util.List;

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;

public class ArticleResourceTest extends TestCase{

	private IRestProxyService restService=new ResteasyRestClientService();

	public void testGetNewArticle()throws Exception{
		NewsQueryBO query = new NewsQueryBO();
		query.setStart(0);
		query.setLimit(3);
		query.setType("16");
		query.setUid(0);
		List<ArticleVO> a = restService.getRestService(ArticleResource.class).getNewArticle(query);
	}
	//public List<PullMessageVO> getPullMessage(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	public void testGetPullMessage()throws Exception{
		List<PullMessageVO> a = restService.getRestService(ArticleResource.class).getPullMessage(0,4);
	}
}
