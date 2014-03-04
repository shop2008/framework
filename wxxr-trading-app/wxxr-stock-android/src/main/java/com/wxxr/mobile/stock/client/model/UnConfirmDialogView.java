package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name="UnConfirmDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.unconfirm_dialog")
public abstract class UnConfirmDialogView extends ViewBase implements IModelUpdater {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;
	private String moneyAmount;
	
	
	@Command(navigations={@Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type =
			ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }),
			@Navigation(on="SubmitLoading", showDialog="SubmitLoadingDialogView")})
	@ExeGuard(cancellable=true, silentPeriod=200, message="正在处理，请稍候...")
	String done(ExecutionStep step, InputEvent event, Object result) {
		
		switch (step) {
		case PROCESS:
			
			hide();
			tradingService.applyDrawMoney(Long.parseLong(moneyAmount)*100);
			break;
		case NAVIGATION:
			return "SubmitLoading";
		default:
			break;
		}
		
		return null;
		
	}

	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if(tempt != null && "moneyAmount".equals(key)) {
					if (tempt instanceof String) {
						moneyAmount = (String) tempt;
					}
				}
			}
		}
	}
}
