/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.db.PullMessageInfo;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.PullMessageInfoDao;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.mobile.stock.app.service.IMessageManagementService;

/**
 * 
 */
public class MessageManagementServiceImpl extends AbstractModule<IStockAppContext> implements IMessageManagementService {
	
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.MessageManagementServiceImpl");

	@Override
	public List<PullMessageBean> getUnReadPullMessage() {
		List<PullMessageBean> msgs =  queryPullMessages();
		if (log.isDebugEnabled()) {
			log.debug(String.format("load %d  pull msgs from db", msgs==null?0:msgs.size()));
		}
		return msgs;
	}

	@Override
	public List<RemindMessageBean> getUnReadRemindMessage() {
		List<RemindMessageInfo> msgs = getService(IDBService.class).getDaoSession().getRemindMessageInfoDao().queryRaw(" where READ=0 and USER_ID=? order by CREATED_DATE desc",getUserId());
		if (log.isDebugEnabled()) {
			log.debug(String.format("load %d  remind msgs from db", msgs==null?0:msgs.size()));
		}
		List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
		if(msgs!=null ){
			for(RemindMessageInfo entity:msgs){
				RemindMessageBean bean=new RemindMessageBean();
				bean.setAcctId(entity.getAcctId());
				bean.setId(entity.getId()+"");
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
		return  remindMessages;
	}




	@Override
	protected void initServiceDependency() {
		addRequiredService(IDBService.class);
	}

	@Override
	protected void startService() {
		context.registerService(IMessageManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IMessageManagementService.class, this);
	}

	
	
	
	protected void insertOrUpdateDB(RemindMessageBean bean) {
		RemindMessageInfoDao dao=getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageInfo> list=dao.queryRaw("where  _id =? and USER_ID=?", bean.getId(),getUserId());
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

	private String getUserId() {
		return getService(IUserIdentityManager.class).getUserId();
	}

	@Override
	public void saveRemindMsg(RemindMessageBean msg) {
		if (msg!=null) {
			insertOrUpdateDB(msg);
		}
	}

	@Override
	public void savePullMsg(PullMessageBean msg) {
		PullMessageInfoDao dao=getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageInfo> list=dao.queryRaw("where PULL_ID =? ", String.valueOf(msg.getPullId()));
		PullMessageInfo entity;
		boolean insert=false;
		if(list==null || list.size()>0){
			entity=list.get(0);
		}else{
			entity=new PullMessageInfo();
			insert=true;
		}
		entity.setUserId(getUserId());
		entity.setPullId(msg.getPullId());
		entity.setArticleUrl(msg.getArticleUrl());
		entity.setMessage(msg.getMessage());
		entity.setPhone(msg.getPhone());
		entity.setCreateDate(msg.getCreateDate());
		entity.setTitle(msg.getTitle());
		
		if(insert)
			msg.setId(dao.insert(entity));
		else
			dao.update(entity);
		
	}
	protected List<PullMessageBean> queryPullMessages() {
		PullMessageInfoDao dao=getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageBean> pullMessageBeans=new ArrayList<PullMessageBean>();
		List<PullMessageInfo> list=dao.queryRaw("where READ=0 ");
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

	@Override
	public PullMessageBean getFirstPullMessage() {
		List<PullMessageInfo> msgs = getService(IDBService.class).getDaoSession().getPullMessageInfoDao().queryRaw(" where USER_ID=? order by CREATE_DATE desc",getUserId());
		
		PullMessageInfo entity = null;
		if(msgs!=null&&msgs.size()>0&&(entity=msgs.get(0))!=null ){
			PullMessageBean bean=new PullMessageBean();
			bean.setArticleUrl(entity.getArticleUrl());
			bean.setCreateDate(entity.getCreateDate());
			bean.setId(entity.getId());
			bean.setMessage(entity.getMessage());
			bean.setPhone(entity.getPhone());
			bean.setRead(entity.getRead());
			bean.setTitle(entity.getTitle());
			bean.setPullId(entity.getPullId());
			return bean;
		}
		return null;
	}

	@Override
	public RemindMessageBean getFirstRemindMessage() {
		List<RemindMessageInfo> msgs = getService(IDBService.class).getDaoSession().getRemindMessageInfoDao().queryRaw(" where USER_ID=? order by _id desc",getUserId());
		if (log.isDebugEnabled()) {
			log.debug(String.format("load %d  remind msgs from db", msgs==null?0:msgs.size()));
		}
		RemindMessageInfo entity = null;
		if(msgs!=null && msgs.size()>0 && (entity=msgs.get(0)) !=null){
			RemindMessageBean bean=new RemindMessageBean();
			bean.setAcctId(entity.getAcctId());
			bean.setId(entity.getId()+"");
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
			return bean;
		}
		return null;
	}
}
