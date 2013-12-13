/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.impl.DBServiceImpl;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.notification.ejb.api.MsgQuery;
import com.wxxr.stock.restful.json.MessageVOs;
import com.wxxr.stock.restful.resource.IMessageRemindResource;

/**
 * @author wangyan
 *
 */
public class RemindMessageLoader extends AbstractEntityLoader<String, RemindMessageBean, MessageVO> {

	private static String COMMAND_NAME="GetRemindMessageCommand";

	private static class GetRemindMessageCommand implements ICommand<List<MessageVO>>{

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
		public Class<List<MessageVO>> getResultType() {
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
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<MessageVO>> createCommand(Map<String, Object> params) {
		return new GetRemindMessageCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<MessageVO> result,
			IReloadableEntityCache<String, RemindMessageBean> cache) {
		boolean updated = false;
		if(cache.getAllKeys()==null ||cache.getAllKeys().length==0){
			List<RemindMessageBean> remindMessageBeans=queryRemindMessages();
			for(RemindMessageBean bean:remindMessageBeans){
				cache.putEntity(bean.getId(), bean);
				updated=true;
			}
		}
		if(result!=null && !result.isEmpty()){
			for (MessageVO vo : result) {
					RemindMessageBean bean=cache.getEntity(vo.getId());
					if(bean==null){
						bean=new RemindMessageBean();
						bean.setId(vo.getId());
						cache.putEntity(bean.getId(), bean);
					}
					bean.setAcctId(vo.getId());
					bean.setAttrs(vo.getAttributes());
					bean.setContent(vo.getContent());
					bean.setCreatedDate(vo.getCreatedDate());
					bean.setTitle(vo.getAttributes().get("title"));
					bean.setType(bean.getType());
					insertOrUpdateDB(bean);
					updated = true;
			}
		}
		return updated;
	}
	
	protected void insertOrUpdateDB(RemindMessageBean bean) {
		RemindMessageInfoDao dao=this.cmdCtx.getKernelContext().getService(DBServiceImpl.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageInfo> list=dao.queryRaw("where id =?", bean.getId());
		RemindMessageInfo entity;
		boolean insert=false;
		if(list==null || list.size()>0){
			entity=new RemindMessageInfo();
			
		}else{
			entity=list.get(0);
			
		}
		entity.setAcctId(bean.getAcctId());
		entity.setId(Long.valueOf(bean.getId()));
		entity.setAttrs(bean.getAttrs().toString());
		entity.setContent(bean.getContent());
		entity.setCreatedDate(bean.getCreatedDate());
		entity.setTitle(bean.getAttrs().get("title"));
		entity.setType(bean.getType());
		if(insert)
			dao.insert(entity);
		else
			dao.update(entity);
	}

	protected List<RemindMessageBean> queryRemindMessages() {
		RemindMessageInfoDao dao=this.cmdCtx.getKernelContext().getService(DBServiceImpl.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
		List<RemindMessageInfo> list=dao.loadAll();
		if(list!=null ){
			for(RemindMessageInfo entity:list){
				RemindMessageBean bean=new RemindMessageBean();
				bean.setAcctId(entity.getAcctId());
				bean.setId(entity.getId()+"");
//				entity.setAttrs(entity.getAttrs().toString());
				bean.setContent(entity.getContent());
				bean.setCreatedDate(entity.getCreatedDate());
				bean.setTitle(entity.getTitle());
				bean.setType(entity.getType());
				remindMessages.add(bean);
			}
		}
		return remindMessages;
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
	protected List<MessageVO> executeCommand(ICommand<List<MessageVO>> command)
			throws Exception {
		
		MessageVOs vos=getRestService(IMessageRemindResource.class).findById(new MsgQuery());
		
		return vos==null ?null: vos.getMessages();
	}

}
