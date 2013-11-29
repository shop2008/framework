package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;


public abstract class BasicCommandHandler implements ICommandHandler{
    private IKernelContext  context;
    @Override
    public void destroy() {
        
    }
    protected <T> T getRestService(Class<T> c) {
        return context.getService(IRestProxyService.class).getRestService(c);
    }

    @Override
    public void init(ICommandExecutionContext p_context) {
        context=p_context.getKernelContext();
    }

}


