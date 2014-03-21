/**
 * 
 */
package com.wxxr.mobile.stock.app.model;


import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradingAccountListBean")
public class TradingAccountList {
	private List<TradingAccInfoBean> t0TradingAccounts;//T日
	private List<TradingAccInfoBean> t1TradingAccounts;//T+1日
	
	private List<GainBean> virtualTradingAccounts;//参赛交易盘
	private List<GainBean> realTradingAccounts;//挑战交易盘
	
	private List<GainBean> allTradingAccounts;//所有
	private List<GainBean> successTradingAccounts; //成功交易记录
}
