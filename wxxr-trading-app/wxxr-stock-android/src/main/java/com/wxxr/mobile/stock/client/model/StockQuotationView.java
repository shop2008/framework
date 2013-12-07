package com.wxxr.mobile.stock.client.model;

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
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.BTTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "StockQuotationView", description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.stock_quotation_view_layout")
public abstract class StockQuotationView extends ViewBase implements ISelectionChangedListener {

	private static final Trace log = Trace.register(StockQuotationView.class);
	
	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(codeBean, marketBean)}")
	StockBaseInfo stockInfoBean;
		
	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss"),
			@Parameter(name="nullString",value="--")
	})
	BTTime2StringConvertor btTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorInt;
	
	@Convertor(params={
			@Parameter(name="format",value="%+.2f"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	//Title
	@Field(valueKey = "text", binding= "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name;
	
	@Field(valueKey = "text", binding= "${'('}${stockQuotationBean!=null?stockQuotationBean.code:'--'}${'.'}${stockQuotationBean!=null?stockQuotationBean.market:'--'}${')'}")
	String code;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.newprice:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String newprice;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.change:'0'}", converter = "stockLong2StringConvertor", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String change;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.risefallrate:'0'}", converter = "stockLong2StringConvertorSpecial", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String risefallRate;
	
	@Field(valueKey = "imageURI", visibleWhen= "${stockQuotationBean!=null}", attributes={
			@Attribute(name = "imageURI", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:drawable/up_arrows':'resourceId:drawable/down_arrows'}")
			})
	String newpriceIcon;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.datetime:null}", converter="btTime2StringConvertor")
	String time;
	//卖
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice5:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice4:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice3:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice2:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice1:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice1;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume5:'0'}", converter="stockLong2StringConvertorInt")
	String sellVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume4:'0'}", converter="stockLong2StringConvertorInt")
	String sellVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume3:'0'}", converter="stockLong2StringConvertorInt")
	String sellVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume2:'0'}", converter="stockLong2StringConvertorInt")
	String sellVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume1:'0'}", converter="stockLong2StringConvertorInt")
	String sellVolume1;
	//买
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice5:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice4:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice3:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice2:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice1:'0'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice1;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume5:'0'}", converter="stockLong2StringConvertorInt")
	String buyVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume4:'0'}", converter="stockLong2StringConvertorInt")
	String buyVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume3:'0'}", converter="stockLong2StringConvertorInt")
	String buyVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume2:'0'}", converter="stockLong2StringConvertorInt")
	String buyVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume1:'0'}", converter="stockLong2StringConvertorInt")
	String buyVolume1;
	//买卖盘
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellsum:'0'}", converter = "stockLong2StringAutoUnitConvertor", attributes={
			@Attribute(name = "textColor", value = "resourceId:color/green")
			})
	String sellSum;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buysum:'0'}", converter = "stockLong2StringAutoUnitConvertor", attributes={
			@Attribute(name = "textColor", value = "resourceId:color/red")
			})
	String buySum;
	
	@Field(valueKey = "text", visibleWhen= "${stockQuotationBean!=null}")
	String quotation;
	
	@Field(valueKey = "text", visibleWhen= "${stockQuotationBean==null}")
	String dataFailed;
	
	@OnShow
	void initBeans() {
	}
	
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
			StockSelection stockSelection = (StockSelection) selection;
			if(stockSelection!=null){
				this.codeBean = stockSelection.getCode();
				this.nameBean = stockSelection.getName();
				this.marketBean = stockSelection.getMarket();
			}
			registerBean("codeBean", this.codeBean);
			registerBean("nameBean", this.nameBean);
			registerBean("marketBean", this.marketBean);
		}
	}
	
//	@Override
//	public void updateModel(Object value) {
//		if (value instanceof Map) {
//			Map temp = (Map) value;
//			for (Object key : temp.keySet()) {
//				Object tempt = temp.get(key);
//				if (tempt != null && "codeBean".equals(key)) {
//					if (tempt instanceof String) {
//						this.codeBean = (String) tempt;
//					}
//					registerBean("codeBean", this.codeBean);
//				} else if (tempt != null && "nameBean".equals(key)) {
//					if (tempt instanceof String) {
//						this.nameBean = (String) tempt;
//					}
//					registerBean("nameBean", this.nameBean);
//				} else if (tempt != null && "marketBean".equals(key)) {
//					if (tempt instanceof String) {
//						this.marketBean = (String) tempt;
//					}
//					registerBean("marketBean", this.marketBean);
//				}
//				log.debug("updateModel: codeBean:"
//						+ codeBean + "nameBean:" + nameBean + "marketBean:" + marketBean);
//			}
//		}
//	}
}
