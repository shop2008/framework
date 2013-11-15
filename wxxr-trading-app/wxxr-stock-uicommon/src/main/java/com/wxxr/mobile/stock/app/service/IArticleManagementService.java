/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.bean.MyArticlesBean;

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
	MyArticlesBean getMyArticles(int start,int limit,int type);
}
