package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.service.IGenericContentService;

@View(name="constructUsPage",withToolbar=true, description="联系我们")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.constract_us_page_layout")
public abstract class ConstructUsPage extends PageBase {

	@Field(valueKey="text", binding="${'版本：'}${vertionValue}")
	String version;

	@Menu(items={"left"})
	private IMenu toolbar;
	
	String vertionValue;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style", visibleWhen="${true}")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		hide();
		return null;
	}
	
	@OnShow
	void initData() {
		vertionValue = AppUtils.getFramework().getApplicationVersion();
		registerBean("vertionValue", vertionValue);
	}

	@Command
	String officalSiteClicked(InputEvent event) {
		AppUtils.getService(IGenericContentService.class).browseContent("http://www.xrcj.cn");
		return null;
	}
	
	@Command
	String serviceTel(InputEvent event) {
		AppUtils.getService(IGenericContentService.class).showDialUI("010-57302539");
		return null;
	}
	
	@Command
	String serviceEmail(InputEvent event) {
		AppUtils.getService(IGenericContentService.class).showEmailUI("dxfdj@7500.com.cn");
		return null;
	}
}
