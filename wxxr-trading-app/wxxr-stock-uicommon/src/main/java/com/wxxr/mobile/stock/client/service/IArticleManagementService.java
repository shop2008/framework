/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import java.util.List;

import com.wxxr.mobile.stock.client.bean.ArticleBean;
import com.wxxr.mobile.stock.client.bean.MyArticlesBean;

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
	List<ArticleBean> getNewArticles(int start,int limit,int type);
	
	MyArticlesBean getMyArticles();
}
