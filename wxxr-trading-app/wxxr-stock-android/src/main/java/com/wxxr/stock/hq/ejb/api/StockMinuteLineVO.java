package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ��ʱ�����VO
 * 
 * @author juyao
 */
@XmlRootElement(name = "StockMinuteLine")
public class StockMinuteLineVO implements Serializable {

	private Long price;// �۸�(���¼�)
	private Long secuvolume;// �ɽ���
	private Long secuamount;// �ɽ���
	private Long avprice;// �ɽ���
	private String hqTime;//����ʱ�䣬Ϊ�˲���
	private Long avgChangeRate;//��ָ֤������ڳ�ָ���ƽ���ǵ��
	@XmlElement
	public String getHqTime() {
		return hqTime;
	}

	public void setHqTime(String hqTime) {
		this.hqTime = hqTime;
	}

	@XmlElement
	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	@XmlElement
	public Long getSecuvolume() {
		return secuvolume;
	}

	public void setSecuvolume(Long secuvolume) {
		this.secuvolume = secuvolume;
	}
	@XmlElement
	public Long getSecuamount() {
		return secuamount;
	}

	public void setSecuamount(Long secuamount) {
		this.secuamount = secuamount;
	}
	@XmlElement
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
	@XmlElement
	public Long getAvgChangeRate() {
		return avgChangeRate;
	}

	public void setAvgChangeRate(Long avgChangeRate) {
		this.avgChangeRate = avgChangeRate;
	}
}
