package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.biz.ViewMoreBean;
import com.wxxr.mobile.stock.client.utils.Constants;

@View(name="VirtualViewMoreView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.virtual_view_more_layout")
public abstract class VirtualViewMoreView extends ViewBase implements IModelUpdater {
	
	String userId = null;
	
	String userName = null;
	
	
	@Command(navigations={
			@Navigation(on="+", showPage="userViewMorePage"),
			@Navigation(on="-", showPage="OtherViewMorePage")})
	CommandResult virtualViewMore(InputEvent event) {
		CommandResult result = null;
		if (StringUtils.isBlank(userId)) {
			//本人
			result = new CommandResult();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isVirtual", false);
			result.setPayload(map);
			result.setResult("OK");
			result.setResult("+");
		} else {
			//他人
			result = new CommandResult();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isVirtual", false);
			map.put("userId", this.userId);
			map.put(Constants.KEY_USER_NAME_FLAG, userName);
			result.setPayload(map);
			result.setResult("-");
		}
		return result;
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof ViewMoreBean) {
			ViewMoreBean viewMoreBean = (ViewMoreBean) value;
			userId = viewMoreBean.getUserId();
			userName = viewMoreBean.getUserName();
		}
		
	}
}
