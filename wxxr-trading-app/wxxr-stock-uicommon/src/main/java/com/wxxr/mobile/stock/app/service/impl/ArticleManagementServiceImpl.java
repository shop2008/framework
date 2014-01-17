/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.mobile.stock.app.service.loader.MyArticleLoader;
import com.wxxr.stock.article.ejb.api.ArticleVO;

/**
 * 文章管理模块
 * @author wangxuyang
 *
 */
public class ArticleManagementServiceImpl extends AbstractModule<IStockAppContext>
		implements IArticleManagementService {
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.ArticleManagementServiceImpl");
	private IReloadableEntityCache<String, ArticleBean> homeArticlesCache;
	private IReloadableEntityCache<String, ArticleBean> helpArticlesCache;
	private IReloadableEntityCache<String, ArticleBean> tradingRuleCache;
	private IReloadableEntityCache<String, ArticleBean> withdrawlNoticeCache;
	private BindableListWrapper<ArticleBean> homeArticles,helpArticles,tradingRuleArticles,withdrawlNoticeArticles;

	//=================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IURLLocatorManagementService.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
	}

	
	@Override
	protected void startService() {
		MyArticleLoader loader = new MyArticleLoader();
		loader.setArticleType(15);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("homeArticles", loader);
		loader = new MyArticleLoader();
		loader.setArticleType(19);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("helpArticles", loader);
		loader = new MyArticleLoader();
		loader.setArticleType(16);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("tradingRuleArticles", loader);
		loader = new MyArticleLoader();
		loader.setArticleType(17);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("withdrawlNoticeArticles", loader);
		context.registerService(IArticleManagementService.class, this);
	}

	
	@Override
	protected void stopService() {
		context.unregisterService(IArticleManagementService.class, this);
	}

	//=================interface method =====================================
	
	

	/**
	 * @return the homeArticles
	 */
	public BindableListWrapper<ArticleBean> getHomeArticles(int start,int limit) {
		if(this.homeArticlesCache == null){
			this.homeArticlesCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("homeArticles");
		}
		if(this.homeArticles == null){
			this.homeArticles = this.homeArticlesCache.getEntities(null, new Comparator<ArticleBean>() {
			
				@Override
				public int compare(ArticleBean o1, ArticleBean o2) {
					int flag = 0;
					flag = o2.getPower() - o1.getPower();
					if(flag==0){ 
						if(StringUtils.isNotBlank(o2.getCreateDate()) || StringUtils.isNotBlank(o1.getCreateDate())){
							flag = o2.getCreateDate().compareTo(o1.getCreateDate());
						}
					}
					return flag;
				}
			});
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("limit", limit);
		this.homeArticlesCache.doReloadIfNeccessay(map);
		return this.homeArticles;
	}

	public BindableListWrapper<ArticleBean> getHelpArticles(int start,int limit) {
		return getHelpArticles(start,limit,false);
	}
	/**
	 * @return the helpArticles
	 */
	public BindableListWrapper<ArticleBean> getHelpArticles(int start,int limit, boolean wait4Finish) {
		if(this.helpArticlesCache == null){
			this.helpArticlesCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("helpArticles");
		}
		if(this.helpArticles == null){
			this.helpArticles = this.helpArticlesCache.getEntities(null, new Comparator<ArticleBean>() {
				@Override
				public int compare(ArticleBean o1, ArticleBean o2) {
					int flag = 0;
					flag = o2.getPower() - o1.getPower();
					if(flag==0){
						if(StringUtils.isNotBlank(o2.getCreateDate()) || StringUtils.isNotBlank(o1.getCreateDate())){
							flag = o2.getCreateDate().compareTo(o1.getCreateDate());
						}
					}
					return flag;
				}
			});
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("limit", limit);
		if(wait4Finish) {
			this.helpArticlesCache.forceReload(map, wait4Finish);
		} else {
			this.helpArticlesCache.doReloadIfNeccessay(map);
		}
		return this.helpArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getTradingRuleArticle() {
		if(this.tradingRuleCache == null){
			this.tradingRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("tradingRuleArticles");
		}
		if(this.tradingRuleArticles == null){
			this.tradingRuleArticles = this.tradingRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.tradingRuleCache.doReloadIfNeccessay(map);
		return this.tradingRuleArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getWithdrawalNoticeArticle() {
		if(this.withdrawlNoticeCache == null){
			this.withdrawlNoticeCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("withdrawlNoticeArticles");
		}
		if(this.withdrawlNoticeArticles == null){
			this.withdrawlNoticeArticles = this.withdrawlNoticeCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.withdrawlNoticeCache.doReloadIfNeccessay(map);
		return this.withdrawlNoticeArticles;
	}


}
