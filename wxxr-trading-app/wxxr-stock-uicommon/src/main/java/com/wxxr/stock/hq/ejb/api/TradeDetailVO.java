package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * 交易明细
 * @author zhengjincheng
 *
 */
@XmlRootElement(name="TradeDetail")
public class TradeDetailVO implements Serializable {
	@XmlElement(name = "quotationCode")
	String quotationCode;// 股票代码
	@XmlElement(name = "marketCode")
	String marketCode;
	@XmlElement(name = "HqTime")
	String HqTime;//HHMM
	@XmlElement(name = "HqDate")
	String HqDate;
	@XmlElement(name = "jyfx")
	int jyfx;//买 1 (外盘)；平盘 0；卖-1（内盘）
	@XmlElement(name = "price")
	Long price;//成交价
	@XmlElement(name = "buyvol")
	Long buyvol=0L;//买盘成交量
	@XmlElement(name = "sellvol")
	Long sellvol=0L;//买盘成交量

    
	public Long getBuyvol() {
		return buyvol;
	}
    
	public void setBuyvol(Long buyvol) {
		this.buyvol = buyvol;
	}
   
	public Long getSellvol() {
		return sellvol;
	}
	public void setSellvol(Long sellvol) {
		this.sellvol = sellvol;
	}
	
	public String getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	
	public String getHqTime() {
		return HqTime;
	}
	public void setHqTime(String hqTime) {
		HqTime = hqTime;
	}
	
	public String getHqDate() {
		return HqDate;
	}
	public void setHqDate(String hqDate) {
		HqDate = hqDate;
	}
	
	public int getJyfx() {
		return jyfx;
	}
	public void setJyfx(int jyfx) {
		this.jyfx = jyfx;
	}
	
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	
	public String getQuotationCode() {
		return quotationCode;
	}
	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradeDetailVO [quotationCode=" + quotationCode
				+ ", marketCode=" + marketCode + ", HqTime=" + HqTime
				+ ", HqDate=" + HqDate + ", jyfx=" + jyfx + ", price=" + price
				+ ", buyvol=" + buyvol + ", sellvol=" + sellvol + "]";
	}
}
