/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoWrapper;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;

/**
 * 股票搜索 Item
 * @author duzhen
 *
 */
@View(name="StockSearchItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.stock_search_layout_item")
public abstract class StockSearchItemView extends ViewBase implements IModelUpdater{
	
	@Bean(type = BindingType.Service)
	IOptionStockManagementService optionStockService;
	
	StockBaseInfoWrapper stockInfo;
	
	@Field(valueKey="text", binding = "${stockInfo!=null?stockInfo.getName():'--'}")
	String stockName;//股票名称
	
	@Field(valueKey="text", binding = "${stockInfo!=null?stockInfo.getCode():'--'}")
	String stockCode;//股票代码
	
	@Field(valueKey="visible",visibleWhen="${isAdd}")
	boolean added;
	
	@Field(valueKey="visible",visibleWhen="${!isAdd}")
	boolean unadd;
	
	@Bean
	boolean isAdd;
	
	//添加自选股
	@Command
	String addStock(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(optionStockService!=null){
				if(stockInfo!=null){
					if(stockInfo.getCode()!=null && stockInfo.getMc()!=null){
						optionStockService.add(stockInfo.getCode(), stockInfo.getMc());
						this.isAdd = true;
						registerBean("isAdd", this.isAdd);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockBaseInfoWrapper) {
			this.stockInfo = (StockBaseInfoWrapper) data;
			if(stockInfo.getAdded()){
				this.isAdd = true;
			}else{
				this.isAdd = false;
			}
			registerBean("isAdd", this.isAdd);
			registerBean("stockInfo",data);
		}		
	}
}
