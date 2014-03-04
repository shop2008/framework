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
public class ReadRemindMessageHandler extends BasicCommandHandler<Void,ReadRemindMessageCommand> {
	public static final String COMMAND_NAME = "ReadRemindMessageCommand";

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(ReadRemindMessageCommand command, IAsyncCallback<Void> callback) {
		try {
			String id=((ReadRemindMessageCommand)command).getId();
			RemindMessageInfoDao dao=getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
			List<RemindMessageInfo> list=dao.queryRaw(" where  _id =?", id);
			if(list!=null && list.size()>=1){
				RemindMessageInfo entity=list.get(0);
				entity.setRead(true);
				dao.update(entity);
			}
			callback.success(null);
		}catch(Throwable t){
			callback.failed(t);
		}
	}

	
}
