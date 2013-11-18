package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;

/**
 * 日K线/周k线/月k线
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="LineListBean")
public class LineList{   
	/**
	 * 日K线
	 */
    private List<StockLineBean> day_list;
    /**
     * 周K线
     */
    private List<StockLineBean> week_list;
    /**
     * 月K线
     */
    private List<StockLineBean> month_list;

    
}
