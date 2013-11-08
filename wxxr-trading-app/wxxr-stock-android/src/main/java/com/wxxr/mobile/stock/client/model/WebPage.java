package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="webPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.web_page_layout")
public abstract class WebPage extends PageBase implements IModelUpdater{

	@Field(valueKey="webUrl")
	String webUrl;
	DataField<String> webUrlField;
	
	
	@Override
	protected void onShow(IBinding<IView> binding) {
		
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map temp = (Map) value;
			for(Object key:temp.keySet()){
				String url = (String) temp.get(key);
				if(url!=null && "webUrl".equals(key)){
					this.webUrl = url;
					this.webUrlField.setValue(this.webUrl);
				}
			}
		}
	}
}
