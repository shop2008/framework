/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.stock.app.db.RemindMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.RemindMessageInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangyan
 *
 */
public class ReadRemindMessageHandler implements ICommandHandler {
	public static final String COMMAND_NAME = "ReadRemindMessageCommand";

	private ICommandExecutionContext context;
	
	public static class ReadRemindMessageCommand implements ICommand<Void>{

		private String id;
		
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<Void> getResultType() {
			return Void.class;
		}

		@Override
		public void validate() {
			
		}
		
		
	}
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
		String id=((ReadRemindMessageCommand)command).getId();
		RemindMessageInfoDao dao=context.getKernelContext().getService(IDBService.class).getDaoSession().getRemindMessageInfoDao();
		List<RemindMessageInfo> list=dao.queryRaw(" _id =?", id);
		if(list==null || list.size()<1){
			return null;
		}
		RemindMessageInfo entity=list.get(0);
		entity.setRead(true);
		dao.update(entity);
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
