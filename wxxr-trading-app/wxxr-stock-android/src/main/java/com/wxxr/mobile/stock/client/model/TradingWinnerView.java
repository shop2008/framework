/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;

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

	@Bean(express = "${service.getEarnRank(0,20)}")
	BindableListWrapper<EarnRankItemBean> rankBean;
	
	@Field(valueKey="options",binding="${rankBean.data}")
	List<EarnRankItemBean> earnRankList;
	
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
				StockSelection selection = new StockSelection();
				selection.setAccid(accid);
				selection.setPosition(position);
				updateSelection(selection);
			}
		}
		return null;
	}
}
