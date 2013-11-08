package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * ������ϸ
 * @author zhengjincheng
 *
 */
@XmlRootElement(name="TradeDetail")
public class TradeDetailVO implements Serializable {
	String quotationCode;// ��Ʊ����
	String marketCode;
	String HqTime;//HHMM
	String HqDate;
	int jyfx;//�� 1 (����)��ƽ�� 0����-1�����̣�
	Long price;//�ɽ���
	Long buyvol=0L;//���̳ɽ���
	Long sellvol=0L;//���̳ɽ���

    @XmlElement
	public Long getBuyvol() {
		return buyvol;
	}
    
	public void setBuyvol(Long buyvol) {
		this.buyvol = buyvol;
	}
    @XmlElement
	public Long getSellvol() {
		return sellvol;
	}
	public void setSellvol(Long sellvol) {
		this.sellvol = sellvol;
	}
	@XmlElement
	public String getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}
	@XmlElement
	public String getHqTime() {
		return HqTime;
	}
	public void setHqTime(String hqTime) {
		HqTime = hqTime;
	}
	@XmlElement
	public String getHqDate() {
		return HqDate;
	}
	public void setHqDate(String hqDate) {
		HqDate = hqDate;
	}
	@XmlElement
	public int getJyfx() {
		return jyfx;
	}
	public void setJyfx(int jyfx) {
		this.jyfx = jyfx;
	}
	@XmlElement
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	@XmlElement
	public String getQuotationCode() {
		return quotationCode;
	}
	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradeDetailVO [quotationCode=" + quotationCode
				+ ", marketCode=" + marketCode + ", HqTime=" + HqTime
				+ ", HqDate=" + HqDate + ", jyfx=" + jyfx + ", price=" + price
				+ ", buyvol=" + buyvol + ", sellvol=" + sellvol + "]";
	}
}
