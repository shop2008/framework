package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * 股票、指数模型
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockBaseInfoBean")
public class StockBaseInfo {
	private String name; //股票或指数 名称
	private String mc;//市场代码： SH，SZ各代表上海，深圳。
	private String abbr;// 股票名称的中文拼音的首字母 如：“新大陆”  为 “xdl”
	private String code;  //股票或指数 代码
	private String type;// 0:指数，1：A股，2：B股
}
