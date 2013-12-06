package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.GainVO;

public class GainBeanLoader  extends AbstractEntityLoader<String, GainBean, GainVO> {

    private static final String COMMAND_NAME = "GetGainBeanCommand";
    private  class GetOtherUserGainBeanCommand extends GetGainBeanCommand {
        String userId;
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
    private  class GetGainBeanCommand implements ICommand<List<GainVO>> {
        int start;
        int limit;
        boolean virtual;
        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<GainVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }
        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public boolean isVirtual() {
            return virtual;
        }

        public void setVirtual(boolean virtual) {
            this.virtual = virtual;
        }
        
        @Override
        public void validate() {
        }
        
    }
    
    
    @Override
    public ICommand<List<GainVO>> createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        if (params.get("userId")!=null){
            GetOtherUserGainBeanCommand cmd = new GetOtherUserGainBeanCommand();
            cmd.setUserId((String) params.get("userId"));
            cmd.setStart((Integer) params.get("start"));
            cmd.setLimit((Integer) params.get("limit"));
            cmd.setVirtual((Boolean) params.get("virtual"));
            return cmd;
        }else{
            GetGainBeanCommand cmd = new GetGainBeanCommand();
            cmd.setStart((Integer) params.get("start"));
            cmd.setLimit((Integer) params.get("limit"));
            cmd.setVirtual((Boolean) params.get("virtual"));
            return cmd;
        }
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<GainVO> result,
            IReloadableEntityCache<String, GainBean> cache) {
        GetGainBeanCommand command = (GetGainBeanCommand) cmd;
        boolean updated = false;
       
        if(result != null){
            for (GainVO vo : result) {
                String key= String.valueOf(vo.getTradingAccountId());
                GainBean bean=cache.getEntity(key);
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    if (cmd instanceof GetOtherUserGainBeanCommand){
                        bean.setUserId(((GetOtherUserGainBeanCommand) cmd).getUserId());
                    }
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
    protected List<GainVO> executeCommand(
            ICommand<List<GainVO>> command) throws Exception {
        List<GainVO> vos=null;
        if ( command instanceof GetOtherUserGainBeanCommand){
            GetOtherUserGainBeanCommand cmd =(GetOtherUserGainBeanCommand) command;
             vos = RestUtils.getRestService(ITradingResource.class).getMoreOtherPersonal(cmd.getUserId(), cmd.getStart(), cmd.getLimit(), cmd.isVirtual());
        }else if (command instanceof GetGainBeanCommand){
            GetGainBeanCommand cmd =(GetGainBeanCommand) command;
            vos = RestUtils.getRestService(ITradingProtectedResource.class).getMorePersonalRecords(cmd.getStart(), cmd.getLimit(), cmd.isVirtual());
        }
        return vos;
    }

}
