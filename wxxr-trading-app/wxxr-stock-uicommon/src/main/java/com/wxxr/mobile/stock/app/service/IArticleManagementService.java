/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

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
	 * 				15-代表首页文章 ；19-代表帮助中心
	 * @return
	 */
	public BindableListWrapper<ArticleBean> getHelpArticles(int start,int limit);
	
	public BindableListWrapper<ArticleBean> getHomeArticles(int start,int limit);
}
