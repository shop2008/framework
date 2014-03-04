package com.wxxr.mobile.stock.client.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.BeanValidation;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.FieldUpdating;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.CrossFieldValidation;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.model.UserRegCallback;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name = "userRegPage", withToolbar = true, description = "快速注册")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.quick_register_layout")
public abstract class UserRegPage extends PageBase {

	static Trace log = Trace.register(UserRegPage.class);
	@Field(valueKey = "text", binding = "${callback.userName}")
	String mobileNum;

	@Field(valueKey = "text", binding = "${callback.password}")
	String newPassword;

	@Field(valueKey = "text", binding = "${callback.retypePassword}")
	String reNewPassword;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Field(valueKey = "text", enableWhen = "${checked}", attributes = { @Attribute(name = "textColor", value = "${checked?'resourceId:color/white':'resourceId:color/gray'}") })
	String registerBtn;

	@Bean(type = BindingType.Service)
	IUserLoginManagementService usrService;

	@Bean(type = BindingType.Service)
	IArticleManagementService articleService;
	
	@Bean(type=BindingType.Pojo, express="${articleService!=null?articleService.getRegisterArticle():null}")
	BindableListWrapper<ArticleBean> registerRuleBean;
	
	@Bean
	UserRegCallback callback = new UserRegCallback();

	@Bean
	boolean checked = true;
	/**
	 * 是否阅读了《注册条款》
	 */
	@Field(valueKey = "checked", binding = "${checked}")
	boolean readChecked;

	/**
	 * ,
			updateFields = {
				@FieldUpdating(fields={"oldPsw","newPsw","reNewPsw"},message="请确保输入的密码正确")
			},
			validations={
				@BeanValidation(bean="callback", message="请确保输入的密码正确"),
				@BeanValidation(bean="callback", group=CrossFieldValidation.class, message="新密码和重复新密码必须一致")
			},
	 * @param step
	 * @param event
	 * @param result
	 * @return
	 */
	@Command(updateFields = { @FieldUpdating(fields = { "mobileNum",
			"newPassword", "reNewPassword" }, message = "请输入正确的手机号、密码") }, 
			validations={
					@BeanValidation(bean="callback", message="请确保输入的密码正确")
				}, 
			navigations = {
			@Navigation(on = "StockAppBizException", message = "%m%n", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
					@Parameter(name = "title", value = "提示") }),
			@Navigation(on = "OK", showPage = "userNickSet") })
	@NetworkConstraint
	@ExeGuard(title = "注册", message = "正在注册，请稍候...", silentPeriod = 200, cancellable=false)
	String commit(ExecutionStep step, InputEvent event, Object result) {
		boolean isPhoneNum = Utils.getInstance().isMobileNum(callback.getUserName());
		if(!isPhoneNum) {
			throw new StockAppBizException("请输入正确的手机号码");
		} 
		
		if(callback.getPassword().length() < 6) {
			throw new StockAppBizException("密码长度为6-12位");
		}
		
		if(!callback.getPassword().equals(callback.getRetypePassword())) {
			throw new StockAppBizException("两次输入的密码不一致");
		}
		switch (step) {
		case PROCESS:
			if (usrService != null) {

				AsyncUtils.execRunnableAsyncInUI(new Runnable() {

					@Override
					public void run() {
						
						
						usrService.register(callback.getUserName(),
								callback.getPassword(),
								callback.getRetypePassword());
						usrService.login(callback.getUserName(),
								callback.getPassword());
					}
				});
				
			}
			break;
		case NAVIGATION:
			
			hide();
			return "OK";
		}
		return null;
	}

	/**
	 * 转向《注册规则》详细界面注册规则
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "registerRules",navigations = { @Navigation(on = "OK", showPage = "webPage") })
	CommandResult registerRules(InputEvent event) {
		CommandResult result = new CommandResult();
		if (registerRuleBean != null)
			result.setPayload((registerRuleBean.getData() != null)
					&& (registerRuleBean.getData().size() > 0) ? registerRuleBean
					.getData().get(0).getArticleUrl() : null);
		result.setResult("OK");

		return result;
	}

	/**
	 * 设置CheckBox是否选中
	 * 
	 * @param event
	 *            InputEvent.EVENT_TYPE_CLICK
	 * @return null
	 */
	@Command(commandName = "setReadChecked")
	String setReadChecked(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			checked = !checked;
			registerBean("checked", checked);
		}
		return null;
	}

	@OnUIDestroy
	protected void clearData() {
		callback.setUserName("");
		callback.setPassword("");
		callback.setRetypePassword("");
	}
}
