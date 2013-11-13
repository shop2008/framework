/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.Stock;

/**
 * 股票搜索 Item
 * @author duzhen
 *
 */
@View(name="StockSearchItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.stock_search_layout_item")
public abstract class StockSearchItemView extends ViewBase implements IModelUpdater{
	
	private static final Trace log = Trace.register(StockSearchItemView.class);
	
	@Field(valueKey="text")
	String stockName;//股票名称
	DataField<String> stockNameField;
	
	@Field(valueKey="text")
	String stockCode;//股票代码
	DataField<String> stockCodeField;
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof Stock) {
			Stock stock = (Stock) data;
			stockName = stock.getName();
			stockNameField.setValue(stockName);
			stockCode = stock.getCode();
			stockCodeField.setValue(stockCode);
		}		
	}
//	/**
//	 * 事件处理-单击一条股票
//	 * 
//	 * */
//	@Command
//	String handleItemClick1(InputEvent event){
//		if(log.isDebugEnabled()) {
//			log.debug("StockSearchItemView : handle item click stockname:" + stockName);
//		}
//		return null;
//	}
}
