package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;

@View(name="SystemNewsPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.system_news_layout")
public abstract class SystemNewsPage extends PageBase {

	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${false}")})
	String refreshView;
	
	@Field(valueKey="options")
	List<PullMessageBean> newsInfos;
	
	/**处理消息点击事*/
	@Command
	String handleItemClick(InputEvent event) {
		return null;
	}
}
