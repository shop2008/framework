/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;

/**
 * @author neillin
 *
 */
@View(name="helpCenter", description="帮助中心")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.help_center_page_layout")
public abstract class HelpCenterView extends ViewBase {
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
	
	@Bean(type=BindingType.Pojo,express="${articleService.getMyArticles(0,10,19)}")
	MyArticlesBean articlesBean;
	
	@Field(valueKey="options",binding="${articlesBean!=null?articlesBean.helpArticles:null}")
	List<ArticleBean> helpArticles;
	
	@Command(navigations={@Navigation(on="web",showPage="webPage")})
	CommandResult goWebPageOnItemClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				if(articlesBean!=null){
					List<ArticleBean> article = articlesBean.getHelpArticles();
					if(article!=null && article.size()>0){
						ArticleBean bean = article.get(position);
						if(bean!=null){
							String articleUrl = bean.getArticleUrl();
							if(articleUrl!=null){
								result.setPayload(articleUrl);
							}
						}
					}
				}
			}
			result.setPayload("web");
			return result;
		}
		return null;
	}
}
 