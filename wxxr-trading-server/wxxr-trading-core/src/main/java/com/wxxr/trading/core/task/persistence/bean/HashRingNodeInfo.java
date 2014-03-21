/**
 * 
 */
package com.wxxr.trading.core.task.persistence.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.trading.core.task.persistence.HashRingNodeDAO;

/**
 * @author Neil Lin
 *
 */
@Entity
@Table(name = "H_RING_NODE")
@Persistence(daoClass = HashRingNodeDAO.class)
public class HashRingNodeInfo {
	@Id
	@Column(name = "NODE_ID", nullable = false, length=8)
	private int nodeId;
	
	@Column(name="LAST_UPDATED_DATE",nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdatedDate;
	
	@Column(name="STATUS",nullable = false, length = 10)
	@Enumerated(EnumType.STRING) 
	private NodeStatus status;
	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}
	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	/**
	 * @return the status
	 */
	public NodeStatus getStatus() {
		return status;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(NodeStatus status) {
		this.status = status;
	}
	
	

}
