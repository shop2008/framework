/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;



/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="UserCreateTradAccInfoBean")
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
	
	/**
     * 止损比例1
     */
	private float rateData1;
	
	private  String rateString1;
	/**
     * 止损比例2
     */
	private float rateData2;
	private  String rateString2;

	/**
     * 止损比例3
     */
	private float rateData3;
	   private  String rateString3;

	/**
     * 保证金比例1
     */
	private float deposit1;
	
	private float deposit2;
	
	private float deposit3;

	private List<String> requestamount;


	
}
