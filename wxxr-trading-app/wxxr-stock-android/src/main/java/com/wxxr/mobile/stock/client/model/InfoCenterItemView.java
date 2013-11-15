/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockBasicMarketInfoBean;

/**
 * @author wangxuyang
 *
 */
@View(name="infoCenterItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.price_center_page_layout_item")
public abstract class InfoCenterItemView extends ViewBase implements IModelUpdater{
	@Field(valueKey="text")
	String stockName;//股票名称
	DataField<String> stockNameField;
	
	@Field(valueKey="text")
	String stockCode;//股票代码
	DataField<String> stockCodeField;
	
	@Field(valueKey="text")
	String stockPrice;//股票最新价
	DataField<String> stockPriceField;
	
	@Field(valueKey="text")
	String stockDeltaPercent;//股票涨跌幅
	DataField<String> stockDeltaPercentField;
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockBasicMarketInfoBean) {
			StockBasicMarketInfoBean stock = (StockBasicMarketInfoBean) data;
			stockName = stock.getName();
			stockNameField.setValue(stockName);
			stockCode = stock.getCode();
			stockCodeField.setValue(stockCode);
			stockPrice = String.valueOf(stock.getCurrentPrice());
			stockPriceField.setValue(stockPrice);
			stockDeltaPercent =String.format("%.2f", (stock.getCurrentPrice()-stock.getTodayInitPrice())/stock.getCurrentPrice()*100)+"%";
			stockDeltaPercentField.setValue(stockDeltaPercent);
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
