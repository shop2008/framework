package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="SHZhiShuPage",withToolbar=true,description="指数界面")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.zhishu_page_layout1")
public abstract class SHzhiShuPage extends PageBase implements IModelUpdater {
	static Trace log = Trace.getLogger(SHzhiShuPage.class);
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;

	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getQuotations()}")
	QuotationListBean quotationBean;
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.shBean:null}")
	StockQuotationBean shBean;
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="(%.2f%%)"),
			@Parameter(name="multiple", value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="formatUnit",value="手"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor convertorSecuvolume;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor convertorSecuamount;
	
	@Bean
	String stockName;
	@Bean
	String stockCode;
	@Bean
	String stockMarket;

	@ViewGroup(viewIds={"GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;		
	
	/**昨收*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.close:null}",converter="stockLong2StringAutoUnitConvertor")
	String close;
	
	/**开盘*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.open:null}",converter="stockLong2StringAutoUnitConvertor")
	String open;
	
	/**最高*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.high:null}",converter="stockLong2StringAutoUnitConvertor")
	String high;
	
	/**最底*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.low:null}",converter="stockLong2StringAutoUnitConvertor")
	String low;
	
	/**成交量*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.secuvolume:null}",converter="convertorSecuvolume")
	String secuvolume;
	
	/**成交额*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.secuamount:'--'}",converter="convertorSecuamount")
	String secuamount;
	
	/**量比*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.lb:null}",converter="stockLong2StringAutoUnitConvertor")
	String lb;
	
	/**平盘家数*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.ppjs:'--'}")
	String ppjs;
	
	/**上涨家数*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.szjs:'--'}")
	String szjs;
	
	/**下跌家数*/
	@Field(valueKey="text",binding="${shBean!=null?shBean.xdjs:'--'}")
	String xdjs;
	
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("SHzhiShuPage : handleTMegaTopRefresh");
		}
		infoCenterService.getQuotations();
		return null;
	}	
	
	@OnShow
	protected void initData(){
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			HashMap<String, String>temp = new HashMap<String, String>();
			Map data = (Map) value;
			for (Object key : data.keySet()) {
	            if(key.equals("code")){
	            	String val = (String)data.get(key);
	            	this.stockCode = val;
	            	registerBean("stockCode", this.stockCode);
	            }
	            if(key.equals("market")){
	            	String val = (String)data.get(key);
	            	this.stockMarket = val;
	            	registerBean("stockMarket", this.stockMarket);
	            }
	            if(key.equals("name")){
					String name = (String) data.get(key);
					this.stockName = name;
					registerBean("stockName", this.stockName);
				}
	        }
			if(this.stockCode!=null && this.stockMarket!=null){
				temp.put("code", this.stockCode);
				temp.put("market", this.stockMarket);
				registerBean("map", temp);
			}
		}
	}
}
