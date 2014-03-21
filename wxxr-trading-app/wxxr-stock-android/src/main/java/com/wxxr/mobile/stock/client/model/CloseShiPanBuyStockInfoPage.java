/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
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
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.Float2PercentStringConvertor;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * 模拟盘详情
 *  
 * @author 
 * 
 */
@View(name = "CloseShiPanBuyStockInfoPage", withToolbar = true, description="实盘详情")
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.close_shipan_buy_stock_info_layout")
public abstract class CloseShiPanBuyStockInfoPage extends PageBase implements
		IModelUpdater {

	@Bean
	String acctId;
	
	@Bean
	String assetType;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${tradingService.getAuditDetail(acctId)}")
	AuditDetailBean auditDetail;

	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss"),
			@Parameter(name="nullString",value="--")
	})
	LongTime2StringConvertor longTime2StringConvertorBuy;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple", value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertorInt;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f元")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial1;
	
	@Convertor(params={
			@Parameter(name="format",value="-%.0f")
	})
	Float2PercentStringConvertor float2PercentStringConvertor;	
	
	/*** 交易盘编号*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.id:'--'}")
	String id;

	/*** 买入日期  */
	@Field(valueKey = "text", binding = "${auditDetail!=null?(auditDetail.buyDay==0?'-1':auditDetail.buyDay):'-1'}",converter="longTime2StringConvertorBuy")
	String buyDay;
	
	/*** 卖出日期 */
	@Field(valueKey = "text", binding = "${auditDetail!=null?(auditDetail.deadline==0?'-1':auditDetail.deadline):'-1'}")
	String sellDay;

	/*** 申购金额*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.fund:null}",converter="stockLong2StringAutoUnitConvertorInt")
	String applyFee;

	/*** 交易综合费*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.cost:null}",converter="stockLong2StringConvertorSpecial")
	String usedFee;
	
	/*** 冻结资金*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.frozenAmount:null}",converter="stockLong2StringConvertorSpecial")
	String frozenVol;
	
	/*** 扣除实盘积分*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.frozenAmount:null}",converter="stockLong2StringConvertorSpecial1")
	String jifenfrozenVol;
	
	/*** 止损*/
	@Field(valueKey = "text", binding = "${auditDetail!=null?auditDetail.capitalRate:''}",converter="float2PercentStringConvertor")
	String lossLimit;
	
	@Field(valueKey = "visible",visibleWhen="${assetType!=null && assetType!='VOUCHER'}")
	boolean djassetType = true;
	
	@Field(valueKey = "visible",visibleWhen="${assetType!=null && assetType == 'VOUCHER'}")
	boolean jfassetType;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen="${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@OnShow
	protected void initStockInfo() {
		registerBean("acctId", acctId);
	}

	
	@Override
	public void updateModel(Object data) {
		if (data instanceof Map) {
			Map result = (Map) data;
			for (Object key : result.keySet()) {
				if(key.equals(Constants.KEY_ACCOUNT_ID_FLAG)){
	            	String accid = result.get(key)+"";
	            	registerBean("acctId", accid);
	            }
				if(key.equals(Constants.KEY_ASSET_TYPE)){
	            	String assetType = (String)result.get(key);
	            	registerBean("assetType", assetType);
	            }
			}
		}
	}
}
