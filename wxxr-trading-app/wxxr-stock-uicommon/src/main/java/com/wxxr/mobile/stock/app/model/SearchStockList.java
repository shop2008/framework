/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.sync.model.StockBaseInfo;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="SearchStockListBean")
public class SearchStockList {
	private List<StockBaseInfo> searchResult;
}
