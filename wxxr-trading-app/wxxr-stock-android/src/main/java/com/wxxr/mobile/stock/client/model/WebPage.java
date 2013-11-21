package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="webPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.web_page_layout")
public abstract class WebPage extends PageBase implements IModelUpdater{

	static Trace log= Trace.getLogger(WebPage.class);
	@Bean
	String tempUrl = "#";
	
	@Field(valueKey="webUrl",binding="${tempUrl!=null?tempUrl:'#'}")
	String webUrl;
	
	
	
	@OnShow
	void initUrl(){
		registerBean("tempUrl", tempUrl);
	}
	
	@Override
	public void updateModel(Object value) {
	    if (value instanceof Map) {
	        Map temp = (Map)value;
	        for (Object key : temp.keySet()) {
	            String tempt = (String)temp.get(key);
	            if (tempt != null && "result".equals(key)) {
	            	log.info("WebPage webUrl=" + tempt);
	            	this.webUrl = tempt;
	            	this.tempUrl = tempt;
	            	registerBean("tempUrl", tempt);
	            }
	        }
	    }
	}
}
