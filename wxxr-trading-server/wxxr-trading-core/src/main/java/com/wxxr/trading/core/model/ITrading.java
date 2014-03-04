/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.Date;

import com.wxxr.trading.core.storage.api.InheritableBizObject;


/**
 * ����
 * @author wangyan
 *
 */
public interface ITrading extends InheritableBizObject<Long> {
	/**
	 * �û�
	 * @return
	 */
	String getOwnerId();
	/**
	 * ������
	 * @return
	 */
	String getBroker();
	/**
	 * ��������
	 * @return
	 */
	Integer getTradingCode();
	/**
	 * ����״̬
	 * @return
	 */
	TradingStatus getStatus();


	/**
	 * �����˻�id
	 * @return
	 */
	Long[] getTradingParties();
	
	
	String getSubStatus();
	
	Date getCreatedTime();
	
	Date getCompletedTime();
}
