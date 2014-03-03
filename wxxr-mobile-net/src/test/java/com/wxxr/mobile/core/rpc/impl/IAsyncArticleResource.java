package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.mobile.core.async.api.Async;

public interface IAsyncArticleResource{

	public Async<ArticleVOs> getNewArticle(String type,int start,int limit) throws Exception ;
}
