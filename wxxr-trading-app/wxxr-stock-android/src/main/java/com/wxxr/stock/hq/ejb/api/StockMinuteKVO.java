package com.wxxr.stock.hq.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 *  分时线VO
 * @author juyao
 *
 */
@XmlRootElement(name = "StockMinuteK")
public class StockMinuteKVO extends AuditableLKeyObject {
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "date")
    private String date; //
    @XmlElement(name="minute")
    private String close;//昨收
    private List<StockMinuteLineVO> list; //
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
    
    public List<StockMinuteLineVO> getList() {
        return list;
    }

    public void setList(List<StockMinuteLineVO> list) {
        this.list = list;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockMinuteKVO [date=" + date + ", close=" + close + ", list="
				+ list + "]";
	}


}
