package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
/**
 *  分时线
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className = "StockMinuteKBean")
public class StockMinuteK  {
    private String date; //日期时间
    private String close;//昨收
    private List<StockMinuteLineBean> list; //
    private String market;
    private String code;

}
