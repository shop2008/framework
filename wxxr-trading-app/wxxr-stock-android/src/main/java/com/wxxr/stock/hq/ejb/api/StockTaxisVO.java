package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * �ǵ�����VO
 * @author juyao
 * 
 */
@XmlRootElement(name="StockTaxis")
public class StockTaxisVO  implements Serializable {
		private String name;//��������
		private String code;
		private String market;
		private Long newprice;//����
		private Long risefallrate;// �ǵ��
		private Long secuvolume;// �ɽ���
		private Long secuamount;// �ɽ���
		private Long profitrate;// ��ӯ��
		private Long handrate;// ������
		private Long marketvalue;// ��ֵ
		private Long lb;// ����
		
		@XmlElement
		public Long getLb() {
			return lb;
		}
		public void setLb(Long lb) {
			this.lb = lb;
		}
		@XmlElement
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		@XmlElement
		public Long getHandrate() {
			return handrate;
		}
		public void setHandrate(Long handrate) {
			this.handrate = handrate;
		}
		@XmlElement
		public String getMarket() {
			return market;
		}
		public void setMarket(String market) {
			this.market = market;
		}
		@XmlElement
		public Long getMarketvalue() {
			return marketvalue;
		}
		public void setMarketvalue(Long marketvalue) {
			this.marketvalue = marketvalue;
		}
		@XmlElement
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlElement
		public Long getNewprice() {
			return newprice;
		}
		public void setNewprice(Long newprice) {
			this.newprice = newprice;
		}
		@XmlElement
		public Long getProfitrate() {
			return profitrate;
		}
		public void setProfitrate(Long profitrate) {
			this.profitrate = profitrate;
		}
		@XmlElement
		public Long getRisefallrate() {
			return risefallrate;
		}
		public void setRisefallrate(Long risefallrate) {
			this.risefallrate = risefallrate;
		}
		@XmlElement
		public Long getSecuamount() {
			return secuamount;
		}
		public void setSecuamount(Long secuamount) {
			this.secuamount = secuamount;
		}
		@XmlElement
		public Long getSecuvolume() {
			return secuvolume;
		}
		public void setSecuvolume(Long secuvolume) {
			this.secuvolume = secuvolume;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "StockTaxisVO [name=" + name + ", code=" + code
					+ ", market=" + market + ", newprice=" + newprice
					+ ", risefallrate=" + risefallrate + ", secuvolume="
					+ secuvolume + ", secuamount=" + secuamount
					+ ", profitrate=" + profitrate + ", handrate=" + handrate
					+ ", marketvalue=" + marketvalue + ", lb=" + lb + "]";
		}
}
