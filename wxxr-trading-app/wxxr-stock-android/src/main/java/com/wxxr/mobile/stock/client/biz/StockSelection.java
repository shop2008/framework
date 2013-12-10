/**
 * 
 */
package com.wxxr.mobile.stock.client.biz;

import com.wxxr.mobile.core.ui.api.ISelection;

/**
 * @author xijiadeng
 *
 */
public class StockSelection implements ISelection {

	private String market,code,name;
	private int type;
	private long buyPrice;
	
	private String accid = "";
	private boolean virtual;
	
	public StockSelection(){
		market = "";
		code = "";
		name = "";
	}
	public StockSelection(String market, String code, String name){
		this.market = market;
		this.code = code;
		this.name = name;
	}
	
	public StockSelection(String market, String code, String name,long buy){
		this.market = market;
		this.code = code;
		this.name = name;
		this.buyPrice = buy;
	}
	
	public StockSelection(String market, String code, String name, int type){
		this.market = market;
		this.code = code;
		this.name = name;
		this.type = type;
	}
	
	public StockSelection(String accid, boolean virtual){
		this.accid = accid;
		this.virtual = virtual;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	
	
	public boolean getVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}
	
	
}
