package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 股票排行，参数
 * @author zhengjincheng
 *
 */
@XmlRootElement(name = "taxis")
public class TaxisVO implements Serializable  {
	@XmlElement(name = "taxis")
    private String taxis;
	@XmlElement(name = "orderby")
    private String orderby;
	@XmlElement(name = "start")
    private Long start;
	@XmlElement(name = "limit")
    private Long limit;	
	@XmlElement(name = "blockId")
    private Long blockId;
    
    public String getTaxis() {
        return taxis;
    }

    public void setTaxis(String taxis) {
        this.taxis = taxis;
    }
    
	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	
	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}
	
	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}
	
	public Long getBlockId() {
		return blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	
	public boolean hasSameSearchConditions(TaxisVO other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (blockId == null) {
			if (other.blockId != null)
				return false;
		} else if (!blockId.equals(other.blockId))
			return false;
		if (orderby == null) {
			if (other.orderby != null)
				return false;
		} else if (!orderby.equals(other.orderby))
			return false;
		if (taxis == null) {
			if (other.taxis != null)
				return false;
		} else if (!taxis.equals(other.taxis))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaxisVO [taxis=" + taxis + ", orderby=" + orderby + ", start="
				+ start + ", limit=" + limit + ", blockId=" + blockId + "]";
	}
}
