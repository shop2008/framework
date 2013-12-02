/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.ArticleResource;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;

/**
 * @author wangyan
 *
 */
public class PullMessageLoader extends AbstractEntityLoader<Long, PullMessageBean, PullMessageVO> {

	private final static String COMMAND_NAME="GetPullMessasge";
	
	private static class GetPullMessasgeCommand implements ICommand<List<PullMessageVO>>{

		private int start,limit;
		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
		 */
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
		 */
		@Override
		public Class<List<PullMessageVO>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			// TODO Auto-generated method stub
			
		}

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @return the limit
		 */
		public int getLimit() {
			return limit;
		}

		/**
		 * @param limit the limit to set
		 */
		public void setLimit(int limit) {
			this.limit = limit;
		}
		
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<PullMessageVO>> createCommand(
			Map<String, Object> params) {
		Integer start=(Integer) params.get("start");
		Integer limit=(Integer) params.get("limit");
		GetPullMessasgeCommand command=new GetPullMessasgeCommand();
		command.setLimit(limit);
		command.setStart(start);
		return command;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<PullMessageVO> result,
			IReloadableEntityCache<Long, PullMessageBean> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
			for (PullMessageVO vo : result) {
					PullMessageBean bean=cache.getEntity(vo.getId());
					if(bean==null){
						bean=new PullMessageBean();
						bean.setId(vo.getId());
						cache.putEntity(bean.getId(), bean);
					}
					bean.setArticleUrl(vo.getArticleUrl());
					bean.setCreateDate(vo.getCreateDate());
					bean.setMessage(vo.getMessage());
					bean.setPhone(vo.getPhone());
					bean.setTitle(vo.getTitle());
					updated = true;
				
			}
		}		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<PullMessageVO> executeCommand(
			ICommand<List<PullMessageVO>> command) throws Exception {
		return getRestService(ArticleResource.class).getPullMessage(((GetPullMessasgeCommand)command).getStart(), ((GetPullMessasgeCommand)command).getLimit());
	}

}
