package com.wxxr.mobile.stock.client.model;




import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.StringTime2StringConvertor;

@View(name="InfoNoticesItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.notice_info_item_layout")
public abstract class InfoNoticeItemView extends ViewBase implements IModelUpdater {

	@Bean
	PullMessageBean message;
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	@Field(valueKey="visible", binding="${message.read==false}")
	boolean readFlag;
	
	@Field(valueKey="text", binding="${message.createDate}", converter="stime2StrConvertor")
	String time;
	
	@Field(valueKey="text", binding="${message.title}")
	String title;
	
	@Field(valueKey="text", binding="${message.message}")
	String content;
	
	
	
	@Convertor(
			params={@Parameter(name="format", value="HH:mm"),
					@Parameter(name="nullString", value="--:--")
			}
			)
	
	StringTime2StringConvertor stime2StrConvertor;
	
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof PullMessageBean) {
			registerBean("message", value);
			message = (PullMessageBean) value;
		}
	}
	
}
