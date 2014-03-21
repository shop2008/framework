package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 股票、指数vo
 * @author wangxuyang
 *
 */
@XmlRootElement(name = "STOCK")
public class StockBaseInfoVO implements Serializable{
	@XmlElement(name="name")
	private String name; //股票或指数 名称
	@XmlElement(name="mc")
	private String mc;//市场代码： SH，SZ各代表上海，深圳。
	@XmlElement(name="abbr")
	private String abbr;// 股票名称的中文拼音的首字母 如：“新大陆”  为 “xdl”
	@XmlElement(name="code")
	private String code;  //股票或指数 代码
	@XmlElement(name="type")
	private String type;// 0:指数，1：A股，2：B股

	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "StockBaseInfoVO [name=" + name + ", mc=" + mc + ", abbr="
				+ abbr + ", code=" + code + ", type=" + type + "]";
	}
	
}
