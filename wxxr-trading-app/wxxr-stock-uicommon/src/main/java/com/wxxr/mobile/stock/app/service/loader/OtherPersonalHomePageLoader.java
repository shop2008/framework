package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

public class OtherPersonalHomePageLoader  extends AbstractEntityLoader<String, PersonalHomePageBean, PersonalHomePageVO> {
    private static final String COMMAND_NAME = "GetOtherPersonalHomePageLoaderCommand";
    private static class GetOtherPersonalHomePageLoader implements ICommand<List<PersonalHomePageVO>> {
        private String userId;
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
            if (this.userId==null){
                throw new IllegalArgumentException();
            }
           
        }
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }   
    }
    
    @Override
    public ICommand<List<PersonalHomePageVO>> createCommand(Map<String, Object> params) {
        GetOtherPersonalHomePageLoader cmd = new GetOtherPersonalHomePageLoader();
        cmd.setUserId((String) params.get("userId"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<PersonalHomePageVO> result,
            IReloadableEntityCache<String, PersonalHomePageBean> cache) {
        GetOtherPersonalHomePageLoader command = (GetOtherPersonalHomePageLoader) cmd;
        boolean updated = false;

        if(result != null){
            for (PersonalHomePageVO vo : result) {
                String key=command.getUserId();
                PersonalHomePageBean bean=cache.getEntity(key);
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(key, bean);
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
        GetOtherPersonalHomePageLoader cmd = (GetOtherPersonalHomePageLoader)command;
        PersonalHomePageVO vo=null;
        String userId=cmd.getUserId();
        vo = RestUtils.getRestService(ITradingResource.class).getOtherHomeFromTDay(userId);
        if (vo!=null){
            ArrayList<PersonalHomePageVO> result=new ArrayList<PersonalHomePageVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}
