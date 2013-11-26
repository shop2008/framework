package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.service.IArticleManagementService;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.resource.ArticleResource;

public class NewArticleManagementServiceImpl extends AbstractModule<IStockAppContext> implements IArticleManagementService {
    private static final Trace log = Trace.register(NewArticleManagementServiceImpl.class);

    private ICommandHandler handler;
    private HashMap<String,MyArticlesBean> cache=new HashMap<String,MyArticlesBean>();
    @Override
    public MyArticlesBean getMyArticles(final int start, final int limit, final int type) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("method getNewArticles invoked,param[start=%s,limit=%s,type=%s]", start,limit,type));
        }
        if (cache.get(String.valueOf(type))==null){
            cache.put(String.valueOf(type), new MyArticlesBean() );
        }
        context.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Future<MyArticlesBean> result=context.getService(ICommandExecutor.class).submitCommand(new GetMyArticle(start,  limit,  type));
                MyArticlesBean r=null;
                try {
                    r = result.get();
                    cache.get(String.valueOf(type)).setHelpArticles(r.getHelpArticles());
                    cache.get(String.valueOf(type)).setHomeArticles(r.getHomeArticles());
                } catch (Exception e) {
                    log.error("getMyArticles"+e.getMessage());
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
        handler=  new GetMyArticlehandler();
        context.getService(ICommandExecutor.class).registerCommandHandler("GetMyArticle", handler);
        context.registerService(IArticleManagementService.class, this);
    }

    @Override
    protected void stopService() {
        context.unregisterService(IArticleManagementService.class, this);
        context.getService(ICommandExecutor.class).unregisterCommandHandler("GetMyArticle", handler);
        handler=null;
        cache.clear();
        cache=null;
    }

}
@NetworkConstraint(allowConnectionTypes={})
//@NetworkConstraint(allowConnectionTypes={NetworkConnectionType.G3,NetworkConnectionType.G4,NetworkConnectionType.WIFI,NetworkConnectionType.GSM})
class GetMyArticle implements ICommand<MyArticlesBean> {
    private int start, limit, type;
    public int getStart() {
        return start;
    }

    public int getLimit() {
        return limit;
    }

    public int getType() {
        return type;
    }

    public GetMyArticle(int p_start, int p_limit, int p_type) {
        start = p_start;
        limit = p_limit;
        type = p_type;
    }

    @Override
    public String getCommandName() {
        return "GetMyArticle";
    }

    @Override
    public Class<MyArticlesBean> getResultType() {
        return MyArticlesBean.class;
    }

    @Override
    public void validate() {

    }
    
}
class GetMyArticlehandler implements ICommandHandler{
    private MyArticlesBean articles = new MyArticlesBean();

    private IKernelContext  context;
    
    
    @Override
    public void destroy() {
        
    }

    @Override
    public void init(ICommandExecutionContext p_context) {
        context=p_context.getKernelContext();
    }

    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof GetMyArticle){
            GetMyArticle g=(GetMyArticle) command;
            NewsQueryBO bo=new NewsQueryBO();
            bo.setLimit(g.getLimit());
            bo.setStart(g.getStart());
            bo.setType(String.valueOf(g.getType()));
            List<ArticleVO> list= context.getService(IRestProxyService.class).getRestService(ArticleResource.class).getNewArticle(bo);
            if (list!=null&&list.size()>0) {
                if (bo.getType().equals("15")) {
                    articles.setHomeArticles(fromVO(list));
                }
                if (bo.getType().equals("19")) {
                    articles.setHelpArticles(fromVO(list));
                }
            }
            return (T) articles;
        }
        
        return null;
    }
    private ArticleBean fromVO(ArticleVO vo){
        if (vo==null) {
            return null;
        }
        ArticleBean article = new ArticleBean();
        article.setTitle(vo.getTitle());
        article.setAbstractInfo(vo.getAbstracts());
        article.setArticleUrl(getAbsoluteURL(vo.getArticleUrl()));
        article.setImageUrl(getAbsoluteURL(vo.getThumbnails()));
        return article;
    }
    private List<ArticleBean> fromVO(List<ArticleVO> volist){
        List<ArticleBean> list = null;
        if (volist!=null&&volist.size()>0) {
            list = new ArrayList<ArticleBean>();
            for (ArticleVO article : volist) {
                list.add(fromVO(article));
            }
        }
        return list;
    }
    private String getArticleHostURL(){
        return context.getService(IURLLocatorManagementService.class).getMagnoliaURL();
    }
    private String getAbsoluteURL(String relativeUrl){
        return getArticleHostURL()+relativeUrl;
    }
}
