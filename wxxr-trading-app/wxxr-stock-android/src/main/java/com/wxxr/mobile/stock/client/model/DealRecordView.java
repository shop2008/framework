package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.model.TradingRecord;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;


@View(name="DealRecordView", description="模拟盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.deal_record_layout")
public abstract class DealRecordView extends ViewBase implements IModelUpdater,ISelectionChangedListener {

	/**注册服务 ITradingManagementService*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${accIdTemp=selection;tradingService.getDealDetail(accIdTemp)}")
	DealDetailBean dealDetail;
	
	@Field(valueKey="text",binding="${selection!=null?selection:null}")
	String accId;
	DataField<String> accIdField;
	
	/**交易记录*/
	@Field(valueKey="options",binding="${dealDetail!=null?dealDetail.tradingRecords:null}")
	List<TradingRecord> tradingRecords;
	
	/**申请资金*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.fund:'--'}")
	String fund;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.plRisk:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(dealDetail!=null && dealDetail.plRisk>0)?'resourceId:color/red':((dealDetail!=null && dealDetail.plRisk<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String plRisk;
	
	/**盈亏总额（交易盘，除去费用）*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.totalGain:'--'}${'元'}",attributes={
			@Attribute(name = "textColor", value = "${(dealDetail!=null && dealDetail.totalGain>0)?'resourceId:color/red':((dealDetail!=null && dealDetail.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String totalGain;
	
	
	/**交易图片*/
	@Field(valueKey="imageURI",binding="${dealDetail!=null?dealDetail.imgUrl[0]:'--'}")
	String imgUrl;
	
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		String temp = providerId;
	}
	
	/**
	 * 初始化
	 * */
	@OnShow
	void ininViews(){
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			
		}
	}
	
	/**转入行情界面*/
	@Command(navigations={@Navigation(on="hangqing",showPage="")})
	String stockMarketAction(InputEvent event){
		String result = "hangqing";
		return result;
	}
	
	/**转让操作盘详情界面*/
	@Command(navigations = { @Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage") })
	CommandResult detailsAction(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			resutl.setPayload(accIdField.getValue());
			resutl.setResult("TBuyStockInfoPage");
			return resutl;
		}

		return null;
	}	
}
