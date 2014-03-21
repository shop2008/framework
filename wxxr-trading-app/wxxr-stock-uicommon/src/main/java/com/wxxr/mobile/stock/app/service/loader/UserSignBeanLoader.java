/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.UserSignBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.command.GetUserSignCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

/**
 * @author wangyan
 *
 */
public class UserSignBeanLoader extends AbstractEntityLoader<String, UserSignBean, UserSignVO,GetUserSignCommand> {

	@Override
	public GetUserSignCommand createCommand(
			Map<String, Object> params) {
		return new GetUserSignCommand();
	}
	@Override
	public boolean handleCommandResult(GetUserSignCommand cmd,List<UserSignVO> result,
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
				bean.setSign(vo.getSign());
				bean.setSignDate(vo.getSignDate());
				bean.setSuccess(vo.getSuccess());
				updated = true;
			}
		}
		return updated;
	}
	
	protected String getCommandName() {
		
		return GetUserSignCommand.COMMAND_NAME;
	}
	
	
	@Override
	protected void executeCommand(GetUserSignCommand command,
			IAsyncCallback<List<UserSignVO>> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).
			getUserSignMessage().onResult(new DelegateCallback<UserSignVO, List<UserSignVO>>(callback) {

				@Override
				protected List<UserSignVO> getTargetValue(UserSignVO vo) {
					List<UserSignVO> list=new ArrayList<UserSignVO>();
					if(vo != null){
						list.add(vo);
					}
					return list;
				}
			});;
	}

}
