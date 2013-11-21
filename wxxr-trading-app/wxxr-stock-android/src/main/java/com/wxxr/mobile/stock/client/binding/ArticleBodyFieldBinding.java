package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView;
import com.wxxr.mobile.stock.client.widget.ArticleBodyViewKeys;

public class ArticleBodyFieldBinding extends BasicFieldBinding {

	public ArticleBodyFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		
	}

	@Override
	public void activate(IView model) {
		IUIComponent comp = model.getChild(getFieldName());
		String url = comp.getAttribute(ArticleBodyViewKeys.loadUrl);
		ArticleBodyView view = (ArticleBodyView)getUIControl();
		if (url!=null && !url.equals("")) {
			view.loadUrl(url);
		} 
		super.activate(model);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void updateModel() {
		super.updateModel();
	}
	
	@Override
	protected void updateUI(boolean arg0) {
		super.updateUI(arg0);
	}
}
