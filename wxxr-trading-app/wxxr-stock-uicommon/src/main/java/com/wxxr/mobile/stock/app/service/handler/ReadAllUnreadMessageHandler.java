/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class ReadAllUnreadMessageHandler extends BasicCommandHandler<Void,ReadAllUnreadMessageCommand> {

	public static final String COMMAND_NAME = "ReadAllUnreadMessageCommand";
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(ReadAllUnreadMessageCommand command,IAsyncCallback<Void> callback) {
		try {
			RemindMessageInfoDao dao=getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
			List<RemindMessageInfo> list=dao.queryRaw(" where read=0 ");
			if(list != null){
				for(RemindMessageInfo entity:list){
					entity.setRead(true);
					dao.update(entity);
				}
			}
			callback.success(null);
		}catch(Throwable t){
			callback.failed(t);
		}
	}

}
