/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * 模拟盘详情
 * 
 * @author duzhen
 * 
 */
@View(name = "TradingRecordDoneDetailPage", withToolbar = true, description="成交明细")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.chengjiao_operation_detail_page_layout")
public abstract class TradingRecordDoneDetailPage extends PageBase implements
		IModelUpdater {
	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(codeBean, marketBean)}")
	StockBaseInfo stockInfoBean;
		
	@Bean
	String marketBean;
	@Bean
	String codeBean;
	@Bean
	String describeBean;
	@Bean
	String dateBean;
	@Bean
	String priceBean;
	@Bean
	String volBean;
	@Bean
	String amountBean;
	@Bean
	String brokerageBean;
	@Bean
	String taxBean;
	@Bean
	String feeBean;

	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="formatUnit", value="元")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss")
	})
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Field(valueKey = "text", binding = "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name;

	@Field(valueKey = "text", binding = "${codeBean}")
	String code;

	@Field(valueKey = "text", binding = "${describeBean}")
	String describe;

	@Field(valueKey = "text", binding = "${dateBean}", converter="longTime2StringConvertor")
	String date;

	@Field(valueKey = "text", binding = "${priceBean}", converter="stockLong2StringConvertorNoSign")
	String price;

	@Field(valueKey = "text", binding = "${volBean}${'股'}")
	String vol;
	
	@Field(valueKey = "text", binding = "${amountBean}", converter="stockLong2StringConvertorNoSign")
	String amount;

	@Field(valueKey = "text", binding = "${brokerageBean}", converter="stockLong2StringConvertorNoSign")
	String brokerage;

	@Field(valueKey = "text", binding = "${taxBean}", converter="stockLong2StringConvertorNoSign")
	String tax;

	@Field(valueKey = "text", binding = "${feeBean}", converter="stockLong2StringConvertorNoSign")
	String fee;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@Override
	public void updateModel(Object data) {

		if (data instanceof Map) {
			Map result = (Map) data;
			for (Object key : result.keySet()) {
				Object tempt = (Object) result.get(key);

				if ("market".equals(key)) {
					if (tempt != null)
						registerBean("marketBean", tempt);
					else
						registerBean("marketBean", "");
				} else if ("code".equals(key)) {
					if (tempt != null)
						registerBean("codeBean", tempt);
					else
						registerBean("codeBean", "");
				} else if ("describe".equals(key)) {
					if (tempt != null)
						registerBean("describeBean", tempt);
					else
						registerBean("describeBean", "");
				} else if ("date".equals(key)) {
					if (tempt != null)
						registerBean("dateBean", tempt);
					else
						registerBean("dateBean", "");
				} else if ("price".equals(key)) {
					if (tempt != null)
						registerBean("priceBean", tempt);
					else
						registerBean("priceBean", "");
				} else if ("vol".equals(key)) {
					if (tempt != null)
						registerBean("volBean", tempt);
					else
						registerBean("volBean", "");
				} else if ("amount".equals(key)) {
					if (tempt != null)
						registerBean("amountBean", tempt);
					else
						registerBean("amountBean", "");
				} else if ("brokerage".equals(key)) {
					if (tempt != null)
						registerBean("brokerageBean", tempt);
					else
						registerBean("brokerageBean", "");
				} else if ("tax".equals(key)) {
					if (tempt != null)
						registerBean("taxBean", tempt);
					else
						registerBean("taxBean", "");
				} else if ("fee".equals(key)) {
					if (tempt != null)
						registerBean("feeBean", tempt);
					else
						registerBean("feeBean", "");
				}
			}
		}
	}
}
