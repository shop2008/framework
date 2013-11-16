/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;


/**
 * 交易盘
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradingAccountBean")
public class TradingAccount {
	/*private Long id;
	private int type;//交易盘类型  0-模拟盘；1-实盘
	private String stockCode;//最大持股代码
	private String stockName;//最大持股名称
	private String createDate;//申请日期
	private String endDate;//截止日期
	private float initCredit;//额度（申请资金）
	private float income;//总收益
	private int status;//状态 0-未结算 ； 1-已结算
	private float available;//可用资金
*/	
	
	/**
	 * 交易盘编号
	 */
	private long id;
	/**
	 * 买入日期  
	 */
	private long buyDay;   
	/**
	 * 卖出日期 
	 */
	private long sellDay;  
	/**
	 * 申购金额
	 */
	private long applyFee;
	/**
	 * 可用资金
	 */
	private long avalibleFee;
	/**
	 * 总盈亏率
	 */
	private long gainRate;  
	/**
	 * 总盈亏额
	 */
	private long totalGain;
	/**
	 * 交易综合费
	 */
	private long usedFee;
	/**
	 * 止损
	 */
	private float lossLimit;//ֹ
	/**
	 * 冻结资金
	 */
	private long frozenVol;
	/**
	 * 最大持股代码
	 */
	private String maxStockCode;
	/**
	 * 最大持股市场
	 */
	private String maxStockMarket;
	/**
	 * 交易盘类型
	 */
	private String type;
	/**
	 * 交易订单
	 */
	private List<StockTradingOrderBean> tradingOrders;//
	/**
	 * 交易盘状态 CLOSED-已结算；UNCLOSE-未结算,CLEARING-正在结算
	 */
	private String over;
	/**是否为模拟盘*/
	private boolean virtual;
	/**
	 * 1:表示T日交易盘,0:T+1日交易盘
	 */
	private int status;
}
