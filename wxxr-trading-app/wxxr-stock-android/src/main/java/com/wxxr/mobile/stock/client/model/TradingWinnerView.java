/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.EarnRankItem;
import com.wxxr.mobile.stock.client.service.IArticleManagementService;

/**
 * 赚钱榜逻辑视图
 * @author neillin
 *
 */
@View(name="tradingWinner")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.layout_earn_money_rank")
public abstract class TradingWinnerView extends ViewBase {
	@Field(valueKey="options")
	List<EarnRankItem> earnRankList;
	DataField<List> earnRankListField;
	
	@OnShow	
	protected void updateEarnRankList() {
		earnRankList = null;//TODO 
		earnRankListField.setValue(earnRankList);
	}
}
