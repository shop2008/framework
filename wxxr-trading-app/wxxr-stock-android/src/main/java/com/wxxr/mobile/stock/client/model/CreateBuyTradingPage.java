package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

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
	
	/**止损比例*/
	@Field(valueKey="text",binding="${'终止止损：-'}${CapitalRate!=null?CapitalRate:'--'}")
	String capitalRate;
	
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${rateData1!=null?rateData1:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==1?true:false}")
	})
	String rate1;
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${rateData2!=null?rateData2:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==2?true:false}")
	})
	String rate2;
	
	/**止损*/
	@Field(valueKey="text",binding="${'积分创建'}${rateData3!=null?rateData3:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==3?true:false}")
	})
	String rate3;
	
	float _rate1,_rate2,_rate3; //止损
	float _deposit1,_deposit2,_deposit3; //保证金
	
	/**实盘券综合费用比例,手续费*/
	float voucherCostRate;	
	
	
	
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==0}")
	boolean showChallengeTrading;
	
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==1}")
	boolean showParticipatingTrading;
	
	
	@Field(valueKey="options",binding="${moneyData!=null?moneyData:null}")
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
	
	int checkedbox1 = 0;
	
	
	int changeMoney = 0;
	
	@Bean
	List<String> moneyData;
	
	int currentViewId = 0;
	

	String zhfzf,djDeposit;
	float djMoney;
	
	//交易综合费
	@Field(valueKey="text",binding="${zhfzf!=null?zhfzf:'0.00元'}")
	String jiaoyi_zhf;
	
	//冻结余额
	@Field(valueKey="text",binding="${djDeposit!=null?djDeposit:'0.00元'}")
	String frozen_money;
	
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
	
	private void updataRate1(){
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && _deposit1>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
			djDeposit = String.format("%.0f", (changeMoney*10000 * _deposit1))+"元";
			djMoney = (changeMoney*10000 * _deposit1);
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}		
	}
	
	private void updataRate2(){
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && _deposit2>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
			djDeposit = String.format("%.0f", ((changeMoney*10000) * _deposit2))+"元";
			djMoney = ((changeMoney*10000) * _deposit2);
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}	
	}
	private void updataRate3(){
		if(userCreateTradAccInfo!=null)
		costRate = userCreateTradAccInfo.getCostRate();
		if(changeMoney>0 && _deposit2>0 && costRate>0){
			zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
			djDeposit = String.format("%.0f", (changeMoney*10000.0))+"元";
			djMoney = changeMoney*10000;
			registerBean("zhfzf", zhfzf);
			registerBean("djDeposit", djDeposit);
		}	
	}
	//挑战交易盘
	@Command
	String showChallengeTradingClick(InputEvent event){
		this.currentViewId = 0;
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
			float money = changeMoney * 10000;
			switch(currentRadioBtnId){
			case 1:
				if(money>0 && _rate1>0 && djMoney>0 ){
					
				}
				break;
			case 2:
				if(money>0 && _rate2>0 && djMoney>0 ){
					
				}
				break;
			case 3:
				if(money>0 && _rate3>0 && djMoney>0 ){
					
				}
				break;
			}
		}
		return null;
	}
	
	
	//参赛交易-提交
		@Command
		String submitDataClick1(InputEvent event){
			if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
				
			}
			return null;
		}
	
	
	//初始化数据
	@OnShow
	protected void initData(){

		String CapitalRate  = String.format("%.0f", userCreateTradAccInfo.getCapitalRate()*100)+"%";
		registerBean("CapitalRate", CapitalRate);
		
		this.checkedbox = 0;
		this.checkedbox1 = 0;
		registerBean("checkedbox", checkedbox);
		registerBean("checkedbox1", checkedbox1);
		
		this.zhfzf = "0.00元";
		this.djDeposit = "0元";
		this.changeMoney = 0;
		this.costRate = 0;
		this.currentViewId = 0;
		this.currentRadioBtnId = 1;
		registerBean("zhfzf", zhfzf);
		registerBean("djDeposit", djDeposit);
		registerBean("currentViewId", currentViewId);
		registerBean("currentRadioBtnId", currentRadioBtnId);
		registerBean("rateData1", "--");
		registerBean("rateData2", "--");
		registerBean("rateData3", "--");	
		 if(userCreateTradAccInfo!=null){
			 ArrayList<String> money1 = new ArrayList<String>();
			Long maxAmount = userCreateTradAccInfo.getMaxAmount();
			if(maxAmount!=null && maxAmount>0){
				long max = maxAmount/10000;
				for(int i=0; i<max;i++){
					money1.add(String.valueOf(i+1)+"万");
				}
			}
			registerBean("moneyData", money1);
			dataResolution();
			rateData();
		 }
	}
	
	
	private void dataResolution(){
		String rateString = userCreateTradAccInfo.getRateString();
		if(rateString==null){
			return ;
		}
		String[] data = rateString.split(",");
		if(data!=null&&data.length>0){
			int index;
			index = data[0].indexOf(";");
			_rate1= Float.parseFloat(data[0].substring(0, index)); 
			_deposit1 = Float.parseFloat(data[0].substring(index+1, data[0].length()));
			
			index = data[1].indexOf(";");
			_rate2 = Float.parseFloat(data[1].substring(0, index)); 
			_deposit2 = Float.parseFloat(data[1].substring(index+1, data[1].length()));
			
			index = data[2].indexOf(";");
			_rate3 = Float.parseFloat(data[2].substring(0, index)); 
			_deposit3 = Float.parseFloat(data[2].substring(index+1, data[2].length()));
		}
	}
	
	private void rateData(){
		String rateString1 = "-"+String.format("%.0f", _rate1*100)+"%";
		String rateString2 = "-"+String.format("%.0f", _rate2*100)+"%";
		String rateString3 = "-"+String.format("%.0f", _rate3*100)+"%";	
		registerBean("rateData1", rateString1);
		registerBean("rateData2", rateString2);
		registerBean("rateData3", rateString3);				
	}
	
	@Override
	public void updateModel(Object value) {

	}
}
