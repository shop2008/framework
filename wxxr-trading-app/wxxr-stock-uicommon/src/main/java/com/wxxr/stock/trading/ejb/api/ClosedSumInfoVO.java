package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author chenchao
 *
 */

@XmlRootElement(name = "ClosedSumInfoVO")
public class ClosedSumInfoVO implements Serializable{
	
	private static final long serialVersionUID = -4958155134857709147L;
	private String accId;//id
	/**清算详情*/
	@XmlElement(name = "auditDetailVO")
	private AuditDetailVO auditDetailVO;
	
	/**成交详情*/
	@XmlElement(name = "dealDetailVO")
	private DealDetailVO dealDetailVO;
	
	
	public AuditDetailVO getAuditDetailVO() {
		return auditDetailVO;
	}
	public void setAuditDetailVO(AuditDetailVO auditDetailVO) {
		this.auditDetailVO = auditDetailVO;
	}
	public DealDetailVO getDealDetailVO() {
		return dealDetailVO;
	}
	public void setDealDetailVO(DealDetailVO dealDetailVO) {
		this.dealDetailVO = dealDetailVO;
	}
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	@Override
	public String toString() {
		return "ClosedSumInfoVO [auditDetailVO=" + auditDetailVO
				+ ", dealDetailVO=" + dealDetailVO + "]";
	}
	
	
}
