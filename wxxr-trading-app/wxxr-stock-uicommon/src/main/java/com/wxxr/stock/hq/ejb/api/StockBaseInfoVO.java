package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * ��Ʊ��ָ�� VO
 * @author zhengjincheng
 *
 */
@XmlRootElement(name = "STOCK")
public class StockBaseInfoVO implements Serializable{
	@XmlElement(name="name")
	private String name; //��Ʊ��ָ�� ���
	@XmlElement(name="market")
	private String mc;//�г����룺 SH��SZ������Ϻ������ڡ�
	@XmlElement(name="spell")
	private String abbr;// ��Ʊ��Ƶ�����ƴ��������ĸ �磺���´�½��  Ϊ ��xdl��
	@XmlElement(name="code")
	private String code;  //��Ʊ��ָ�� ����
	@XmlElement(name="type")
	private String type;// 0:ָ��1��A�ɣ�2��B��

	
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
