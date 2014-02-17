package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.TradingConfigBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockFloat2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockString2StringConvertor1;
import com.wxxr.stock.trading.ejb.api.LossRateNDepositRate;

@View(name="CreateT3TradingPageView",withToolbar=true,description="挑战交易盘T+3")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.create_t3_trading_page_view")
public abstract class CreateT3TradingPageView extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(CreateT3TradingPageView.class);
	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getTradingConfig('T_PLUS_THREE',false)}")
	TradingConfigBean userCreateTradAccInfo;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f%%"),
			@Parameter(name="multiple", value="100f"),
			@Parameter(name="nullString",value="--")
	})
	StockString2StringConvertor1 stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockFloat2StringConvertor stockLong2StringConvertorYuan;
	
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
	
	@Field(valueKey="options",binding="${userCreateTradAccInfo!=null?userCreateTradAccInfo.accualOptions:null}")
	List<String> money;
	
	@Field(valueKey="options",binding="${userCreateTradAccInfo!=null?userCreateTradAccInfo.rateList:null}")
	List<LossRateNDepositRate> depositCash;
	
	@Field(valueKey="text",binding="${changeLossRate!=null?changeLossRate:'--'}",converter="stockLong2StringConvertorSpecial")
	String lossRate;
	
	@Field(valueKey="text", binding="${djMoney!=null?djMoney:'0.0元'}")
	String dj_money;
	
	@Field(valueKey="text", binding="${originalFee!=null?originalFee:'--'}",converter="stockLong2StringConvertorYuan")
	String originalFeeValue;
	
	@Field(valueKey="text", binding="${discountFee!=null?discountFee:'--'}", converter="stockLong2StringConvertorYuan")
	String discountFeeValue;
	@Bean
	String djMoney;
	@Bean
	float originalFee;//原价-每万元手续费
	@Bean
	float discountFee;//优惠价
	
	int changeMoney = 0;
	float changeDeposit = 0.0f;
	float changeLossRate = 0.0f;
	
	@Bean
	int checkedbox1 = 0;
	
	@Field(valueKey="checked",binding="${checkedbox1==0?true:false}")
	boolean isChecked1;
	
	//同意守则
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(checkedbox1==0)?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox1==0}")
			})
	String submitBtnStatus1;
	
	
	@Command(commandName="handleCreateTrading",navigations = { 
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")})				
			}
	)
	@NetworkConstraint
	@ExeGuard(title = "创建交易盘", message = "正在处理，请稍候...", silentPeriod = 1)
	String handleCreateTrading(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			if(userCreateService!=null){
				if(userCreateTradAccInfo!=null){
					List<LossRateNDepositRate> temp = userCreateTradAccInfo.getRateList();
					if(temp!=null){
						Long money = (long) (changeMoney * 10000 * 100);
						log.info("CreateT3TradingPageView handleCreateTrading money="+money+"LossRate=  "+changeLossRate +" changeDeposit=  "+changeDeposit);
						userCreateService.createTradingAccount(money, changeLossRate, false, changeDeposit, "CASH", "ASTOCKT3");
						getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
					}
				}
			}
		}
		return null;
	}
	
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
	
	@OnShow
	void initData(){
		this.changeMoney = 0;
		this.changeDeposit = 0.0f;
		this.djMoney = "0.0元";
		registerBean("djMoney", this.djMoney);
		this.changeLossRate = 0.0f;
		registerBean("changeLossRate", this.changeLossRate);
		this.originalFee = 0;
		registerBean("originalFee", this.originalFee);
		this.discountFee = 0;
		registerBean("discountFee", this.discountFee);
	}
	
	@Command
	String moneyTextChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
				Object changedText = event.getProperty("changedText");
				if(changedText instanceof String){
					if(changedText instanceof String){
						String money = (String)changedText;
						if(money!=null && !StringUtils.isBlank(money)){
							String cMoney = money.substring(0, money.length()-1);
							this.changeMoney = Integer.parseInt(cMoney);
						}
					}
				}
				updataData(changeMoney, changeDeposit, changeLossRate);
			}
		return null;
	}
	
	@Command
	String depositCashTextChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
			Object changedText = event.getProperty("changedText");
			if(changedText instanceof String){
				String deposit = (String) changedText;
				if(deposit!=null && !StringUtils.isBlank(deposit)){
					deposit = String.format("%.2f", Float.parseFloat(deposit.substring(0, deposit.indexOf("%")))/100);
					this.changeDeposit = Float.parseFloat(deposit);
				}
				if(userCreateTradAccInfo!=null)
				{
					List<LossRateNDepositRate> temp = userCreateTradAccInfo.getRateList();
					if(temp!=null && temp.size()>0){
						for(int i=0; i<temp.size();i++){
							LossRateNDepositRate data = temp.get(i);
							if(deposit.equals(data.getDepositCash())){
								this.changeLossRate = Float.parseFloat(data.getLossRate());
								registerBean("changeLossRate", this.changeLossRate);
							}
						}
					}
				}
				updataData(changeMoney, changeDeposit, changeLossRate);
			}
		}
		return null;
	}
	
	private void updataData(int money, float deposit, float lossRate){
		if(money>0 && deposit>0){
			djMoney = String.format("%.2f", money * 10000 * deposit) + "元";
			registerBean("djMoney", this.djMoney);
		}
		if(userCreateTradAccInfo!=null && money>0){
			this.originalFee = Float.parseFloat(userCreateTradAccInfo.getOriginalFee()) * money * 10000;
			registerBean("originalFee", this.originalFee);
			this.discountFee = Float.parseFloat(userCreateTradAccInfo.getDiscountFee()) * money * 10000;
			registerBean("discountFee", this.discountFee);
		}
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
	
	@Override
	public void updateModel(Object value) {
	}
}
