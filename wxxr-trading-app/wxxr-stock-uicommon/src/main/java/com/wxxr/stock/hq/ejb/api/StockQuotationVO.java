package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 行情数据-五档盘口VO
 * 
 * @author juyao
 */
@XmlRootElement(name = "")
public class StockQuotationVO implements Serializable {
	// datetime 2011-12-27/13:26:56"
	@XmlElement(name = "code")
	private String code;// 股票或指数 代码
	@XmlElement(name = "market")
	private String market;// 市场代码： SH，SZ各代表上海，深圳。
	@XmlElement(name = "datetime")
	private String datetime;
	@XmlElement(name = "close")
	private Long close;// 昨收
	@XmlElement(name = "open")
	private Long open;// 开盘
	@XmlElement(name = "high")
	private Long high;// 最高
	@XmlElement(name = "low")
	private Long low;// 最低
	@XmlElement(name = "newprice")
	private Long newprice;// 最新
	@XmlElement(name = "averageprice")
	private Long averageprice;// 均价
	@XmlElement(name = "secuvolume")
	private Long secuvolume;// 成交量
	@XmlElement(name = "secuamount")
	private Long secuamount;// 成交额
	@XmlElement(name = "lb")
	private Long lb;// 量比
	@XmlElement(name = "profitrate")
	private Long profitrate;// 市盈率
	@XmlElement(name = "handrate")
	private Long handrate;// 换手率
	@XmlElement(name = "risefallrate")
	private Long risefallrate;// 涨跌幅
	@XmlElement(name = "marketvalue")
	private Long marketvalue;// 市值
	@XmlElement(name = "capital")
	private Long capital;// 流通盘
	@XmlElement(name = "buyprice1")
	private Long buyprice1;// 买1
	@XmlElement(name = "buyprice2")
	private Long buyprice2;// 买2
	@XmlElement(name = "buyprice3")
	private Long buyprice3;// 买3
	@XmlElement(name = "buyprice4")
	private Long buyprice4;// 买4
	@XmlElement(name = "buyprice5")
	private Long buyprice5;// 买5
	@XmlElement(name = "buyvolume1")
	private Long buyvolume1;// 买1量
	@XmlElement(name = "buyvolume2")
	private Long buyvolume2;// 买2量
	@XmlElement(name = "buyvolume3")
	private Long buyvolume3;// 买3量
	@XmlElement(name = "buyvolume4")
	private Long buyvolume4;// 买4量
	@XmlElement(name = "buyvolume5")
	private Long buyvolume5;// 买5量
	@XmlElement(name = "sellprice1")
	private Long sellprice1;// 卖1
	@XmlElement(name = "sellprice2")
	private Long sellprice2;// 卖2
	@XmlElement(name = "sellprice3")
	private Long sellprice3;// 卖3
	@XmlElement(name = "sellprice4")
	private Long sellprice4;// 卖4
	@XmlElement(name = "sellprice5")
	private Long sellprice5;// 卖5
	@XmlElement(name = "sellvolume1")
	private Long sellvolume1;// 卖1量
	@XmlElement(name = "sellvolume2")
	private Long sellvolume2;// 卖2量
	@XmlElement(name = "sellvolume3")
	private Long sellvolume3;// 卖3量
	@XmlElement(name = "sellvolume4")
	private Long sellvolume4;// 卖4量
	@XmlElement(name = "sellvolume5")
	private Long sellvolume5;// 卖5量



	// 以下为指数属性
	@XmlElement(name = "ppjs")
	private Long ppjs;// 平盘家数
	@XmlElement(name = "szjs")
	private Long szjs;// 上涨家数
	@XmlElement(name = "xdjs")
	private Long xdjs;// 下跌家数

	// 股票买盘，卖盘
	@XmlElement(name = "sellsum")
	private Long sellsum;// 买盘
	@XmlElement(name = "buysum")
	private Long buysum;// 卖盘
	// 涨跌额
	@XmlElement(name = "change")
	private Long change;
	// status 1：正常 ：2 ：停牌
	@XmlElement(name = "status")
	private Long status;
	
	
	public Long getChange() {
		return change;
	}
	public void setChange(Long change) {
		this.change = change;
	}
	
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	public Long getSellsum() {
		return sellsum;
	}

	public void setSellsum(Long sellsum) {
		this.sellsum = sellsum;
	}

	
	public Long getBuysum() {
		return buysum;
	}

	public void setBuysum(Long buysum) {
		this.buysum = buysum;
	}

	
	public Long getAverageprice() {
		return averageprice;
	}

	public void setAverageprice(Long averageprice) {
		this.averageprice = averageprice;
	}

	
	public Long getPpjs() {
		return ppjs;
	}

	public void setPpjs(Long ppjs) {
		this.ppjs = ppjs;
	}

	
	public Long getSzjs() {
		return szjs;
	}

	public void setSzjs(Long szjs) {
		this.szjs = szjs;
	}

	
	public Long getXdjs() {
		return xdjs;
	}

	public void setXdjs(Long xdjs) {
		this.xdjs = xdjs;
	}

	
	public Long getBuyprice1() {
		return buyprice1;
	}

	public void setBuyprice1(Long buyprice1) {
		this.buyprice1 = buyprice1;
	}

	
	public Long getBuyprice2() {
		return buyprice2;
	}

	public void setBuyprice2(Long buyprice2) {
		this.buyprice2 = buyprice2;
	}

	
	public Long getBuyprice3() {
		return buyprice3;
	}

	public void setBuyprice3(Long buyprice3) {
		this.buyprice3 = buyprice3;
	}

	
	public Long getBuyprice4() {
		return buyprice4;
	}

	public void setBuyprice4(Long buyprice4) {
		this.buyprice4 = buyprice4;
	}

	
	public Long getBuyprice5() {
		return buyprice5;
	}

	public void setBuyprice5(Long buyprice5) {
		this.buyprice5 = buyprice5;
	}

	
	public Long getBuyvolume1() {
		return buyvolume1;
	}

	public void setBuyvolume1(Long buyvolume1) {
		this.buyvolume1 = buyvolume1;
	}

	
	public Long getBuyvolume2() {
		return buyvolume2;
	}

	public void setBuyvolume2(Long buyvolume2) {
		this.buyvolume2 = buyvolume2;
	}

	
	public Long getBuyvolume3() {
		return buyvolume3;
	}

	public void setBuyvolume3(Long buyvolume3) {
		this.buyvolume3 = buyvolume3;
	}

	
	public Long getBuyvolume4() {
		return buyvolume4;
	}

	public void setBuyvolume4(Long buyvolume4) {
		this.buyvolume4 = buyvolume4;
	}

	
	public Long getBuyvolume5() {
		return buyvolume5;
	}

	public void setBuyvolume5(Long buyvolume5) {
		this.buyvolume5 = buyvolume5;
	}

	
	public Long getCapital() {
		return capital;
	}

	public void setCapital(Long capital) {
		this.capital = capital;
	}

	
	public Long getClose() {
		return close;
	}

	public void setClose(Long close) {
		this.close = close;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	
	public Long getHandrate() {
		return handrate;
	}

	public void setHandrate(Long handrate) {
		this.handrate = handrate;
	}

	
	public Long getHigh() {
		return high;
	}

	public void setHigh(Long high) {
		this.high = high;
	}

	
	public Long getLb() {
		return lb;
	}

	public void setLb(Long lb) {
		this.lb = lb;
	}

	
	public Long getLow() {
		return low;
	}

	public void setLow(Long low) {
		this.low = low;
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

	
	public Long getNewprice() {
		return newprice;
	}

	public void setNewprice(Long newprice) {
		this.newprice = newprice;
	}

	
	public Long getOpen() {
		return open;
	}

	public void setOpen(Long open) {
		this.open = open;
	}

	
	public Long getProfitrate() {
		return profitrate;
	}

	public void setProfitrate(Long profitrate) {
		this.profitrate = profitrate;
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

	
	public Long getSellprice1() {
		return sellprice1;
	}

	public void setSellprice1(Long sellprice1) {
		this.sellprice1 = sellprice1;
	}

	
	public Long getSellprice2() {
		return sellprice2;
	}

	public void setSellprice2(Long sellprice2) {
		this.sellprice2 = sellprice2;
	}

	
	public Long getSellprice3() {
		return sellprice3;
	}

	public void setSellprice3(Long sellprice3) {
		this.sellprice3 = sellprice3;
	}

	
	public Long getSellprice4() {
		return sellprice4;
	}

	public void setSellprice4(Long sellprice4) {
		this.sellprice4 = sellprice4;
	}

	
	public Long getSellprice5() {
		return sellprice5;
	}

	public void setSellprice5(Long sellprice5) {
		this.sellprice5 = sellprice5;
	}

	
	public Long getSellvolume1() {
		return sellvolume1;
	}

	public void setSellvolume1(Long sellvolume1) {
		this.sellvolume1 = sellvolume1;
	}

	
	public Long getSellvolume2() {
		return sellvolume2;
	}

	public void setSellvolume2(Long sellvolume2) {
		this.sellvolume2 = sellvolume2;
	}

	
	public Long getSellvolume3() {
		return sellvolume3;
	}

	public void setSellvolume3(Long sellvolume3) {
		this.sellvolume3 = sellvolume3;
	}

	
	public Long getSellvolume4() {
		return sellvolume4;
	}

	public void setSellvolume4(Long sellvolume4) {
		this.sellvolume4 = sellvolume4;
	}

	
	public Long getSellvolume5() {
		return sellvolume5;
	}

	public void setSellvolume5(Long sellvolume5) {
		this.sellvolume5 = sellvolume5;
	}

	
	public Long getRisefallrate() {
		return risefallrate;
	}

	public void setRisefallrate(Long risefallrate) {
		this.risefallrate = risefallrate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockQuotationVO [code=" + code + ", market=" + market
				+ ", datetime=" + datetime + ", close=" + close + ", open="
				+ open + ", high=" + high + ", low=" + low + ", newprice="
				+ newprice + ", averageprice=" + averageprice + ", secuvolume="
				+ secuvolume + ", secuamount=" + secuamount + ", lb=" + lb
				+ ", profitrate=" + profitrate + ", handrate=" + handrate
				+ ", risefallrate=" + risefallrate + ", marketvalue="
				+ marketvalue + ", capital=" + capital + ", buyprice1="
				+ buyprice1 + ", buyprice2=" + buyprice2 + ", buyprice3="
				+ buyprice3 + ", buyprice4=" + buyprice4 + ", buyprice5="
				+ buyprice5 + ", buyvolume1=" + buyvolume1 + ", buyvolume2="
				+ buyvolume2 + ", buyvolume3=" + buyvolume3 + ", buyvolume4="
				+ buyvolume4 + ", buyvolume5=" + buyvolume5 + ", sellprice1="
				+ sellprice1 + ", sellprice2=" + sellprice2 + ", sellprice3="
				+ sellprice3 + ", sellprice4=" + sellprice4 + ", sellprice5="
				+ sellprice5 + ", sellvolume1=" + sellvolume1
				+ ", sellvolume2=" + sellvolume2 + ", sellvolume3="
				+ sellvolume3 + ", sellvolume4=" + sellvolume4
				+ ", sellvolume5=" + sellvolume5 + ", ppjs=" + ppjs + ", szjs="
				+ szjs + ", xdjs=" + xdjs + ", sellsum=" + sellsum
				+ ", buysum=" + buysum + ", change=" + change + ", status="
				+ status + "]";
	}
}
