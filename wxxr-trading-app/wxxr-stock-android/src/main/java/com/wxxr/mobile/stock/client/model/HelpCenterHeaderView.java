package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


@View(name="HelpCenterHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_list_header_view_layout")
public abstract class HelpCenterHeaderView extends ViewBase {

	@Bean(type=BindingType.Service)
	IUserManagementService userManager;
	
	@Bean(type=BindingType.Pojo, express="${userManager.getMyUserInfo()}")
	UserBean userBean;
	
	@Bean(type=BindingType.Pojo, express="${userManager.getGuideGainRule()}")
	String awardAmount;
	@Field(valueKey="visible", binding="${userBean==null?true:(userBean.allowGuideGain==true?true:false)}")
	boolean UnGainScoreBody;
	DataField<Boolean> UnGainScoreBodyField;
	
	@Field(valueKey="visible", binding="${userBean==null?false:(userBean.allowGuideGain==true?false:true)}")
	boolean GainScoreBody;
	
	DataField<Boolean> GainScoreBodyField;
	
	@Field(valueKey="text", binding="了解短线放大镜交易规则，领取新手奖励${awardAmount}实盘积分")
	String unGainAmount;
	
	@Field(valueKey="text", binding="您已领取新手奖励${awardAmount}实盘积分，点击查看")
	String gainAmount;
	
	@Command(navigations={@Navigation(on="*", showPage="guidePage")})
	String toGuiPage(InputEvent event) {
		return "";
	}
	
	@Command(navigations={@Navigation(on="*", showPage="constructUsPage")})
	String toConstructUs(InputEvent event) {
		return "";
	}
	
	@OnHide
	void initData() {
		UnGainScoreBodyField.getDomainModel().doEvaluate();
		//GainScoreBodyField.getDomainModel().doEvaluate();
	}
	
}
