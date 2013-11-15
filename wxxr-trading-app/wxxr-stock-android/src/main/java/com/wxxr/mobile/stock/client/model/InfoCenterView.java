/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockBasicMarketInfoBean;

/**
 * @author neillin
 * 
 */
@View(name = "infoCenter", description = "行情中心")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.price_center_page_layout")
public abstract class InfoCenterView extends ViewBase {
	//=====上证======
	@Field(valueKey="text")
	String shPrice;//上证指数
	DataField<String> shPriceField;
	
	@Field(valueKey="text")
	String shDelta;//上证指数涨跌量
	DataField<String> shDeltaField;
	
	@Field(valueKey="text")
	String shDeltaPer;//上证指数涨跌幅
	DataField<String> shDeltaPerField;
	
	//====深证=======
	@Field(valueKey="text")
	String szPrice;//深证指数
	DataField<String> szPriceField;
	
	@Field(valueKey="text")
	String szDelta;//深证指数涨跌量
	DataField<String> szDeltaField;
	
	@Field(valueKey="text")
	String szDeltaPer;//深证指数涨跌幅
	DataField<String> szDeltaPerField;

	// 股票列表
	@Field(valueKey = "options")
	List<StockBasicMarketInfoBean> stockInfos;
	DataField<List> stockInfosField;

	
	@OnShow
	protected void updateInfo() {
		//TODO set sh
		shPrice = "2012.03";
		shPriceField.setValue(shPrice);
		
		shDelta = "2.02";
		shDeltaField.setValue(shDelta);
		
		shDeltaPer ="1.01%";
		shDeltaPerField.setValue(shDeltaPer);
		//TODO set sz
		szPrice = "8012.03";
		shPriceField.setValue(szPrice);
		
		shDelta = "8.02";
		shDeltaField.setValue(szDelta);
		
		szDeltaPer ="1.21%";
		szDeltaPerField.setValue(szDeltaPer);
		//TODO set stock list
		stockInfos = new ArrayList<StockBasicMarketInfoBean>();
		StockBasicMarketInfoBean st = new StockBasicMarketInfoBean();
		st.setCode("600521");
		st.setName("华海药业");
		st.setCurrentPrice(11.88f);
		st.setTodayInitPrice(12.31f);
		stockInfos.add(st);
		stockInfosField.setValue(stockInfos);
	}

	/**
	 * 事件处理-单击深证指数 
	 * 
	 * */
	@Command(description="",commandName="handleSZClick")
	String handleSZClick(InputEvent event){
		//TODO
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
		}
		return null;
	}
	/**
	 * 事件处理 -单击上证指数 
	 * 
	 * */
	@Command(description="",commandName="handleSHClick")
	String handleSHClick(InputEvent event){
		//TODO
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
		}
		return null;
	}
	/**
	 * 事件处理- 单击涨跌幅标题（股票列表排序-按涨跌幅）
	 * 
	 * */
	@Command(description="",commandName="orderByPercent")
	String orderByPercent(InputEvent event){
		//TODO
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
		}
		return null;
	}
	/**
	 * 事件处理- 单击涨跌幅标题（股票列表排序-按当前价）
	 * 
	 * */
	@Command(description="",commandName="orderByPrice")
	String orderByPrice(InputEvent event){
		//TODO
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
		}
		return null;
	}
	
}
