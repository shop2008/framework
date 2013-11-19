package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;


@View(name="auditDetail",description="实盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.audit_detail_page_layout")
public abstract class AuditDetailView extends ViewBase implements IModelUpdater {

	static Trace log;
	/**注册服务 ITradingManagementService*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tempId=selection;tradingService.getAuditDetail(tempId)}")
	AuditDetailBean auditData;
	
	/**实盘额度*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.fund:'--'}")
	String fund;
	
	/**实盘盈亏率*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.plRisk:'--'}")
	String plRisk;
	
	/**实盘收益*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.totalGain:'--'}")
	String totalGain;
	
	/**补偿交易综合费*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.tradingCost:'--'}")
	String tradingCost;
	
	/**账户管理费*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.accountPay:'--'}")
	String accountPay;
	
	/**玩家实得收益*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.userGain:'--'}")
	String userGain;
	
	/**终止止损*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.capitalRate:'--'}")
	String capitalRate;
	
	/**冻结资金*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.frozenAmount:'--'}")
	String frozenAmount;
	
	/**扣减数量*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.payOut:'--'}")
	String payOut;
	
	/**解冻数量*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.plRisk:'--'}")
	String unfreezeAmount;
	
	
	@OnShow
	void initData(){
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map tempMap = (Map) value;
			for (Object key : tempMap.keySet()) {
	            String tempt = (String)tempMap.get(key);
	            if (tempt != null && "result".equals(key)) {
	            	
	            }
	        }
		}
	}
}
