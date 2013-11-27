package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.common.IEntityLoader;

public abstract class AbstractEntityLoader<K, V, T> implements IEntityLoader<K, V, T>, ICommandHandler{

	protected ICommandExecutionContext cmdCtx;

	public AbstractEntityLoader() {
		super();
	}

	@Override
	public void init(ICommandExecutionContext ctx) {
		this.cmdCtx = ctx;
	}

	@Override
	public void destroy() {
		this.cmdCtx = null;
	}

	@Override
	public void registerCommandHandler(ICommandExecutor executor) {
		executor.registerCommandHandler(getCommandName(), this);
	}

	@Override
	public void unregisterCommandHandler(ICommandExecutor executor) {
		executor.unregisterCommandHandler(getCommandName(), this);
	}
	
	protected  abstract String getCommandName();
	
	protected <S> S getRestService(Class<S> serviceInterface) {
		return this.cmdCtx.getKernelContext().getService(IRestProxyService.class).getRestService(serviceInterface);
	}
	
	protected <S> S getRestService(Class<S> serviceInterface,String serverUrl) {
		return this.cmdCtx.getKernelContext().getService(IRestProxyService.class).getRestService(serviceInterface,serverUrl);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R> R execute(ICommand<R> command) throws Exception {
		Object val = execute(command);
		return (R)val;
	}
	
	protected abstract List<T> executeCommand(ICommand<List<T>> command) throws Exception;


}