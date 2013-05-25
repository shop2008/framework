/**
 * 
 */
package com.wxxr.mobile.core.log.spi;


/**
 * @author Neil
 *
 */
public class JDKLoggerFactory extends DummyLoggerFactory {

	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.ILoggerFactory#createLogger(java.lang.String)
	 */
	public Log createLogger(String category) {
		return JDKLogger.getLogger(category);
	}

}
