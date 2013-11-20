/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;

/**
 * @author wangxuyang
 *
 */
@View(name="earnRankListItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.earn_money_rank_layout_item")
public abstract class TradingWinnerItemView extends ViewBase implements IModelUpdater{
	
	@Bean
	EarnRankItemBean earnRank;
	
	@Field(valueKey="text",binding="${earnRank!=null?earnRank.title:'--'}")
	String text;
	
	@Field(valueKey="imageURI",binding="${earnRank!=null?earnRank.imgUrl:null}")
	String imageUrl;
	
	int postion = 0;
	
	@Field(valueKey="enabled")
	boolean isClose;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof EarnRankItemBean){
			registerBean("earnRank", value);
		}
	}
	
	@Command(description="点击文本事件")
	String showItemView(InputEvent event){

		return null;
	}
	
	@Command(navigations={@Navigation(on="operationDetails",showPage="OperationDetails")})
	CommandResult earnRankItemClick(InputEvent event){
		CommandResult result = new CommandResult();
		if(earnRank!=null){
			String accid = earnRank.getAcctId();
			if(accid!=null){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("isVirtual", true);
				map.put("accid", accid);
				updateSelection(accid);
				result.setPayload(map);
				result.setResult("operationDetails");
				return result;
			}
		}
		return null;
	}
}
