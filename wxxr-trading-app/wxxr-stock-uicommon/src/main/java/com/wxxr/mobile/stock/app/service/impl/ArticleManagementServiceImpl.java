/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.resource.ArticleResource;

/**
 * 文章管理模块
 * @author wangxuyang
 *
 */
public class ArticleManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IArticleManagementService {
	private static final Trace log = Trace.register(ArticleManagementServiceImpl.class);
	
	private MyArticlesBean articles = new MyArticlesBean();
	
	//=================private method =======================================
	private String getArticleHostURL(){
		return getService(IURLLocatorManagementService.class).getMagnoliaURL();
	}
	private String getAbsoluteURL(String relativeUrl){
		return getArticleHostURL()+relativeUrl;
	}
	private ArticleBean fromVO(ArticleVO vo){
		if (vo==null) {
			return null;
		}
		ArticleBean article = new ArticleBean();
		article.setTitle(vo.getTitle());
		article.setAbstractInfo(vo.getAbstracts());
		article.setArticleUrl(getAbsoluteURL(vo.getArticleUrl()));
		article.setImageUrl(getAbsoluteURL(vo.getThumbnails()));
		return article;
	}
	private List<ArticleBean> fromVO(List<ArticleVO> volist){
		List<ArticleBean> list = null;
		if (volist!=null&&volist.size()>0) {
			list = new ArrayList<ArticleBean>();
			for (ArticleVO article : volist) {
				list.add(fromVO(article));
			}
		}
		return list;
	}
	


	//=================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IURLLocatorManagementService.class);
		addRequiredService(IRestProxyService.class);
	}

	
	@Override
	protected void startService() {
		context.registerService(IArticleManagementService.class, this);
	}

	
	@Override
	protected void stopService() {
		context.unregisterService(IArticleManagementService.class, this);
	}

	//=================interface method =====================================
	@Override
	public  MyArticlesBean getMyArticles(int start,int limit,int type) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("method getNewArticles invoked,param[start=%s,limit=%s,type=%s]", start,limit,type));
		}
		final NewsQueryBO query = new NewsQueryBO();
		query.setLimit(limit);
		query.setStart(start);
		query.setType(String.valueOf(type));
		Future<List<ArticleVO>> future = context.getExecutor().submit(new Callable<List<ArticleVO>>() {
			public List<ArticleVO> call() throws Exception {
				try {
					if (log.isDebugEnabled()) {
						log.debug("fetching articles...");
					}
					return context.getService(IRestProxyService.class).getRestService(ArticleResource.class).getNewArticle(query);
				} catch (Exception e) {
					log.warn("Error when fetch articles from server side", e);
				}	
				return null;
			}
		});
		List<ArticleVO> list = null;
		try {
			list = future.get();
		}  catch (Exception e) {
			log.warn(String.format("Error when fetch articles[%s]", query.toString()), e);
		}
		if (log.isDebugEnabled()) {
			log.debug("data:"+list);
		}
		if (list!=null&&list.size()>0) {
			if (query.getType().equals("15")) {
				articles.setHomeArticles(fromVO(list));
			}
			if (query.getType().equals("19")) {
				articles.setHelpArticles(fromVO(list));
			}
		}
		return this.articles;
	}
}
