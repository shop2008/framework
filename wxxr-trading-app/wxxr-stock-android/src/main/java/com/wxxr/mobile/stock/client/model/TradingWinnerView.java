/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
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
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.earn_money_rank_layout")
public abstract class TradingWinnerView extends ViewBase{
	@Field(valueKey="options")
	List<EarnRankItem> earnRankList;
	DataField<List> earnRankListField;
	
	@OnShow	
	protected void updateEarnRankList() {
		earnRankList = new ArrayList<EarnRankItem>();
		for(int i=0;i<10;i++){
			EarnRankItem items = new EarnRankItem();
			items.setTitle("动视暴雪第三季度财报：净利润同比下滑75%-----------"+i);
			items.setImgUrl("http://hqpiczs.dfcfw.com/EM_Quote2010PictureProducter/picture/0000011R.png");
			earnRankList.add(items);
		}
		earnRankListField.setValue(this.earnRankList);
	}
	
//	@Command
//	String showItemViewClick(InputEvent event){
//		Object temp = event.getProperty("position");
//		if(temp instanceof Integer){
//			int position = (Integer)temp;
//		}
//		return null;
//	}
}
