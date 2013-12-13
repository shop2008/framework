package com.wxxr.mobile.stock.client.binding;

import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class BuyStockClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	
	public BuyStockClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final IView v = context.getViewModel();
		IAsyncCallback cb = new IAsyncCallback() {
			
			@Override
			public void success(Object result) {
				if(v != null)
					v.hide();
			}
			
			@Override
			public void failed(Object cause) {
			}
		};
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

}
