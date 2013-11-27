package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.stock.app.common.IEntityLoader;

public abstract class AbstractEntityLoader<K, V, T> implements IEntityLoader<K, V, T>, ICommandHandler<List<T>>{

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

}