package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "ChampionShipItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.champion_ship_page_layout_item")
public abstract class ChampionShipItemView extends ViewBase implements
		IModelUpdater {
	
	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(msgRank!=null?msgRank.maxStockCode:'', msgRank!=null?msgRank.maxStockMarket:'')}")
	StockBaseInfo stockInfoBean;
	@Bean
	MegagameRankBean msgRank;
	
	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.rankSeq:'--'}")
	String rankSeq;
	
	@Field(valueKey = "text", binding = "${msgRank!=null?(msgRank.maxStockCode==null||msgRank.maxStockCode==''?'--':msgRank.maxStockCode):'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.over=='CLOSED'?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String maxStockCode;

	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.nickName:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.over=='CLOSED'?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String nickName;

	@Field(valueKey = "text", binding = "${msgRank!=null?msgRank.gainRate:'--'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.over=='CLOSED'?'resourceId:color/gray':(msgRank.totalGain>0?'resourceId:color/red':(msgRank.gainRates==0?'resourceId:color/gray':'resourceId:color/green'))}")
			})
	String gainRate;

	@Field(valueKey = "text", binding = "${stockInfoBean!=null?stockInfoBean.name:'无持仓'}", attributes={
			@Attribute(name = "textColor", value = "${msgRank.over=='CLOSED'?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String maxStockName;

	@Override
	public void updateModel(Object value) {
		if (value instanceof MegagameRankBean) {
			msgRank = (MegagameRankBean)value;
			registerBean("msgRank",value);
		}
	}
	
	/**
	 * 用户点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "otherUserPage") })
	CommandResult handlerUserClicked(InputEvent event) {
		CommandResult result = new CommandResult();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put(Constants.KEY_USER_ID_FLAG, msgRank.getUserId());
		map.put(Constants.KEY_USER_NAME_FLAG, msgRank.getNickName());
		result.setPayload(map);
		result.setResult("");
		return result;
	}
}
