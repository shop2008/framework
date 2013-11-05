/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.module.IArticleManagerModule;

/**
 * @author neillin
 *
 */
@View(name="tradingMain")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.layout_home")
public abstract class TradingMainView extends ViewBase {
	
	//@Field(valueKey="options")
	//List<Article> articles;

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.ViewBase#onShow(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	@Override
	protected void onShow(IBinding<IView> binding) {
		//articles = getUIContext().getKernelContext().getService(IArticleManagerModule.class).getNewArticles(0, 4, 15);
	}
	
}
