package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IClientInfoService;
import com.wxxr.mobile.stock.client.service.IGenericContentService;

@View(name="AlertVertionUpdate", provideSelection=true)
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.alert_vertion_update")
public abstract class AlertVertionUpdate extends ViewBase  implements ISelectionChangedListener{

	String url;
	
	@Field(valueKey="checked", binding="${checked}")
	boolean readChecked;
	
	@Bean
	boolean checked = false;

	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	@Command
	String downloadApk(InputEvent event) {
		//下载最新Apk
		AppUtils.getFramework().getService(IGenericContentService.class).startDownloadService(url);
		hide();
		return null;
	}
	
	@OnCreate
	void registerListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		ISelection selection = service.getSelection("home");
		selectionChanged("home", selection);
		service.addSelectionListener("home",this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof VertionUpdateSelection) {
			VertionUpdateSelection updateSelection = (VertionUpdateSelection) selection;
			String downloadUrl = updateSelection.getDownloadUrl();
			//System.out.println("---download url---"+downloadUrl);
			url = downloadUrl;
		}
	}
	
	@OnDestroy
	void unregisterListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().removeSelectionListener("home",this);
	}
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String setReadChecked(InputEvent event) {
		checked = !checked;
		registerBean("checked", checked);
		AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(!checked);
		return null;
	}
	
	
}
