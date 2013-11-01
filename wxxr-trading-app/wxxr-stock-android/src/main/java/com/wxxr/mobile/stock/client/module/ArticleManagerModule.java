/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.bean.Article;

/**
 * @author wangxuyang
 *
 */
public class ArticleManagerModule extends AbstractModule<IStockAppContext>
		implements IArticleManagerModule {
	private static final Trace log = Trace.register(ArticleManagerModule.class);
	
	//=================interface method =====================================
	@Override
	public List<Article> getNewArticles(int start, int limit, int type) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("method getNewArticles invoked,param[start=%s,limit=%s,type=%s]", start,limit,type));
		}
		return mockData();
	}
	
	//=================private method =======================================
	private List<Article> mockData(){
		List<Article> articles = new ArrayList<Article>();
		String[] titles = {"Google","Baidu","Sina","网易"};
		String[] articleUrls = {"http://www.google.com.hk/","http://www.baidu.com","http://www.sina.com","http://www.163.com"};
		String[] imageUrls = {};
		for (int i = 0; i < 4; i++) {
			Article article = new Article();
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

	}

	
	@Override
	protected void startService() {
		context.registerService(IArticleManagerModule.class, this);

	}

	
	@Override
	protected void stopService() {
		context.unregisterService(IArticleManagerModule.class, this);
	}
	
	

}
