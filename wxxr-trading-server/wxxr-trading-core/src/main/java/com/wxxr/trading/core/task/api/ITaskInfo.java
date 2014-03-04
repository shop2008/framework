/**
 * 
 */
package com.wxxr.trading.core.task.api;

/**
 * @author neillin
 *
 */
public interface ITaskInfo {
	Long getJobId();
	String getJobType();
	String getJobRequest();
}
