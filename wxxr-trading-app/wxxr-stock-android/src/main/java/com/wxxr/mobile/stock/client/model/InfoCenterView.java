/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisListBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;

/**
 * @author neillin
 * 
 */
@View(name = "infoCenter", description = "行情中心")
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.price_center_page_layout")
public abstract class InfoCenterView extends ViewBase implements IModelUpdater{
	
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getQuotations()}")
	QuotationListBean quotationBean;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStocktaxis('newprice','desc',0,20)}")
	StockTaxisListBean stockTaxis;
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.shBean:null}")
	StockQuotationBean shBean;
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.szBean:null}")
	StockQuotationBean szBean;
	
	/**-------上证指数 上海*/
	
	//箭头
	@Field(valueKey="text",enableWhen="${(shBean!=null && shBean.newprice > shBean.close)?true:false}",visibleWhen="${(shBean!=null && shBean.newprice != shBean.close)?true:false}")
	String shType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${shBean!=null?shBean.risefallrate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sh_risefallrate;
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${shBean!=null?shBean.market:'--'}")
	String sh_market;
	// 最新
	@Field(valueKey="text",binding="${shBean!=null?shBean.newprice:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sh_newprice;
	// 涨跌额
	@Field(valueKey="text",binding="${shBean!=null?shBean.change:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sh_change;
	
	/**-----------深圳成指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(szBean!=null && szBean.newprice > szBean.close)?true:false}",visibleWhen="${(szBean!=null && szBean.newprice != szBean.close)?true:false}")
		String szType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${szBean!=null?szBean.risefallrate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sz_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${szBean!=null?szBean.market:'--'}")
	String sz_market;
	
	// 最新
	@Field(valueKey="text",binding="${szBean!=null?szBean.newprice:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sz_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${szBean!=null?szBean.change:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String sz_change;
	
	// 股票列表
	@Field(valueKey = "options",binding="${(stockTaxis!=null&&stockTaxis.list!=null)?stockTaxis.list:null}")
	List<StockTaxisBean> stockInfos;

	
	@OnShow
	protected void updateInfo() {

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
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
