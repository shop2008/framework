/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class UserInfoLoader extends AbstractEntityLoader<String, UserBean, UserVO> {

	private static final String COMMAND_NAME = "GetUserInfoCommand";

	
	@NetworkConstraint
	private static class GetUserInfoCommand implements ICommand<List<UserVO>>{

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
		public Class<List<UserVO>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			
		}
		
	}
	@Override
	public ICommand<List<UserVO>> createCommand(Map<String, Object> params) {
		return new GetUserInfoCommand();
	}

	@Override
	public boolean handleCommandResult(ICommand<?> cmd, List<UserVO> result,
			IReloadableEntityCache<String, UserBean> cache) {
		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (UserVO vo : result) {
				UserBean bean = cache.getEntity(UserBean.class.getCanonicalName());
				if (bean == null) {
					bean = new UserBean();
					cache.putEntity(UserBean.class.getCanonicalName(), bean);
				}
				bean.setNickName(vo.getNickName());
				bean.setUsername(vo.getUserName());
				bean.setPhoneNumber(vo.getMoblie());
				bean.setUserPic(vo.getIcon());
				updated = true;
			}
		}
		return updated;
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	protected List<UserVO> executeCommand(ICommand<List<UserVO>> command)
			throws Exception {
		UserVO vo=null;
		try{
			vo= getRestService(StockUserResource.class).getUser();
		}catch(Exception e){
			vo=null;
		}
		List<UserVO> list=new ArrayList<UserVO>();
		list.add(vo);
		return list;
	}

}
