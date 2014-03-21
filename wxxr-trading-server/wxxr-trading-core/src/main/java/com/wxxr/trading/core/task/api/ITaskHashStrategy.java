/**
 * 
 */
package com.wxxr.trading.core.task.api;


/**
 * @author linzhenwu
 *
 */
public interface ITaskHashStrategy {
	long calculateHash(ITaskInfo info);
}
