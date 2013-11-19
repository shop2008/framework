package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.client.utils.Utils;



@View(name="tradingRecordDetailItem")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.deal_record_layout_item")
public abstract class TradingRecordDetailItemView extends ViewBase implements IModelUpdater {

	
	TradingRecordBean tradingRecord;
	
	/**日期 ${tradingRecord!=null?tradingRecord.date:'--'}*/
	@Field(valueKey="text",binding="${tradingRecord!=null?utils.getDate(tradingRecord.date):'--'}")
	String date;

	/**时间*/
	@Field(valueKey="text",binding="${tradingRecord!=null?utils.getTime(tradingRecord.date):'--'}")
	String time;
	
	/**股票市场*/
	@Field(valueKey="text",binding="${tradingRecord!=null?tradingRecord.market:'--'}")
	String market;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${tradingRecord!=null?tradingRecord.code:'--'}")
	String code;
	
	/**成交方向*/
	@Field(valueKey="text",binding="${tradingRecord!=null?tradingRecord.describe:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(tradingRecord!=null && tradingRecord.describe=='买入成交')?'resourceId:color/red':((tradingRecord!=null && tradingRecord.describe=='卖出成交')?'resourceId:color/green':'resourceId:color/white')}")
	})
	String describe;
	
	/**成交价格*/
	@Field(valueKey="text",binding="${tradingRecord!=null?tradingRecord.price:'--'}")
	String price;
	
	/**成交量*/
	@Field(valueKey="text",binding="${tradingRecord!=null?tradingRecord.vol:'--'}${'股'}")
	String vol;
	
	long amount;// 成交金额
	long brokerage;// 佣金
	long tax;// 印花税
	long fee;// 过户费
	int day;// 1:表示t日,0:表示t+1日
	boolean beDone;// 订单是否完成，DONE为true；否则为false	
	
	@Bean
	Utils utils = Utils.getInstance();
	
	@OnShow
	void initData(){
		registerBean("utils", utils);
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof TradingRecordBean){
			registerBean("tradingRecord", value);
		}
	}

}
