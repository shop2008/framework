/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.client.bean.ArticleBean;

/**
 * @author neillin
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="MyArticlesBean")
public class MyArticles {
	private List<ArticleBean> homeArticles;
	private List<ArticleBean> helpArticles;
}
