package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
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
import com.wxxr.mobile.stock.client.utils.Utils;
import com.wxxr.stock.trading.ejb.api.LossRateNDepositRate;

@View(name="CreateT1TradingPageView",withToolbar=true,description="挑战交易盘T+1")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.create_t1_trading_page_view")
public abstract class CreateT1TradingPageView extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(CreateT1TradingPageView.class);
	
	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getTradingConfig('T_PLUS_ONE',false)}")
	TradingConfigBean userCreateTradAccInfo;
	
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
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockFloat2StringConvertor stockLong2StringConvertorYuan;

	@Convertor(params={
			@Parameter(name="format",value="%.0f%%"),
			@Parameter(name="multiple", value="100f"),
			@Parameter(name="nullString",value="0.00")
	})
	StockString2StringConvertor1 stockLong2StringConvertorSpecial;
	
	//申请金额
	@Field(valueKey="options",binding="${userCreateTradAccInfo!=null&&radionId==1?userCreateTradAccInfo.accualOptions:userCreateTradAccInfo.voucherOptions}",attributes={
			@Attribute(name = "nullValue", value = "${nullValue}"),
			@Attribute(name = "text",value = "${moneyText}")
	})
	List<String> money;
	
	@Bean
	String nullValue = null;
	
	//申请金额
	@Field(valueKey="options",binding="${userCreateTradAccInfo!=null?userCreateTradAccInfo.voucherOptions:null}")
	List<String> voucherMoney;
	
	//保证金＋止损
	@Bean
	String lossRate;
	@Bean
	String depositCash;
	
	@Field(valueKey="text",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "enabled", value = "${radionId==1}")
	},binding="${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.rateList!=null&&userCreateTradAccInfo.rateList.size()>0)?userCreateTradAccInfo.rateList.get(0).lossRate:null}")
	String rate_01;
	
	@Field(valueKey="enabled",enableWhen="${radionId==1}")
	boolean rate_txt_01;
	
	@Field(valueKey="enabled",enableWhen="${radionId==1}")
	boolean loss_txt_01;
	
	@Field(valueKey="text",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "enabled", value = "${radionId==1}")
	},binding="${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.rateList!=null&&userCreateTradAccInfo.rateList.size()>0)?userCreateTradAccInfo.rateList.get(0).depositCash:null}")
	String deposit_01;
	
	@Field(valueKey="enabled",enableWhen="${radionId==1}")
	boolean deposit_txt_01;
	
	@Field(valueKey="enabled",enableWhen="${radionId==1}")
	boolean deposit_radio_01;
	
	@Field(valueKey="enabled",enableWhen="${radionId==2}")
	boolean deposit_layout_01;
	
	/**积分*/
	
	@Field(valueKey="text",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "enabled", value = "${radionId==2}")
	},binding="${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.voucherRateList!=null&&userCreateTradAccInfo.voucherRateList.size()>0)?userCreateTradAccInfo.voucherRateList.get(0).lossRate:null}")
	String voucherRate_02; 
	
	@Field(valueKey="enabled",enableWhen="${radionId==2}")
	boolean voucherRate_txt_02;
	
	@Field(valueKey="enabled",enableWhen="${radionId==2}")
	boolean voucher_txt_02;
	
	@Field(valueKey="enabled",enableWhen="${radionId==2}")
	boolean loss_txt_02;
	
	@Field(valueKey="enabled",enableWhen="${radionId==2}")
	boolean voucher_radio_02;
	
	@Field(valueKey="enabled",enableWhen="${radionId==1}")
	boolean voucher_layout_02;
	
	
//	@Field(valueKey="text",binding="${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.rateList!=null&&userCreateTradAccInfo.rateList.size()>0)?utils.getValue(userCreateTradAccInfo.rateList.get(0).depositCash):'--'}" +
//			"${'保证金\\n'}" +
//			"${'-'}" +
//			"${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.rateList!=null&&userCreateTradAccInfo.rateList.size()>0)?utils.getValue(userCreateTradAccInfo.rateList.get(0).lossRate):'--'}" +
//			"${'止损'}",
//			attributes={
//			@Attribute(name = "checked", value = "${radionId==1?true:false}")
//	})
//	String rateList;
	
	@Bean
	Utils utils = Utils.getInstance();
	
	/**积分创建-止损*/
//	@Bean
//	String vLossRate;
//	@Field(valueKey="text",binding="${'积分创建\\n'}" +
//			"${'-'}" +
//			"${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.voucherRateList!=null&&userCreateTradAccInfo.voucherRateList.size()>0)?utils.getValue(userCreateTradAccInfo.voucherRateList.get(0).lossRate):'--'}" +
//			"${'止损'}",
//			attributes={
//			@Attribute(name = "checked", value = "${radionId==2?true:false}")
//	})
//	String voucherRate;
	
	@Field(valueKey="text", binding="${originalFee!=null?originalFee:'--'}",converter="stockLong2StringConvertorYuan")
	String originalFeeValue;
	
	@Field(valueKey="text", binding="${djMoney!=null?djMoney:'0.0元'}")
	String dj_money;
	
	@Bean
	int checkedbox1 = 0;
	
	@Field(valueKey="checked",binding="${checkedbox1==0?true:false}")
	boolean isChecked1;

	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(checkedbox1==0)?'resourceId:color/white':'resourceId:color/gray'}"),
			@Attribute(name = "enabled", value = "${checkedbox1==0}")
			})
	String submitBtnStatus1;
	
	@Bean
	List<String> createMoney;
	
	@OnShow
	void initDatas(){
		this.radionId = 1;
		registerBean("radionId", this.radionId);
		registerBean("nullValue", this.nullValue);
		this.moneyText = null;
		registerBean("moneyText", this.moneyText);
		this.changeMoney = 0;
		this.vChangeMoney = 0;
		this.djMoney = "0.00元";
		registerBean("djMoney", this.djMoney);
		this.originalFee = 0;
		registerBean("originalFee", this.originalFee);
	}

	int changeMoney = 0;
	int vChangeMoney = 0;
	int radionId = 1;
	@Bean
	String moneyText;
	@Command
	String moneyTextChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
			Object changedText = event.getProperty("changedText");
			if(changedText instanceof String){
				String money = (String)changedText;
				if(money!=null && !StringUtils.isBlank(money)){
					this.moneyText = money;
					registerBean("moneyText", this.moneyText);
					String cMoney = money.substring(0, money.length()-1);
					switch(radionId){
						case 1:
							changeMoney = Integer.parseInt(cMoney);
							upData();
							break;
						case 2:
							vChangeMoney = Integer.parseInt(cMoney);
							updataVoucher();
							break;
					}
				}
			}
		}
		return null;
	}
	
	@Bean
	String djMoney;
	
	@Bean
	float originalFee;
	
	private void upData(){
		this.originalFee = Float.parseFloat(userCreateTradAccInfo.getOriginalFee()) * changeMoney * 10000;
		registerBean("originalFee", this.originalFee);
		if(userCreateTradAccInfo!=null){
			List<LossRateNDepositRate> temp = userCreateTradAccInfo.getRateList();
			if(temp!=null && temp.size()>0){
				LossRateNDepositRate lossRate = temp.get(0);
				this.djMoney = String.format("%.2f", changeMoney * 10000 * Float.parseFloat(lossRate.getDepositCash())) + "元"; 
				registerBean("djMoney", this.djMoney);
			}
		}
	}
	
	private void updataVoucher(){
		this.originalFee = Float.parseFloat(userCreateTradAccInfo.getOriginalFee()) * vChangeMoney * 10000;
		registerBean("originalFee", this.originalFee);
		if(userCreateTradAccInfo!=null){
			List<LossRateNDepositRate> temp = userCreateTradAccInfo.getVirtualRateList();
			if(temp!=null && temp.size()>0){
				LossRateNDepositRate lossRate = temp.get(0);
				this.djMoney = String.format("%.2f", vChangeMoney * 10000 * Float.parseFloat(lossRate.getDepositCash())) + "元"; 
				registerBean("djMoney", this.djMoney);
			}
		}
	}
	
	@Command
	String VoucherMoneyTextChanged(InputEvent event){
		return null;
	}
	
	@Command
	String RateClick(InputEvent event){
		this.radionId = 1;
		this.nullValue = null;
		if(changeMoney==0){
			this.moneyText = null;
		}else{
			this.moneyText = String.valueOf(changeMoney)+"万";
		}
		registerBean("moneyText", this.moneyText);
		registerBean("nullValue", this.nullValue);
		registerBean("radionId", this.radionId);
		upData();
		return null;
	}
	
	@Command
	String VoucherRateClick(InputEvent event){
		this.radionId = 2;
		this.nullValue = null;
		if(vChangeMoney==0){
			this.moneyText = null;
		}else{
			this.moneyText = String.valueOf(vChangeMoney)+"万";
		}
		registerBean("moneyText", this.moneyText);
		registerBean("nullValue", this.nullValue);
		registerBean("radionId", this.radionId);
		updataVoucher();
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
					switch(radionId){
					case 1:
						List<LossRateNDepositRate> temp = userCreateTradAccInfo.getRateList();
						if(temp!=null && temp.size()>0){
							LossRateNDepositRate lossRate = temp.get(0);
							Long money = (long) (changeMoney * 10000 * 100);
							log.info("CreateT1TradingPageView handleCreateTrading money="+money+"LossRate=  "+lossRate.getLossRate() +" changeDeposit=  "+lossRate.getDepositCash());
							userCreateService.createTradingAccount(money, Float.parseFloat(lossRate.getLossRate()), false, Float.parseFloat(lossRate.getDepositCash()), "CASH", "ASTOCKT1");
						}
						break;
					case 2:
						List<LossRateNDepositRate> temp1 = userCreateTradAccInfo.getVoucherRateList();
						if(temp1!=null && temp1.size()>0){
							LossRateNDepositRate lossRate = temp1.get(0);
							Long money = (long) (vChangeMoney * 10000 * 100);
							log.info("CreateT1TradingPageView handleCreateTrading VoucherMoney="+money+"VoucherLossRate=  "+lossRate.getLossRate() +" VoucherDeposit=  "+lossRate.getDepositCash());
							userCreateService.createTradingAccount(money, Float.parseFloat(lossRate.getLossRate()), false, Float.parseFloat(lossRate.getDepositCash()), "VOUCHER", "ASTOCKT1");
						}
						
						break;
					}
					getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
				}
			}
		}
		return null;
	}	
	
	
	@Override
	public void updateModel(Object value) {
	}
}
