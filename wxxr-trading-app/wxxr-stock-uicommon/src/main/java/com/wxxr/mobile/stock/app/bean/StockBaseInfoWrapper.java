/**
 * 
 */
package com.wxxr.mobile.stock.app.bean;

import java.util.Date;

import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author wangxuyang
 *
 */
public class StockBaseInfoWrapper {
	private StockBaseInfo stock;
	private boolean added;
	public StockBaseInfoWrapper(StockBaseInfo stock, boolean added) {
		if (stock==null) {
			throw new IllegalArgumentException();
		}
		this.stock = stock;
		this.added = added;
	}
	
	public String getName() {
		return stock.getName();
	}

	public String getMc() {
		return stock.getMc();
	}

	public String getAbbr() {
		return stock.getAbbr();
	}
	public String getCode() {
		return stock.getCode();
	}
	
	public Long getCapital() {
		return stock.getCapital();
	}

	public Long getMarketCapital() {
		return stock.getMarketCapital();
	}
	
	public int getType() {
		return stock.getType();
	}

	public Long getEps() {
		return stock.getEps();
	}

	public Date getEps_report_date() {
		return stock.getEps_report_date();
	}

	public String getCorpCode() {
		return stock.getCorpCode();
	}
	public boolean getAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

}
