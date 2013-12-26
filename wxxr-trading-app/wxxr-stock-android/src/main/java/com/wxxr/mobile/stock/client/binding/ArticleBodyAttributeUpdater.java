package com.wxxr.mobile.stock.client.binding;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView;

public class ArticleBodyAttributeUpdater implements
		IAttributeUpdater<ArticleBodyView> {

	@Override
	public boolean acceptable(Object control) {
		return control instanceof ArticleBodyView;
	}
	@Override
	public <T> void updateControl(ArticleBodyView control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		String url = (String)field.getAttribute(attrType);
		control.loadURL(url);
		System.out.println("--AttributeUpdater--"+url);
	}

	@Override
	public <T> T updateModel(ArticleBodyView arg0, AttributeKey<T> arg1,
			IUIComponent arg2) {
		return null;
	}

}
