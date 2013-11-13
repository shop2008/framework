/**
 * 
 */
package com.wxxr.mobile.stock.client.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.ArticleBean;
import com.wxxr.mobile.stock.client.bean.MyArticlesBean;
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
	
	private MyArticlesBean articles = new MyArticlesBean();
	

	
	
	//=================private method =======================================
	private ArticleBean fromVO(ArticleVO vo){
		if (vo==null) {
			return null;
		}
		ArticleBean article = new ArticleBean();
		article.setTitle(vo.getTitle());
		article.setAbstractInfo(vo.getAbstracts());
		article.setArticleUrl(vo.getArticleUrl());
		article.setImageUrl(vo.getThumbnails());
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
	
	private List<ArticleBean> mockData(){
		List<ArticleBean> articles = new ArrayList<ArticleBean>();
		String[] titles = {"Google","Baidu","Sina","网易"};
		String[] articleUrls = {"http://www.google.com.hk/","http://www.baidu.com","http://www.sina.com","http://www.163.com"};
		String[] imageUrls = {"http://www.google.com.hk/logos/doodles/2013/raymond-loewys-120th-birthday-ca-fr-us-nl-uk-ie-6388231276855296-hp.jpg",
				"http://www.baidu.com/img/bdlogo.gif","http://ui.sina.com/assets/img/www/worldmap.jpg",
				"http://image.gxq.com.cn/upload/ad/2013/11/07/bd7bae043de331091b93d9394eec0298.jpg"};
		for (int i = 0; i < 4; i++) {
			ArticleBean article = new ArticleBean();
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

	//=================interface method =====================================
	@Override
	public MyArticlesBean getMyArticles(int start,int limit,int type) {
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
					articles.setHomeArticles(fromVO(list)==null?null:mockData());
				}
				if (query.getType().equals("19")) {
					articles.setHelpArticles(fromVO(list)==null?null:mockData());
				}
			}
		}, 10, TimeUnit.SECONDS);
		return this.articles;
	}


	
	

}
