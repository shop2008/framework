/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;



/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="UserCreateTradAccInfo")
public class UserCreateTradAccInfo {
	/**
	 * 保证金比例
	 */
	private float depositRate;
	/**
	 * 综合费用比例,手续费
	 */
	private float costRate;
	/**
	 * 止损比例
	 */
	private float capitalRate;
	/**
	 * 可申请最大金额
	 */
	private Long maxAmount;
	/**
	 * 用户唯一标识
	 */
	private String userId;
	/**
	 * 止损+保证金
	 */
	private String rateString;
	/**
	 * 实盘券综合费用比例,手续费
	 */
	private float voucherCostRate;
	
	
	
}
