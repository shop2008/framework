package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.mobile.core.rpc.impl.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
@Path("/rest/article")
public interface ArticleResource  {

	@POST
    @Path("/getnewarticle")
    @Produces( { "application/json" })
    //@GZIP
    public List<ArticleVO> getNewArticle(NewsQueryBO query) throws Exception ;
	
	
	/*@POST
    @Path("/getarticlebystock")
    @Produces( { "application/json" })
    //@GZIP
    public List<ArticleVO> getArticleByStock(StockArticleQuery query) throws Exception ;
		
	
	@POST
    @Path("/getarticlebyuser")
    @Produces( { "application/json" })
    //@GZIP
    public List<ArticleVO> getArticleByUser(UserArticleQueryBO query) throws Exception;
	@POST
    @Path("/getnew2cd")
    @Produces( { "application/json" })
    //@GZIP
    public List<ArticleVO> getNew2Cd(NewsDateQueryBO query) throws Exception;
	
	@GET
    @Path("/getarticlebyId")
    @Produces({ "application/json", "application/xml" })
    //@GZIP
    public ArticleVO getNewArticleById(@QueryParam("articleId") String articleId,@QueryParam("type") String type) throws Exception;
	*/
	
	
}
