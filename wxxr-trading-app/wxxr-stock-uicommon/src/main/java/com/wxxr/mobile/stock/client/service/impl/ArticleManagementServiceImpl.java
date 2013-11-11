/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.service.IArticleManagementService;
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
	
	private List<Article> homeArticles;
	private List<Article> helpArticles;
	//=================interface method =====================================
	@Override
	public List<Article> getNewArticles(int start, int limit,  int type) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("method getNewArticles invoked,param[start=%s,limit=%s,type=%s]", start,limit,type));
		}
		final NewsQueryBO query = new NewsQueryBO();
		query.setLimit(limit);
		query.setStart(start);
		query.setType(String.valueOf(type));
		context.invokeLater(new Runnable() {
			public void run() {	
				List<ArticleVO> list = null;
				try {
					list = context.getService(IRestProxyService.class).getRestService(ArticleResource.class).getNewArticle(query);
				} catch (Exception e) {
					log.error("Error when fetch home articles", e);
				}	
				if (query.getType().equals("15")) {
					homeArticles = fromVO(list);
				}
				if (query.getType().equals("19")) {
					helpArticles = fromVO(list);
				}
			}
		}, 10, TimeUnit.SECONDS);
		
		switch (type) {
		case 15:
			return homeArticles;
		case 19:
			return getHelpArticles(start, limit);
		default:
			break;
		}
		return mockData();
	}
	
	
	//=================private method =======================================
	private Article fromVO(ArticleVO vo){
		return null;
	}
	private List<Article> fromVO(List<ArticleVO> volist){
		return null;
	}
	private List<Article> getHomeArticles(int start, int limit) {
		final NewsQueryBO query = new NewsQueryBO();
		query.setLimit(limit);
		query.setStart(start);
		query.setType("19");
		context.invokeLater(new Runnable() {
			public void run() {	
				List<ArticleVO> list = null;
				try {
					list = context.getService(IRestProxyService.class).getRestService(ArticleResource.class).getNewArticle(query);
				} catch (Exception e) {
					log.error("Error when fetch home articles", e);
				}
				homeArticles = fromVO(list);
			}
		}, 10, TimeUnit.SECONDS);
		return homeArticles;
	}


	private List<Article> getHelpArticles(int start, int limit) {
		final NewsQueryBO query = new NewsQueryBO();
		query.setLimit(limit);
		query.setStart(start);
		query.setType("15");
		context.invokeLater(new Runnable() {
			public void run() {	
				List<ArticleVO> list = null;
				try {
					list = context.getService(IRestProxyService.class).getRestService(ArticleResource.class).getNewArticle(query);
				} catch (Exception e) {
					log.error("Error when fetch help articles", e);
				}
				helpArticles = fromVO(list);
			}
		}, 10, TimeUnit.SECONDS);
		return helpArticles;
	}
	
	private List<Article> mockData(){
		List<Article> articles = new ArrayList<Article>();
		String[] titles = {"Google","Baidu","Sina","网易"};
		String[] articleUrls = {"http://www.google.com.hk/","http://www.baidu.com","http://www.sina.com","http://www.163.com"};
		String[] imageUrls = {"http://www.google.com.hk/logos/doodles/2013/raymond-loewys-120th-birthday-ca-fr-us-nl-uk-ie-6388231276855296-hp.jpg",
				"http://www.baidu.com/img/bdlogo.gif","http://ui.sina.com/assets/img/www/worldmap.jpg",
				"http://image.gxq.com.cn/upload/ad/2013/11/07/bd7bae043de331091b93d9394eec0298.jpg"};
		for (int i = 0; i < 4; i++) {
			Article article = new Article();
			article.setId(String.format("%s", i));
			article.setTitle(titles[i]);
			article.setAbstractInfo(titles[i]+"'s 摘要 ");
			article.setArticleUrl(articleUrls[i]);
			article.setImageUrl(imageUrls[i]);
			article.setType(15);
			articles.add(article);
		}
		return articles;
	}

	//=================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
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


	
	

}
