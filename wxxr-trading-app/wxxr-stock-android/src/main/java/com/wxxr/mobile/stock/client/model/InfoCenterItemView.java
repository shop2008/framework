/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author wangxuyang
 *
 */
@View(name="infoCenterItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.price_center_page_layout_item")
public abstract class InfoCenterItemView extends ViewBase implements IModelUpdater{
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(stockTaxis.code, stockTaxis.market)}")
	StockBaseInfo stockInfoBean;
	
	@Bean
	StockQuotationBean stockTaxis;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertor;

	@Convertor(params={
			@Parameter(name="format",value="%+.2f%%"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Field(valueKey="text",binding="${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String stockName;//股票名称
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.code:'--'}")
	String stockCode;//股票代码
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.newprice:null}",converter="stockLong2StringConvertor",attributes={
			@Attribute(name = "textColor", value = "${(stockTaxis!=null && stockTaxis.risefallrate>0)?'resourceId:color/red':((stockTaxis!=null && stockTaxis.risefallrate<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String stockPrice;//股票最新价
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.risefallrate:null}",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "backgroundColor", value = "${(stockTaxis!=null && stockTaxis.risefallrate>0)?'resourceId:color/red':((stockTaxis!=null && stockTaxis.risefallrate<0)?'resourceId:color/green_bg':'resourceId:color/gray')}")
//			@Attribute(name = "textColor", value = "${(stockTaxis!=null && stockTaxis.risefallrate>0)?'resourceId:color/red':((stockTaxis!=null && stockTaxis.risefallrate<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String stockDeltaPercent;//股票涨跌幅
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockQuotationBean) {
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
