package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IClientInfoService;
import com.wxxr.mobile.stock.client.service.IUpdateVertionService;
import com.wxxr.mobile.stock.client.utils.TextContentConvertor;

@View(name="AlertVertionUpdate", provideSelection=true)
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.alert_vertion_update")
public abstract class AlertVertionUpdate extends ViewBase  implements ISelectionChangedListener{

	String url;
	
	/*@Field(valueKey="checked", binding="${checked}")
	boolean readChecked;*/
	
	boolean checked = false;

	@Field(valueKey="text", binding="${description}",converter="textConvertor")
	String updateDesc;
	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	@Convertor
	TextContentConvertor textConvertor;
	@OnShow
	void initData() {
		//checked = AppUtils.getFramework().getService(IClientInfoService.class).alertUpdateEnabled();
		checked = false;
		registerBean("checked", checked);
	}
	
	@Command
	String downloadApk(InputEvent event) {
		//下载最新Apk
		AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(false);
		AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(false);
		AppUtils.getFramework().getService(IUpdateVertionService.class).startDownload(url);
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
			String description = updateSelection.getUpdateDesc();
			registerBean("description", description);
			url = downloadUrl;
		//	System.out.println("downloadUrl--->"+url);
		}
	}
	
	@OnDestroy
	void unregisterListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().removeSelectionListener("home",this);
	}
	@Command
	String cancel(InputEvent event) {
		AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(false);
		AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(false);
		hide();
		return null;
	}
	
	@OnHide
	void onHideOption() {
		AppUtils.getFramework().getService(IClientInfoService.class).updateDialogShowStatus(false);
		AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(false);
	}
	/*@Command
	String setReadChecked(InputEvent event) {
		checked = !checked;
		registerBean("checked", checked);
		if(checked) {
			AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(false);
		} else {
			AppUtils.getFramework().getService(IClientInfoService.class).setAlertUpdateEnabled(true);
		}
		return null;
	}
	*/
	
}
