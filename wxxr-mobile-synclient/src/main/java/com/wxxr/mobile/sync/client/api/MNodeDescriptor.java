/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

import java.io.Serializable;

import android.util.Base64;

/**
 * @author neillin
 *
 */
public class MNodeDescriptor implements Serializable {
	private static final long serialVersionUID = 8320321923658255508L;
	private String nodeId;
	private byte[] digest;
	private boolean leaf;
	private byte[] leafPayload;
	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}
	/**
	 * @return the digest
	 */
	public byte[] getDigest() {
		return digest;
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
	 * @param digest the digest to set
	 */
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}
	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	/**
	 * @return the leafPayload
	 */
	public byte[] getLeafPayload() {
		return leafPayload;
	}
	/**
	 * @param leafPayload the leafPayload to set
	 */
	public void setLeafPayload(byte[] leafPayload) {
		this.leafPayload = leafPayload;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MNodeDescriptor [nodeId=" + nodeId + ", digest="
				+ (digest != null ? Base64.encodeToString(digest,Base64.DEFAULT) : "N/A") + ", leaf=" + leaf + ", leafPayload="
				+ (leafPayload != null ? Base64.encodeToString(leafPayload,Base64.DEFAULT) : "N/A") + "]";
	}
	
	
	
}
