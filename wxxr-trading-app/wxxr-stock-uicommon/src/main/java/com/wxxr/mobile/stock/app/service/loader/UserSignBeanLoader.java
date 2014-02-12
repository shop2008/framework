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
import com.wxxr.mobile.stock.app.bean.UserSignBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

/**
 * @author wangyan
 *
 */
public class UserSignBeanLoader extends AbstractEntityLoader<String, UserSignBean, UserSignVO> {

	private static final String COMMAND_NAME = "GetUserSignCommand";

	@NetworkConstraint
	@SecurityConstraint(allowRoles={})
	private static class GetUserSignCommand implements ICommand<List<UserSignVO>>{
		public String getCommandName() {
			return COMMAND_NAME;
		}
		public void validate() {
			
			
		}
		public Class<List<UserSignVO>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}
		
	}
	@Override
	public ICommand<List<UserSignVO>> createCommand(
			Map<String, Object> params) {
		return new GetUserSignCommand();
	}
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<UserSignVO> result,
			IReloadableEntityCache<String, UserSignBean> cache) {
		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (UserSignVO vo : result) {
				UserSignBean bean = cache.getEntity(UserSignBean.class.getCanonicalName());
				if (bean == null) {
					bean = new UserSignBean();
					cache.putEntity(VoucherBean.class.getCanonicalName(), bean);
				}
				bean.setFailReason(vo.getFailReason());
				bean.setOngoingDays(vo.getOngoingDays());
				bean.setRewardVol(vo.getRewardVol());
				bean.setSign(vo.isSign());
				bean.setSignDate(vo.getSignDate());
				bean.setSuccess(vo.getSuccess());
				updated = true;
			}
		}
		return updated;
	}
	protected String getCommandName() {
		
		return COMMAND_NAME;
	}
	protected List<UserSignVO> executeCommand(
			ICommand<List<UserSignVO>> command) throws Exception {
		UserSignVO vo=getRestService(ITradingResource.class).getUserSignMessage();
		List<UserSignVO> list=new ArrayList<UserSignVO>();
		list.add(vo);
		return list;
	}

}
