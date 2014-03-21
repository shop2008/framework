/**
 * 
 */
package com.wxxr.trading.core.task.api;

/**
 * @author neillin
 *
 */
public class ReschedulingReqiredException extends Exception {

	private static final long serialVersionUID = 3749149369246988741L;
	private final long nextExecutionTime;
	public ReschedulingReqiredException(long nextTime){
		this.nextExecutionTime = nextTime;
	}
	/**
	 * @return the nextExecutionTime
	 */
	public long getNextExecutionTime() {
		return nextExecutionTime;
	}
	
	
}
