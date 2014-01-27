package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;

@View(name="SystemNewsPage", withToolbar=true, description="系统消息")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.system_news_layout")
public abstract class SystemNewsPage extends PageBase {

	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${false}")})
	String refreshView;
	
	@Field(valueKey="options")
	List<PullMessageBean> newsInfos;
	
	
	@Menu(items = {"left","right"})
	protected IMenu toolbar;
	
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(uiItems = { @UIItem(id = "right", label = "设置", icon = "resourceId:drawable/button_setting")},
			navigations = {@Navigation(on="*",showPage="PushMessageSetPage")})
	String toolbarClickedRight(InputEvent event) {
		return "*";
	}
	/**处理消息点击事*/
	@Command
	String handleItemClick(InputEvent event) {
		return null;
	}
}
