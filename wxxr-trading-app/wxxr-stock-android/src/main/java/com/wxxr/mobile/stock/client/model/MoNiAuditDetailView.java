package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.Float2PercentStringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;


@View(name="mnAuditDetail",description="模拟盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.m_audit_detail_page_layout")
public abstract class MoNiAuditDetailView extends ViewBase implements IModelUpdater,ISelectionChangedListener {

	/**注册服务 ITradingManagementService*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getAuditDetail(accId)}")
	AuditDetailBean auditData;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor1;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor2;
	
	@Convertor(params={
			@Parameter(name="format",value="-%.0f")
	})
	Float2PercentStringConvertor float2PercentStringConvertor;
	
	/**模拟盘额度*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.fund:null}",converter="stockLong2StringAutoUnitConvertor")
	String fund;
	
	/**模拟盘盈亏率*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.plRisk:'--'}")
	String plRisk;
	
	/**模拟盘收益*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.totalGain:null}",converter="stockLong2StringAutoUnitConvertor1")
	String totalGain;
	
	/**补偿交易综合费*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.tradingCost:null}",converter="stockLong2StringAutoUnitConvertor1")
	String tradingCost;
	
	/**账户管理费*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.accountPay:null}",converter="stockLong2StringAutoUnitConvertor")
	String accountPay;
	
	/**玩家实得收益*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.userGain:null}",converter="stockLong2StringAutoUnitConvertor")
	String userGain;
	
	/**终止止损*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.capitalRate:null}",converter="float2PercentStringConvertor")
	String capitalRate;
	
	@Bean
	String accId;
	@Bean
	boolean isVirtual = true;
	
	@OnShow
	void initData(){
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
				this.accId = stockSelection.getAccid();
				this.isVirtual = stockSelection.getVirtual();
			}
			registerBean("accId", this.accId);
			registerBean("isVirtual", this.isVirtual);
		}
	}	
	@Override
	public void updateModel(Object value) {
//		if(value instanceof Map){
//			Map temp = (Map)value;
//	        for (Object key : temp.keySet()) {
//	            Object tempt = temp.get(key);
//	            if("accId".equals(key)){
//	            	this.accId = tempt;
//	            }
//	        }
//	        registerBean("accId", this.accId);
//		}
	}
}
