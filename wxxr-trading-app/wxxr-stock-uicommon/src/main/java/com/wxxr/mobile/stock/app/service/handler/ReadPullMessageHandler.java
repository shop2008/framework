package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.db.PullMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.PullMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

public class ReadPullMessageHandler extends BasicCommandHandler<Void,ReadPullMessageCommand> {

	
	public static final String COMMAND_NAME = "ReadPullMessageCommand";
	
	@Override
	public void execute(ReadPullMessageCommand command,IAsyncCallback<Void> callback) {
		try {
			long id=((ReadPullMessageCommand)command).getId();
			PullMessageInfoDao dao=getService(IDBService.class).getDaoSession().getPullMessageInfoDao();
			List<PullMessageInfo> list=dao.queryRaw("where _id =?", id+"");
			if(list!=null && list.size()>=1){
			PullMessageInfo entity=list.get(0);
			entity.setRead(true);
			dao.update(entity);
			}
			callback.success(null);
		}catch(Throwable t){
			callback.failed(t);
		}
	}


}
