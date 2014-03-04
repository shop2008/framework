/**
 * 
 */
package com.wxxr.trading.core.task.api;

/**
 * @author neillin
 *
 */
public interface ITaskExecutor {
	/**
	 * 由Scheduler调用这个方法来执行具体的任务
	 * @param ctx
	 * @param jobId
	 * @param jobRequest
	 * @throws Exception
	 */
	void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest) throws Exception;
	
	/**
	 * 返回该执行器对应的任务类型
	 * @return
	 */
	String getAcceptableTaskType();
	
}
