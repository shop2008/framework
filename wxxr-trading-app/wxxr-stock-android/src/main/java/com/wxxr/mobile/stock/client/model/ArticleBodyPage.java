package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="ArticleBodyPage", withToolbar=true,description="文章正文")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.article_body")
public abstract class ArticleBodyPage extends PageBase implements IModelUpdater {

	@Field(valueKey="text", binding="${tempUrl!=null?tempUrl:'#'}")
	String webUrl;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@Bean
	String tempUrl = "#";
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}


	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
	        Map temp = (Map)value;
	        for (Object key : temp.keySet()) {
	            String tempt = (String)temp.get(key);
	            if (tempt != null && "result".equals(key)) {
	            	this.webUrl = tempt;
	            	this.tempUrl = tempt;
	            	registerBean("tempUrl", tempt);
	            }
	        }
	    }
	}
	
	@OnUIDestroy
	void clearData() {
		this.tempUrl = null;
		registerBean("tempUrl", this.tempUrl);
	}
}
