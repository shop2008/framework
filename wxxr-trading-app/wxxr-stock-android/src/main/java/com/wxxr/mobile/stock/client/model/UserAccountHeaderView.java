package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
@View(name="userAccountHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.user_account_header_layout")
public abstract class UserAccountHeaderView extends ViewBase {

	/**账户余额*/
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.balance:'--'}",converter="stockL2StrConvertor")
	String userBalance;
	
	/**冻结资金*/
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.frozen:'--'}", converter="stockL2StrConvertor")
	String userFreeze;
	
	/**可用资金*/
	@Field(valueKey="text", binding="${userAssetBean!=null?userAssetBean.usableBal:'--'}", converter="stockL2StrConvertor")
	String userAvalible;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo, express="${usrService.userAssetBean}")
	UserAssetBean userAssetBean;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%.2f"),
					@Parameter(name="formatUnit", value="元"),
					@Parameter(name="multiple", value="100.0f")
			})
	StockLong2StringConvertor stockL2StrConvertor;
}
