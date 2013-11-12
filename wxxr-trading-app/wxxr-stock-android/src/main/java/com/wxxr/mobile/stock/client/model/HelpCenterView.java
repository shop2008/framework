/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.service.IArticleManagementService;

/**
 * @author neillin
 *
 */
@View(name="helpCenter", description="帮助中心")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.help_center_page_layout")
public abstract class HelpCenterView extends ViewBase {
	@Field(valueKey="options")
	List<Article> helpArticles;
	
	DataField<List> helpArticlesField;
	
	@OnShow	
	protected void updateHelpArticles() {
		helpArticles = getUIContext().getKernelContext().getService(IArticleManagementService.class).getNewArticles(0, 4, 19);
		helpArticlesField.setValue(helpArticles);
	}
}
