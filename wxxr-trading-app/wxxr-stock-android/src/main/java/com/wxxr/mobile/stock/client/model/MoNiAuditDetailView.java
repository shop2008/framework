package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;


@View(name="mnAuditDetail",description="模拟盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.m_audit_detail_page_layout")
public abstract class MoNiAuditDetailView extends ViewBase implements IModelUpdater {

	/**注册服务 ITradingManagementService*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getAuditDetail(tempId)}")
	AuditDetailBean auditData;
	
	/**模拟盘额度*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.fund:'--'}")
	String fund;
	
	/**模拟盘盈亏率*/
	@Field(valueKey="text",binding="${auditData!=null ? auditData.plRisk:'--'}")
	String plRisk;
	
	/**模拟盘收益*/
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
	
	@Bean
	String tempId;
	
	@OnShow
	void initData(){
		registerBean("tempId", "");
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map tempMap = (Map) value;
			for (Object key : tempMap.keySet()) {
	            String tempt = (String)tempMap.get(key);
	            if (tempt != null && "result".equals(key)) {
	            	registerBean("tempId", tempt);
	            }
	        }
		}
	}
}
