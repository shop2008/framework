/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisListBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

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
	
	/**
	 * 涨跌排序接口,默认按涨跌幅降序
	 * @param orderby 排序字段名称，即按什么排序 ：“newprice”-按最新价；“risefallrate”-按涨跌幅
	 * @param direction - 排序方向：升序or降序：“asc”-升序，"desc"-降序
	 * @param start - 起始条目
	 * @param limit - 最多可取条数
	 * @return
	 */
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStocktaxis('risefallrate','desc',0,20)}")
	StockTaxisListBean stockTaxis;
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.shBean:null}")
	StockQuotationBean shBean;
	
	@Bean(type = BindingType.Pojo,express = "${quotationBean!=null?quotationBean.szBean:null}")
	StockQuotationBean szBean;
	
	
	@Convertor(params={
			@Parameter(name="multiple",value="100.00"),
			@Parameter(name="format",value="%.2f")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	/**-------上证指数 上海*/
	
	//箭头
	@Field(valueKey="text",enableWhen="${(shBean!=null && shBean.newprice > shBean.close)?true:false}",visibleWhen="${(shBean!=null && shBean.newprice != shBean.close)?true:false}")
	String shType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${shBean!=null?shBean.risefallrate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sh_risefallrate;
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${shBean!=null?shBean.market:'--'}")
	String sh_market;
	// 最新
	@Field(valueKey="text",binding="${shBean!=null?shBean.newprice:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sh_newprice;
	// 涨跌额
	@Field(valueKey="text",binding="${shBean!=null?shBean.change:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(shBean!=null && shBean.newprice > shBean.close)?'resourceId:color/red':((shBean!=null && shBean.newprice < shBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sh_change;
	
	/**-----------深圳成指 深圳*/
	
	//箭头 
		@Field(valueKey="text",enableWhen="${(szBean!=null && szBean.newprice > szBean.close)?true:false}",visibleWhen="${(szBean!=null && szBean.newprice != szBean.close)?true:false}")
		String szType;
	
	// 涨跌幅
	@Field(valueKey="text",binding="${szBean!=null?szBean.risefallrate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sz_risefallrate;
	
	// 市场代码： SH，SZ各代表上海，深圳。
	@Field(valueKey="text",binding="${szBean!=null?szBean.market:'--'}")
	String sz_market;
	
	// 最新
	@Field(valueKey="text",binding="${szBean!=null?szBean.newprice:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringAutoUnitConvertor")
	String sz_newprice;
	
	// 涨跌额
	@Field(valueKey="text",binding="${szBean!=null?szBean.change:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(szBean!=null && szBean.newprice > szBean.close)?'resourceId:color/red':((szBean!=null && szBean.newprice < szBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	},converter="stockLong2StringConvertorSpecial")
	String sz_change;
	
	// 股票列表
	@Field(valueKey = "options",binding="${(stockTaxis!=null&&stockTaxis.list!=null)?stockTaxis.list:null}")
	List<StockTaxisBean> stockInfos;

	@Bean
	int showArrows = 0;
	
	@Bean
	boolean direction = false; //默认：“desc”-升序
	
	
	@Field(valueKey="text",enableWhen="${direction}",visibleWhen="${showArrows==1}")
	String isNewPrice;
	
	@Field(valueKey="text",enableWhen="${direction}",visibleWhen="${showArrows==0}")
	String isRisefallrate;
	
	
	@OnShow
	protected void updateInfo() {

	}

	/**
	 * 事件处理-单击深证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SZ_ZhiShuPage",showPage="SZZhiShuPage")})
	CommandResult handleSZClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(szBean!=null){
				temp.put("code", szBean.getCode());
				temp.put("market", szBean.getMarket());
				temp.put("name", "深圳成指");
				result.setPayload(temp);
			}
			result.setResult("SZ_ZhiShuPage");
			return result;
		}
		return null;
	}
	/**
	 * 事件处理 -单击上证指数 
	 * 
	 * */
	@Command(navigations={@Navigation(on = "SH_ZhiShuPage",showPage="SHZhiShuPage")})
	CommandResult handleSHClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = new HashMap<String, Object>();
			if(shBean!=null){
				temp.put("code", shBean.getCode());
				temp.put("market", shBean.getMarket());
				temp.put("name", "上证指数");
				result.setPayload(temp);
			}
			result.setResult("SH_ZhiShuPage");
			return result;
		}
		return null;
	}
	/**
	 * 事件处理- 单击涨跌幅标题（股票列表排序-按涨跌幅）
	 * 
	 * */
	@Command()
	String risefallrateOrderByClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			String directionValue = "desc";
			this.showArrows = 0;
			registerBean("showArrows", this.showArrows);
			if(direction){
				this.direction = false; //降序
				directionValue = "desc";
			}else{
				this.direction = true; // 升序
				directionValue = "asc";
			}
			registerBean("direction", this.direction);
			if(infoCenterService!=null){
				infoCenterService.getStocktaxis("risefallrate", directionValue, 0, 20);
			}
		}
		return null;
	}
	/**
	 * 事件处理- 单击最新价格标题（股票列表排序-按当前价）
	 * 
	 * */
	@Command()
	String newPriceOrderByClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			String directionValue = "desc";
			this.showArrows = 1;
			registerBean("showArrows", this.showArrows);
			if(direction){
				this.direction = false; //降序
				directionValue = "desc";
			}else{
				this.direction = true; // 升序
				directionValue = "asc";
			}
			registerBean("direction", this.direction);
			if(infoCenterService!=null){
				infoCenterService.getStocktaxis("newprice", directionValue, 0, 20);
			}
		}
		return null;
	}
	
	/**
	 * 点击列表跳转个股界面
	 * */
	@Command(navigations={
			@Navigation(on = "GeGuStockPage",showPage="GeGuStockPage")
	})
	CommandResult gotoPageItemClick(InputEvent event){
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			int position = (Integer) event.getProperty("position");
			CommandResult result = new CommandResult();
			if(stockTaxis!=null){
				List<StockTaxisBean> taxis = stockTaxis.getList();
				if(taxis!=null && taxis.size()>0){
					StockTaxisBean stockTaxis = taxis.get(position);
					String code = stockTaxis.getCode();
					String name = stockTaxis.getName();
					String market = stockTaxis.getMarket();
					if(code!=null && market!=null){
						map.put("code", code);
						map.put("name", name);
						map.put("market", market);
						result.setPayload(map);
						result.setResult("GeGuStockPage");
						return result;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
