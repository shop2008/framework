/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

import java.util.List;

import android.util.Base64;

/**
 * @author neillin
 *
 */
public class MTreeLeaf extends MTreeNode {
	
	private byte[] digest;
	private Object dataGroupId;

	public MTreeLeaf(String nodeName) {
		super(nodeName);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#getChild(java.lang.String)
	 */
	@Override
	public MTreeNode getChild(String childname) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#addChild(com.wxxr.stock.utils.sync.MTreeNode)
	 */
	@Override
	public void addChild(MTreeNode child) {
		throw new IllegalStateException("This is a leaf node");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#removeChild(com.wxxr.stock.utils.sync.MTreeNode)
	 */
	@Override
	public void removeChild(MTreeNode child) {
		throw new IllegalStateException("This is a leaf node");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#removeChild(java.lang.String)
	 */
	@Override
	public MTreeNode removeChild(String childname) {
		throw new IllegalStateException("This is a leaf node");
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#getChildren()
	 */
	@Override
	public List<MTreeNode> getChildren() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.MTreeNode#getChildrenSize()
	 */
	@Override
	public int getChildrenSize() {
		return 0;
	}

	/**
	 * @return the digest
	 */
	public byte[] getDigest() {
		return digest;
	}

	/**
	 * @return the dataGroupId
	 */
	public Object getDataGroupId() {
		return dataGroupId;
	}

	/**
	 * @param digest the digest to set
	 */
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

	/**
	 * @param dataGroupId the dataGroupId to set
	 */
	public void setDataGroupId(Object dataGroupId) {
		this.dataGroupId = dataGroupId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MTreeLeaf [digest=" + (digest != null ? Base64.encodeToString(digest,Base64.DEFAULT) : "N/A")
				+ ", dataGroupId=" + dataGroupId + super.toString()+"]";
	}
	
	

}
