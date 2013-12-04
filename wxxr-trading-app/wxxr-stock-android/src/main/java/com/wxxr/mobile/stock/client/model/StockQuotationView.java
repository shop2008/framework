package com.wxxr.mobile.stock.client.model;

import java.util.Date;
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
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.common.SimpleSelectionImpl;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "StockQuotationView")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.stock_quotation_view_layout")
public abstract class StockQuotationView extends ViewBase implements ISelectionChangedListener {

	private static final Trace log = Trace.register(StockQuotationView.class);
	
	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	@Bean
	Long timeBean;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss")
	})
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%+10.2f"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	//Title
	@Field(valueKey = "text", binding= "${nameBean}")
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
	
	@Field(valueKey = "text", binding= "${timeBean}",converter="longTime2StringConvertor")
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
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume5:'0'}")
	String sellVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume4:'0'}")
	String sellVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume3:'0'}")
	String sellVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume2:'0'}")
	String sellVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume1:'0'}")
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
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume5:'0'}")
	String buyVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume4:'0'}")
	String buyVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume3:'0'}")
	String buyVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume2:'0'}")
	String buyVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume1:'0'}")
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
		// TODO Auto-generated method stub
//		registerBean("nameBean", "鸿达兴业");
//		registerBean("codeBean", "100100");
//		registerBean("marketBean", "SH");
		registerBean("timeBean", new Date().getTime());
	}
	
	@OnCreate
	void registerSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		ISelection selection = service.getSelection("BuyStockDetailPage");
		if(selection != null)
			selectionChanged("BuyStockDetailPage", selection);
		service.addSelectionListener("BuyStockDetailPage", this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection == null)
			return;
		SimpleSelectionImpl impl = (SimpleSelectionImpl)selection;
		String[] stockInfos = (String[])impl.getSelected();
		this.codeBean = stockInfos[0];
		this.nameBean = stockInfos[1];
		this.marketBean = stockInfos[2];
		registerBean("codeBean", this.codeBean);
		registerBean("nameBean", this.nameBean);
		registerBean("marketBean", this.marketBean);
		infoCenterService.getStockQuotation(codeBean, marketBean);
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
