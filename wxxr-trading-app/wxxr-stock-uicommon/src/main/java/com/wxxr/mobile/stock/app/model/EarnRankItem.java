/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="EarnRankItemBean")
public class EarnRankItem {
	/**
	 * 交易盘ID
	 */
	private String acctId;
	/**
	 * 文字
	 */
	private String title;
	/**
	 * 图片
	 */
	private String imgUrl;
}
