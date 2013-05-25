/**
 * 
 */
package com.wxxr.mobile.core.log.spi;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wxxr.mobile.core.log.api.SeverityLevel;

/**
 * @author Neil
 *
 */
public class JDKLogger implements Log {
	
	private static ConcurrentHashMap<String, WeakReference<JDKLogger>> loggers = new ConcurrentHashMap<String, WeakReference<JDKLogger>>();

	public static JDKLogger getLogger(String category){
		WeakReference<JDKLogger> ref = loggers.get(category);
		JDKLogger logger = null;
		if(ref == null){
			logger = new JDKLogger(Logger.getLogger(category),category);
			ref = loggers.putIfAbsent(category, new WeakReference<JDKLogger>(logger));
			if(ref == null){
				ref = loggers.get(category);
			}
		}
		synchronized(ref){
			logger = ref.get();
			if(logger == null){
				logger = new JDKLogger(Logger.getLogger(category),category);
				loggers.put(category, new WeakReference<JDKLogger>(logger));
			}
			return logger;
		}
	}
	
	private static Level translate(final SeverityLevel level) {
		if (level != null) switch (level) {
		case FATAL: return JDKLevel.FATAL;
		case ERROR: return JDKLevel.ERROR;
		case WARN:  return JDKLevel.WARN;
		case INFO:  return JDKLevel.INFO;
		case DEBUG: return JDKLevel.DEBUG;
		case TRACE: return JDKLevel.TRACE;
		}
		return JDKLevel.ALL;
	}

	
	private final Logger log;
	private final String loggerName;
	
	private JDKLogger(Logger logger,String name){
		this.log = logger;
		this.loggerName = name;
	}
	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.Log#isEnabled(com.wxxr.core.log.api.SeverityLevel)
	 */
	public boolean isEnabled(SeverityLevel level) {
		return this.log.isLoggable(translate(level));
	}

	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.Log#log(com.wxxr.core.log.api.SeverityLevel, java.lang.Object)
	 */
	public void log(SeverityLevel level, Object message) {
		log(level, String.valueOf(message),null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.Log#log(com.wxxr.core.log.api.SeverityLevel, java.lang.Object, java.lang.Throwable)
	 */
	public void log(SeverityLevel level, Object message, Throwable t) {
		log(level, String.valueOf(message),null,t);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.core.log.spi.Log#log(com.wxxr.core.log.api.SeverityLevel, java.lang.String, java.lang.Object[], java.lang.Throwable)
	 */
	public void log(SeverityLevel level, String message, Object[] args,
			Throwable t) {
		
        if (isEnabled(level)) try {
            String msg = message;
            try {
				msg = (args == null)||(args.length == 0) ? message : String.format(message, args);
			} catch (Throwable e) {
			}
            if((args != null)&&(args.length > 0)&&(message.equals(msg))){
            	int cnt = 0;
            	StringBuffer buf = new StringBuffer(msg);
            	for (Object object : args) {
					if(cnt > 0){
						buf.append(',');
					}
					buf.append(object);
				}
            	msg = buf.toString();
            }
            final MyLogRecord rec = new MyLogRecord(translate(level), msg);
            if (t != null) rec.setThrown(t);
            rec.setLoggerName(this.loggerName);
//            rec.setParameters(args);
//            rec.setResourceBundleName(logger.getResourceBundleName());
//            rec.setResourceBundle(logger.getResourceBundle());
            log.log(rec);
        } catch (Throwable ignored) {}
	}

}
