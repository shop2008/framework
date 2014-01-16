package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

public class PersonalHomePageLoader extends AbstractEntityLoader<String, PersonalHomePageBean, PersonalHomePageVO> {

    private static final String COMMAND_NAME = "GetPersonalHomePageCommand";
    @NetworkConstraint
    @SecurityConstraint(allowRoles={})
    private static class GetPersonalHomePageVOsCommand implements ICommand<List<PersonalHomePageVO>> {

        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<PersonalHomePageVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
           
        }
        
    }
    
    
    @Override
    public ICommand<List<PersonalHomePageVO>> createCommand(Map<String, Object> params) {
        GetPersonalHomePageVOsCommand cmd = new GetPersonalHomePageVOsCommand();  
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<PersonalHomePageVO> result,
            IReloadableEntityCache<String, PersonalHomePageBean> cache) {
        GetPersonalHomePageVOsCommand command = (GetPersonalHomePageVOsCommand) cmd;
        boolean updated = false;
       
        
        if(result != null){
            for (PersonalHomePageVO vo : result) {
                PersonalHomePageBean bean=cache.getEntity(KUtils.getService(IUserIdentityManager.class).getUserId());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(KUtils.getService(IUserIdentityManager.class).getUserId(), bean);
                }else{
                    ConverterUtils.updatefromVOtoBean(bean, vo);
                }
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
    protected List<PersonalHomePageVO> executeCommand(
            ICommand<List<PersonalHomePageVO>> command) throws Exception {
        GetPersonalHomePageVOsCommand cmd = (GetPersonalHomePageVOsCommand)command;
        PersonalHomePageVO vo = RestUtils.getRestService(ITradingProtectedResource.class).getSelfHomePage();
        if (vo!=null){
            ArrayList<PersonalHomePageVO> result=new ArrayList<PersonalHomePageVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}
