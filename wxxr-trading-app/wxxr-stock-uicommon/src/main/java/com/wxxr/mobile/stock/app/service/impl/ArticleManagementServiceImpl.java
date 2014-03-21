/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.AdStatusBean;
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
	private IReloadableEntityCache<String, ArticleBean> v_tradingRuleCache;
	private IReloadableEntityCache<String, ArticleBean> t1_tradingRuleCache;
	private IReloadableEntityCache<String, ArticleBean> t3_tradingRuleCache;
	private IReloadableEntityCache<String, ArticleBean> td_tradingRuleCache;
	private IReloadableEntityCache<String, ArticleBean> h_getScoreRuleCache;
	private IReloadableEntityCache<String, ArticleBean> h_rechargeCache;
	private IReloadableEntityCache<String, ArticleBean> p_registerCache;
	private IReloadableEntityCache<String, ArticleBean> rewardRuleCache;
	private IReloadableEntityCache<String, ArticleBean> withdrawlNoticeCache;
	private BindableListWrapper<ArticleBean> homeArticles,helpArticles;
	private BindableListWrapper<ArticleBean> v_tradingRuleArticles,t1_tradingRuleArticles,t3_tradingRuleArticles,td_tradingRuleArticles;
	private BindableListWrapper<ArticleBean> h_getScoreArticles,h_rechargeArticles,p_registerArticles,rewardRuleArticles,withdrawlNoticeArticles;
	private AdStatusBean adStatusBean;
	//=================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IURLLocatorManagementService.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
		addRequiredService(IPreferenceManager.class);
	}

	
	@Override
	protected void startService() {
		MyArticleLoader loader = new MyArticleLoader();
		loader.setArticleType(18);//原来15，后改为18
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("homeArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(16);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("v_tradingRuleArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(17);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("withdrawlNoticeArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(11);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("T1tradingRuleArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(19);//原来19
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("helpArticles", loader);
		
		
		loader = new MyArticleLoader();
		loader.setArticleType(22);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("T3tradingRuleArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(23);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("TDtradingRuleArticles", loader);
		
		
		
		loader = new MyArticleLoader();
		loader.setArticleType(24);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("h_getScoreArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(25);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("h_rechargeArticles", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(26);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("rewardRuleCache", loader);
		
		loader = new MyArticleLoader();
		loader.setArticleType(27);
		context.getService(IEntityLoaderRegistry.class).registerEntityLoader("p_registerArticles", loader);
		
		
		loadConfig();
		context.registerService(IArticleManagementService.class, this);
	}

	
	@Override
	protected void stopService() {
		storeConfig();
		context.unregisterService(IArticleManagementService.class, this);
	}
	private IPreferenceManager prefManager;
	protected IPreferenceManager getPrefManager() {
		if (this.prefManager == null) {
			this.prefManager = context.getService(IPreferenceManager.class);
		}
		return this.prefManager;
	}
	private final static String AD_CONTROL_FLAG = "adsetting_off";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private void loadConfig(){
	    IPreferenceManager mgr = getPrefManager();
        Dictionary<String, String> d = mgr.getPreference(getModuleName());
        String off = d != null ? d.get(AD_CONTROL_FLAG) : null;
        if (adStatusBean==null) {
         	adStatusBean = new AdStatusBean();
 		}
        if (StringUtils.isNotBlank(off)) {
        	if (off.equals("true_"+sdf.format(new Date()))) {
        		adStatusBean.setOff(true);
			}
		}
        if (log.isDebugEnabled()) {
			log.debug("Ad status is close:"+adStatusBean.getOff());
		}
	}
	private void storeConfig(){
		 Dictionary<String, String> pref = getPrefManager().getPreference(getModuleName());
	        if(pref == null){
	            pref= new Hashtable<String, String>();
	            getPrefManager().newPreference(getModuleName(), pref);
	        }else{
	            pref = DictionaryUtils.clone(pref);
	        }
	        if (adStatusBean!=null){
	            pref.put(AD_CONTROL_FLAG, adStatusBean.getOff()+"_"+sdf.format(new Date()));
	        }
	        getPrefManager().putPreference(getModuleName(), pref);
		
	}
	@Override
	public String getModuleName() {
		return "ArticleManager";
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
		this.homeArticles.setReloadParameters(map);
		this.homeArticlesCache.doReload(false,map,null);
		return this.homeArticles;
	}


	/**
	 * @return the helpArticles
	 */
	public BindableListWrapper<ArticleBean> getHelpArticles(int start,int limit) {
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
		this.helpArticles.setReloadParameters(map);
		this.helpArticlesCache.doReload(false,map,null);
		return this.helpArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getTradingRuleArticle() {
		if(this.v_tradingRuleCache == null){
			this.v_tradingRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("v_tradingRuleArticles");
		}
		if(this.v_tradingRuleArticles == null){
			this.v_tradingRuleArticles = this.v_tradingRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.v_tradingRuleArticles.setReloadParameters(map);
		this.v_tradingRuleCache.doReload(false,map,null);
		return this.v_tradingRuleArticles;
	}

	public BindableListWrapper<ArticleBean> getRewardRuleArticle() {
		if(this.rewardRuleCache == null){
			this.rewardRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("rewardRuleCache");
		}
		if(this.rewardRuleArticles == null){
			this.rewardRuleArticles = this.rewardRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.rewardRuleArticles.setReloadParameters(map);
		this.rewardRuleCache.doReload(false,map,null);
		return this.rewardRuleArticles;
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
		this.withdrawlNoticeArticles.setReloadParameters(map);
		this.withdrawlNoticeCache.doReload(false,map,null);
		return this.withdrawlNoticeArticles;
	}


	@Override
	public AdStatusBean getAdStatusBean() {
		if (adStatusBean==null) {
			adStatusBean = new AdStatusBean();
		}
		return adStatusBean;
	}


	@Override
	public BindableListWrapper<ArticleBean> getT1TradingRuleArticle() {
		if(this.t1_tradingRuleCache == null){
			this.t1_tradingRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("T1tradingRuleArticles");
		}
		if(this.t1_tradingRuleArticles == null){
			this.t1_tradingRuleArticles = this.t1_tradingRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.t1_tradingRuleArticles.setReloadParameters(map);
		this.t1_tradingRuleCache.doReload(false,map,null);
		return this.t1_tradingRuleArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getT3TradingRuleArticle() {
		if(this.t3_tradingRuleCache == null){
			this.t3_tradingRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("T3tradingRuleArticles");
		}
		if(this.t3_tradingRuleArticles == null){
			this.t3_tradingRuleArticles = this.t3_tradingRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.t3_tradingRuleArticles.setReloadParameters(map);
		this.t3_tradingRuleCache.doReload(false,map,null);
		return this.t3_tradingRuleArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getTDTradingRuleArticle() {
		if(this.td_tradingRuleCache == null){
			this.td_tradingRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("TDtradingRuleArticles");
		}
		if(this.td_tradingRuleArticles == null){
			this.td_tradingRuleArticles = this.td_tradingRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.td_tradingRuleArticles.setReloadParameters(map);
		this.td_tradingRuleCache.doReload(false,map,null);
		return this.td_tradingRuleArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getHow2GetScoreArticle() {
		if(this.h_getScoreRuleCache == null){
			this.h_getScoreRuleCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("h_getScoreArticles");
		}
		if(this.h_getScoreArticles == null){
			this.h_getScoreArticles = this.h_getScoreRuleCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.h_getScoreArticles.setReloadParameters(map);
		this.h_getScoreRuleCache.doReload(false,map,null);
		return this.h_getScoreArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getHow2RechargeArticle() {
		if(this.h_rechargeCache == null){
			this.h_rechargeCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("h_rechargeArticles");
		}
		if(this.h_rechargeArticles == null){
			this.h_rechargeArticles = this.h_rechargeCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.h_rechargeArticles.setReloadParameters(map);
		this.h_rechargeCache.doReload(false,map,null);
		return this.h_rechargeArticles;
	}


	@Override
	public BindableListWrapper<ArticleBean> getRegisterArticle() {
		if(this.p_registerCache == null){
			this.p_registerCache = new GenericReloadableEntityCache<String, ArticleBean, ArticleVO>("p_registerArticles");
		}
		if(this.p_registerArticles == null){
			this.p_registerArticles = this.p_registerCache.getEntities(null,null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", 0);
		map.put("limit", 1);
		this.p_registerArticles.setReloadParameters(map);
		this.p_registerCache.doReload(false,map,null);
		return this.p_registerArticles;
	}


}
