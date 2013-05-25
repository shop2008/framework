package com.wxxr.mobile.android.log;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;

public class Log4jConfigurator {
	private Level rootLevel = Level.DEBUG;
	private String filePattern = "%d - [%p::%c::%C] - %m%n";
	private String logCatPattern = "%m%n";
	private String fileName = "log4j.log";
	private int maxBackupSize = 5;
	private long maxFileSize = 512 * 1024;
	private boolean immediateFlush = true;
	private boolean resetConfiguration = true;
	
	public Log4jConfigurator() {
	}

	/**
	 * @param fileName Name of the log file
	 */
	public Log4jConfigurator(final String fileName) {
		setFileName(fileName);
	}

	/**
	 * @param fileName  Name of the log file
	 * @param rootLevel Log level for the root logger
	 */
	public Log4jConfigurator(final String fileName, final Level rootLevel) {
		this(fileName);
		setRootLevel(rootLevel);
	}

	/**
	 * @param fileName Name of the log file
	 * @param rootLevel Log level for the root logger
	 * @param filePattern Log pattern for the file appender
	 */
	public Log4jConfigurator(final String fileName, final Level rootLevel, final String filePattern) {
		this(fileName);
		setRootLevel(rootLevel);
		setFilePattern(filePattern);
	}

	/**
	 * @param fileName Name of the log file
	 * @param maxBackupSize Maximum number of backed up log files
	 * @param maxFileSize Maximum size of log file until rolling
	 * @param filePattern  Log pattern for the file appender
	 * @param rootLevel Log level for the root logger
	 */
	public Log4jConfigurator(final String fileName, final int maxBackupSize,
			final long maxFileSize, final String filePattern, final Level rootLevel) {
		this(fileName, rootLevel, filePattern);
		setMaxBackupSize(maxBackupSize);
		setMaxFileSize(maxFileSize);
	}

	public void configure() {
		final Logger root = Logger.getRootLogger();
		
		if(isResetConfiguration()) {
			LogManager.getLoggerRepository().resetConfiguration();
		}
		
		root.setLevel(getRootLevel());
	}
	
	/**
	 * Sets the level of logger with name <code>loggerName</code>.
	 * Corresponds to log4j.properties <code>log4j.logger.org.apache.what.ever=ERROR</code>
	 * @param loggerName
	 * @param level
	 */
	public void setLevel(final String loggerName, final Level level) {
		Logger.getLogger(loggerName).setLevel(level);
	}

	public void configureFileAppender(Priority threshold) {
		final RollingFileAppender rollingFileAppender = new RollingFileAppender();
		rollingFileAppender.setFile(getFileName());
		rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
		rollingFileAppender.setMaximumFileSize(getMaxFileSize());
		rollingFileAppender.setImmediateFlush(isImmediateFlush());

		addAppender(rollingFileAppender, threshold);
	}
	
	public void addAppender(Appender appender, Priority threshold){
		final Logger root = Logger.getRootLogger();
		final Layout logCatLayout = new PatternLayout(getLogCatPattern());
		if(appender.getLayout() == null){
			appender.setLayout(logCatLayout);
		}
		if((threshold != null)&&(appender instanceof AppenderSkeleton)){
			((AppenderSkeleton)appender).setThreshold(threshold);
		}
		root.addAppender(appender);
	}
	
	
	public void configureLogCatAppender(Priority threshold) {
		addAppender(new LogCatAppender(),threshold);
	}
	
	
	public void configureConsoleAppender(Priority threshold){
		addAppender(new ConsoleAppender(), threshold);
	}
	/**
	 * Return the log level of the root logger
	 * @return Log level of the root logger
	 */
	public Level getRootLevel() {
		return rootLevel;
	}

	/**
	 * Sets log level for the root logger
	 * @param level Log level for the root logger
	 */
	public void setRootLevel(final Level level) {
		this.rootLevel = level;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(final String filePattern) {
		this.filePattern = filePattern;
	}

	public String getLogCatPattern() {
		return logCatPattern;
	}

	public void setLogCatPattern(final String logCatPattern) {
		this.logCatPattern = logCatPattern;
	}

	/**
	 * Returns the name of the log file
	 * @return the name of the log file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the name of the log file
	 * @param fileName Name of the log file
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Returns the maximum number of backed up log files
	 * @return Maximum number of backed up log files
	 */
	public int getMaxBackupSize() {
		return maxBackupSize;
	}

	/**
	 * Sets the maximum number of backed up log files
	 * @param maxBackupSize Maximum number of backed up log files
	 */
	public void setMaxBackupSize(final int maxBackupSize) {
		this.maxBackupSize = maxBackupSize;
	}

	/**
	 * Returns the maximum size of log file until rolling
	 * @return Maximum size of log file until rolling
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * Sets the maximum size of log file until rolling
	 * @param maxFileSize Maximum size of log file until rolling
	 */
	public void setMaxFileSize(final long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public boolean isImmediateFlush() {
		return immediateFlush;
	}

	public void setImmediateFlush(final boolean immediateFlush) {
		this.immediateFlush = immediateFlush;
	}


	public void setResetConfiguration(boolean resetConfiguration) {
		this.resetConfiguration = resetConfiguration;
	}

	/**
	 * Resets the log4j configuration before applying this configuration. Default is true.
	 * @return True, if the log4j configuration should be reset before applying this configuration. 
	 */
	public boolean isResetConfiguration() {
		return resetConfiguration;
	}

}