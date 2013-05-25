/**
 * 
 */
package com.wxxr.mobile.core.log.spi;


/**
 * @author Neil
 *
 */
public interface ILoggerFactory {
	Log createLogger(String category);
	public class DefaultFactory {
		private static ILoggerFactory factory = new DummyLoggerFactory();
		
		public static ILoggerFactory getLoggerFactory() {
			return factory;
		}
		
		public static void setLoggerFactory(ILoggerFactory f) {
			assert f != null : "LoggerFactory cannot be NULL !";
			factory = f;
		}
	}
}
