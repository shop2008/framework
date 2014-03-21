package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.article.ejb.api.ArticleVOs;
import com.wxxr.stock.restful.json.PullMessageVOs;

public interface ArticleResourceAsync  {

    public Async<ArticleVOs> getNewArticle( String type, int start, int limit);
	public Async<PullMessageVOs> getPullMessage( int start, int limit);
}
