/**
 * 
 */
package com.wxxr.mobile.stock.app.sync.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;


/**
 * @author wangyan
 *
 */
@XmlRootElement(name="MNodeDescriptor")
public class MNodeDescriptorVO implements Serializable {
	private String nodeId;
	private String digest,leafPayload;
	private boolean leaf;
	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @return the digest
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * @param digest the digest to set
	 */
	
	public void setDigest(String digest) {
		this.digest = digest;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MNodeDescriptorVO [nodeId=" + nodeId + ", digest=" + digest + ", leaf=" + leaf + "]";
	}

	/**
	 * @return the leafPayload
	 */
	public String getLeafPayload() {
		return leafPayload;
	}

	/**
	 * @param leafPayload the leafPayload to set
	 */
	public void setLeafPayload(String leafPayload) {
		this.leafPayload = leafPayload;
	}
	
}
