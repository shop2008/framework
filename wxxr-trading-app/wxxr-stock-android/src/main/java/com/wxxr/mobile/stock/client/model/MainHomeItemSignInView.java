package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;

@View(name = "MainHomeItemSignInView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.main_home_item_sign_in_view")
public abstract class MainHomeItemSignInView extends ViewBase implements
		IModelUpdater {

	@Bean
	SignInMessageMenuItem signInBean;

	@Field(valueKey = "imageURI", binding = "${'resourceId:drawable/home_sign_in'}")
	String icon;

	@Field(valueKey = "imageURI", visibleWhen = "${signInBean!=null?!signInBean.hasSignIn:false}")
	String hasSignIn;

	@Field(valueKey = "text", binding = "${signInBean!=null?signInBean.title:'--'}")
	String title;

	@Field(valueKey = "text", binding = "${signInBean!=null?signInBean.date:'--'}")
	String date;

	@Field(valueKey = "text", binding = "${signInBean!=null?signInBean.message:'--'}")
	String message;

	@Field(valueKey = "text", binding = "${signInBean!=null?signInBean.signDays:'0'}")
	String signDays;

	@Field(valueKey = "text", visibleWhen = "${signInBean!=null?!signInBean.hasSignIn:false}")
	String signDaysLayout;

	@Field(valueKey = "text", binding = "${'获得'}${signInBean!=null?signInBean.score:'0'}${'实盘积分'}", visibleWhen = "${signInBean!=null?signInBean.hasSignIn:false}")
	String score;

	@Override
	public void updateModel(Object value) {
		if (value instanceof SignInMessageMenuItem) {
			signInBean = (SignInMessageMenuItem) value;
			registerBean("signInBean", value);
		}
	}
}
