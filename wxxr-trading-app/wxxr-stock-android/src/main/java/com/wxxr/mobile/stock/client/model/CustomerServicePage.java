package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.service.IGenericContentService;

@View(name="CustomerServicePage",withToolbar=true, description="客户服务")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.customer_service_layout")
public abstract class CustomerServicePage extends PageBase {

	
	@Menu(items={"left"})
	protected IMenu toolbar;
	
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style", visibleWhen="${true}")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		hide();
		return null;
	}

	@Command
	String dialOfficialTel(InputEvent event) {
		AppUtils.getService(IGenericContentService.class).showDialUI("010-57302539");
		return null;
	}
	
	@Command(navigations={@Navigation(on="*", showDialog="Copy2ClipBoardDialogView")})
	CommandResult constructUsByQQ(InputEvent event) {
		//AppUtils.getService(IGenericContentService.class).showEmailUI("dxfdj@7500.com.cn");
		AppUtils.getService(IGenericContentService.class).copyTextToClipBoard("2092149934");
		CommandResult result = new CommandResult();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("message", "客服QQ号码已复制到剪切板");
		result.setResult("*");
		result.setPayload(map);
		return result;
	}
	
	@Command(navigations={@Navigation(on="*", showDialog="Copy2ClipBoardDialogView")})
	CommandResult constructUsByWeChat(InputEvent event) {
		//AppUtils.getService(IGenericContentService.class).showEmailUI("dxfdj@7500.com.cn");
		AppUtils.getService(IGenericContentService.class).copyTextToClipBoard("duanxianfadajing");
		CommandResult result = new CommandResult();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("message", "客服微信号码已复制到剪切板");
		result.setResult("*");
		result.setPayload(map);
		return result;
	}
	
	
}
