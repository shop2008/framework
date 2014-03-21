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
	 * 注册一个任务执行器
	 * @param executor
	 */
	void registerTaskExecutor(ITaskExecutor executor);
	
	/**
	 * 注销一个任务执行器
	 * @param executor
	 * @return
	 */
	boolean unregisterTaskExecutor(ITaskExecutor executor);
	
	/**
	 * 安排一个任务在指定的延迟时间后执行
	 * @param taskType
	 * @param jobRequest
	 * @param delay
	 * @param unit
	 */
	Long scheduleTask(String taskType, String jobRequest, long delay , TimeUnit unit);
	
	/**
	 * 安排一个任务在指定的时间后执行
	 * @param taskType
	 * @param jobRequest
	 * @param startTime
	 */
	Long scheduleTask(String taskType, String jobRequest, Date startTime);
	
	/**
	 * 注销一个已经安排的任务
	 * @param jobId
	 * @return
	 */
	boolean unscheduleTask(Long jobId);
	
	/**
	 * 注销指定类型的所有活跃任务
	 * @param taskType
	 * @return
	 */
	boolean unscheduleTasksOfType(String taskType);

}
