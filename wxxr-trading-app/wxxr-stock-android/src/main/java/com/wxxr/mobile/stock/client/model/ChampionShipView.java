/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.MegagameRank;

/**
 * @author neillin
 *
 */
@View(name="championShip")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.champion_ship_page_layout")
public abstract class ChampionShipView extends ViewBase {
	private static final Trace log = Trace.register(TradingMainView.class);

	@Field(valueKey="options")
	List<MegagameRank> ChampionShip;
	DataField<List> ChampionShipField;
	
	
	@OnShow
	protected void updataMegagameRank() {
		ChampionShip = new ArrayList<MegagameRank>();
		for(int i=0; i<20;i++){
			MegagameRank message = new MegagameRank();
			message.setNickName("张三"+i);
			message.setStatus(1);
			message.setMaxStockCode("600598");
			message.setMaxStockMarket("天利高新");
			message.setGainRate("+6.25%");
			message.setRankSeq(i+1);
			ChampionShip.add(message);
		}
		ChampionShipField.setValue(this.ChampionShip);
	}
	
}
