package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.ArticleBean;

@View(name = "helpCenterItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.help_center_page_layout_item")
public abstract class HelpCenterItemView extends ViewBase implements
		IModelUpdater {

	@Field(valueKey = "text")
	String title;
	@Field(valueKey = "text")
	String abstractInfo;

	String articleUrl;
	DataField<String> titleField;
	DataField<String> abstractInfoField;

	
	@Command(commandName="handleItemClick")
	String handleItemClick(InputEvent event){
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof ArticleBean) {
			ArticleBean article = (ArticleBean) data;
			this.title = article.getTitle();
			this.titleField.setValue(this.title);
			this.abstractInfo = article.getAbstractInfo();
			this.abstractInfoField.setValue(this.abstractInfo);
			this.articleUrl = article.getArticleUrl();
		}
	}

}
