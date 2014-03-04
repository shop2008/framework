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
	 * ��Scheduler�������������ִ�о��������
	 * @param ctx
	 * @param jobId
	 * @param jobRequest
	 * @throws Exception
	 */
	void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest) throws Exception;
	
	/**
	 * ���ظ�ִ������Ӧ����������
	 * @return
	 */
	String getAcceptableTaskType();
	
}
