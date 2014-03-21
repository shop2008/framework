/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.command.GetMyArticlesCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.article.ejb.api.ArticleVOs;
import com.wxxr.stock.restful.resource.ArticleResource;
import com.wxxr.stock.restful.resource.ArticleResourceAsync;

/**
 * @author neillin
 *
 */
public class MyArticleLoader extends AbstractEntityLoader<String, ArticleBean, ArticleVO, GetMyArticlesCommand> {

	private int articleType;
	
	@Override
	public GetMyArticlesCommand createCommand(Map<String, Object> params) {
		if(params == null){
			return null;
		}
		GetMyArticlesCommand cmd = new GetMyArticlesCommand();
		Integer val = (Integer)params.get("start");
		if(val != null){
			cmd.setStart(val);
		}
		val = (Integer)params.get("limit");
		if(val != null){
			cmd.setLimit(val);
		}
		cmd.setType(this.articleType);
		return cmd;
	}

	@Override
	public boolean handleCommandResult(GetMyArticlesCommand g, List<ArticleVO> result,
			IReloadableEntityCache<String, ArticleBean> cache) {
		if(g.getStart() == 0){
			cache.clear();
		}
		if((result != null)&&(result.size() > 0)){
			for (ArticleBean article : fromVO(result)) {
				cache.putEntity(article.getId(), article);
			}
		}
		return true;
	}

	
    private ArticleBean fromVO(ArticleVO vo){
        if (vo==null) {
            return null;
        }
        ArticleBean article = new ArticleBean();
        article.setId(vo.getId());
        article.setTitle(vo.getTitle());
        article.setAbstractInfo(vo.getAbstracts());
        article.setPower(vo.getPower());
        article.setCreateDate(vo.getArticleCreateDate());
        article.setType(Integer.valueOf(vo.getType()));
        article.setArticleUrl(getAbsoluteURL(vo.getArticleUrl()));
        article.setImageUrl(getAbsoluteURL(vo.getThumbnails()==null?vo.getThumbnailsInfo():vo.getThumbnails()));
        return article;
    }
    
    private String getArticleHostURL(){
        return KUtils.getService(IURLLocatorManagementService.class).getMagnoliaURL();
    }
    
    private String getAbsoluteURL(String relativeUrl){
        return getArticleHostURL()+relativeUrl;
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

	@Override
	protected String getCommandName() {
		return GetMyArticlesCommand.COMMAND_NAME;
	}

	@Override
	protected void executeCommand(GetMyArticlesCommand command,IAsyncCallback<List<ArticleVO>> callback) {
    	GetMyArticlesCommand g=(GetMyArticlesCommand) command;

        getRestService(ArticleResourceAsync.class,ArticleResource.class).getNewArticle(String.valueOf(g.getType()),g.getStart(),g.getLimit()).
        onResult(new DelegateCallback<ArticleVOs, List<ArticleVO>>(callback) {

			@Override
			protected List<ArticleVO> getTargetValue(ArticleVOs vos) {
		        return vos==null?null:vos.getArticles();
			}
		});
	}

	/**
	 * @return the articleType
	 */
	public int getArticleType() {
		return articleType;
	}

	/**
	 * @param articleType the articleType to set
	 */
	public void setArticleType(int articleType) {
		this.articleType = articleType;
	}

	
}
