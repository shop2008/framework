/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockBaseInfoBean;

/**
 * 股票搜索 Item
 * @author duzhen
 *
 */
@View(name="StockSearchItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.stock_search_layout_item")
public abstract class StockSearchItemView extends ViewBase implements IModelUpdater{
	
	StockBaseInfoBean stock;
	
	@Field(valueKey="text", binding = "${stock!=null?stock.name:'--'}")
	String stockName;//股票名称
	
	@Field(valueKey="text", binding = "${stock!=null?stock.code:'--'}")
	String stockCode;//股票代码
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockBaseInfoBean) {
			registerBean("stock",data);
		}		
	}
}
