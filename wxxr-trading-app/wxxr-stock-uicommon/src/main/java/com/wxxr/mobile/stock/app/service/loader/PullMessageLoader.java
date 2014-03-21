/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.command.GetPullMessasgeCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.PullMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.PullMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.stock.restful.json.PullMessageVOs;
import com.wxxr.stock.restful.resource.ArticleResource;
import com.wxxr.stock.restful.resource.ArticleResourceAsync;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;

/**
 * @author wangyan
 *
 */
public class PullMessageLoader extends AbstractEntityLoader<String, PullMessageBean, PullMessageVO, GetPullMessasgeCommand> {
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetPullMessasgeCommand createCommand(
			Map<String, Object> params) {
		
		GetPullMessasgeCommand command=new GetPullMessasgeCommand();
		if(params == null) {
			command.setStart(0);
			command.setLimit(20);
		} else {
			Integer start=(Integer) params.get("start");
			Integer limit=(Integer) params.get("limit");
			command.setLimit(limit);
			command.setStart(start);
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetPullMessasgeCommand cmd,List<PullMessageVO> result,
			IReloadableEntityCache<String, PullMessageBean> cache) {
		
		
		boolean updated = false;

		List<PullMessageBean> pullMessageBeans=queryRemindMessages();
		for(PullMessageBean bean:pullMessageBeans){
			String key=bean.getPullId()+"";
			cache.putEntity(key, bean);
			if (!bean.isRead()) {
				bean.setRead(true);
				insertOrUpdateDB(bean);
			}
			updated=true;
		}

		if(result!=null && !result.isEmpty()){
			for (PullMessageVO vo : result) {
					String key=vo.getId()+"";
					PullMessageBean bean=cache.getEntity(key);
					if(bean==null){
						bean=new PullMessageBean();
						cache.putEntity(key, bean);
						bean.setPullId(vo.getId());
						bean.setArticleUrl(vo.getArticleUrl());
						bean.setCreateDate(vo.getCreateDate());
						bean.setMessage(vo.getMessage());
						bean.setPhone(vo.getPhone());
						bean.setTitle(vo.getTitle());
						insertOrUpdateDB(bean);
						updated = true;
					}

					
			}
		}		
		return updated;
	}

	protected void insertOrUpdateDB(PullMessageBean bean) {
		PullMessageInfoDao dao=this.getKernelContext().getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageInfo> list=dao.queryRaw("where PULL_ID =?", String.valueOf(bean.getPullId()));
		PullMessageInfo entity;
		boolean insert=false;
		if(list==null || list.size()>0){
			entity=list.get(0);
		}else{
			entity=new PullMessageInfo();
			insert=true;
		}
		entity.setRead(true);
		entity.setUserId(getUserId());
		entity.setPullId(bean.getPullId());
		entity.setArticleUrl(bean.getArticleUrl());
		entity.setMessage(bean.getMessage());
		entity.setPhone(bean.getPhone());
		entity.setCreateDate(bean.getCreateDate());
		entity.setTitle(bean.getTitle());
		
		if(insert)
			bean.setId(dao.insert(entity));
		else
			dao.update(entity);
		
	}
	
	protected List<PullMessageBean> queryRemindMessages() {
		PullMessageInfoDao dao=this.getKernelContext().getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
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
				bean.setPullId(entity.getPullId());
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
		return GetPullMessasgeCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetPullMessasgeCommand command, IAsyncCallback<List<PullMessageVO>> callback) {
		getRestService(ArticleResourceAsync.class, ArticleResource.class).getPullMessage(((GetPullMessasgeCommand)command).getStart(), ((GetPullMessasgeCommand)command).getLimit()).
		onResult(new DelegateCallback<PullMessageVOs,List<PullMessageVO>>(callback) {

			@Override
			protected List<PullMessageVO> getTargetValue(PullMessageVOs vos) {
				if(vos==null || vos.getPullMessages()==null){
					return new ArrayList<PullMessageVO>();
				}else{
					return vos.getPullMessages();
				}
			}
		});
	}
	protected String getUserId() {
		return this.getKernelContext().getService(IUserIdentityManager.class).getUserId();
	}
}
