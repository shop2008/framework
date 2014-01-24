/**
 * 
 */
package com.wxxr.mobile.service;

import java.util.Date;
import java.util.Timer;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;


/**
 * 
 * @author fudapeng
 * 
 */
public class TimeService extends AbstractModule<IAndroidAppContext> implements ITimeService {

	private static final Trace log = Trace.register(TimeService.class);
	private Timer timer = new Timer();
	@Override
	public void startService() {
		context.registerService(ITimeService.class, this);
	}


	@Override
	public void stopService() {
		context.unregisterService(ITimeService.class, this);
	}
	
	@Override
	public String getTime() throws Exception {
		  java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	       String r = format1.format(new Date());
	       return r;
	}

	@Override
	protected void initServiceDependency() {
	}
}

