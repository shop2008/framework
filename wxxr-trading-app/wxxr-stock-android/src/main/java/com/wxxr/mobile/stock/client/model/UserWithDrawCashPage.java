package com.wxxr.mobile.stock.client.model;

import android.R.bool;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.ColorUtils;

/**
 * 提现金界面
 * @author renwenjie
 *
 */
@View(name="userWithDrawCashPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.withdraw_cath_page_layout")
public abstract class UserWithDrawCashPage extends PageBase {

	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Bean(type=BindingType.Pojo, express="${user!=null?user.userAsset:null}")
	UserAssetBean userAssetBean;
	
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.usableBal:'--'}")
	String avaliCashAmount;
	
	/**可用现金数量*/
	@Field(valueKey="text")
	String availCashAmount;
	
	@Field(valueKey="text")
	String commitBtn;
	
	DataField<String> availCashAmountField;
	DataField<String> commitBtnField;
	
	@Field(valueKey="checked")
	boolean readChecked;
	
	DataField<Boolean> readCheckedField;
	/**
	 * 返回到上一个界面
	 * @param event
	 * @return
	 */
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 返回到上一个界面
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}

	@Command(
			commandName="commit",
			navigations={
					@Navigation(on="isNull", showDialog=""),
					@Navigation(on="notEnough", showDialog=""),
					@Navigation(on="NotDiv", showDialog=""),
					@Navigation(on="OK", showPage="")
					}
	)
	String commit(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String withDrawCashAmount = this.availCashAmountField.getValue();
			if (withDrawCashAmount == null) {
				return "isNull";
			} else {
				
				float avaliableAmount = Float.parseFloat(user.getBalance());
				float drawCashAmount = Float.parseFloat(withDrawCashAmount);
				if (drawCashAmount > avaliableAmount) {
					//提示可提现金不足
					return "notEnough";
				} else if((((int)drawCashAmount) % 100) != 0) {
					//提示必须是100的整数倍
					return "NotDiv";
				} else {
					
					
					return "OK";
				}
			}
		}
		return null;
	}
	
	String availCashAmountTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String availCashAmountStr = (String)event.getProperty("changedText");
			this.availCashAmount = availCashAmountStr;
			this.availCashAmountField.setValue(availCashAmountStr);
		}
		return null;
	}
	
	@Command
	String setReadChecked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {

			if (this.readChecked == true) {
				this.readChecked = false;
				this.readCheckedField.setValue(false);
				
				this.commitBtnField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
				this.commitBtnField.setAttribute(AttributeKeys.enabled, false);
			} else {
				this.readChecked = true;
				this.readCheckedField.setValue(true);
				
				this.commitBtnField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_WHITE);
				this.commitBtnField.setAttribute(AttributeKeys.enabled, true);
			}
			
		}
		return null;
	}
	
	@OnShow
	protected void initData() {
		this.readChecked = true;
		this.readCheckedField.setValue(true);
	}
}
