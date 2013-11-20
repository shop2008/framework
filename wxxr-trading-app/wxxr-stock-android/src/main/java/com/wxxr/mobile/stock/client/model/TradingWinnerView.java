/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * 赚钱榜逻辑视图
 * @author neillin
 *
 */
@View(name="tradingWinner", description="赚钱榜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.earn_money_rank_layout")
public abstract class TradingWinnerView extends ViewBase implements IModelUpdater{
	
	
	@Bean(type = BindingType.Service)
	ITradingManagementService earnRankService;

	@Bean(type = BindingType.Pojo, express = "${earnRankService.getEarnRank(0,20)}")
	RankListBean rankBean;
	
	@Field(valueKey="options",binding="${rankBean!=null?rankBean.earnRankBeans:null}")
	List<EarnRankItemBean> earnRankList;
	
	@OnShow	
	protected void updateEarnRankList() {
	}
	
	
	@Override
	public void updateModel(Object value) {
		
	}
}
