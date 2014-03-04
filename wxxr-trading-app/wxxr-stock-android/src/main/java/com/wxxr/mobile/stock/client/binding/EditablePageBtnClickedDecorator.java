package com.wxxr.mobile.stock.client.binding;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class EditablePageBtnClickedDecorator implements InputEventDecorator {
	private final InputEventDecorator next;
	
	public EditablePageBtnClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final View v = (View)context.getUIControl();
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_SOURCE_VIEW,v);
		IAsyncCallback<Object> cb = new IAsyncCallback<Object>() {
			
			@Override
			public void success(Object result) {
				v.setFocusable(false);
				v.setFocusableInTouchMode(false);
				v.clearFocus();
			}
			
			@Override
			public void failed(Throwable cause) {
				v.setFocusable(false);
				v.setFocusableInTouchMode(false);
				v.clearFocus();
			}

			@Override
			public void cancelled() {
				v.setFocusable(false);
				v.setFocusableInTouchMode(false);
				v.clearFocus();
			}

			@Override
			public void setCancellable(ICancellable cancellable) {
				
			}
		};
		List<IAsyncCallback> list = (List<IAsyncCallback>)((SimpleInputEvent)event).getProperty(InputEvent.PROPERTY_ATTACH_CALLBACK);
		if(list != null)
			list.add(cb);
		else {
			list = new ArrayList<IAsyncCallback>();
			list.add(cb);
			((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_ATTACH_CALLBACK, list);
		}
			
		this.next.handleEvent(context, event);
	}
	
}
