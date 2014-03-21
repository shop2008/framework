package com.wxxr.mobile.stock.client.biz;

import com.wxxr.mobile.core.ui.api.ISelection;

public class VertionUpdateSelection implements ISelection {
	
	private String downloadUrl;
	private String updateDesc;
	
	public VertionUpdateSelection(String url,String updateDesc) {
		this.downloadUrl = url;
		this.updateDesc = updateDesc;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


	public String getUpdateDesc() {
		return updateDesc;
	}


	public void setUpdateDesc(String updateDesc) {
		this.updateDesc = updateDesc;
	}
	
}
