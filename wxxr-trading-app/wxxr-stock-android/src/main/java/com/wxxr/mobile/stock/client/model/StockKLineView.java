package com.wxxr.mobile.stock.client.model;

import java.util.Date;
import java.util.List;

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
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.BTTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "StockKLineView", description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.stock_kline_view_layout")
public abstract class StockKLineView extends ViewBase implements ISelectionChangedListener{
	
	private static final Trace log = Trace.register(StockKLineView.class);
	
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
	//0:个股 or 1:买入
	@Bean
	int type = 1;
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss"),
			@Parameter(name="nullString",value="--")
	})
	BTTime2StringConvertor btTime2StringConvertor;
	
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
			@Parameter(name="multiple", value="1000")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	//买入
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorBuy;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getDayStockline(codeBean, marketBean)}")
	BindableListWrapper<StockLineBean> lineListBean;
	//K 线
	@Field(valueKey="options", binding="${lineListBean != null ? lineListBean.getData() : null}")
	List<StockLineBean> dayLineList;
	//Title数据
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	
	// Title
	@Field(valueKey = "text", binding= "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name;

	@Field(valueKey = "text", binding = "${'('}${stockQuotationBean!=null?stockQuotationBean.code:'--'}${'.'}${stockQuotationBean!=null?stockQuotationBean.market:'--'}${')'}")
	String code;
	//个股界面
	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.newprice:'0'}", converter = "stockLong2StringConvertorNoSign", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/stock_up':'resourceId:color/stock_down'}") })
	String newprice;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.change:'0'}", converter = "stockLong2StringConvertor", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/stock_up':'resourceId:color/stock_down'}") })
	String change;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.risefallrate:'0'}", converter = "stockLong2StringConvertorSpecial", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/stock_up':'resourceId:color/stock_down'}") })
	String risefallRate;

	@Field(valueKey = "imageURI", visibleWhen = "${stockQuotationBean!=null&&type == 0}", attributes = { @Attribute(name = "imageURI", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:drawable/up_arrows':'resourceId:drawable/down_arrows'}") })
	String newpriceIcon;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.datetime:null}", converter = "btTime2StringConvertor")
	String time;
	//买入界面
	@Field(valueKey = "text", binding = "${stockQuotationBean!=null?stockQuotationBean.handrate:'0'}", converter = "stockLong2StringConvertorBuy")
	String handrate;
	@Field(valueKey = "text", binding = "${stockQuotationBean!=null?stockQuotationBean.lb:'0'}", converter = "stockLong2StringConvertorNoSign")
	String lb;
	@Field(valueKey = "text", binding = "${stockQuotationBean!=null?stockQuotationBean.secuamount:'0'}", converter = "stockLong2StringAutoUnitConvertor")
	String secuamount;
	@Field(valueKey = "text", binding = "${stockQuotationBean!=null?stockQuotationBean.capital:'0'}", converter = "stockLong2StringAutoUnitConvertor")
	String capital;
	@Field(valueKey = "text", visibleWhen = "${type == 1}")
	String buyRateLayout;
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
				this.type = stockSelection.getType();
			}
			registerBean("codeBean", this.codeBean);
			registerBean("nameBean", this.nameBean);
			registerBean("marketBean", this.marketBean);
			registerBean("type", this.type);
			if(infoCenterService != null)
				infoCenterService.getDayStockline(codeBean, marketBean);
		}
	}
	
	@OnShow
	void initStockData() {
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
