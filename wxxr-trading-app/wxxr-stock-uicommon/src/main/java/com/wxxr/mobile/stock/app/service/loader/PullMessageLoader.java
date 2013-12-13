/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.PullMessageInfo;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.PullMessageInfoDao;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.stock.restful.json.PullMessageVOs;
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
		if(cache.getAllKeys()==null ||cache.getAllKeys().length==0){
			List<PullMessageBean> pullMessageBeans=queryRemindMessages();
			for(PullMessageBean bean:pullMessageBeans){
				cache.putEntity(bean.getId(), bean);
				updated=true;
			}
		}
		
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
					insertOrUpdateDB(bean);
					updated = true;
			}
		}		
		return updated;
	}

	protected void insertOrUpdateDB(PullMessageBean bean) {
		PullMessageInfoDao dao=this.cmdCtx.getKernelContext().getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageInfo> list=dao.queryRaw("where _id =?", String.valueOf(bean.getId()));
		PullMessageInfo entity;
		boolean insert=false;
		if(list==null || list.size()>0){
			entity=list.get(0);
		}else{
			entity=new PullMessageInfo();
			insert=true;
		}
		entity.setId(Long.valueOf(bean.getId()));
		entity.setArticleUrl(bean.getArticleUrl());
		entity.setMessage(bean.getMessage());
		entity.setPhone(bean.getPhone());
		entity.setCreateDate(bean.getCreateDate());
		entity.setTitle(bean.getTitle());
		entity.setRead(false);
		if(insert)
			dao.insert(entity);
		else
			dao.update(entity);
	}
	
	protected List<PullMessageBean> queryRemindMessages() {
		PullMessageInfoDao dao=this.cmdCtx.getKernelContext().getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageBean> pullMessageBeans=new ArrayList<PullMessageBean>();
		List<PullMessageInfo> list=dao.loadAll();
		if(list!=null ){
			for(PullMessageInfo entity:list){
				PullMessageBean bean=new PullMessageBean();
				bean.setArticleUrl(entity.getArticleUrl());
				bean.setCreateDate(entity.getCreateDate());
				bean.setId(entity.getId());
				bean.setMessage(entity.getMessage());
				bean.setPhone(entity.getPhone());
				bean.setRead(entity.getRead());
				bean.setTitle(entity.getTitle());
				pullMessageBeans.add(bean);
			}
		}
		return pullMessageBeans;
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
		PullMessageVOs vos= getRestService(ArticleResource.class).getPullMessage(((GetPullMessasgeCommand)command).getStart(), ((GetPullMessasgeCommand)command).getLimit());
		return vos==null ?null:vos.getPullMessages();
	}

}
