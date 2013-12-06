/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.stock.app.bean.ArticleBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.restful.resource.ArticleResource;

/**
 * @author neillin
 *
 */
public class MyArticleLoader extends AbstractEntityLoader<String, ArticleBean, ArticleVO> {

	private static final String COMMAND_NAME = "GetMyArticles";
	 @NetworkConstraint
	private static class GetMyArticlesCommand implements ICommand<List<ArticleVO>> {

		private int start, limit, type;
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class<List<ArticleVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
			
		}
		
		
		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @return the limit
		 */
		public int getLimit() {
			return limit;
		}


		/**
		 * @param start the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @param limit the limit to set
		 */
		public void setLimit(int limit) {
			this.limit = limit;
		}

		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * @param type the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}

	}
	
	private int articleType;
	
	@Override
	public ICommand<List<ArticleVO>> createCommand(Map<String, Object> params) {
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
	public boolean handleCommandResult(ICommand<?> cmd, List<ArticleVO> result,
			IReloadableEntityCache<String, ArticleBean> cache) {
		GetMyArticlesCommand g=(GetMyArticlesCommand)cmd;
		if(g.start == 0){
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
        article.setArticleUrl(getAbsoluteURL(vo.getArticleUrl()));
        article.setImageUrl(getAbsoluteURL(vo.getThumbnails()));
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
		return COMMAND_NAME;
	}

	@Override
	protected List<ArticleVO> executeCommand(ICommand<List<ArticleVO>> command)
			throws Exception {
    	GetMyArticlesCommand g=(GetMyArticlesCommand) command;
        NewsQueryBO bo=new NewsQueryBO();
        bo.setLimit(g.getLimit());
        bo.setStart(g.getStart());
        bo.setType(String.valueOf(g.getType()));
        return RestUtils.getRestService(ArticleResource.class).getNewArticle(bo);
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
