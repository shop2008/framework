package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 日K线数据:周K线数据:月K线数据
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockLineBean")
public class StockLine {
	private String date;
	private String time;//供测试使用
	private Long close;// 昨收
	private Long open;// 开盘
	private Long high;// 最高
	private Long low;// 最低
	private Long price;// 收盘价
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long start;//开始
	private Long limit;//偏移量
	
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
