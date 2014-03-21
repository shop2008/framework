package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;

/**
 * 涨跌排行
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockTaxisListBean")
public class StockTaxisList{
  
    private List<StockTaxisBean> list;

  
}
