/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.command.GetRemindMessageCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.event.NewRemindingMessagesEvent;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.notification.ejb.api.MsgQuery;
import com.wxxr.stock.restful.json.MessageVOs;
import com.wxxr.stock.restful.resource.IMessageRemindResource;
import com.wxxr.stock.restful.resource.IMessageRemindResourceAsync;

/**
 * @author wangyan
 *
 */
public class RemindMessageLoader extends AbstractEntityLoader<String, RemindMessageBean, MessageVO, GetRemindMessageCommand> {
    private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.loader.RemindMessageLoader");
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetRemindMessageCommand createCommand(Map<String, Object> params) {
		GetRemindMessageCommand cmd=new GetRemindMessageCommand();
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetRemindMessageCommand cmd,List<MessageVO> result,
			IReloadableEntityCache<String, RemindMessageBean> cache) {
		boolean updated = false;
		if(cache.getAllKeys()!=null ||cache.getAllKeys().length>=0){
			List<RemindMessageBean> remindMessageBeans=queryRemindMessages();
			for(RemindMessageBean bean:remindMessageBeans){
				cache.putEntity(bean.getId(), bean);
				updated=true;
			}
		}
		
		if(result!=null && !result.isEmpty()){
		   List<RemindMessageBean> list  = new ArrayList<RemindMessageBean>();
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
					list.add(bean);
			}
			if (list.size()>0) {
			   if (log.isDebugEnabled()) {
                  log.debug(String.format("%d messages received.", list.size()));
               }
               NewRemindingMessagesEvent event = new NewRemindingMessagesEvent(list.toArray(new RemindMessageBean[list.size()]));
               KUtils.getService(IEventRouter.class).routeEvent(event);
            }
		}
		return updated;
	}
	
	protected void insertOrUpdateDB(RemindMessageBean bean) {
		RemindMessageInfoDao dao=this.getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageInfo> list=dao.queryRaw("where _id =? and USER_ID=?", bean.getId(),getUserId());
		RemindMessageInfo entity;
		boolean insert=false;
		if(list==null || list.size()>0){
			entity=list.get(0);
		}else{
			entity=new RemindMessageInfo();
			insert=true;
		}
		entity.setAcctId(bean.getAcctId());
		entity.setId(Long.valueOf(bean.getId()));
		StringBuilder attrs=new StringBuilder();
		if(bean.getAttrs()!=null){
			for(Entry<String, String> entry:bean.getAttrs().entrySet()){
				attrs.append(entry.getKey()).append(":::").append(entry.getValue()).append(",");
			}
		}

		entity.setAttrs(attrs.toString());
		entity.setContent(bean.getContent());
		entity.setCreatedDate(bean.getCreatedDate());
		entity.setTitle(bean.getAttrs().get("title"));
		entity.setType(bean.getType());
		entity.setUserId(getUserId());
		if(insert)
			dao.insert(entity);
		else
			dao.update(entity);
	}

	protected List<RemindMessageBean> queryRemindMessages() {
		RemindMessageInfoDao dao=this.getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
		List<RemindMessageInfo> list=dao.queryRaw("where USER_ID=?",getUserId());
		if(list!=null ){
			for(RemindMessageInfo entity:list){
				RemindMessageBean bean=new RemindMessageBean();
				bean.setAcctId(entity.getAcctId());
				bean.setId(String.valueOf(entity.getId()));
				Map<String,String> attr=new HashMap<String, String>();
				String[] atts=entity.getAttrs().split(",");
				if(atts!=null){
					for(String att:atts){
						String[] ats=att.split(":::");
						if(ats!=null && ats.length==2){
							attr.put(ats[0], ats[1]);
						}
					}
				}
				bean.setAttrs(attr);
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
		
		return GetRemindMessageCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetRemindMessageCommand cmd, IAsyncCallback<List<MessageVO>> callback) {
		
		getRestService(IMessageRemindResourceAsync.class, IMessageRemindResource.class).findById(new MsgQuery()).
		onResult(new DelegateCallback<MessageVOs, List<MessageVO>>(callback) {

			@Override
			protected List<MessageVO> getTargetValue(MessageVOs vos) {
				return vos==null ?null: vos.getMessages();
			}
		});
	}

	protected String getUserId() {
		return this.getKernelContext().getService(IUserIdentityManager.class).getUserId();
	}
}

