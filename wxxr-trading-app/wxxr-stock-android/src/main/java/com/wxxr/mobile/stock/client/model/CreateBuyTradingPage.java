package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="creataBuyTradePage",withToolbar=true,description="创建")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.create_buy_page_layout")
public abstract class CreateBuyTradingPage extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(CreateBuyTradingPage.class);
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@ViewGroup(viewIds={"readRecord","auditDetail","mnAuditDetail"},defaultViewId="readRecord")
	private IViewGroup contents;

	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getUserCreateTradAccInfo()}")
	UserCreateTradAccInfoBean userCreateTradAccInfo;
	
	/**可申请最大金额*/
	long maxAmount;

	
	/**综合费用比例,手续费*/
	float costRate;	
	
	/**保证金比例*/
	float depositRate; 
	
	/**用户唯一标识*/
	String userId;
	
	int currentRadioBtnId = 1;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	/**止损比例 - 参赛交易盘*/
	@Field(valueKey="text",binding="${userCreateTradAccInfo.rateString3!=null?userCreateTradAccInfo.rateString3:'--'}")
	String capitalRate;
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${userCreateTradAccInfo.rateString1!=null?userCreateTradAccInfo.rateString1:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==1?true:false}")
	})
	String rate1;
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${userCreateTradAccInfo.rateString2!=null?userCreateTradAccInfo.rateString2:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==2?true:false}")
	})
	String rate2;
	
	/**止损*/
	@Field(valueKey="text",binding="${'积分创建'}${userCreateTradAccInfo.rateString3!=null?userCreateTradAccInfo.rateString3:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==3?true:false}")
	})
	String rate3;
	
	/**实盘券综合费用比例,手续费*/
	float voucherCostRate;	
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==0}")
	boolean showChallengeTrading;
	
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==1}")
	boolean showParticipatingTrading;
	
	
	@Field(valueKey="options",binding="${userCreateTradAccInfo.requestamount!=null?userCreateTradAccInfo.requestamount:null}")
	List<String> money;
	
	//同意守则-挑战交易盘
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${checkedbox==0?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox==0?true:false}")
			}
	)
	String submitBtnStatus;
	
	@Field(valueKey="checked",binding="${checkedbox==0?true:false}")
	boolean isChecked;
	int checkedbox = 0;
	
	
	//同意守则-挑战交易盘
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${checkedbox1==0?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox1==0?true:false}")
			})
	String submitBtnStatus1;
	
	@Field(valueKey="checked",binding="${checkedbox1==0?true:false}")
	boolean isChecked1;
	@Field(valueKey="checked",binding="${radioBtn==0?true:false}")
	boolean cansaiRadio;
	
	@Field(valueKey="checked",binding="${radioBtn==1?true:false}")
	boolean tiaozhanRadio;
	
	@Bean
	int radioBtn = 0;
	
	int checkedbox1 = 0;
	
	
	int changeMoney = 0;
	
//	@Bean
//	List<String> moneyData;
	
	int currentViewId = 0;
	

	String zhfzf,djDeposit;
	float djMoney;
	
	//交易综合费
	@Field(valueKey="text",binding="${zhfzf!=null?zhfzf:'0.00元'}")
	String jiaoyi_zhf;
	
	//冻结余额
	@Field(valueKey="text",binding="${djDeposit!=null?djDeposit:'0.00元'}")
	String frozen_money;
	
	@Field(valueKey="visible",visibleWhen="${currentRadioBtnId != 3}")
	boolean frozen_visibility;
	
	@Field(valueKey="visible",visibleWhen="${currentRadioBtnId==3}")
	boolean jifen_visibility;
	
	//切换RadioButtn事件
	@Command
	String rateClick1(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 1;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate1();
		}
		return null;
	}
	@Command
	String rateClick2(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 2;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate2();
		}
		return null;
	}
	
	@Command
	String rateClick3(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 3;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate3();
		}
		return null;
	}
	private float getDeposit1(){
	   return  userCreateTradAccInfo.getDeposit1();
	}
	private float getDeposit2(){
	       return  userCreateTradAccInfo.getDeposit2();
	}
	private float getDeposit3(){
        return  userCreateTradAccInfo.getDeposit3();
    }
	
	private float getRate1(){
	    return  userCreateTradAccInfo.getRateData1();
	}
	private float getRate2(){
        return  userCreateTradAccInfo.getRateData2();
    }
	private float getRate3(){
        return  userCreateTradAccInfo.getRateData3();
    }
	
	private void updataRate1(){
		//金钱以分为单位:
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && getDeposit1()>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney * 10000) * costRate))+"元";
			djDeposit = String.format("%.0f", ((changeMoney* 10000)* getDeposit1()))+"元";
			djMoney = ((changeMoney*10000 * 100) * getDeposit1());
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}		
	}
	
	private void updataRate2(){
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && getDeposit2()>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney * 10000) * costRate))+"元";
			djDeposit = String.format("%.0f", ((changeMoney*10000) * getDeposit2()))+"元";
			djMoney = ((changeMoney*10000 * 100) * getDeposit2());
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}	
	}
	private void updataRate3(){
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
			djDeposit = String.format("%.0f", changeMoney*10000.0)+"元";
			djMoney = (changeMoney*10000*100)*getDeposit3();
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}	
	}
	//挑战交易盘
	@Command
	String showChallengeTradingClick(InputEvent event){
		this.currentViewId = 0;
		this.radioBtn = 1;
		registerBean("radioBtn", this.radioBtn);
		registerBean("currentViewId", currentViewId);
		this.checkedbox = 0;
		registerBean("checkedbox", checkedbox);
		return null;
	}
	
	//参赛交易盘 
	@Command
	String showParticipatingTradingClick(InputEvent event){
		this.currentViewId = 1;
		registerBean("currentViewId", currentViewId);
		this.checkedbox1 = 0;
		registerBean("checkedbox1", checkedbox1);
		this.radioBtn = 0;
		registerBean("radioBtn", this.radioBtn);
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
	String moneyTextChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
			Object changedText = event.getProperty("changedText");
			if(changedText instanceof String){
				String money = (String)changedText;
				String cMoney = money.substring(0, money.length()-1);
				changeMoney = Integer.parseInt(cMoney);
				if(userCreateTradAccInfo!=null)
					costRate = userCreateTradAccInfo.getCostRate();
				switch(currentRadioBtnId){
				case 1:
					updataRate1();
					break;
				case 2:
					updataRate2();
					break;
				case 3:
					updataRate3();
					break;
				}
			}
		}
		return null;
	}
	
	//同意守则-挑战交易盘
	@Command
	String isCheckBoxChecked(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(checkedbox==0){
				checkedbox = 1;
			}else{
				checkedbox = 0;
			}
			registerBean("checkedbox", checkedbox);
		}
		return null;
	}
	//同意守则-参赛交易盘
	@Command
	String isCheckBoxChecked1(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(checkedbox1==0){
				checkedbox1 = 1;
			}else{
				checkedbox1 = 0;
			}
			registerBean("checkedbox1", checkedbox1);
		}
		return null;
	}
	
	//挑战交易-提交
	@Command
	String submitDataClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			long money = changeMoney * 10000 * 100;
			float _rate = 0.0f;
			float _depositRate = 0.0f;
			String assetType = "VOUCHER";
			switch(currentRadioBtnId){
			case 1:
				if(money>0 && getRate1()>0 && getDeposit1()>0 ){
					_rate = getRate1();
					_depositRate = getDeposit1();
				}
				assetType = "CASH"; // 现金
				break;
			case 2:
				if(money>0 && getRate2()>0 && djMoney>0 ){
					_rate = getRate2();
					_depositRate = getDeposit2();
				}
				assetType = "CASH";
				break;
			case 3:
				if(money>0 && getRate3()>0 && djMoney>0 ){
					_rate = getRate3();
					_depositRate = getDeposit3();
				}
				assetType= "VOUCHER"; //积分
				break;
			}
			if(money>0 && _rate>0 && _depositRate>0 && assetType!=null){
				userCreateService.createTradingAccount(money, _rate, false, _depositRate, assetType);
				getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
			}
		}
		return "home";
	}
	
	
	//参赛交易-提交
		@Command
		String submitDataClick1(InputEvent event){
			if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
				if(getRate3()>0 && getDeposit3()>0){
					userCreateService.createTradingAccount(10000000l, getRate3(), true, getDeposit3(), "CASH");
					getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
				}
			}
			return null;
		}
	
	
	//初始化数据
	@OnShow
	protected void initData(){

		this.radioBtn = 0;
		registerBean("radioBtn", this.radioBtn);
		this.checkedbox = 0;
		this.checkedbox1 = 0;
		registerBean("checkedbox", checkedbox);
		registerBean("checkedbox1", checkedbox1);
		
		this.zhfzf = "0.00元";
		this.djDeposit = "0元";
		this.changeMoney = 0;
		this.costRate = 0;
		this.currentViewId = 1;
		this.currentRadioBtnId = 1;
		registerBean("zhfzf", zhfzf);
		registerBean("djDeposit", djDeposit);
		registerBean("currentViewId", currentViewId);
		registerBean("currentRadioBtnId", currentRadioBtnId);
		registerBean("rateData1", "--");
		registerBean("rateData2", "--");
		registerBean("rateData3", "--");	
//		 if(userCreateTradAccInfo!=null){
//		     String CapitalRate  = String.format("%.0f", userCreateTradAccInfo.getCapitalRate()*100)+"%";
//		     registerBean("CapitalRate", CapitalRate);
//		 }
	}
	
	@Override
	public void updateModel(Object value) {
	   log.debug(value.toString());

	}
}
