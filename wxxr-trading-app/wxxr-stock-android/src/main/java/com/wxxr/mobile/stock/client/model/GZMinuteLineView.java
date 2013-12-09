package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.BTTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="GZMinuteLineView",description="个股界面")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.gegu_zhishu_minute_line_layout")
public abstract class GZMinuteLineView extends ViewBase implements IModelUpdater,ISelectionChangedListener{
	static Trace log = Trace.getLogger(GZMinuteLineView.class);
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService; 
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(codeBean,marketBean)}")
	StockQuotationBean quotationBean;	
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getMinuteline(map)}")
	StockMinuteKBean minute;
	@Convertor(params={
			@Parameter(name="multiple",value="1000f"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000f"),
			@Parameter(name="format",value="%+.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor1;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss"),
			@Parameter(name="nullString",value="--")
	})
	BTTime2StringConvertor btTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="(%.2f%%)"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	
	@Convertor(params={
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial1;
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor2;
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor3;
	
	@Bean
	Map<String, Object> map;
	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	
	@Bean
	String stockType = "1"; //0-指数，1-个股
	
	/**
	 * 0-显示：最新价、涨跌幅、涨跌额
	 * 1-显示：换手、量比、成交额、liu'tong
	 * */
	@Bean
	int minuteHeaderType = 0; 
	
	@Field(valueKey="visible",visibleWhen="${minuteHeaderType == 0}")
	boolean minuteHeaderOne = true;
	
	@Field(valueKey="visible",visibleWhen="${minuteHeaderType == 1}")
	boolean minuteHeaderTwo = false;
	
	/**箭头*/
	@Field(valueKey="text",enableWhen="${quotationBean!=null && quotationBean.newprice > quotationBean.close}",visibleWhen="${quotationBean!=null && quotationBean.newprice != quotationBean.close}")
	String arrows;	
	
	/**股票名称*/
	@Field(valueKey="text",binding="${nameBean!=null?nameBean:'--'}")
	String name;
	
	/**股票代码+市场代码*/
	@Field(valueKey="text",binding="${'('}${(quotationBean!=null && quotationBean.code!=null)?quotationBean.code:'--'}${'.'}${(quotationBean!=null && quotationBean.market!=null)?quotationBean.market:'--'}${')'}")
	String codeAndmarket;
	
	/**涨跌幅*/
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.risefallrate!=null)?quotationBean.risefallrate:null}",converter="stockLong2StringConvertorSpecial1",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.newprice > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.newprice < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String risefallrate;
	
	/**涨跌额*/
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.change!=null)?quotationBean.change:null}",converter="stockLong2StringAutoUnitConvertor1",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.newprice > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.newprice < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String change;
	
	/**最新价*/
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.newprice!=null)?quotationBean.newprice:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.newprice > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.newprice < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String newprice;
	
	/**时间*/
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.datetime!=null)?quotationBean.datetime:null}",converter="btTime2StringConvertor")
	String datetime;
	
	/**换手率*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.handrate:null}", converter = "stockLong2StringConvertorSpecial")
	String handrate;
	
	/**量比*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.lb:null}", converter = "stockLong2StringAutoUnitConvertor")
	String lb;
	
	/**成交额*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.secuamount:null}", converter = "stockLong2StringAutoUnitConvertor3")
	String secuamount;
	
	/**流通盘*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.capital:null}", converter = "stockLong2StringAutoUnitConvertor2")
	String capital;
	
	@Field(valueKey="options",binding="${minute!=null?minute.list:null}",attributes={
			@Attribute(name = "stockClose", value = "${minute!=null?minute.close:'0'}"),
			@Attribute(name = "stockDate", value = "${minute!=null?minute.date:'0'}"),
			@Attribute(name="stockType",value="${stockType}"),
			@Attribute(name = "stockBorderColor",value="#535353"),
			@Attribute(name = "stockUpColor",value="#BA2514"),
			@Attribute(name = "stockDownColor",value="#3C7F00"),
			@Attribute(name = "stockAverageLineColor",value="#FFE400"),
			@Attribute(name = "stockCloseColor",value="#FFFFFF")
	})
	List<StockMinuteLineBean> stockMinuteData;
	
	@OnCreate
	void registerSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		selectionChanged("",service.getSelection(StockSelection.class));
		service.addSelectionListener(this);
	}
	
	@OnDestroy
	void removeSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		service.removeSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof StockSelection){
			HashMap<String, Object> minuteMap = new HashMap<String, Object>();
			StockSelection stockSelection = (StockSelection) selection;
			if(stockSelection!=null){
				this.codeBean = stockSelection.getCode();
				this.nameBean = stockSelection.getName();
				this.marketBean = stockSelection.getMarket();
				this.minuteHeaderType = stockSelection.getType();
			}
			registerBean("codeBean", this.codeBean);
			registerBean("nameBean", this.nameBean);
			registerBean("marketBean", this.marketBean);
			if(this.codeBean!=null && this.marketBean!=null){
				minuteMap.put("code", this.codeBean);
				minuteMap.put("market", this.marketBean);
				this.map = minuteMap;
				registerBean("map", this.map);
			}
			if(("000001".equals(this.codeBean) && "SH".equals(this.marketBean)) || ("399001".equals(this.codeBean) && "SZ".equals(this.marketBean))){
				this.stockType = "0";
			}			
		}
	}
	
	@Override
	public void updateModel(Object value) {
		
	}
}
