/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVo;

/**
 * @author wangxuyang
 *
 */
@XmlRootElement(name = "DrawMoneyRecordVos")
public class DrawMoneyRecordVos {
	
	private List<DrawMoneyRecordVo> drawMoneyRecordVos;
	@XmlElement
	public List<DrawMoneyRecordVo> getDrawMoneyRecordVos() {
		return drawMoneyRecordVos;
	}

	public void setDrawMoneyRecordVos(List<DrawMoneyRecordVo> drawMoneyRecordVos) {
		this.drawMoneyRecordVos = drawMoneyRecordVos;
	}
}
