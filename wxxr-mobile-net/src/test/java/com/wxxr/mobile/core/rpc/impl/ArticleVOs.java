/**
 * 
 */
package com.wxxr.mobile.core.rpc.impl;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "ArticlesVos")
public class ArticleVOs {

	@XmlElement(name="articles")
	private List<ArticleVO> articles;

	/**
	 * @return the articles
	 */
	
	public List<ArticleVO> getArticles() {
		return articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(List<ArticleVO> articles) {
		this.articles = articles;
	}
	
}
