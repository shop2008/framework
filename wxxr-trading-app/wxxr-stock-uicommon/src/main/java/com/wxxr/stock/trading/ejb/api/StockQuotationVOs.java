/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "StockQuotationVOs")
public class StockQuotationVOs {
		@XmlElement(name="stockQuotataions")
		private List<StockQuotationVO> stockQuotataions;

		/**
		 * @return the stockQuotataions
		 */
		
		public List<StockQuotationVO> getStockQuotataions() {
			return stockQuotataions;
		}

		/**
		 * @param stockQuotataions the stockQuotataions to set
		 */
		public void setStockQuotataions(List<StockQuotationVO> stockQuotataions) {
			this.stockQuotataions = stockQuotataions;
		}
		
}
