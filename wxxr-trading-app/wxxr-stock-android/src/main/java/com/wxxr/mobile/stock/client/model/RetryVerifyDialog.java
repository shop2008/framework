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

@View(name="RetryVerifyDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.error_verify_dialog")
public abstract class RetryVerifyDialog extends ViewBase implements IModelUpdater {

	String type;
	private String moneyAmount;
	
	@Command(navigations={@Navigation(on="InputPswDialog", showDialog="InputPswDialog", closeCurrentView=true)})
	CommandResult reVerify(InputEvent event) {
		hide();
		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("moneyAmount", moneyAmount);
		result.setPayload(map);
		result.setResult("InputPswDialog");
		return result;
	}
	
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
	
	
	
	/*@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "type".equals(key)) {
					if (tempt instanceof String) {
						type = (String) tempt;
					}
				}
			}
		}
	}*/
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "type".equals(key)) {
					if (tempt instanceof String) {
						type = (String) tempt;
					}
				}
				
				if (tempt != null && "moneyAmount".equals(key)) {
					if (tempt instanceof String) {
						moneyAmount = (String) tempt;
					}
				}
			}
		}
	}
}
