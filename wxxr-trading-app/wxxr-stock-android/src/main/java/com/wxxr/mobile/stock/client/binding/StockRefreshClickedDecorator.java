package com.wxxr.mobile.stock.client.binding;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;

public class StockRefreshClickedDecorator implements InputEventDecorator {

	private final InputEventDecorator next;
	private long handleTime = 0;

	public StockRefreshClickedDecorator(InputEventDecorator decor) {
		this.next = decor;
	}

	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final IView v = context.getViewModel();
		setProgressFieldStatus(v, true);
		handleTime = System.currentTimeMillis();

		IAsyncCallback<Object> cb = new IAsyncCallback<Object>() {

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
			public void failed(Throwable cause) {
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
			public void cancelled() {
				setProgressFieldStatus(v, false);
			}

			@Override
			public void setCancellable(ICancellable cancellable) {
				
			}
		};
		((SimpleInputEvent) event)
				.addProperty(InputEvent.PROPERTY_CALLBACK, cb);
		this.next.handleEvent(context, event);
	}

	private void setProgressFieldStatus(IView v, boolean status) {
		IDataField<Boolean> fieldProgress = v.getChild("progress",
				IDataField.class);
		IDataField<Boolean> fieldRefresh = v.getChild("refresh",
				IDataField.class);
		if (fieldProgress != null)
			fieldProgress.setValue(status);
		if (fieldRefresh != null)
			fieldRefresh.setValue(!status);
	}
}
