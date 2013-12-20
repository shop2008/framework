package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

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
		IAsyncCallback cb = new IAsyncCallback() {
			
			@Override
			public void success(Object result) {
				//v.clearFocus();
			}
			
			@Override
			public void failed(Object cause) {
				//v.clearFocus();
			}
		};
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}
	
}
