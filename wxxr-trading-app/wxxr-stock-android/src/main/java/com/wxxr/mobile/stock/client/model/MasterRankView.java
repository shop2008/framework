package com.wxxr.mobile.stock.client.model;

import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * 高手排行榜
 * @author renwenjie
 */

@View(name="masterRankView", description="挑战高手榜")
@AndroidBinding(type=AndroidBindingType.FRAGMENT, layoutId="R.layout.master_rank_layout")
public abstract class MasterRankView extends ViewBase {

	
	
	
	@Field(valueKey="enabled", binding="${totalGainDescFlag}")
	boolean totalGainDescEnabled;
	
	@Field(valueKey="enabled", binding="${positiveGainDescFlag}")
	boolean positiveGainDescEnabled;
	
	
	@Bean
	boolean totalGainDescFlag = true;
	
	@Bean
	boolean positiveGainDescFlag = true;
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(express="${tradingService.getGain(0,10)}")
	BindableListWrapper<GainBean> masterRankListBean;
	
	
	@Field(valueKey="options", binding="${masterRankListBean.data}")
	List<GainBean> masterRankList;
	
	
	
	
	/**
	 * 显示正收益
	 * @param event
	 * @return
	 */
	@Command
	String showPositiveGain(InputEvent event) {
		positiveGainDescFlag = !positiveGainDescFlag;
		registerBean("positiveGainDescFlag", positiveGainDescFlag);
		return null;
	}
	
	/**
	 * 显示总收益
	 * @param event
	 * @return
	 */
	@Command
	String showTotalGain(InputEvent event) {
		totalGainDescFlag = !totalGainDescFlag;
		registerBean("totalGainDescFlag", totalGainDescFlag);
		return null;
	}
}
