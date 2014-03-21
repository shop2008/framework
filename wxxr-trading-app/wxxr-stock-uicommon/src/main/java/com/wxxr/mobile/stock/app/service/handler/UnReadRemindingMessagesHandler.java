/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class UnReadRemindingMessagesHandler extends BasicCommandHandler<List<RemindMessageBean>,UnReadRemindingMessagesCommand> {

	public static final String COMMAND_NAME = "UnReadRemindingMessagesCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(UnReadRemindingMessagesCommand command, IAsyncCallback<List<RemindMessageBean>> callback) {
		try {
			RemindMessageInfoDao dao=getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
			List<RemindMessageBean> remindMessages=new ArrayList<RemindMessageBean>();
			List<RemindMessageInfo> list=dao.queryRaw(" where READ=0 ");
			if(list!=null ){
				for(RemindMessageBean entity:remindMessages){
					RemindMessageBean bean=new RemindMessageBean();
					bean.setAcctId(entity.getAcctId());
					bean.setAcctId(entity.getId());
	//					entity.setAttrs(entity.getAttrs().toString());
					bean.setContent(entity.getContent());
					bean.setCreatedDate(entity.getCreatedDate());
					bean.setTitle(entity.getAttrs().get("title"));
					bean.setType(entity.getType());
					remindMessages.add(bean);
				}
			}
			callback.success(remindMessages);
		}catch(Throwable t){
			callback.failed(t);
		}

	}
}
