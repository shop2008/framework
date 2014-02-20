/**
 * 
 */
package com.wxxr.archetype.mobile.handler;


import com.wxxr.archetype.mobile.command.StartTimeCommand;
import com.wxxr.archetype.mobile.service.TimeBean;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author fudapeng
 *
 */

public class StartTimeCommandHandler implements ICommandHandler<TimeBean,StartTimeCommand> {

	@Override
	public void destroy() {

	}
	Thread currentThread = null;
	
	boolean isCancellable = false;
	
	public  void execute(StartTimeCommand command, IAsyncCallback<TimeBean> callback) {
		StartTimeCommand cmd = (StartTimeCommand)command;
		TimeBean bean = cmd.getBean();
		try {
			ICancellable cancellable = new ICancellable(){
				@Override
				public void cancel() {
					currentThread.interrupt();
				}
				@Override
				public boolean isCancelled() {
					return isCancellable;
				}
			};
			if(callback != null){
				callback.setCancellable(cancellable);
			}
			currentThread = Thread.currentThread();
			Thread.sleep(1000*2);
			
			//假设现在已经不可以取消
			isCancellable = false;
			callback.setCancellable(null);
			Thread.sleep(1000*2);
			
			
			//假设现在已经又可以取消
			callback.setCancellable(cancellable);
			Thread.sleep(1000*2);
			
			((com.wxxr.archetype.mobile.service.TimeBean) bean).setTicking(true);
			isCancellable = true;
			callback.success(bean);
		} catch (InterruptedException e) {
			log.warn("StartTimeCommandHandler InterruptedException ... ");
			callback.cancelled();
		}
	}
	private static final Trace log = Trace.register(StartTimeCommandHandler.class);
	@Override
	public void init(ICommandExecutionContext arg0) {

	}

}
