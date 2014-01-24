package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="ApplyMoneyAuthConfirmDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.apply_money_auth_confirm_dialog")
public abstract class ApplyMoneyAuthConfirmDialog extends ViewBase implements IModelUpdater {

	
	@Field(valueKey = "text",binding="${accountName}")
	String accountName;

	
	@Field(valueKey = "text",binding="${bankName}")
	String bankName;
	
	@Field(valueKey = "text",binding="${bankAddr}")
	String bankAddr;
	
	
	@Field(valueKey = "text",binding="${bankNum}")
	String bankNum;
	
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		if(value instanceof Map) {
			Map map = (Map)value;
			if (map.containsKey("accountName")) {
				registerBean("accountName", map.get("accountName"));
			}
			
			if (map.containsKey("bankName")) {
				registerBean("bankName", map.get("bankName"));
			}
			
			if (map.containsKey("bankAddr")) {
				registerBean("bankAddr", map.get("bankAddr"));
			}
			
			if (map.containsKey("bankNum")) {
				registerBean("bankNum", map.get("bankNum"));
			}
		}
	}
}
