/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

/**
 * @author wangxuyang
 *
 */
public class Stock {
	private String code;
	private String name;
	private String pyCode;//拼音代码	
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
	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

}
