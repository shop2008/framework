package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.webkit.WebView;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView;
import com.wxxr.mobile.stock.client.widget.ArticleBodyView.HideProgressListener;

public class HideProgressEventBinding implements IBinding<IView>{

	private WebView control;
	private String fieldName;
	private String commandName;
	private IView pModel;
	
	private HideProgressListener hideProgressListener = new HideProgressListener() {
		
		@Override
		public void hide() {
			IUIComponent field = pModel.getChild(fieldName);
			
			if (field != null) {
				SimpleInputEvent event = new SimpleInputEvent("HideDialog",field);
				field.invokeCommand(commandName, event);
			} else {
				SimpleInputEvent event = new SimpleInputEvent("HideDialog",pModel);
				pModel.invokeCommand(commandName, event);
			}
		}
	};
	
	public HideProgressEventBinding(View view,String cmdName,String field) {
		this.control = (WebView)view;
		this.commandName = cmdName;
		this.fieldName = field;
	}
	@Override
	public void notifyDataChanged(ValueChangedEvent... events) {
		
	}

	@Override
	public void activate(IView model) {
		this.pModel = model;
		if (control instanceof ArticleBodyView) {
			ArticleBodyView articleBodyView = (ArticleBodyView) control;
			articleBodyView.setHideProgressListener(hideProgressListener);
		}
		
	}

	@Override
	public void deactivate() {
		if (control instanceof ArticleBodyView) {
			ArticleBodyView articleBodyView = (ArticleBodyView) control;
			articleBodyView.setHideProgressListener(null);
		}
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public void destroy() {
		deactivate();
		this.control = null;
	}

	@Override
	public void init(IWorkbenchRTContext ctx) {
		
	}

	@Override
	public Object getUIControl() {
		return this.control;
	}

}
