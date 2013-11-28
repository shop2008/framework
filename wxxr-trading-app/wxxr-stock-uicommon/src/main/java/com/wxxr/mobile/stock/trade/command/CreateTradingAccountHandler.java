package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class CreateTradingAccountHandler implements ICommandHandler{
    private IKernelContext  context;
    private static final Trace log = Trace.register(CreateTradingAccountHandler.class);
    @Override
    public void destroy() {
        
    }
    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof CreateTradingAccountCommand){
            CreateTradingAccountCommand g=(CreateTradingAccountCommand) command;
            
            StockResultVO vo = getRestService(ITradingProtectedResource.class)
                    .createTradingAccount(g.getCaptitalAmount(), g.getCapitalRate(),
                            g.isVirtual(), g.getDepositRate());
          
                return (T) vo;
        }
        return null;
    }

    private <T> T getRestService(Class<T> c) {
        return context.getService(IRestProxyService.class).getRestService(c);
    }

    @Override
    public void init(ICommandExecutionContext p_context) {
        context=p_context.getKernelContext();
    }

}
