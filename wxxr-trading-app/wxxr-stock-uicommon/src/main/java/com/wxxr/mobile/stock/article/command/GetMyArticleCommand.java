package com.wxxr.mobile.stock.article.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
@NetworkConstraint(allowConnectionTypes={})
public class GetMyArticleCommand implements ICommand<MyArticlesBean> {
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

    public GetMyArticleCommand(int p_start, int p_limit, int p_type) {
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