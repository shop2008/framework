/**
 * 
 */
package com.wxxr.mobile.core.log.spi;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author Neil
 *
 */
public class MyLogRecord extends LogRecord {
    private static final long serialVersionUID = 2492784413065296060L;
    private static final String LOGGER_CLASS_NAME = Trace.class.getName();

    private boolean resolved;
    private final String loggerClassName;

    MyLogRecord(final Level level, final String msg) {
        super(level, msg);
        loggerClassName = LOGGER_CLASS_NAME;
    }

    MyLogRecord(final Level level, final String msg, final String loggerClassName) {
        super(level, msg);
        this.loggerClassName = loggerClassName;
    }

    public String getSourceClassName() {
        if (! resolved) {
            resolve();
        }
        return super.getSourceClassName();
    }

    public void setSourceClassName(final String sourceClassName) {
        resolved = true;
        super.setSourceClassName(sourceClassName);
    }

    public String getSourceMethodName() {
        if (! resolved) {
            resolve();
        }
        return super.getSourceMethodName();
    }

    public void setSourceMethodName(final String sourceMethodName) {
        resolved = true;
        super.setSourceMethodName(sourceMethodName);
    }

    private void resolve() {
        resolved = true;
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean found = false;
        for (StackTraceElement element : stack) {
            final String className = element.getClassName();
            if (found) {
                if (! loggerClassName.equals(className)) {
                    setSourceClassName(className);
                    setSourceMethodName(element.getMethodName());
                    return;
                }
            } else {
                found = loggerClassName.equals(className);
            }
        }
        setSourceClassName("<unknown>");
        setSourceMethodName("<unknown>");
    }

    protected Object writeReplace() {
        final LogRecord replacement = new LogRecord(getLevel(), getMessage());
        replacement.setResourceBundle(getResourceBundle());
        replacement.setLoggerName(getLoggerName());
        replacement.setMillis(getMillis());
        replacement.setParameters(getParameters());
        replacement.setResourceBundleName(getResourceBundleName());
        replacement.setSequenceNumber(getSequenceNumber());
        replacement.setSourceClassName(getSourceClassName());
        replacement.setSourceMethodName(getSourceMethodName());
        replacement.setThreadID(getThreadID());
        replacement.setThrown(getThrown());
        return replacement;
    }


}
