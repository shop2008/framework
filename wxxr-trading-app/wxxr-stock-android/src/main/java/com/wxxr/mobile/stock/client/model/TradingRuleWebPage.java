package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;

@View(name="TradingRuleWebPage",withToolbar=true,description="文章正文")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.web_page_layout")
public abstract class TradingRuleWebPage extends PageBase implements IModelUpdater {

	static Trace log= Trace.getLogger(TradingRuleWebPage.class);
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	/**获取文章*/
	@Bean(type=BindingType.Service)
	IArticleManagementService articleService;
//	
	@Bean(express="${articleService.getTradingRuleArticle()}")
	BindableListWrapper<ArticleBean> ruleArticle;
	
	@Field(valueKey="text",binding="${(ruleArticle!=null && ruleArticle.getData()!=null && ruleArticle.getData().size()>0)?ruleArticle.getData().get(0).getArticleUrl():'#'}")
	String webUrl;
	
	@Override
	public void updateModel(Object value) {
		
	}
}
