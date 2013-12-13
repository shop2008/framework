package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.stock.app.db.PullMessageInfo;
import com.wxxr.mobile.stock.app.db.dao.PullMessageInfoDao;
import com.wxxr.mobile.stock.app.service.impl.DBServiceImpl;

public class ReadPullMessageHandler implements ICommandHandler {

	
	public static final String COMMAND_NAME = "ReadPullMessageCommand";
	private ICommandExecutionContext context;
	
	public static class ReadPullMessageCommand implements ICommand<Void>{

		private long id;
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

		/**
		 * @return the id
		 */
		public long getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(long id) {
			this.id = id;
		}
		
		
	}
	@Override
	public void destroy() {

	}

	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		long id=((ReadPullMessageCommand)command).getId();
		PullMessageInfoDao dao=context.getKernelContext().getService(DBServiceImpl.class).getDaoSession().getPullMessageInfoDao();
		List<PullMessageInfo> list=dao.queryRaw(" id =?", id+"");
		if(list==null || list.size()<1){
			return null;
		}
		PullMessageInfo entity=list.get(0);
		entity.setRead(true);
		dao.update(entity);
		return null;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
