package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.common.IEntityLoader;
import com.wxxr.mobile.stock.app.service.handler.BasicCommandHandler;

public abstract class AbstractEntityLoader<K, V, T, C extends ICommand<List<T>>> extends BasicCommandHandler<List<T>, C>implements IEntityLoader<K, V, T, C>, ICommandHandler<List<T>, C>{	
	

	public AbstractEntityLoader() {
		super();
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
	
	protected <S> S getRestService(Class<S> serviceInterface,Class<?> ifaceRest) {
		return this.getKernelContext().getService(IRestProxyService.class).getRestService(serviceInterface,ifaceRest);
	}
	
	protected <S> S getRestService(Class<S> serviceInterface,Class<?> ifaceRest,String serverUrl) {
		return this.getKernelContext().getService(IRestProxyService.class).getRestService(serviceInterface,ifaceRest,serverUrl);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public  void execute(C command, IAsyncCallback<List<T>> callback) {
		executeCommand(command,callback);
	}
	
	protected abstract void executeCommand(C command,IAsyncCallback<List<T>> callback);


}