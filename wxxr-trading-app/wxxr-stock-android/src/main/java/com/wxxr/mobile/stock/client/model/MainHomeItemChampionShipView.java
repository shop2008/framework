package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "MainHomeItemChampionShipView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.main_home_item_champion_ship_view")
public abstract class MainHomeItemChampionShipView extends ViewBase implements
		IModelUpdater {

	@Bean
	ChampionShipMessageMenuItem ChampionShipBean;

	@Convertor(params = { @Parameter(name = "format", value = "+%.2f%%"),
			@Parameter(name = "multiple", value = "100.00") })
	StockLong2StringConvertor stockLong2StringConvertorSpecial;

	@Convertor(params = { @Parameter(name = "format", value = "+%.2f"),
			@Parameter(name = "multiple", value = "100.00") })
	StockLong2StringConvertor stockLong2StringConvertor;

	@Field(valueKey = "imageURI", binding = "${'resourceId:drawable/home_champion_ship'}")
	String icon;

	@Field(valueKey = "text", binding = "${ChampionShipBean!=null?ChampionShipBean.title:'--'}")
	String title;

	@Field(valueKey = "text", binding = "${ChampionShipBean!=null?ChampionShipBean.date:'--'}")
	String date;

	@Field(valueKey = "text", binding = "${ChampionShipBean!=null?ChampionShipBean.nickName:'--'}")
	String nickName;

	@Field(valueKey = "text", binding = "${ChampionShipBean!=null?ChampionShipBean.stockName:'--'}")
	String stockName;

	@Field(valueKey = "text", binding = "${ChampionShipBean!=null?ChampionShipBean.incomeRate:'0'}", converter = "stockLong2StringConvertorSpecial", attributes = { @Attribute(name = "textColor", value = "${ChampionShipBean==null?'resourceId:color/gray':ChampionShipBean.incomeRate>0?'resourceId:color/stock_text_up':(ChampionShipBean.incomeRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}") })
	String incomeRate;

	@Override
	public void updateModel(Object value) {
		if (value instanceof ChampionShipMessageMenuItem) {
			ChampionShipBean = (ChampionShipMessageMenuItem) value;
			registerBean("ChampionShipBean", value);
		}
	}
}
