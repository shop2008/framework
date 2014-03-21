package com.wxxr.mobile.stock.app.db;

import java.util.Date;

import com.wxxr.mobile.dao.annotation.Column;
import com.wxxr.mobile.dao.annotation.Entity;
import com.wxxr.mobile.dao.annotation.Id;
import com.wxxr.mobile.dao.annotation.Table;

@Entity
@Table(name="O_STOCK")
public class OptionStock {
	@Id
	private Long id;
	@Column(nullable=false)
	private String stockCode;
	@Column(nullable=false)
	private String mc;
	@Column
	private  Date createDate;
	@Column
	private  Date lastUpdate;
	@Column
	private Long newprice;
	@Column
	private Long risefallrate;
	@Column
	private  Integer power;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Long getNewprice() {
		return newprice;
	}
	public void setNewprice(Long newprice) {
		this.newprice = newprice;
	}
	public Long getRisefallrate() {
		return risefallrate;
	}
	public void setRisefallrate(Long risefallrate) {
		this.risefallrate = risefallrate;
	}
	@Override
	public String toString() {
		return "OptionStock [id=" + id + ", stockCode=" + stockCode + ", mc="
				+ mc + ", createDate=" + createDate + ", lastUpdate="
				+ lastUpdate + ", newprice=" + newprice + ", risefallrate="
				+ risefallrate + ", power=" + power + "]";
	}
	
}

