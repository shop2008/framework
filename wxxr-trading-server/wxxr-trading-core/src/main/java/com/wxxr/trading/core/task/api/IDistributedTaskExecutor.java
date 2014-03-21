/**
 * 
 */
package com.wxxr.trading.core.task.api;

/**
 * @author linzhenwu
 *
 */
public interface IDistributedTaskExecutor extends ITaskExecutor {
	ITaskHashStrategy getHashStrategy();
}
