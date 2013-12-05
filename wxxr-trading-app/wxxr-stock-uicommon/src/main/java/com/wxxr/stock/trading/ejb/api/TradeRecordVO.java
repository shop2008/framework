
package com.wxxr.stock.trading.ejb.api;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "TradeRecordVO")
public class TradeRecordVO{
	@XmlElement(name = "")
	
	private long id;
	
	@XmlElement(name = "")
	private long date;//日期
	@XmlElement(name = "")
	private String market;//股票市场
	
	@XmlElement(name = "")
	private String code;//股票代码
	
	@XmlElement(name = "")
	private String describe;//成交方向：
	
	@XmlElement(name = "")
	private long price;//成交价格
	
	@XmlElement(name = "")
	private long vol;//成交量
	
	@XmlElement(name = "")
	private long amount;//成交金额
	
	@XmlElement(name = "")
	private long brokerage;//佣金
	
	@XmlElement(name = "")
	private long tax;//印花税
	
	@XmlElement(name = "")
	private long fee;//过户费
	
	@XmlElement(name = "")
	private int day;//1:表示t日,0:表示t+1日
	
	private boolean beDone;//订单是否完成，DONE为true；否则为false
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradingRecordDTO [id=").append(id).append(", date=")
				.append(date).append(", market=").append(market)
				.append(", code=").append(code).append(", describe=")
				.append(describe).append(", price=").append(price)
				.append(", vol=").append(vol).append(", amount=")
				.append(amount).append(", brokerage=").append(brokerage)
				.append(", tax=").append(tax).append(", fee=").append(fee)
				.append(", day=").append(day).append(", beDone=")
				.append(beDone).append("]");
		return builder.toString();
	}
	
}
