/**
 * 
 */
package com.wxxr.mobile.stock.app.db;

import java.util.Date;

import com.wxxr.mobile.dao.annotation.Column;
import com.wxxr.mobile.dao.annotation.Entity;
import com.wxxr.mobile.dao.annotation.Id;
import com.wxxr.mobile.dao.annotation.Table;

/**
 * @author wangxuyang
 *
 */
@Entity
@Table(name="stock_info")
public class StockInfo {
	@Id
	private Long id;
	@Column(unique=true,nullable=false)
	private String code;  //股票或指数 代码
	@Column(unique=true,nullable=false)
	private String name;//股票或指数 名称
	@Column(nullable=false)
	private String mc;//市场代码： SH，SZ各代表上海，深圳。
	@Column(nullable=false)
	private String abbr;// 股票名称的中文拼音的首字母 如：“新大陆”  为 “xdl”
	@Column
	private int type;// 0:指数，1：A股，2：B股
	@Column
	private Long capital;// 总股本
	@Column
	private Long marketCapital;// 流通盘
	@Column
	private Long eps;//
	@Column
	private Date eps_report_date;//报告时间
	@Column
	private String corpCode;// 上市公司代码 
	
	public String getName() {
		return name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getCapital() {
		return capital;
	}
	public void setCapital(Long capital) {
		this.capital = capital;
	}
	public Long getMarketCapital() {
		return marketCapital;
	}
	public void setMarketCapital(Long marketCapital) {
		this.marketCapital = marketCapital;
	}
	public Long getEps() {
		return eps;
	}
	public void setEps(Long eps) {
		this.eps = eps;
	}
	public Date getEps_report_date() {
		return eps_report_date;
	}
	public void setEps_report_date(Date eps_report_date) {
		this.eps_report_date = eps_report_date;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	
	
}
