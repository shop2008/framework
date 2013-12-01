package com.wxxr.mobile.stock.client.model;

import java.util.Date;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.common.SimpleSelectionImpl;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "StockKLineView", withToolbar=true, description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.stock_kline_view_layout")
public abstract class StockKLineView extends ViewBase implements ISelectionChangedListener{
	
	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	@Bean
	Long timeBean;
	//0:个股 or 1:买入
	@Bean
	int type;
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
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	//买入
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="10000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorBuy;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getDayline(codeBean, marketBean)}")
	LineListBean lineListBean;
	//K 线
	@Field(valueKey="options", binding="${lineListBean != null ? lineListBean.day_list : null}")
	List<StockLineBean> dayLineList;
	//Title数据
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	
	// Title
	@Field(valueKey = "text", binding = "${nameBean}")
	String name;

	@Field(valueKey = "text", binding = "${'('}${stockQuotationBean!=null?stockQuotationBean.code:'--'}${'.'}${stockQuotationBean!=null?stockQuotationBean.market:'--'}${')'}")
	String code;
	//个股界面
	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.newprice:'0'}", converter = "stockLong2StringConvertorNoSign", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}") })
	String newprice;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.change:'0'}", converter = "stockLong2StringConvertor", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}") })
	String change;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${stockQuotationBean!=null?stockQuotationBean.risefallrate:'0'}", converter = "stockLong2StringConvertorSpecial", attributes = { @Attribute(name = "textColor", value = "${stockQuotationBean==null?'resourceId:color/gray':stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}") })
	String risefallRate;

	@Field(valueKey = "imageURI", visibleWhen = "${stockQuotationBean!=null&&type == 0}", attributes = { @Attribute(name = "imageURI", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:drawable/up_arrows':'resourceId:drawable/down_arrows'}") })
	String newpriceIcon;

	@Field(valueKey = "text", visibleWhen = "${type == 0}", binding = "${timeBean}", converter = "longTime2StringConvertor")
	String time;
	//买入界面
	@Field(valueKey = "text", visibleWhen = "${type == 1}", binding = "${stockQuotationBean!=null?stockQuotationBean.handrate:'0'}", converter = "stockLong2StringConvertorBuy")
	String handrate;
	@Field(valueKey = "text", visibleWhen = "${type == 1}", binding = "${stockQuotationBean!=null?stockQuotationBean.lb:'0'}", converter = "stockLong2StringConvertorNoSign")
	String lb;
	@Field(valueKey = "text", visibleWhen = "${type == 1}", binding = "${stockQuotationBean!=null?stockQuotationBean.secuamount:'0'}", converter = "stockLong2StringAutoUnitConvertor")
	String secuamount;
	@Field(valueKey = "text", visibleWhen = "${type == 1}", binding = "${stockQuotationBean!=null?stockQuotationBean.capital:'0'}", converter = "stockLong2StringAutoUnitConvertor")
	String capital;
//	@Override
//	public void updateModel(Object data) {
//		if (data instanceof ArticleBean) {
//			registerBean("nameBean", "鸿达兴业");
//			registerBean("codeBean", "100100");
//			registerBean("marketBean", "SH");
//			registerBean("timeBean", new Date().getTime());
//		}
//	}
	
	@OnCreate
	void registerSelectionListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().addSelectionListener("BuyStockDetailPage", this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
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
	
	@OnShow
	void initStockData() {
		registerBean("nameBean", "鸿达兴业");
		registerBean("type", 1);
//		registerBean("marketBean", "SH");
		registerBean("timeBean", new Date().getTime());
	}
}
