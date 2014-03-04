package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GuideGainBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


@View(name="HelpCenterHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_list_header_view_layout")
public abstract class HelpCenterHeaderView extends ViewBase implements IModelUpdater {

	@Bean(type=BindingType.Service)
	IUserManagementService userManager;
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager userIdentity;
	
	@Bean(type=BindingType.Pojo, express="${userIdentity.isUserAuthenticated()?userManager.checkGuideGain():userManager.getGuideGainRule()}",effectingFields={"unGainAmount"})
	GuideGainBean awardAmount;
	ELBeanValueEvaluator<GuideGainBean> awardAmountUpdater;
	
	@Field(valueKey="visible", visibleWhen="${awardAmount!=null && awardAmount.allow}")
	boolean UnGainScoreBody;
	
	@Field(valueKey="visible", visibleWhen="${awardAmount!=null && !awardAmount.allow}")
	boolean GainScoreBody;
	
	DataField<Boolean> GainScoreBodyField;
	
	@Field(valueKey="text", binding="了解短线放大镜交易规则，领取新手奖励${awardAmount!=null?awardAmount.guideGain:''}实盘积分")
	String unGainAmount;
	
//	@Field(valueKey="text", binding="您已领取新手奖励实盘积分!")
//	String gainAmount;
	
	@Command(navigations={
			@Navigation(on="guidePage", showPage="guidePage"),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})		
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String toGuiPage(InputEvent event) {
		return "guidePage";
	}
	
	@Command(uiItems=@UIItem(id="leftok",label="确定",icon=""),navigations={
		@Navigation(on="*",showPage="userLoginPage")
	})
	String onCancelClick(InputEvent event){
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if(v != null)
			v.hide();
		return "";
	}
	@Command(navigations={@Navigation(on="*", showPage="CustomerServicePage")})
	String toConstructUs(InputEvent event) {
		return "";
	}
	
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		awardAmountUpdater.doEvaluate();
		return null;
	}
	
	@Override
	public void updateModel(Object arg0) {
		
	}
}
