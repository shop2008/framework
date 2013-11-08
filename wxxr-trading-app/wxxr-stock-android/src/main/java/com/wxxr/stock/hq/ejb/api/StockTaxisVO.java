package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * �ǵ�����VO
 * 
 * @author juyao
 * 
 */
@XmlRootElement(name = "StockTaxis")
public class StockTaxisVO implements Serializable {
	private String name;// 无限新锐
	private String code;
	private String market;
	private Long newprice;// 最新
	private Long risefallrate;// 涨跌幅
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long profitrate;// 市盈率
	private Long handrate;// 换手率
	private Long marketvalue;// 市值
	private Long lb;// 量比

	public Long getLb() {
		return lb;
	}

	public void setLb(Long lb) {
		this.lb = lb;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getHandrate() {
		return handrate;
	}

	public void setHandrate(Long handrate) {
		this.handrate = handrate;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Long getMarketvalue() {
		return marketvalue;
	}

	public void setMarketvalue(Long marketvalue) {
		this.marketvalue = marketvalue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNewprice() {
		return newprice;
	}

	public void setNewprice(Long newprice) {
		this.newprice = newprice;
	}

	public Long getProfitrate() {
		return profitrate;
	}

	public void setProfitrate(Long profitrate) {
		this.profitrate = profitrate;
	}

	public Long getRisefallrate() {
		return risefallrate;
	}

	public void setRisefallrate(Long risefallrate) {
		this.risefallrate = risefallrate;
	}

	public Long getSecuamount() {
		return secuamount;
	}

	public void setSecuamount(Long secuamount) {
		this.secuamount = secuamount;
	}

	public Long getSecuvolume() {
		return secuvolume;
	}

	public void setSecuvolume(Long secuvolume) {
		this.secuvolume = secuvolume;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockTaxisVO [name=" + name + ", code=" + code + ", market="
				+ market + ", newprice=" + newprice + ", risefallrate="
				+ risefallrate + ", secuvolume=" + secuvolume + ", secuamount="
				+ secuamount + ", profitrate=" + profitrate + ", handrate="
				+ handrate + ", marketvalue=" + marketvalue + ", lb=" + lb
				+ "]";
	}
}
