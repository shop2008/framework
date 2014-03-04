package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.command.GetPersonalHomePageVOsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

public class PersonalHomePageLoader extends AbstractEntityLoader<String, PersonalHomePageBean, PersonalHomePageVO, GetPersonalHomePageVOsCommand> {

    @Override
    public GetPersonalHomePageVOsCommand createCommand(Map<String, Object> params) {
        GetPersonalHomePageVOsCommand cmd = new GetPersonalHomePageVOsCommand();  
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetPersonalHomePageVOsCommand cmd,List<PersonalHomePageVO> result,
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
        return GetPersonalHomePageVOsCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetPersonalHomePageVOsCommand command, IAsyncCallback<List<PersonalHomePageVO>> callback) {
        GetPersonalHomePageVOsCommand cmd = (GetPersonalHomePageVOsCommand)command;
        getRestService(ITradingProtectedResourceAsync.class, ITradingProtectedResource.class).getSelfHomePage().
        onResult(new DelegateCallback<PersonalHomePageVO, List<PersonalHomePageVO>>(callback) {

			@Override
			protected List<PersonalHomePageVO> getTargetValue(
					PersonalHomePageVO vo) {
		        if (vo!=null){
		            ArrayList<PersonalHomePageVO> result=new ArrayList<PersonalHomePageVO>();
		            result.add(vo);
		            return result;
		        }
		        return null;
			}
		});
    }

}


