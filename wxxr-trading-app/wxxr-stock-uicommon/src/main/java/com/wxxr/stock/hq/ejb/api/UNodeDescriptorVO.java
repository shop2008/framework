/**
 * 
 */
package com.wxxr.stock.hq.ejb.api;

import java.util.Arrays;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;


/**
 * @author wangyan
 *
 */
@XmlRootElement(name="MNodeDescriptor")
public class UNodeDescriptorVO extends MNodeDescriptorVO {
	@XmlElement(name = "level")
	private int level=-1;
	@XmlElement(name = "children")
	private MNodeDescriptorVO[] children;		// the sequence of children is significant !!!

	/**
	 * @return the children
	 */
	
	public MNodeDescriptorVO[] getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(MNodeDescriptorVO[] children) {
		this.children = children;
	}


	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UNodeDescriptorVO:")
				.append("getNodeId()=").append(getNodeId())
				.append(", getDigest()=").append(getDigest())
				.append(", isLeaf()=").append(isLeaf())
				.append(", level=").append(level)
				.append("[children=")
				.append(Arrays.toString(children))
				.append("]");
		return builder.toString();
	}
	
}
