package com.wxxr.mobile.stock.article.command;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.resource.ArticleResource;

public class GetMyArticlehandler implements ICommandHandler{
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
        if (command instanceof GetMyArticleCommand){
            GetMyArticleCommand g=(GetMyArticleCommand) command;
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
