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
	
	public StockSelection(){
		
	}
	
	public StockSelection(String market, String code, String name){
		this.market = market;
		this.code = code;
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.ISelection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return false;
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
	
	

}
