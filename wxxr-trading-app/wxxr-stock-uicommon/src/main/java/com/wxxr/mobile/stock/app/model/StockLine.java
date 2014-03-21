package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 日K线数据:周K线数据:月K线数据
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockLineBean")
public class StockLine {
	private String date;
	private String time;//供测试使用
	private Long close;// 昨收
	private Long open;// 开盘
	private Long high;// 最高
	private Long low;// 最低
	private Long price;// 收盘价
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long start;//开始
	private Long limit;//偏移量
	
	private String market;//市场
    private String code;//股票代码

	
    @Override
    public String toString() {
        return "StockLine [date=" + date + ", time=" + time + ", close=" + close + ", open=" + open + ", high=" + high + ", low=" + low + ", price=" + price + ", secuvolume=" + secuvolume + ", secuamount=" + secuamount + ", start=" + start + ", limit=" + limit + ", market=" + market + ", code="
                + code + "]";
    }
	
}
