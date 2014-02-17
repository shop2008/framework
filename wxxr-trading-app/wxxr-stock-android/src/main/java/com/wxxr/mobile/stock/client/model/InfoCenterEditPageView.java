package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

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
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;


@View(name = "InfoCenterEditPageView", description = "行情中心", withToolbar = true, provideSelection=true)
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.price_center_edit_page_layout")
public abstract class InfoCenterEditPageView extends PageBase implements IModelUpdater {
	final Trace log = Trace.getLogger(InfoCenterEditPageView.class);
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@Bean(type = BindingType.Service)
	IOptionStockManagementService optionStockService;
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${optionStockService.getMyOptionStocks(null,null)}")
	BindableListWrapper<StockQuotationBean> stockTaxis;
	
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('000001','SH')}")
	StockQuotationBean shBean;
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('399001','SZ')}")
	StockQuotationBean szBean;
	
	@Bean(type = BindingType.Pojo,express = "${infoCenterService.getStockQuotation('399005','SZ')}")
	StockQuotationBean zxBean;

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
			@Parameter(name="format",value="(%+.2f%%)"),
			@Parameter(name="multiple", value="1000f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	
	/**-------上证指数 上海*/

	//箭头
	@Field(valueKey="text",enableWhen="${(shBean!=null && shBean.newprice > shBean.close)?true:false}",visibleWhen="${(shBean!=null && shBean.newprice != shBean.close)?true:false}")
	String shType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${shBean!=null?shBean.risefallrate:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sh_risefallrate;
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${shBean!=null?shBean.market:null}")
	String sh_market;
	// 最新
	@Field(valueKey="text",binding="${shBean!=null?shBean.newprice:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sh_newprice;
	// 涨跌额
	@Field(valueKey="text",binding="${shBean!=null?shBean.change:null}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String sh_change;
	
	/**-----------深圳成指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(szBean!=null && szBean.newprice > szBean.close)?true:false}",visibleWhen="${(szBean!=null && szBean.newprice != szBean.close)?true:false}",upateAsync=true)
		String szType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${szBean!=null?szBean.risefallrate:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sz_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${szBean!=null?szBean.market:null}")
	String sz_market;
	
	// 最新
	@Field(valueKey="text",binding="${szBean!=null?szBean.newprice:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sz_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${szBean!=null?szBean.change:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String sz_change;
	
	
	/**-----------深圳中小版指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(zxBean!=null && zxBean.newprice > zxBean.close)?true:false}",visibleWhen="${(zxBean!=null && zxBean.newprice != zxBean.close)?true:false}",upateAsync=true)
		String zxType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.risefallrate:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String zx_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.market:null}")
	String zx_market;
	
	// 最新
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.newprice:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String zx_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${zxBean!=null?zxBean.change:null}",upateAsync=true,attributes={
			@Attribute(name = "textColor", value = "${(zxBean!=null && zxBean.newprice > zxBean.close)?'resourceId:color/red':((zxBean!=null && zxBean.newprice < zxBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor1")
	String zx_change;	
	
	
	// 股票列表
	@Field(valueKey = "options",binding="${stockTaxis!=null?stockTaxis.data:null}")
	List<StockQuotationBean> stockInfosData;
	
	/**
	 * 事件处理-单击深证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SZ_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleSZClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(szBean!=null){
				if(szBean.getCode()!=null && szBean.getMarket()!=null){
					temp.put("code", szBean.getCode());
					temp.put("market", szBean.getMarket());
					temp.put("name", "深圳成指");
					updateSelection(new StockSelection(szBean.getMarket(),szBean.getCode(),"深圳成指"));
					result.setPayload(temp);
					result.setResult("SZ_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}
	/**
	 * 事件处理 -单击上证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SH_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleSHClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(shBean!=null){
				if(shBean.getCode()!=null && shBean.getMarket()!=null){
					temp.put("code", shBean.getCode());
					temp.put("market", shBean.getMarket());
					temp.put("name", "上证指数");
					updateSelection(new StockSelection(shBean.getMarket(),shBean.getCode(),"上证指数"));
					result.setPayload(temp);
					result.setResult("SH_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}
	
	/**
	 * 事件处理 -单击中小版指 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "ZX_ZhiShuPage",showPage="ZhiShuPage")})
	CommandResult handleZXClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(zxBean!=null){
				if(zxBean.getCode()!=null && zxBean.getMarket()!=null){
					temp.put("code", zxBean.getCode());
					temp.put("market", zxBean.getMarket());
					temp.put("name", "中小版指");
					updateSelection(new StockSelection(zxBean.getMarket(),zxBean.getCode(),"中小版指"));
					result.setPayload(temp);
					result.setResult("ZX_ZhiShuPage");
					return result;
				}
			}
		}
		return null;
	}	
	
	
	@Command
	String onDropHandle(InputEvent event){
		if("DropItemEvent".equals(event.getEventType()) || "MoveTopItemEvent".equals(event.getEventType())){
			int from = (Integer) event.getProperty("from");
			int to = (Integer) event.getProperty("to");
			if(stockTaxis!=null){
				List<StockQuotationBean> temp = stockTaxis.getData();
				if(temp!=null && temp.size()>0){
					HashMap<String, Integer> map = new HashMap<String, Integer>();
					StockQuotationBean tempData = temp.get(from);
					temp.remove(tempData);
					temp.add(to, tempData);
					if(temp!=null && temp.size()>0){
						for(int i=0; i<temp.size();i++){
							StockQuotationBean qb = temp.get(i);
							if(qb!=null){
								String key = qb.getCode() +"."+ qb.getMarket();
								map.put(key, i+1);
							}
						}
					}
					if(map!=null){
						this.optionStockService.updateOrder(map);
					}
				}
			}
		}
		
		if("RemoveItemEvent".equals(event.getEventType())){
			int which = (Integer) event.getProperty("which");
			String code = (String) event.getProperty("code");
			String market = (String) event.getProperty("market");
//			if(stockTaxis!=null){
//				List<StockQuotationBean> data = stockTaxis.getData();
//				if(data!=null && data.size()>0){
//					String code = data.get(which).getCode();
//					String market = data.get(which).getMarket();
//				}
//			}
			if(code!=null && market!=null){
				log.info("InfoCenterEditPageView Remove Data Code=   "+code+"    Market=  "+market);
				this.optionStockService.delete(code, market);
			}
		}
		return null;
	}
	
	@Command
	String onRemoveItemHandle(InputEvent event){
//		if("RemoveItemEvent".equals(event.getEventType())){
//			List<StockInfoBean> temp = null;
//			int which = (Integer) event.getProperty("which");
//			stockData.remove(which);
//			temp = this.stockData;
//			log.info("which=   "+which   +" stockData=   "+temp.toString());
//			registerBean("stockData", temp);
//		}
		return null;
	}
	
	
	@Override
	public void updateModel(Object value) {

	}

}
