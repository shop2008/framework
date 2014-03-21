package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name="WarnAlertDailog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.warn_alert_dailog")
public abstract class WarnAlertDailog extends ViewBase implements IModelUpdater {

	@Bean
	String message;
	
	@Field(valueKey="text",binding="${message}")
	String warnMsg;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map result = (Map) value;
			for (Object key : result.keySet()) {
				if("result".equals(key)){
					this.message = (String) result.get(key);
					registerBean("message", this.message);
				}
			}
		}
	}
}
