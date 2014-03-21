/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class UNReadRemindingMessageLoader extends AbstractEntityLoader<String, RemindMessageBean, RemindMessageBean, UnReadRemindingMessagesCommand>{


	@Override
	public UnReadRemindingMessagesCommand createCommand(Map<String, Object> params) {
		UnReadRemindingMessagesCommand command=new UnReadRemindingMessagesCommand();
		return command;
	}

	@Override
	public boolean handleCommandResult(UnReadRemindingMessagesCommand cmd, List<RemindMessageBean> result,
			IReloadableEntityCache<String, RemindMessageBean> cache) {
		cache.clear();
		if(result!=null){
			for(RemindMessageBean bean:result){
				cache.putEntity(bean.getId(), bean);
			}
		}
		return true;
	}

	@Override
	protected String getCommandName() {
		return UnReadRemindingMessagesCommand.COMMAND_NAME;
	}

	@Override
	protected void executeCommand(UnReadRemindingMessagesCommand cmd, IAsyncCallback<List<RemindMessageBean>> callback) {
		try {
			RemindMessageInfoDao dao= getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
			List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
			List<RemindMessageInfo> list=dao.queryRaw(" where read=0 and USER_ID=?",getUserId());
			if(list!=null ){
				for(RemindMessageInfo entity:list){
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
			callback.success(remindMessages);
		}catch(Throwable t){
			callback.failed(t);
		}
	}

	protected String getUserId() {
		return this.getKernelContext().getService(IUserIdentityManager.class).getUserId();
	}
}

