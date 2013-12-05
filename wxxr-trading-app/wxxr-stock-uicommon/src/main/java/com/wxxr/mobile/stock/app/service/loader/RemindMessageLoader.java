/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.notification.ejb.api.MsgQuery;
import com.wxxr.stock.restful.resource.IMessageRemindResource;

/**
 * @author wangyan
 *
 */
public class RemindMessageLoader extends AbstractEntityLoader<String, RemindMessageBean, MessageVO> {

	private static String COMMAND_NAME="GetRemindMessageCommand";

	private static class GetRemindMessageCommand implements ICommand<List<MessageVO>>{

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
		 */
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
		 */
		@Override
		public Class<List<MessageVO>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			// TODO Auto-generated method stub
			
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<MessageVO>> createCommand(Map<String, Object> params) {
		return new GetRemindMessageCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<MessageVO> result,
			IReloadableEntityCache<String, RemindMessageBean> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
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
					bean.setTitle(vo.getAttributes().get(""));
					bean.setType(bean.getType());
					updated = true;
				
			}
		}
		return updated;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<MessageVO> executeCommand(ICommand<List<MessageVO>> command)
			throws Exception {
		return getRestService(IMessageRemindResource.class).findById(new MsgQuery());
	}

}
