/**
 * 
 */
package com.wxxr.mobile.android.log;

import com.wxxr.mobile.core.log.spi.ILoggerFactory;
import com.wxxr.mobile.core.log.spi.Log;


/**
 * @author Neil
 *
 */
public class AndroidLoggerFactory implements ILoggerFactory {

	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.ILoggerFactory#createLogger(java.lang.String)
	 */
	public Log createLogger(String category) {
		return AndroidLogger.getLogger(category);
	}

}
