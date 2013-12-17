/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class UnReadRemindingMessagesHandler implements ICommandHandler {

	public static final String COMMAND_NAME = "UnReadRemindingMessagesCommand";

	private ICommandExecutionContext  context;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#destroy()
	 */
	@Override
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {

		RemindMessageInfoDao dao=this.context.getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
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
		return (T) remindMessages;

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

	public static class UnReadRemindingMessagesCommand implements ICommand<List<RemindMessageBean>>{

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<List<RemindMessageBean>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		@Override
		public void validate() {
			
		}
		
	}
}
