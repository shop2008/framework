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
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;


@View(name="DealRecordView", description="模拟盘",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.deal_record_layout")
public abstract class DealRecordView extends ViewBase implements IModelUpdater,ISelectionChangedListener{

	static Trace log = Trace.getLogger(DealRecordView.class);
	/**注册服务 ITradingManagementService*/
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	@Bean(type=BindingType.Pojo,express="${tradingService.getDealDetail(accId)}")
	DealDetailBean dealDetail;

	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Bean
	String accId = "";
	
	@Bean
	boolean isVirtual = true;
	
	/**交易记录*/
	@Field(valueKey="options",binding="${dealDetail!=null?dealDetail.tradingRecords:null}")
	List<TradingRecordBean> tradingRecords;
	
	/**申请资金*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.fund:null}",converter="stockLong2StringAutoUnitConvertor")
	String fund;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.plRisk:null}",attributes={
			@Attribute(name = "textColor", value = "${(dealDetail!=null && dealDetail.totalGain>0)?'resourceId:color/red':((dealDetail!=null && dealDetail.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String plRisk;
	
	/**盈亏总额（交易盘，除去费用）*/
	@Field(valueKey="text",binding="${dealDetail!=null?dealDetail.totalGain:null}",converter="stockLong2StringConvertorYuan",attributes={
			@Attribute(name = "textColor", value = "${(dealDetail!=null && dealDetail.totalGain>0)?'resourceId:color/red':((dealDetail!=null && dealDetail.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String totalGain;
	
	
	/**交易图片*/
	@Field(valueKey="imageURI",binding="${dealDetail!=null?dealDetail.imgUrl[0]:'--'}",visibleWhen="${dealDetail!=null&&dealDetail.imgUrl!=null}")
	String imgUrl;
	
	/**
	 * 初始化
	 * */
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			
		}
	}
	
	@OnCreate
	void registerSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		selectionChanged("",service.getSelection(AccidSelection.class));
		service.addSelectionListener(this);
	}
	
	@OnDestroy
	void removeSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		service.removeSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof AccidSelection){
			AccidSelection accidSelection = (AccidSelection) selection;
			if(accidSelection!=null){
				this.accId = accidSelection.getAccid();
				this.isVirtual = accidSelection.isVirtual();
			}
			registerBean("accId", this.accId);
			registerBean("isVirtual", this.isVirtual);
		}
	}
	
	/**转入行情界面*/
	@Command(navigations={@Navigation(on="hangqing",showPage="GeGuStockPage")})
	CommandResult StockQuotationAction(InputEvent event){
		String code = null;
		String name = null;
		String market = null;
		CommandResult result = new CommandResult();
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			int position = (Integer) event.getProperty("position");
			if(dealDetail!=null){
				List<TradingRecordBean> recordListBean = dealDetail.getTradingRecords();
				if(recordListBean!=null && recordListBean.size()>0){
					TradingRecordBean tradingRecord = recordListBean.get(position);
					if(tradingRecord!=null){
						code = tradingRecord.getCode();
						market = tradingRecord.getMarket();
						if(code!=null && market!=null){
							StockBaseInfo stockInfo =stockInfoSyncService.getStockBaseInfoByCode(code, market);
							if(stockInfo!=null){
								name = stockInfo.getName();
							}
						}
					}
				}
			}
			if(name!=null && code!=null && market!=null){
				map.put("name", name);
				map.put("code", code);
				map.put("market", market);
				result.setPayload(map);
				updateSelection(new StockSelection(market, code, name));
				result.setResult("hangqing");
			}
		}
		return result;
	}
	
	/**转让操作盘详情界面*/
	@Command(navigations = { 
			@Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage"),
			@Navigation(on="ShiPanStockInfoPage",showPage = "ShiPanBuyStockInfoPage")
			})
	CommandResult detailsAction(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if(isVirtual){
				resutl.setResult("TBuyStockInfoPage");
			}else{
				resutl.setResult("ShiPanStockInfoPage");
			}
			resutl.setPayload(accId);
			return resutl;
		}

		return null;
	}	
}
