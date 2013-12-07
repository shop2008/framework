package com.wxxr.stock.restful.json;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;

@XmlRootElement(name = "VoucherDetailsVO")
public class VoucherDetailsVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(name="addToday")
	private long addToday;//当日增加
	@XmlElement(name="reduceToday")
	private long reduceToday;//当日减少
	@XmlElement(name="bal")
	private long bal;//余额
	@XmlElement(name="details")
	private List<GainPayDetailsVO> list;//实盘积分明细


	public List<GainPayDetailsVO> getList() {
		return list;
	}

	public void setList(List<GainPayDetailsVO> list) {
		this.list = list;
	}


	public long getAddToday() {
		return addToday;
	}

	public void setAddToday(long addToday) {
		this.addToday = addToday;
	}


	public long getReduceToday() {
		return reduceToday;
	}

	public void setReduceToday(long reduceToday) {
		this.reduceToday = reduceToday;
	}


	public long getBal() {
		return bal;
	}

	public void setBal(long bal) {
		this.bal = bal;
	}

	@Override
	public String toString() {
		return "VoucherDetailsVO [addToday=" + addToday + ", bal=" + bal
				+ ", list=" + list + ", reduceToday=" + reduceToday + "]";
	}

	
}
