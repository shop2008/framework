/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.client.bean.Article;

/**
 * @author neillin
 *
 */
@BindableBean(className="MyArticlesBean")
public class MyArticles {
	private List<Article> homeArticles;
	private List<Article> helpArticles;

}
