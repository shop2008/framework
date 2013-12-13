package com.wxxr.mobile.stock.client.binding;

import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class StockRefreshClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;

	public StockRefreshClickedDecorator(InputEventDecorator decor) {
		this.next = decor;
	}

	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		IView v = context.getViewModel();
		final IDataField<Boolean> fieldProgress = v.getChild("progress",
				IDataField.class);
		final IDataField<Boolean> fieldRefresh = v.getChild("refresh",
				IDataField.class);
		if (fieldProgress != null)
			fieldProgress.setValue(true);
		if (fieldRefresh != null)
			fieldRefresh.setValue(false);
		IAsyncCallback cb = new IAsyncCallback() {

			@Override
			public void success(Object result) {
				if (fieldProgress != null)
					fieldProgress.setValue(false);
				if (fieldRefresh != null)
					fieldRefresh.setValue(true);
			}

			@Override
			public void failed(Object cause) {
				if (fieldProgress != null)
					fieldProgress.setValue(false);
				if (fieldRefresh != null)
					fieldRefresh.setValue(true);
			}
		};
		((SimpleInputEvent) event)
				.addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

}
