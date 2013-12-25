package com.wxxr.mobile.stock.client.biz;

import com.wxxr.mobile.core.ui.api.ISelection;

public class VertionUpdateSelection implements ISelection {
	
	private String downloadUrl;

	
	public VertionUpdateSelection(String url) {
		this.downloadUrl = url;
	}
	
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
