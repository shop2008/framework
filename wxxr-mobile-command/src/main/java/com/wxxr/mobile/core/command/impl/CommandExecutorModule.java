/**
 * 
 */
package com.wxxr.mobile.core.command.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.command.api.ICommandValidator;
import com.wxxr.mobile.core.command.api.UnsupportedCommandException;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.IAsyncCallback;

/**
 * @author neillin
 *
 */
public class CommandExecutorModule<T extends IKernelContext> extends AbstractModule<T> implements
		ICommandExecutor {

	private int maxThread = 5;
	private int minThread = 3;
	private int commandQueueSize = 100;
	private ExecutorService executor;
	private Map<String, ICommandHandler> handlers = new HashMap<String, ICommandHandler>();
	private List<ICommandValidator> validators = new ArrayList<ICommandValidator>();
	private LinkedBlockingDeque<Runnable> taskQueue;
	private ICommandExecutionContext cmdCtx = new ICommandExecutionContext() {
		
		public IKernelContext getKernelContext() {
			return context;
		}
	};
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandExecutor#submitCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	public <V> Future<V> submitCommand(final ICommand<V> command) {
		final ICommandHandler handler = validateCommand(command);
		return this.executor.submit(new Callable<V>() {

			public V call() throws Exception {
				return handler.execute(command);
			}
		});
	}

	/**
	 * @param command
	 * @return
	 */
	protected <V> ICommandHandler validateCommand(final ICommand<V> command) {
		final ICommandHandler handler;
		synchronized(this.handlers){
			handler = this.handlers.get(command.getCommandName());
		}
		if(handler == null){
			throw new UnsupportedCommandException("Cannot find command handler for command :"+command.getCommandName());
		}
		command.validate();
		ICommandValidator[] valids = null;
		synchronized(this.validators){
			valids = this.validators.toArray(new ICommandValidator[0]);
		}
		if(valids != null){
			for (ICommandValidator iCommandValidator : valids) {
				iCommandValidator.checkCommandConstraints(command);
			}
		}
		return handler;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandExecutor#registerCommandHandler(java.lang.String, com.wxxr.mobile.core.command.api.ICommandHandler)
	 */
	public ICommandExecutor registerCommandHandler(String cmdName,
			ICommandHandler handler) {
		synchronized(this.handlers){
			this.handlers.put(cmdName, handler);
			handler.init(cmdCtx);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandExecutor#unregisterCommandHandler(java.lang.String, com.wxxr.mobile.core.command.api.ICommandHandler)
	 */
	public ICommandExecutor unregisterCommandHandler(String cmdName,
			ICommandHandler handler) {
		synchronized(this.handlers){
			ICommandHandler old = this.handlers.get(cmdName);
			if(old == handler){
				this.handlers.remove(cmdName);
				handler.destroy();
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandExecutor#registerCommandValidator(com.wxxr.mobile.core.command.api.ICommandValidator)
	 */
	public ICommandExecutor registerCommandValidator(ICommandValidator validator) {
		synchronized(this.validators){
			if(!validators.contains(validator)){
				this.validators.add(validator);
				validator.init(cmdCtx);
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandExecutor#unregisterCommandValidator(com.wxxr.mobile.core.command.api.ICommandValidator)
	 */
	public ICommandExecutor unregisterCommandValidator(
			ICommandValidator validator) {
		synchronized(this.validators){
			if(this.validators.remove(validator)){
				validator.destroy();
			}
		}
		return this;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
	}

	@Override
	protected void startService() {
		this.taskQueue = new LinkedBlockingDeque<Runnable>(this.commandQueueSize);
		this.executor = new ThreadPoolExecutor(this.minThread, this.maxThread, 60, TimeUnit.SECONDS, this.taskQueue, 
				new ThreadFactory() {
					private AtomicInteger seqNo = new AtomicInteger(1);
					public Thread newThread(Runnable r) {
						return new Thread(r, "Command executor thread -- "+seqNo.getAndIncrement());
					}
				}, new ThreadPoolExecutor.AbortPolicy());
		context.registerService(ICommandExecutor.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ICommandExecutor.class, this);
		if(this.executor != null){
			this.executor.shutdownNow();
			this.executor = null;
		}
		if(this.taskQueue != null){
			this.taskQueue.clear();
			this.taskQueue = null;
		}
		synchronized(this.validators){
			for (ICommandValidator validator : this.validators) {
				validator.destroy();
			}
			this.validators.clear();
		}
		synchronized(this.handlers){
			for (ICommandHandler handler : this.handlers.values()) {
				handler.destroy();
			}
			this.handlers.clear();
		}
	}

	/**
	 * @return the maxThread
	 */
	public int getMaxThread() {
		return maxThread;
	}

	/**
	 * @return the minThread
	 */
	public int getMinThread() {
		return minThread;
	}

	/**
	 * @return the commandQueueSize
	 */
	public int getCommandQueueSize() {
		return commandQueueSize;
	}

	/**
	 * @param maxThread the maxThread to set
	 */
	public void setMaxThread(int maxThread) {
		this.maxThread = maxThread;
	}

	/**
	 * @param minThread the minThread to set
	 */
	public void setMinThread(int minThread) {
		this.minThread = minThread;
	}

	/**
	 * @param commandQueueSize the commandQueueSize to set
	 */
	public void setCommandQueueSize(int commandQueueSize) {
		this.commandQueueSize = commandQueueSize;
	}

	public <V> void submitCommand(final ICommand<V> command, final IAsyncCallback callback) {
		if(callback == null){
			throw new IllegalArgumentException("Invalid callback NULL !");
		}
		final ICommandHandler handler = validateCommand(command);
		this.executor.submit(new Runnable() {
			
			public void run() {
				try {
					callback.success(handler.execute(command));
				}catch(Throwable t){
					callback.failed(t);
				}
				
			}
		});
		
	}

}
