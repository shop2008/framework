/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.ArticleBean;
import com.wxxr.mobile.stock.client.bean.MyArticlesBean;
import com.wxxr.mobile.stock.client.service.IArticleManagementService;

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
}
