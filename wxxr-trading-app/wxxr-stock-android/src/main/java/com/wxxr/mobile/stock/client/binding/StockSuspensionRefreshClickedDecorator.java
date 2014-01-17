package com.wxxr.mobile.stock.client.binding;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;

public class StockSuspensionRefreshClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	private long handleTime = 0;
	
	public StockSuspensionRefreshClickedDecorator(InputEventDecorator decor) {
		this.next = decor;
	}

	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final IView v = context.getViewModel();
		setProgressFieldStatus(v, true);
		handleTime = System.currentTimeMillis();

		IAsyncCallback cb = new IAsyncCallback() {

			@Override
			public void success(Object result) {
				long time = System.currentTimeMillis();
				if (time - handleTime < 1000) {
					AppUtils.invokeLater(new Runnable() {

						@Override
						public void run() {
							setProgressFieldStatus(v, false);
						}
					}, 500, TimeUnit.MILLISECONDS);
				} else {
					setProgressFieldStatus(v, false);
				}
			}

			@Override
			public void failed(Object cause) {
				long time = System.currentTimeMillis();
				if (time - handleTime < 1000) {
					AppUtils.invokeLater(new Runnable() {

						@Override
						public void run() {
							setProgressFieldStatus(v, false);
						}
					}, 500, TimeUnit.MILLISECONDS);
				} else {
					setProgressFieldStatus(v, false);
				}
			}
		};
		((SimpleInputEvent) event)
				.addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

	private void setProgressFieldStatus(IView v, boolean status) {
		IDataField<Boolean> fieldProgress = v.getChild("progressSus",
				IDataField.class);
		IDataField<Boolean> fieldRefresh = v.getChild("refreshSus",
				IDataField.class);
		if (fieldProgress != null)
			fieldProgress.setValue(status);
		if (fieldRefresh != null)
			fieldRefresh.setValue(!status);
	}
}
