package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.DrawMoneyRecordBean;
import com.wxxr.mobile.stock.client.utils.DateFormatConvertor;
import com.wxxr.mobile.stock.client.utils.StringTime2StringConvertor;


@View(name = "ApplyMoneyRecordItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.apply_money_record_item_layout")
public abstract class ApplyMoneyRecordItemView extends ViewBase implements IModelUpdater {

	/**申请日期*/
	@Field(valueKey="text", binding="${dataBean!=null?dataBean.drawDate:null}", converter="dateConvertor")
	String applyDate;
	
	/**申请时间*/
	@Field(valueKey="text", binding="${dataBean!=null?dataBean.drawDate:null}", converter="timeConvertor")
	String applyTime;
	
	/**申请数量*/
	@Field(valueKey="text", binding="${dataBean!=null?dataBean.drawAmount:null}")
	String applyAmount;
	
	/**审请状态*/
	@Field(valueKey="text", binding="${dataBean!=null?dataBean.drawState:null}")
	String applyStatus;

	@Convertor(params={@Parameter(name="subString1",value="yyyy年MM月dd日"), @Parameter(name="nullString",value="--年--月--日")})
	DateFormatConvertor dateConvertor;
	
	@Convertor(params={@Parameter(name="subString2",value="HH:mm:ss"), @Parameter(name="nullString",value="--:--:--")})
	DateFormatConvertor timeConvertor;
	@Bean
	DrawMoneyRecordBean dataBean;
	@Override
	public void updateModel(Object value) {
		if (value instanceof DrawMoneyRecordBean) {
			registerBean("dataBean", value);
		}
	}
}
