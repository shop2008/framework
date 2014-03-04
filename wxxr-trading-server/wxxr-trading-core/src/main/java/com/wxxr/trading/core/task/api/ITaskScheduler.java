/**
 * 
 */
package com.wxxr.trading.core.task.api;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author neillin
 *
 */
public interface ITaskScheduler {
	/**
	 * ע��һ������ִ����
	 * @param executor
	 */
	void registerTaskExecutor(ITaskExecutor executor);
	
	/**
	 * ע��һ������ִ����
	 * @param executor
	 * @return
	 */
	boolean unregisterTaskExecutor(ITaskExecutor executor);
	
	/**
	 * ����һ��������ָ�����ӳ�ʱ���ִ��
	 * @param taskType
	 * @param jobRequest
	 * @param delay
	 * @param unit
	 */
	Long scheduleTask(String taskType, String jobRequest, long delay , TimeUnit unit);
	
	/**
	 * ����һ��������ָ����ʱ���ִ��
	 * @param taskType
	 * @param jobRequest
	 * @param startTime
	 */
	Long scheduleTask(String taskType, String jobRequest, Date startTime);
	
	/**
	 * ע��һ���Ѿ����ŵ�����
	 * @param jobId
	 * @return
	 */
	boolean unscheduleTask(Long jobId);
	
	/**
	 * ע��ָ�����͵����л�Ծ����
	 * @param taskType
	 * @return
	 */
	boolean unscheduleTasksOfType(String taskType);

}
