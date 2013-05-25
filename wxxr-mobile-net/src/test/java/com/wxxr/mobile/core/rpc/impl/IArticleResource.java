package com.wxxr.mobile.core.rpc.impl;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.MediaType;

@Path("/rest/article")
@Consumes(MediaType.APPLICATION_JSON)
public interface IArticleResource{

	@POST
    @Path("/getnewarticle")
    @Produces( { "application/json" })
    public List<ArticleVO> getNewArticle(NewsQueryBO query) throws Exception;
	
	@POST
    @Path("/getarticlebystock")
    @Produces( { "application/json" })
    public List<ArticleVO> getArticleByStock(StockArticleQuery query) throws Exception;
	
	
	@POST
    @Path("/getarticlebyuser")
    @Produces( { "application/json" })
    public List<ArticleVO> getArticleByUser(UserArticleQueryBO query) throws Exception;
	
	@POST
    @Path("/getnew2cd")
    @Produces( { "application/json" })
    public List<ArticleVO> getNew2Cd(NewsDateQueryBO query) throws Exception;
	
	@GET
    @Path("/getarticlebyId")
    @Produces({ "application/json", "application/xml" })
    public ArticleVO getNewArticleById(@QueryParam("articleId") String articleId,@QueryParam("type") String type) throws Exception;
}
