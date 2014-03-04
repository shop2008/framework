/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.command.GetUserInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class UserInfoLoader extends AbstractEntityLoader<String, UserBean, UserVO, GetUserInfoCommand> {

	@Override
	public GetUserInfoCommand createCommand(Map<String, Object> params) {
		return new GetUserInfoCommand();
	}

	@Override
	public boolean handleCommandResult(GetUserInfoCommand cmd, List<UserVO> result,
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
//				bean.setUserPic(vo.getIcon());
				updated = true;
			}
		}
		return updated;
	}

	@Override
	protected String getCommandName() {
		return GetUserInfoCommand.COMMAND_NAME;
	}

	@Override
	protected void executeCommand(GetUserInfoCommand cmd, IAsyncCallback<List<UserVO>> callback) {
			getRestService(StockUserResourceAsync.class,StockUserResource.class).getUser().
			onResult(new DelegateCallback<UserVO, List<UserVO>>(callback) {

				@Override
				protected List<UserVO> getTargetValue(UserVO vo) {
					List<UserVO> list= null;
					if(vo != null) {
						list = new ArrayList<UserVO>();
						list.add(vo);
					}
					return list;
				}
			});
	}

}

