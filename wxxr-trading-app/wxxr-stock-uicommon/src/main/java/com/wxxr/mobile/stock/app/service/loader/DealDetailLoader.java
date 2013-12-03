package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.DealDetailVO;

public class DealDetailLoader extends AbstractEntityLoader<String, DealDetailBean, DealDetailVO> {

    private static final String COMMAND_NAME = "GetDealDetailCommand";
    
    private static class GetDealDetailVOsCommand implements ICommand<List<DealDetailVO>> {
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
        public Class<List<DealDetailVO>> getResultType() {
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
    public ICommand<List<DealDetailVO>> createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetDealDetailVOsCommand cmd = new GetDealDetailVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<DealDetailVO> result,
            IReloadableEntityCache<String, DealDetailBean> cache) {
        GetDealDetailVOsCommand command = (GetDealDetailVOsCommand) cmd;
        boolean updated = false;
       
        if(result != null){
            for (DealDetailVO vo : result) {
                DealDetailBean bean=cache.getEntity(vo.getId());
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
    protected List<DealDetailVO> executeCommand(
            ICommand<List<DealDetailVO>> command) throws Exception {
        GetDealDetailVOsCommand cmd = (GetDealDetailVOsCommand)command;
        DealDetailVO vo = RestUtils.getRestService(ITradingResource.class).getDealDetail(cmd.getAcctID());
        if (vo!=null){
            ArrayList<DealDetailVO> result=new ArrayList<DealDetailVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}
