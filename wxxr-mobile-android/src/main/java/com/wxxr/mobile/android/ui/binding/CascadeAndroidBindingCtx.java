package com.wxxr.mobile.android.ui.binding;

import android.content.Context;
import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

public class CascadeAndroidBindingCtx implements IAndroidBindingContext {
	private final IAndroidBindingContext parent;
	private boolean ready;
	
	public CascadeAndroidBindingCtx(IAndroidBindingContext ctx){
		this.parent = ctx;
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IBindingContext#getWorkbenchManager()
	 */
	public IWorkbenchManager getWorkbenchManager() {
		return parent.getWorkbenchManager();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.android.ui.IAndroidBindingContext#getUIContext()
	 */
	public Context getUIContext() {
		return parent.getUIContext();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.android.ui.IAndroidBindingContext#getBindingControl()
	 */
	public View getBindingControl() {
		return parent.getBindingControl();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.android.ui.IAndroidBindingContext#isOnShow()
	 */
	public boolean isOnShow() {
		return ready && parent.isOnShow();
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @param ready the ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@Override
	public void hideView() {
		this.parent.hideView();
	}

}
