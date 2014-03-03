package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;

@Path("/rest/article")
public interface IArticleResource  {

	@POST
    @Path("/getnewarticle")
	@Produces({"application/json;charset=utf-8"})
	@Consumes({ "application/json"})
    public ArticleVOs getNewArticle(@QueryParam("type") String type,@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception ;
	
}
