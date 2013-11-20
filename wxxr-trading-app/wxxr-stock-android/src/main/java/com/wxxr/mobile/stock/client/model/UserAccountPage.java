package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 用户账户
 * @author renwenjie
 *
 */
@View(name="userAccountPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_account_page_layout")
public abstract class UserAccountPage extends PageBase {

	/**
	 * 账户余额
	 */
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.balance:'--'}")
	String userBalance;
	
	/**
	 * 冻结资金
	 */
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.frozen:'--'}")
	String userFreeze;
	
	/**
	 * 可用资金
	 */
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.usableBal:'--'}")
	String userAvalible;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Bean(type=BindingType.Pojo, express="${user!=null?user.userAsset!=null?user.userAsset:null:null}")
	UserAssetBean userAssetBean;
	
	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	@Command(
			commandName="drawCash",
			navigations={
					@Navigation(on="WITHDRAW", showPage="userWithDrawCashPage"),
					@Navigation(on="ALERTBIND", showDialog="unBindCardDialog")
					}
			)
	String drawCash(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			boolean isBindCard = usrService.isBindCard();
			System.out.println("*****"+isBindCard);
			if (isBindCard) {
				return "WITHDRAW";
			} else {
				/**提示用户绑定银行卡*/
				return "ALERTBIND";
			}
		}
		return null;
	}
	
	private void showDialog() {
		getUIContext().getWorkbenchManager().getWorkbench().showPage("unBindCardDialog", null, null);
	}

	/**
	 * 进入收支明细业务界面
	 * @param event
	 * @return
	 */
	@Command(
			commandName="incomeDetail",
			navigations={
					@Navigation(on="OK", 
					showPage="uRealPanelScorePage"
					)
			}
	)
	String incomeDetail(InputEvent event) {
		return "OK";
	}
	
	/**
	 * 进入实盘积分明细
	 * @param event
	 * @return
	 */
	@Command(
			commandName="actualIntegralDetail",
			navigations={
					@Navigation(on="OK", 
					showPage="uRealPanelScorePage"
					)
			}
	)
	String actualIntegralDetail(InputEvent event) {
		return "OK";
	}

}
