/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import android.content.Intent;

import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Workbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.common.WorkbenchBase;

/**
 * @author neillin
 *
 */
@View
@Workbench(title="Mobile Stock client",description="Mobile Stock client description",exceptionNavigations={
		@Navigation(on="LoginFailedException",message="resourceId:message/login_required",params={
				@Parameter(name="title",value="安全验证"),
				@Parameter(name="onOK",value="确 认"),
				@Parameter(name="autoClosed",type=ValueType.INETGER,value="5")}),
		@Navigation(on="UserLoginRequiredException",message="resourceId:message/login_required",params={
						@Parameter(name="title",value="安全验证"),
						@Parameter(name="onOK",value="确 认"),
						@Parameter(name="autoClosed",type=ValueType.INETGER,value="5")}),
		@Navigation(on="RequiredNetNotAvailablexception",message="resourceId:message/network_not_available",params={
				@Parameter(name="title",value="网络异常"),
				@Parameter(name="onOK",value="确 认"),
				@Parameter(name="autoClosed",type=ValueType.INETGER,value="5")
		})
})
public class StockWorkbench extends WorkbenchBase {

	public StockWorkbench(IWorkbenchRTContext ctx) {
		super(ctx);
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.WorkbenchBase#showHomePage()
	 */
	@Override
	public void showHomePage() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(IAndroidPageNavigator.PARAM_KEY_INTENT_FLAG, String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		super.showPage(HOME_PAGE_ID,map,null);
	}

}
