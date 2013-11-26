package com.wxxr.mobile.stock.app.service.impl;

import java.util.HashMap;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.mobile.stock.article.command.GetMyArticleCommand;
import com.wxxr.mobile.stock.article.command.GetMyArticlehandler;

public class NewArticleManagementServiceImpl extends AbstractModule<IStockAppContext> implements IArticleManagementService {
    private static final Trace log = Trace.register(NewArticleManagementServiceImpl.class);

    private HashMap<String, MyArticlesBean> cache = new HashMap<String, MyArticlesBean>();

    @Override
    public MyArticlesBean getMyArticles(final int start, final int limit, final int type) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("method getNewArticles invoked,param[start=%s,limit=%s,type=%s]", start, limit, type));
        }
        if (cache.get(String.valueOf(type)) == null) {
            cache.put(String.valueOf(type), new MyArticlesBean());
        }
        context.getService(ICommandExecutor.class).submitCommand(new GetMyArticleCommand(start, limit, type), new IAsyncCallback() {
            @Override
            public void failed(Object cause) {
                log.error("getMyArticles fail" + cause.toString());
            }

            @Override
            public void success(Object result) {
                if (result != null && result instanceof MyArticlesBean) {
                    MyArticlesBean a = (MyArticlesBean) result;
                    cache.get(String.valueOf(type)).setHelpArticles(a.getHelpArticles());
                    cache.get(String.valueOf(type)).setHomeArticles(a.getHomeArticles());
                }
            }

        });
        return cache.get(String.valueOf(type));
    }

    @Override
    protected void initServiceDependency() {
        addRequiredService(IURLLocatorManagementService.class);
        addRequiredService(IRestProxyService.class);
        addRequiredService(ICommandExecutor.class);
    }

    @Override
    protected void startService() {
        context.getService(ICommandExecutor.class).registerCommandHandler("GetMyArticle", new GetMyArticlehandler());
        context.registerService(IArticleManagementService.class, this);
    }

    @Override
    protected void stopService() {
        context.unregisterService(IArticleManagementService.class, this);
        cache.clear();
        cache = null;
    }

}
