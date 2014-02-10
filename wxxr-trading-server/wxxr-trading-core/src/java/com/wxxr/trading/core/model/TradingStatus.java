/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * @author wangyan
 *
 */
public enum TradingStatus {
	NEW,		// 新建
	SUBMITTED,	// 提交
	PROCESSING, // 处理中
	DONE,		// 交易完成
	FAILED,		// 交易失败
	CANCELLED,	// 交易取消
	PARTIAL_CANCELLED,	// 部分取消
	PARTIAL_DONE;	// 交易部分完成
}
