package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;

public class AuditDetailLoader extends AbstractEntityLoader<String, AuditDetailBean, AuditInfoVO> {

    private static final String COMMAND_NAME = "GetAuditDetailVOsCommand";
    
    private static class GetAuditDetailVOsCommand implements ICommand<List<AuditInfoVO>> {
        private String acctID;


        public String getAcctID() {
            return acctID;
        }
        public void setAcctID(String acctID) {
            this.acctID = acctID;
        }


        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<AuditInfoVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
            if(this.acctID == null){
                throw new IllegalArgumentException();
            }
        }
        
    }
    
    
    @Override
    public ICommand<List<AuditInfoVO>> createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetAuditDetailVOsCommand cmd = new GetAuditDetailVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<AuditInfoVO> result,
            IReloadableEntityCache<String, AuditDetailBean> cache) {
        GetAuditDetailVOsCommand command = (GetAuditDetailVOsCommand) cmd;
        boolean updated = false;
       
        if(result != null){
            for (AuditInfoVO vo : result) {
                AuditDetailBean bean=cache.getEntity(vo.getId());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(vo.getId(), bean);
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
    protected List<AuditInfoVO> executeCommand(
            ICommand<List<AuditInfoVO>> command) throws Exception {
        GetAuditDetailVOsCommand cmd = (GetAuditDetailVOsCommand)command;
        AuditInfoVO vo = RestUtils.getRestService(ITradingResource.class).getAuditDetail(cmd.getAcctID());
        if (vo!=null){
            ArrayList<AuditInfoVO> result=new ArrayList<AuditInfoVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}

