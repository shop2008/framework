package com.wxxr.mobile.stock.client.binding;

import android.view.View;

import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class SellStockClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	
	public SellStockClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final View v = (View)context.getUIControl();
		v.setEnabled(false);
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_SOURCE_VIEW,v);
		IAsyncCallback cb = new IAsyncCallback() {
			
			@Override
			public void success(Object result) {
				v.setEnabled(true);
			}
			
			@Override
			public void failed(Object cause) {
				v.setEnabled(true);
			}
		};
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

}
