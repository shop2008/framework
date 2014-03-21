package com.wxxr.mobile.stock.client.biz;

import com.wxxr.mobile.core.ui.api.ISelection;

public class AccidSelection implements ISelection {

	private String accid = "";
	private boolean virtual;
	private int position = -1;
	private String assetType = "";
	private boolean isSelected = false;
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public AccidSelection(){}
	public AccidSelection(String accid, boolean virtual){
		this.accid = accid;
		this.virtual = virtual;
	}
	
	public AccidSelection(String accid, int position){
		this.accid = accid;
		this.position = position;
	}
	public AccidSelection(String accid, int position, boolean selected){
		this.accid = accid;
		this.position = position;
		this.isSelected = selected;
	}
	
	public AccidSelection(String accid, boolean virtual,int position){
		this.accid = accid;
		this.virtual = virtual;
		this.position = position;
	}
	
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
}
