/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;

/**
 * @author wangxuyang
 *
 */
@View(name="infoCenterItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.price_center_page_layout_item")
public abstract class InfoCenterItemView extends ViewBase implements IModelUpdater{
	
	@Bean
	StockTaxisBean stockTaxis;
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.name:'--'}")
	String stockName;//股票名称
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.code:'--'}")
	String stockCode;//股票代码
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.newprice:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(stockTaxis!=null && stockTaxis.risefallrate>0)?'resourceId:color/red':((stockTaxis!=null && stockTaxis.risefallrate<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String stockPrice;//股票最新价
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.risefallrate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(stockTaxis!=null && stockTaxis.risefallrate>0)?'resourceId:color/red':((stockTaxis!=null && stockTaxis.risefallrate<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String stockDeltaPercent;//股票涨跌幅
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockTaxisBean) {
			registerBean("stockTaxis", data);
		}		
	}
	/**
	 * 事件处理-单击一条股票
	 * 
	 * */
	@Command(description="",commandName="handleStockItemClick")
	String handleStockItemClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
		}
		return null;
	}

}
