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
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="SZZhiShuPage",withToolbar=true,description="指数界面")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.zhishu_page_layout")
public abstract class SZzhiShuPage extends PageBase implements IModelUpdater {
	static Trace log = Trace.getLogger(SZzhiShuPage.class);
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
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.szBean:null}")
	StockQuotationBean szBean;

	@Bean
	Map<String, String> map;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getMinuteline(map)}")
	StockMinuteKBean minute;	
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000.00"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss"),
			@Parameter(name="nullString",value="--")
	})
	LongTime2StringConvertor longTime2StringConvertorBuy;
	
	@Convertor(params={
			@Parameter(name="format",value="(%.2f%%)"),
			@Parameter(name="multiple", value="100.00"),
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
	
	@Field(valueKey="options",binding="${minute!=null?minute.list:null}",attributes={
			@Attribute(name = "stockClose", value = "${minute!=null?minute.close:'0'}"),
			@Attribute(name = "stockDate", value = "${minute!=null?minute.date:'0'}"),
			@Attribute(name = "stockType", value = "0"),
			@Attribute(name = "stockBorderColor",value="#535353"),
			@Attribute(name = "stockUpColor",value="#BA2514"),
			@Attribute(name = "stockDownColor",value="#3C7F00"),
			@Attribute(name = "stockAverageLineColor",value="#FFE400"),
			@Attribute(name = "stockCloseColor",value="#FFFFFF")
	})
	List<StockMinuteLineBean> stockMinuteData;	
	
	@Bean
	String stockName;
	
	/**箭头*/
	@Field(valueKey="text",enableWhen="${shBean!=null && shBean.newprice > shBean.close}",visibleWhen="${shBean!=null && shBean.newprice != shBean.close}")
	String arrows;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${stockName!=null?stockName:'--'}")
	String name;
	
	/**股票或指数 代码*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.code:'--'}${'.'}${szBean!=null?szBean.market:'--'}")
	String codeAndmarket;
	
	/**涨跌额*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.change:null}",converter="stockLong2StringAutoUnitConvertor")
	String change;
	
	/**涨跌幅*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.risefallrate:null}",converter="stockLong2StringConvertorSpecial")
	String risefallrate;
	
//	/**市场代码： SH，SZ各代表上海，深圳。*/
//	@Field(valueKey="text",binding="${szBean!=null?szBean.market:'--'}")
//	String market;
	
	/**时间。*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.datetime:null}",converter="longTime2StringConvertorBuy")
	String datetime;
	
	/**最新*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.newprice:null}",converter="stockLong2StringAutoUnitConvertor")
	String newprice;
	
	/**昨收*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.close:null}",converter="stockLong2StringAutoUnitConvertor")
	String close;
	
	/**开盘*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.open:null}",converter="stockLong2StringAutoUnitConvertor")
	String open;
	
	/**最高*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.high:null}",converter="stockLong2StringAutoUnitConvertor")
	String high;
	
	/**最底*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.low:null}",converter="stockLong2StringAutoUnitConvertor")
	String low;
	
	/**成交量*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.secuvolume:'--'}")
	String secuvolume;
	
	/**成交额*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.secuamount:'--'}")
	String secuamount;
	
	/**量比*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.lb:'--'}")
	String lb;
	
	/**平盘家数*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.ppjs:'--'}")
	String ppjs;
	
	/**上涨家数*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.szjs:'--'}")
	String szjs;
	
	/**下跌家数*/
	@Field(valueKey="text",binding="${szBean!=null?szBean.xdjs:'--'}")
	String xdjs;
	
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("SZzhiShuPage : handleTMegaTopRefresh");
		}
		infoCenterService.getQuotations();
		return null;
	}	
	
	@OnShow
	protected void initData(){
		
	}
	
	@Override
	public void updateModel(Object value) {
		String code = null;
		String market = null;
		HashMap<String, String> temp = new HashMap<String, String>();
		if(value instanceof Map){
			Map data = (Map) value;
			for (Object key : data.keySet()) {
	            if(key.equals("code")){
	            	String val = (String)data.get(key);
	            	code = val;
	            }
	            if(key.equals("market")){
	            	String val = (String)data.get(key);
	            	market = val;
	            }
	            if(key.equals("name")){
					String name = (String) data.get(key);
					this.stockName = name;
					registerBean("stockName", this.stockName);
				}
	        }
			if(code!=null && market!=null){
				temp.put(code, market);
				registerBean("map", temp);
			}
		}
	}
}
