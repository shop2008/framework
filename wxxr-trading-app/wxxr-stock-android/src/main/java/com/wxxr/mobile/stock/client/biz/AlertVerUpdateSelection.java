package com.wxxr.mobile.stock.client.biz;

import com.wxxr.mobile.core.ui.api.ISelection;

public class AlertVerUpdateSelection implements ISelection {

	private boolean isAlertVertionUpdate;
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public AlertVerUpdateSelection(boolean isAlert) {
		this.isAlertVertionUpdate = isAlert;
	}
	
	public boolean isAlertVertionUpdate() {
		return isAlertVertionUpdate;
	}
	public void setAlertVertionUpdate(boolean isAlertVertionUpdate) {
		this.isAlertVertionUpdate = isAlertVertionUpdate;
	}
	
	

}
