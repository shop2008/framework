package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GuideGainBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="GuideVoucherItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.guide_voucher_item_view")
public abstract class GuideVoucherItemView extends ViewBase implements IModelUpdater {
	@Bean(type=BindingType.Service)
	IUserManagementService userManager;
	
	@Bean(type=BindingType.Service)
	IUserIdentityManager userIdentity;
	
	@Bean(type=BindingType.Pojo, express="${userIdentity.isUserAuthenticated()?userManager.checkGuideGain():userManager.getGuideGainRule()}")
	GuideGainBean awardAmount;
	ELBeanValueEvaluator<GuideGainBean> awardAmountUpdater;
	
	
	@Field(valueKey="text", binding="哇！您竟然都看完了，只有少数人能够有这样的耐心哦，您可以得到${awardAmount!=null?awardAmount.guideGain:''}实盘积分奖励了！",visibleWhen="${awardAmount!=null && awardAmount.allow}")
	String unGainAmount;
	
	@Field(valueKey="visible", visibleWhen="${awardAmount!=null && !awardAmount.allow}")
	boolean gainAmount;
	
	@Field(valueKey = "visible", visibleWhen = "${awardAmount!=null && awardAmount.allow}")
	boolean centerBtn;
	DataField<Boolean> centerBtnField;
	
	@Command(navigations = {
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")}),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	@ExeGuard(title = "领取奖励", message = "正在处理，请稍候...", silentPeriod = 1)
	String centerBtnClick(ExecutionStep step, InputEvent event, Object result) {
		switch (step) {
		case PROCESS:
			if(userManager!=null){
				this.userManager.getGuideGain();
			}
			break;
		case NAVIGATION:
			awardAmountUpdater.doEvaluate();
			hide();
			break;
		default:
			break;
		}
		return null;
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
	
	
//	@Command
//	String handlerReTryClicked(InputEvent event) {
//		awardAmountUpdater.doEvaluate();
//		return null;
//	}
	
	@Override
	public void updateModel(Object value) {

	}
}
