
package com.wxxr.stock.trading.ejb.api;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "ClosedSummaryVO")
public class ClosedSummaryVO{
	@XmlElement(name = "id")
	private long id;
	/**成交详情*/
	@XmlElement(name = "auditDetailVO")
	private AuditInfoVO auditDetailVO;
	/**清算详情*/
	@XmlElement(name = "dealDetailVO")
	private DealDetailInfoVO dealDetailVO;
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClosedSummaryVO [id=").append(id)
				.append(", auditDetailVO=").append(auditDetailVO)
				.append(", dealDetailVO=").append(dealDetailVO).append("]");
		return builder.toString();
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}


	/**
	 * @return the auditDetailVO
	 */
	public AuditInfoVO getAuditDetailVO() {
		return auditDetailVO;
	}


	/**
	 * @param auditDetailVO the auditDetailVO to set
	 */
	public void setAuditDetailVO(AuditInfoVO auditDetailVO) {
		this.auditDetailVO = auditDetailVO;
	}


	/**
	 * @return the dealDetailVO
	 */
	public DealDetailInfoVO getDealDetailVO() {
		return dealDetailVO;
	}


	/**
	 * @param dealDetailVO the dealDetailVO to set
	 */
	public void setDealDetailVO(DealDetailInfoVO dealDetailVO) {
		this.dealDetailVO = dealDetailVO;
	}

}
