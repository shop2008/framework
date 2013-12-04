package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 分时线数据VO
 * 
 * @author juyao
 */
@XmlRootElement(name = "StockMinuteLine")
public class StockMinuteLineVO implements Serializable {
	@XmlElement(name="price")
	private Long price;// 价格(最新价)
	@XmlElement(name="secuvolume")
	private Long secuvolume;// 成交量
	@XmlElement(name="secuamount")
	private Long secuamount;// 成交额
	@XmlElement(name="avprice")
	private Long avprice;// 成交额
	@XmlElement(name="hqTime")
	private String hqTime;//补充时间，为了测试
	@XmlElement(name="avgChangeRate")
	private Long avgChangeRate;//上证指数，和深圳成指数的平均涨跌幅
	
	public String getHqTime() {
		return hqTime;
	}

	public void setHqTime(String hqTime) {
		this.hqTime = hqTime;
	}

	
	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	
	public Long getSecuvolume() {
		return secuvolume;
	}

	public void setSecuvolume(Long secuvolume) {
		this.secuvolume = secuvolume;
	}
	
	public Long getSecuamount() {
		return secuamount;
	}

	public void setSecuamount(Long secuamount) {
		this.secuamount = secuamount;
	}
	
	public Long getAvprice() {
		return avprice;
	}

	public void setAvprice(Long avprice) {
		this.avprice = avprice;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockMinuteLineVO [price=" + price + ", secuvolume="
				+ secuvolume + ", secuamount=" + secuamount + ", avprice="
				+ avprice + ", hqTime=" + hqTime + ", avgChangeRate="+avgChangeRate+" ]";
	}
	
	public Long getAvgChangeRate() {
		return avgChangeRate;
	}

	public void setAvgChangeRate(Long avgChangeRate) {
		this.avgChangeRate = avgChangeRate;
	}
}
