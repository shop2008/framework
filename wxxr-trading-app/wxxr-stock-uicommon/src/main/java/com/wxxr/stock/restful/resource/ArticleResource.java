package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.stock.article.ejb.api.ArticleVOs;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
@Path("/rest/article")
public interface ArticleResource  {

	@POST
    @Path("/getnewarticle")
	@Produces({"application/json;charset=utf-8"})
	@Consumes({ "application/json"})
    public ArticleVOs getNewArticle(@QueryParam("type") String type,@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception ;
	@GET
    @Path("/getPullMessage")
    @Produces({ "application/json;charset=utf-8"})
	@Consumes
	public List<PullMessageVO> getPullMessage(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
}
