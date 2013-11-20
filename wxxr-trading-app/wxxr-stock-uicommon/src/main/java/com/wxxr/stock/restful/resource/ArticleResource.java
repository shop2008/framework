package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
@Path("/rest/article")
public interface ArticleResource  {

	@POST
    @Path("/getnewarticle")
	@Produces({"application/json"})
	@Consumes({ "application/json"})
    //@GZIP
    public List<ArticleVO> getNewArticle(NewsQueryBO query) throws Exception ;
}
