/**
 * 
 */
package com.wxxr.trading.core.model;

/**
 * @author wangyan
 *
 */
public enum TradingStatus {
	NEW,		// �½�
	SUBMITTED,	// �ύ
	PROCESSING, // ������
	DONE,		// �������
	FAILED,		// ����ʧ��
	CANCELLED,	// ����ȡ��
	PARTIAL_CANCELLED,	// ����ȡ��
	PARTIAL_DONE;	// ���ײ������
}
