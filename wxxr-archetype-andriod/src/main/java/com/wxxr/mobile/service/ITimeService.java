/**
 * 
 */
package com.wxxr.mobile.service;

/**
 * @author fudapeng
 *
 */
public interface ITimeService {
	TimeBean getTime();
	void startTicking();
	void stopTicking();
}
