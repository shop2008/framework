/**
 * 
 */
package com.wxxr.mobile.stock.client;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;

import android.app.Application;

import com.wxxr.mobile.android.log.Log4jConfigurator;
import com.wxxr.mobile.core.log.spi.ILoggerFactory;
import com.wxxr.mobile.core.log.spi.Log4jLoggerFactory;

/**
 * @author neillin
 *
 */
public class StockApplication extends Application
{
	private Log4jConfigurator logConfig = new Log4jConfigurator();
	private StockAppFramework framework;
	@Override
	public void onCreate()
	{
		ILoggerFactory.DefaultFactory.setLoggerFactory(new Log4jLoggerFactory());
		logConfig.configure();
		super.onCreate();
		this.framework = new StockAppFramework(this);
		if(this.framework.isInDebugMode()){
			logConfig.configureLogCatAppender("com.wxxr.mobile",Level.DEBUG);
//			logConfig.configureLogCatAppender("com.wxxr.mobile.stock",Level.DEBUG);
		}else{
			logConfig.configureFileAppender("/",Level.WARN);
			logConfig.configureLogCatAppender("/", Level.WARN);
		}
		this.framework.startLater(1,TimeUnit.SECONDS);
	}


	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		if(this.framework != null){
			this.framework.stop();
			this.framework = null;
		}
		super.onTerminate();
	}
	
	public StockAppFramework getFramework() {
		return this.framework;
	}
	
}