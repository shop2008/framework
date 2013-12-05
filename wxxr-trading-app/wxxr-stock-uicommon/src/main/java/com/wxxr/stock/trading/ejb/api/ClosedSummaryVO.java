
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

}
