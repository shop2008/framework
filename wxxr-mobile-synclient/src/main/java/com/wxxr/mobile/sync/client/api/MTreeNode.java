/*
 * @(#)MTreeNode.java	 2010-8-15
 *
 * Copyright 2004-2010 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.sync.client.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.util.Base64;


/**
 * @class desc A MTreeNode.
 * 
 * @author taozhicheng
 * @version $Revision$
 * @created time 2010-8-15 05:15:39
 */
public class MTreeNode {
	
	/**
	 * @param node
	 * @return
	 */
	public static List<MTreeNode> getSortedChildren(MTreeNode node) {
		List<MTreeNode> children = node.getChildren();
		if((children != null)&&(children.size() > 0)){
			Collections.sort(children, new Comparator<MTreeNode>() {			
				@Override
				public int compare(MTreeNode o1, MTreeNode o2) {
					return o1.getNodeName().compareTo(o2.getNodeName());
				}
			});
		}
		return children;
	}

	
	private String nodeName;
	private MTreeNode parent;
	private Map<String, MTreeNode> children;
	private byte[] digest;

	public MTreeNode(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * get node name.
	 * 
	 * @return String node name.
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * set node name.
	 * 
	 * @param nodeName
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * get parent node.
	 * 
	 * @return
	 */
	public MTreeNode getParent() {
		return parent;
	}

	/**
	 * set this parent node.
	 * 
	 * @param parent
	 */
	public void setParent(MTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * Get Child Node.
	 * 
	 * @param childname
	 * @return MTreeNode
	 */
	public MTreeNode getChild(String childname) {
		MTreeNode child = null;

		if (children != null)
			child = children.get(childname);

		return child;
	}

	/**
	 * add child node.
	 * 
	 * @param child
	 */
	public void addChild(MTreeNode child) {
		if (child == null)
			throw new IllegalArgumentException(
					"MTreeNode.addChild Error: the child is NULL!!!");

		if (children == null)
			children = Collections.synchronizedMap(new LinkedHashMap<String, MTreeNode>());

		children.put(child.getNodeName(), child);
		child.setParent(this);
	}

	/**
	 * remove child node.
	 * 
	 * @param child
	 */
	public void removeChild(MTreeNode child) {
		if (child == null)
			return;

		removeChild(child.getNodeName());
	}

	/**
	 * remove child node.
	 * 
	 * @param child
	 * @return MTreeNode
	 */
	public MTreeNode removeChild(String childname) {
		if (childname == null)
			return null;

		MTreeNode child = null;
		if (children != null) {
			child = children.remove(childname);
			if (child != null)
				child.setParent(null);
		}

		return child;
	}

	/**
	 * get all children.
	 * 
	 * @return Iterator.
	 */
	public List<MTreeNode> getChildren() {
		return children == null ? null : new ArrayList<MTreeNode>(children.values());
	}

	/**
	 *  Returns the number of children in this node.
	 * 
	 * @return the number of children in this node.
	 */
	public int getChildrenSize() {
		if (children != null) {
			return children.size();
		}

		return 0;
	}

	public void clear() {
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		if (null==this.children || this.children.isEmpty()) {
			this.children = null;
		} else {
			MTreeNode[] nodes = this.children.values().toArray(	new MTreeNode[this.children.size()]);
			for (MTreeNode mTreeNode : nodes) {
				mTreeNode.clear();
			}
			this.children = null;
		}
		this.parent = null;
		this.digest = null;
		this.nodeName = null;
	}

	/**
	 * @return the digest
	 */
	public byte[] getDigest() {
		return digest;
	}

	/**
	 * @param digest
	 *            the digest to set
	 */
	public void setDigest(byte[] digest) {
		this.digest = digest;
	}

//    public void adjustNodeName(long grpId, int childs) {
//        long nameId = grpId / childs;
//        this.nodeName= String.valueOf(nameId);
//        
//        List<MTreeNode> orginalChilds = new ArrayList<MTreeNode>();
//        Iterator<MTreeNode> it = getChildren();
//        while(it!=null && it.hasNext()){
//            orginalChilds.add((MTreeNode) it.next());
//        }
//        children.clear();
//        for (MTreeNode child : orginalChilds) {
//            addChild(child);
//        }
//        if (this.parent != null && !this.parent.getNodeName().equals("/")) {
//            this.parent.adjustNodeName(nameId, childs);
//        }
//    }

    public String getPath() {
    	StringBuffer buf = new StringBuffer();
    	if(this.parent != null){
    		String p = this.getParent().getPath();
    		buf.append(p);
    		if("/".equals(p) == false){
    			buf.append('/');
    		}
    	}
    	return buf.append(getNodeName()).toString();
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MTreeNode [nodeName=" + nodeName + ", path=" + getPath() + ", childrenSize=" +( (children==null)? 0:children.size() )+ ", digest=" + ((digest!=null)? Base64.encodeToString(digest,Base64.DEFAULT):null) + "]";
    }
     
    

}
