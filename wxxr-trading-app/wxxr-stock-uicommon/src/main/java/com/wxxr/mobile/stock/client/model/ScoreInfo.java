package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.client.bean.ScoreBean;
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="ScoreInfoBean")
public class ScoreInfo {

	/**
	 * 积分余额
	 */
	private String balance;
	
	/**
	 * 所有积分信息
	 */
	private List<ScoreBean> scores;

	
}
