package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * �ɷֹ�VO
 * @author juyao
 *
 */
@XmlRootElement(name="ComponentStocks")
public class ComponentStocksVO implements Serializable {
	@XmlElement(name = "name")
	private String name;//股票名称
	@XmlElement(name = "code")
	private String code;//股票代码
	@XmlElement(name = "risefallrate")
	private Long risefallrate;//涨跌幅
	@XmlElement(name = "count")
	private Long count;//���׵���
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "ComponentStocksVO [name=" + name + ", code=" + code
				+ ", risefallrate=" + risefallrate + ", count=" + count + "]";
	}	
}
