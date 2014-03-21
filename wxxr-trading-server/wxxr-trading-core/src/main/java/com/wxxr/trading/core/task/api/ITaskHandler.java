/**
 * 
 */
package com.wxxr.trading.core.task.api;


/**
 * @author neillin
 *
 */
public interface ITaskHandler {
	HandlerStatus handleTask(ITaskInfo task);
}
