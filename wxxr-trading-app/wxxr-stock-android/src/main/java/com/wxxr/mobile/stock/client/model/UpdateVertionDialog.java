package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.biz.VertionUpdateSelection;
import com.wxxr.mobile.stock.client.service.IGenericContentService;
import com.wxxr.mobile.stock.client.service.IUpdateVertionService;
import com.wxxr.mobile.stock.client.utils.TextContentConvertor;

@View(name="updateVertionDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.update_vertion_dialog")
public abstract class UpdateVertionDialog extends ViewBase implements ISelectionChangedListener{

	String url;
	String updateDescBean = null;
	
	@Field(valueKey="text", binding="${description}",converter="textConvertor")
	String updateDesc;
	
	@Convertor
	TextContentConvertor textConvertor;
	
	@Command
	String downloadApk(InputEvent event) {
		//下载最新Apk
		//AppUtils.getFramework().getService(IGenericContentService.class).startDownloadService(url);
		AppUtils.getFramework().getService(IUpdateVertionService.class).startDownload(url);
		hide();
		return null;
	}
	
	@OnCreate
	void registerListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		ISelection selection = service.getSelection("AppManageView");
		selectionChanged("AppManageView", selection);
		service.addSelectionListener("AppManageView",this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof VertionUpdateSelection) {
			VertionUpdateSelection updateSelection = (VertionUpdateSelection) selection;
			String downloadUrl = updateSelection.getDownloadUrl();
			//System.out.println("---download url---"+downloadUrl);
			String description = updateSelection.getUpdateDesc();
			registerBean("description", description);
			url = downloadUrl;
		}
	}
	
	@OnDestroy
	void unregisterListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().removeSelectionListener("AppManageView",this);
	}
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
}
