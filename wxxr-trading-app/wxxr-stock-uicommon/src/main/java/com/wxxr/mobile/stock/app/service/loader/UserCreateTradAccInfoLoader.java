package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.command.GetUserCreateTradAccInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

public class UserCreateTradAccInfoLoader extends AbstractEntityLoader<String, UserCreateTradAccInfoBean, UserCreateTradAccInfoVO, GetUserCreateTradAccInfoCommand>  {
    @Override
    public GetUserCreateTradAccInfoCommand createCommand(Map<String, Object> params) {
        
        return new GetUserCreateTradAccInfoCommand();
    }

    @Override
    public boolean handleCommandResult(GetUserCreateTradAccInfoCommand cmd,List<UserCreateTradAccInfoVO> result, IReloadableEntityCache<String, UserCreateTradAccInfoBean> cache) {
        boolean updated = false;
        for (UserCreateTradAccInfoVO vo : result) {
//            String id = String.valueOf(vo.getUserId());
            UserCreateTradAccInfoBean bean = cache.getEntity("userId");
            if(bean == null ) {
                bean = ConverterUtils.fromVO(vo);
                cache.putEntity("userId", bean);
                updated = true;
            }else{
                ConverterUtils.setfromVO(bean,vo);
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return GetUserCreateTradAccInfoCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetUserCreateTradAccInfoCommand cmd, IAsyncCallback<List<UserCreateTradAccInfoVO>> callback) {
        getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getCreateStrategyInfo().
        onResult(new DelegateCallback<UserCreateTradAccInfoVO, List<UserCreateTradAccInfoVO>>(callback) {

        	@Override
        	protected List<UserCreateTradAccInfoVO> getTargetValue(
        			UserCreateTradAccInfoVO vo) {
        		List<UserCreateTradAccInfoVO> result= null;
        		if (vo!=null){
        			result = new ArrayList<UserCreateTradAccInfoVO>(); 
        			result.add(vo);  
        		}
        		return result;
        	}
        });
     }
}

