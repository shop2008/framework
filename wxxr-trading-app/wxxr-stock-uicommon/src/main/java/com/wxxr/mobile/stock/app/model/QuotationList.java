/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="QuotationListBean")
public class QuotationList {
	/**
	 * 上证指数
	 */
	private StockQuotationBean shBean;
	/**
	 * 深证指数
	 */
	private StockQuotationBean szBean;
	
	private List<StockQuotationBean> list;
}
