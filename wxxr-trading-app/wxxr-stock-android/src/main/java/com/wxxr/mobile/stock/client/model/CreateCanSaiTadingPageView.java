package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name="CreateCanSaiTadingPageView",withToolbar=true,description="创建参赛盘")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.create_cansai_trading_layout")
public abstract class CreateCanSaiTadingPageView extends PageBase implements IModelUpdater {

	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getUserCreateTradAccInfo()}")
	UserCreateTradAccInfoBean userCreateTradAccInfo;
	
	@Menu(items={"left"})
	private IMenu toolbar;
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Bean
	int checkedbox1 = 0;
	
	/**止损比例 - 参赛交易盘*/
	@Field(valueKey="text",binding="${userCreateTradAccInfo.rateString3!=null?userCreateTradAccInfo.rateString3:'--'}")
	String capitalRate;
	
	@Field(valueKey="checked",binding="${checkedbox1==0?true:false}")
	boolean isChecked1;
	
	//同意守则-参赛交易盘
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(checkedbox1==0 && userCreateTradAccInfo.getRateData3()>0 && userCreateTradAccInfo.getDeposit3()>0)?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox1==0 && userCreateTradAccInfo.getRateData3()>0 && userCreateTradAccInfo.getDeposit3()>0}")
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
	
	
	@Command
	String handleCreateTrading(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			
		}
		return null;
	}
	
	@Override
	public void updateModel(Object value) {

	}

}
