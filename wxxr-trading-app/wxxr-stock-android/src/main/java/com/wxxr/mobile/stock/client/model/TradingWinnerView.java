/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;

/**
 * 赚钱榜逻辑视图
 * @author neillin
 *
 */
@View(name="tradingWinner", description="赚钱榜",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.earn_money_rank_layout")
public abstract class TradingWinnerView extends ViewBase {
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService service;

	@Bean(express = "${service.getEarnRank(0,20)}", effectingFields={"earnRankList"})
	BindableListWrapper<EarnRankItemBean> rankBean;
	
	@Field(valueKey="options",binding="${rankBean.data}")
	List<EarnRankItemBean> earnRankList;
	DataField<List> earnRankListField;
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "true")})
	String acctRefreshView;
	
	int start = 0;
	
	@Command
	String handleRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")){
			this.start = 0;
			this.service.getEarnRank(0,20);
		}else if(event.getEventType().equals("BottomRefresh")){
			if(rankBean!=null && rankBean.getData()!=null){
				this.start = rankBean.getData().size();
				this.service.getEarnRank(start,20); 
			}
		}
		return null;
	}
	
//	@Command
//	String handleBottomRefresh(InputEvent event){
//		start = start+1;
//		this.service.getEarnRank((start*20)+1,20);
//		return null;
//	}
	@Command(navigateMethod="handleReTryClickedResult")
	String handlerReTryClicked(InputEvent event) {
		AsyncUtils.execRunnableAsyncInUI(new Runnable() {
			
			@Override
			public void run() {
				earnRankListField.getDomainModel().doEvaluate();
			}
		});
		return null;
	}
	
	String handleReTryClickedResult(InputEvent event, Object result){
		return null;
	}
	
	@OnShow	
	protected void updateEarnRankList() {
	}
	
	@Command
	String handleItemClick(InputEvent event){
		Integer position = (Integer)event.getProperty("position");
		if(rankBean!=null){
			EarnRankItemBean earn = rankBean.getData().get(position);
			if(earn!=null && position!=null){
				String accid = earn.getAcctId();
				AccidSelection selection = new AccidSelection(accid,position);
				updateSelection(selection);
			}
		}
		return null;
	}
}
