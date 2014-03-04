/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.bean.AdStatusBean;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;

/**
 * 文章管理模块
 * @author wangxuyang
 *
 */
public interface IArticleManagementService {
	
	/**
	 * 查询文章列表
	 * @param start-起始位置 
	 * @param limit-查询条数
	 * @param type-文章类型
	 * 				
	 * @return
	 */
	public BindableListWrapper<ArticleBean> getHelpArticles(int start,int limit);
		
	public BindableListWrapper<ArticleBean> getHomeArticles(int start,int limit);
	//参赛盘创建规则
	public BindableListWrapper<ArticleBean> getTradingRuleArticle();
	//T+1挑战盘创建规则
	public BindableListWrapper<ArticleBean> getT1TradingRuleArticle();
	//T+3挑战盘创建规则
	public BindableListWrapper<ArticleBean> getT3TradingRuleArticle();
	//T+D挑战盘创建规则
	public BindableListWrapper<ArticleBean> getTDTradingRuleArticle();
	//积分奖励规则
	public BindableListWrapper<ArticleBean> getRewardRuleArticle();
	//如何获取积分
	public BindableListWrapper<ArticleBean> getHow2GetScoreArticle();
	//如何充值
	public BindableListWrapper<ArticleBean> getHow2RechargeArticle();
	//提现须知
	public BindableListWrapper<ArticleBean> getWithdrawalNoticeArticle();
	//注册条款
	public BindableListWrapper<ArticleBean> getRegisterArticle();
	public AdStatusBean getAdStatusBean();
	

}
