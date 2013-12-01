package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="articleBodyPage", withToolbar=true, description="文章正文")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.article_body_layout")
public abstract class ArticleBodyPage extends PageBase implements IModelUpdater {
	
	@Field(valueKey="visible",binding="${true}",attributes={@Attribute(name="loadUrl", value="${url}")})
	boolean articleBodyVisible;
	
	@Bean
	String url = "";
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}
	
	
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>) value;
		String url = map.get("loadUrl");
		registerBean("url", url);
	}

}
