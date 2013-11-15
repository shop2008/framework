package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 用户积分实体
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="ScoreBean")
public class Score {

	/**
	 * 积分类别
	 */
	private String catagory;
	
	/**
	 * 积分获取日期
	 */
	private String date;
	
	/**
	 * 积分获得数量
	 */
	private String amount;
	
}
