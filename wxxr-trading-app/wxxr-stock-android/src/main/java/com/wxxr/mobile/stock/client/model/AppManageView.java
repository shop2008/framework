package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="AppManageView", withToolbar=true, description="管理")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.app_manage_page")
public abstract class AppManageView extends ViewBase {

	@Bean(type=BindingType.Service)
	IUserLoginManagementService loginMgr;
	
	@Bean(type=BindingType.Pojo, express="${loginMgr.myUserInfo}")
	UserBean userInfo;
	
	
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		hide();
		return null;
	}
	/**未登录布局的显示及隐藏*/
	@Field(valueKey="visible", visibleWhen="${userInfo==null?true:false}")
	boolean unLoginBody;

	
	/**登录后布局的显示及隐藏*/
	@Field(valueKey="visible", visibleWhen="${userInfo!=null?true:false}")
	boolean loginedBody;
	
	/**用户头像*/
	@Field(valueKey="imageURI", binding="${userInfo!=null&&userInfo.userPic!=null?userInfo.userPic:'resourceId:drawable/head4'}")
	String userPic;
	
	/**用户昵称*/
	@Field(valueKey="text", binding="${userInfo!=null&&userInfo.nickName!=null?userInfo.nickName:'设置昵称'}")
	String userNickName;
	
	/**用户手机号码*/
	@Field(valueKey="text", binding="${userInfo!=null&&userInfo.phoneNumber!=null?userInfo.phoneNumber:'--'}")
	String userPhoneNum;
	
	/**积分余额*/
	@Field(valueKey="text")//, binding="${}",converter="scoreConvertor")
	String scroeBalance;
	
	/**帐户余额*/
	@Field(valueKey="text", converter="profitConvertor")
	String userBalance;
	
	/**设置是否接收推送消息*/
	@Field(valueKey="checked")
	boolean pushMsgEnabled;
	
	/**版本*/
	@Field(valueKey="text")
	String vertionField;
	@Convertor(params = { @Parameter(name = "format", value = "%.0f"),
			@Parameter(name = "nullString", value = "0") })
	StockLong2StringConvertor scoreConvertor;
	
	@Convertor(params = { @Parameter(name = "format", value = "%.2f"),
			@Parameter(name = "nullString", value = "0.00"),
			@Parameter(name = "multiple", value = "100.00f") })
	StockLong2StringConvertor profitConvertor;
	/**跳转到登录界面*/
	@Command(navigations={@Navigation(on="*", showPage="userLoginPage")})
	String login(InputEvent event) {
		//TODO 跳转到登录界面
		return "*";
	}
	
	/**跳转到用户积分界面*/
	@Command(navigations={@Navigation(on="*", showPage="userLoginPage")})
	String enterUserScorePage(InputEvent event) {
		//TODO 跳转到用户积分界面
		return "*";
	}
	
	/**跳转到用户帐户界面*/
	@Command
	String enterUserAccountPage(InputEvent event) {
		//TODO 跳转到用户帐户界面
		return "*";
	}
	
	/**提取现金*/
	@Command
	String applyMoney(InputEvent event) {
		return null;
	}
	
	/**进入我的主页*/
	@Command
	String enterUserPage(InputEvent event) {
		return null;
	}
	
	/**进入我的认证页面*/
	@Command
	String enterUserAuthPage(InputEvent event) {
		return null;
	}
	
	/**进入用户交易记录界面*/
	@Command
	String enterUserTradeRecordPage(InputEvent event) {
		return null;
	}
	
	/**当切换时会回调此方法*/
	@Command
	String pushMsgStatusChanged(InputEvent event) {
		boolean isChecked = (Boolean) event.getProperty("isChecked");
		System.out.println("---isChecked---"+isChecked);
		return null;
	}
	
	/**给短信放大镜打分*/
	@Command
	String playScoreForApp(InputEvent event) {
		return null;
	}
	
	/**新手指引*/
	@Command
	String playerInstruction(InputEvent event) {
		return null;
	}
	
	/**联系我们*/
	@Command
	String constructUs(InputEvent event) {
		return null;
	}
	
	/**版本*/
	@Command
	String handleVertionClick(InputEvent event) {
		return null;
	}
	
}
