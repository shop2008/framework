/**
 * 
 */
package com.wxxr.mobile.stock.app.model;


import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradingAccountListBean")
public class TradingAccountList {
	private List<TradingAccountBean> allTradingAccounts;//所有
	private List<TradingAccountBean> t0TradingAccounts;//T日
	private List<TradingAccountBean> t1TradingAccountBeans;//T+1日
	private List<TradingAccountBean> virtualTradingAccountBeans;//参赛交易盘
	private List<TradingAccountBean> realTradingAccountBeans;//挑战交易盘
}
