/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.module.IArticleManagerModule;

/**
 * @author neillin
 *
 */
@View(name="tradingMain")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.home_view_layout")
public abstract class TradingMainView extends ViewBase {
	
	@Field(valueKey="options")
	List<Article> articles;
	DataField<List> articlesField;
	
	@OnShow
	protected void updataArticles() {
		articles = getUIContext().getKernelContext().getService(IArticleManagerModule.class).getNewArticles(0, 4, 15);
		articlesField.setValue(articles);
	}

	/**
	 * 创建买入页跳转
	 * 
	 * */
	@Command(description="",commandName="createBuyClick")
	String createBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
//			getUIContext().getWorkbenchManager().getPageNavigator().showPage(arg0, null, null);
		}
		return null;
	}
}
