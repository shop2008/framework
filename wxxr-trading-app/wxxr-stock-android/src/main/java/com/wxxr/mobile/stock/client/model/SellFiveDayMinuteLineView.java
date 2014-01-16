/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
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
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author xijiadeng
 *
 */
@View(name="SellFiveDayMinuteLineView")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.sell_five_day_minute_line_view")
public abstract class SellFiveDayMinuteLineView extends ViewBase implements
		IModelUpdater, ISelectionChangedListener {
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getFiveDayMinuteline(codeBean,marketBean)}")
	BindableListWrapper<StockMinuteKBean> fiveDayMinuteBean;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(codeBean,marketBean)}")
	StockQuotationBean quotationBean;	
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(codeBean!=null?codeBean:'', marketBean!=null?marketBean:'')}")
	StockBaseInfo stockInfoBean;

	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
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
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor1;
	
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	
	@Convertor(params={
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial1;	
	
	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	
	@Bean
	long buyPrice;
	
	@Bean
	String stockType = "1"; //0-指数，1-个股	
	
	/**股票名称*/
	@Field(valueKey="text",binding="${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name1;
	
	/**股票代码+市场代码*/
	@Field(valueKey="text",binding="${'('}${(quotationBean!=null && quotationBean.code!=null)?quotationBean.code:'--'}${'.'}${(quotationBean!=null && quotationBean.market!=null)?quotationBean.market:'--'}${')'}")
	String codeAndmarket1;
	
	/**换手率*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.handrate:null}", converter = "stockLong2StringConvertorSpecial")
	String handrate;
	
	/**量比*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.lb:null}", converter = "stockLong2StringAutoUnitConvertor")
	String lb;
	
	/**成交额*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.secuamount:null}", converter = "stockLong2StringAutoUnitConvertor1")
	String secuamount;
	
	/**流通盘*/
	@Field(valueKey = "text", binding = "${quotationBean!=null?quotationBean.capital:null}", converter = "stockLong2StringAutoUnitConvertor2")
	String capital;

	
	@Field(valueKey="options",binding="${fiveDayMinuteBean!=null?fiveDayMinuteBean.getData(true):null}",attributes={
			@Attribute(name="stockType",value="${stockType}"),
			@Attribute(name="buyPrice",value="${buyPrice}"),
			@Attribute(name = "stockBorderColor",value="#535353"),
			@Attribute(name = "stockUpColor",value="#BA2514"),
			@Attribute(name = "stockDownColor",value="#3C7F00"),
			@Attribute(name = "stockAverageLineColor",value="#FFE400"),
			@Attribute(name = "stockCloseColor",value="#FFFFFF")
	}, upateAsync=true)
	List<StockMinuteKBean> fiveDayMinute;
	DataField<List> fiveDayMinuteField;
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		infoCenterService.getStockQuotation(codeBean,marketBean);
		stockInfoSyncService.getStockBaseInfoByCode(codeBean, marketBean);
		fiveDayMinuteField.getDomainModel().doEvaluate();
		return null;
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
				this.buyPrice = stockSelection.getBuyPrice();
//				StockQuotationBean stockQuo = infoCenterService.getStockQuotation(codeBean,marketBean);
//				this.quotationBean = stockQuo;
//				BindableListWrapper<StockMinuteKBean> minuteBean = infoCenterService.getFiveDayMinuteline(codeBean,marketBean);
//				this.fiveDayMinuteBean = minuteBean;
//				StockBaseInfo stockInfo = stockInfoSyncService.getStockBaseInfoByCode(codeBean, marketBean);
//				this.stockInfoBean = stockInfo;
				
//				registerBean("quotationBean", this.quotationBean);
//				registerBean("fiveDayMinuteBean", this.fiveDayMinuteBean);
//				registerBean("stockInfoBean", this.stockInfoBean);
			}
			registerBean("codeBean", this.codeBean);
			registerBean("nameBean", this.nameBean);
			registerBean("marketBean", this.marketBean);
			registerBean("buyPrice", this.buyPrice);
			if(("000001".equals(this.codeBean) && "SH".equals(this.marketBean)) || ("399001".equals(this.codeBean) && "SZ".equals(this.marketBean))){
				this.stockType = "0";
			}			
		}
	}

	@Override
	public void updateModel(Object value) {
	}
}
