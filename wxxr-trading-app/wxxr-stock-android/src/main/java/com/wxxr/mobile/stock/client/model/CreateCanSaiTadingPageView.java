package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingConfigBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockString2StringConvertor1;
import com.wxxr.stock.trading.ejb.api.LossRateNDepositRate;

@View(name="CreateCanSaiTadingPageView",withToolbar=true,description="创建参赛盘")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.create_cansai_trading_layout")
public abstract class CreateCanSaiTadingPageView extends PageBase implements IModelUpdater {

	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getTradingConfig('T_PLUS_ONE',true)}")
	TradingConfigBean userCreateTradAccInfo;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style", visibleWhen="${true}")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f%%"),
			@Parameter(name="multiple", value="100f"),
			@Parameter(name="nullString",value="0.00")
	})
	StockString2StringConvertor1 stockLong2StringConvertorSpecial;
	
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Bean
	int checkedbox1 = 0;
	
	@Field(valueKey="text",binding="${userCreateTradAccInfo!=null?userCreateTradAccInfo.virtualApplyAmount:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String virtualApplyAmount;
	
	/**止损比例 - 参赛交易盘*/
	@Field(valueKey="text",converter="stockLong2StringConvertorSpecial",binding="${(userCreateTradAccInfo!=null && userCreateTradAccInfo.getVirtualRateList()!=null && userCreateTradAccInfo.getVirtualRateList().size()>0)?userCreateTradAccInfo.getVirtualRateList().get(0).lossRate:null}")
	String capitalRate;
	
	@Field(valueKey="checked",binding="${checkedbox1==0?true:false}")
	boolean isChecked1;
	
	//同意守则-参赛交易盘
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(checkedbox1==0 && userCreateTradAccInfo.virtualApplyAmount>0)?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox1==0 && userCreateTradAccInfo.virtualApplyAmount>0}")
			})
	String submitBtnStatus1;
	
	@Command
	String isCheckBoxChecked1(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(this.checkedbox1==0){
				this.checkedbox1 = 1;
			}else{
				this.checkedbox1 = 0;
			}
			registerBean("checkedbox1", checkedbox1);
		}
		return null;
	}
	
	//交易规则
	@Command(navigations={
			@Navigation(on = "TradingRuleWebPage",showPage="TradingRuleWebPage")
	})
	String showTradingRulePage(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			return "TradingRuleWebPage";
		}
		return null;
		
	}
	
	//奖励规则
	@Command(navigations={
			@Navigation(on = "RewardRuleWebPage",showPage="RewardRuleWebPage")
	})
	String RewardRuleClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			return "RewardRuleWebPage";
		}
		return null;
		
	}
	
	@Command(commandName="handleCreateTrading",navigations = { 
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")}),
			 @Navigation(on = "success", message = "创建成功", params = {
						@Parameter(name = "title", value = "提示"),
						@Parameter(name = "icon", value = "resourceId:drawable/remind_focus"),
						@Parameter(name = "onOK", value = "leftok"),
						@Parameter(name = "cancelable", value = "false")}) 
			}
	)
	@SecurityConstraint(allowRoles = { "" })
	@NetworkConstraint
	@ExeGuard(title = "创建交易盘", message = "正在处理，请稍候...", silentPeriod = 1)
	String handleCreateTrading(ExecutionStep step, InputEvent event, Object result){
		switch(step){
		case PROCESS:
			if(userCreateService!=null){
				if(userCreateTradAccInfo!=null){
					List<LossRateNDepositRate> temp = userCreateTradAccInfo.getVirtualRateList();
					if(temp!=null){
						userCreateService.createTradingAccount(Long.parseLong(userCreateTradAccInfo.getVirtualApplyAmount()), Float.parseFloat(temp.get(0).getLossRate()), true, Float.parseFloat(temp.get(0).getDepositCash()), "CASH", "ASTOCKT1");
					}
				}
			}
			break;
		case NAVIGATION:
			return "success";
		}
		return null;
	}
	
	@Command(uiItems = @UIItem(id = "leftok", label = "确定", icon = "resourceId:drawable/home", visibleWhen="true"))
	String confirmOkClick(InputEvent event) {
		IView v = (IView) event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if (v != null)
			v.hide();
		hide();
		return null;
	}
	
	@Override
	public void updateModel(Object value) {

	}

}
