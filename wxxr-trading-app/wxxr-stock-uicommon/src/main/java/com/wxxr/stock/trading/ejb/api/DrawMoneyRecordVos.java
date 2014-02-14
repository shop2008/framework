/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangxuyang
 *
 */
@XmlRootElement(name = "DrawMoneyRecordVos")
public class DrawMoneyRecordVos {
	@XmlElement(name="drawMoneyRecordVos")
	private List<DrawMoneyRecordVo> drawMoneyRecordVos;

	public List<DrawMoneyRecordVo> getDrawMoneyRecordVos() {
		return drawMoneyRecordVos;
	}

	public void setDrawMoneyRecordVos(List<DrawMoneyRecordVo> drawMoneyRecordVos) {
		this.drawMoneyRecordVos = drawMoneyRecordVos;
	}
}
