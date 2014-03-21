package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * ��K�����VO:��K�����VO:��K�����VO
 * @author juyao
 *
 */
@XmlRootElement(name="StockLine")
public class StockLineVO implements Serializable  {
	@XmlElement(name = "date")
	private String date;//日期
	@XmlElement(name = "time")
	private String time;//时间
	@XmlElement(name = "close")
	private Long close;//收盘价
	@XmlElement(name = "open")
	private Long open;//开盘价
	@XmlElement(name = "high")
	private Long high;//最高价
	@XmlElement(name = "low")
	private Long low;// 最低价
	@XmlElement(name = "price")
	private Long price;//当前价
	@XmlElement(name = "secuvolume")
	private Long secuvolume;// �ɽ���
	@XmlElement(name = "secuamount")
	private Long secuamount;// �ɽ���
	@XmlElement(name = "start")
	private Long start;//��ʼ
	@XmlElement(name = "limit")
	private Long limit;//ƫ����
	
	public Long getClose() {
		return close;
	}
	public void setClose(Long close) {
		this.close = close;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getHigh() {
		return high;
	}
	public void setHigh(Long high) {
		this.high = high;
	}
	
	public Long getLow() {
		return low;
	}
	public void setLow(Long low) {
		this.low = low;
	}
	
	public Long getOpen() {
		return open;
	}
	public void setOpen(Long open) {
		this.open = open;
	}
	
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
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
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	
	public Long getLimit() {
		return limit;
	}
	public void setLimit(Long limit) {
		this.limit = limit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockLineVO [date=" + date + ", time=" + time + ", close="
				+ close + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", price=" + price + ", secuvolume=" + secuvolume
				+ ", secuamount=" + secuamount + ", start=" + start
				+ ", limit=" + limit + "]";
	}
}
