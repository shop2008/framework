/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.Float2PercentStringConvertor;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;

/**
 * 模拟盘详情
 * 
 * @author duzhen
 * 
 */
@View(name = "TBuyStockInfoPage", withToolbar = true, description="模拟盘详情")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_info_layout")
public abstract class TBuyStockInfoPage extends PageBase implements
		IModelUpdater {

	@Bean
	String acctId;

	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss")
	})
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertorInt;
	
	@Convertor(params={
			@Parameter(name="format",value="-%.0f")
	})
	Float2PercentStringConvertor float2PercentStringConvertor;
	
	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${tradingService.getTradingAccountInfo(acctId)}")
	TradingAccountBean tradingBean;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.id:'--'}")
	String id;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.buyDay:'-1'}", converter = "longTime2StringConvertor")
	String buyDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.sellDay:'-1'}", converter = "longTime2StringConvertor")
	String sellDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.applyFee:''}", converter = "stockLong2StringAutoUnitConvertorInt")
	String applyFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.lossLimit:''}", converter = "float2PercentStringConvertor")
	String lossLimit;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@OnShow
	protected void initStockInfo() {
		registerBean("acctId", acctId);
	}

	
	@Override
	public void updateModel(Object data) {
		if (data instanceof Map) {
			Map result = (Map) data;
			for (Object key : result.keySet()) {
				if("result".equals(key)){
					Object tempt = result.get(key);
					registerBean("acctId", tempt);
				}
			}
		}
	}
}
