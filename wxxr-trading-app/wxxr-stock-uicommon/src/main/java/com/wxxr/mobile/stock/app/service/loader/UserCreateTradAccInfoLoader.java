package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

public class UserCreateTradAccInfoLoader extends AbstractEntityLoader<String, UserCreateTradAccInfoBean, UserCreateTradAccInfoVO>  {
    private static final String COMMAND_NAME = "GetUserCreateTradAccInfo";
    @NetworkConstraint
    private static class GetUserCreateTradAccInfoCommand implements ICommand<List<UserCreateTradAccInfoVO>> {

        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<UserCreateTradAccInfoVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
        }
        
    }
    @Override
    public ICommand<List<UserCreateTradAccInfoVO>> createCommand(Map<String, Object> params) {
        
        return new GetUserCreateTradAccInfoCommand();
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<UserCreateTradAccInfoVO> result, IReloadableEntityCache<String, UserCreateTradAccInfoBean> cache) {
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
        return COMMAND_NAME;
    }

    @Override
    protected List<UserCreateTradAccInfoVO> executeCommand(ICommand<List<UserCreateTradAccInfoVO>> command) throws Exception {
        List<UserCreateTradAccInfoVO> result=new ArrayList<UserCreateTradAccInfoVO>(); 
        UserCreateTradAccInfoVO vo=getRestService(ITradingProtectedResource.class).getCreateStrategyInfo();
        if (vo!=null){
            result.add(vo);  
        }
        return result;
    }
}
