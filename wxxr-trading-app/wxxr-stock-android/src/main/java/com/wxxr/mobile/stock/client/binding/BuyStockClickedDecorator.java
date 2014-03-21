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

public class BuyStockClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	
	public BuyStockClickedDecorator(InputEventDecorator decor){
		this.next = decor;
	}
	
	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final View v = (View)context.getUIControl();
		if(v != null)
			v.setEnabled(false);
		((SimpleInputEvent)event).addProperty(InputEvent.PROPERTY_SOURCE_VIEW, context.getViewModel());
		IAsyncCallback<Object> cb = new IAsyncCallback<Object>() {
			
			@Override
			public void success(Object result) {
				if(v != null)
					v.setEnabled(true);
			}
			
			@Override
			public void failed(Throwable cause) {
				if(v != null)
					v.setEnabled(true);
			}

			@Override
			public void cancelled() {
				if(v != null)
					v.setEnabled(true);
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
