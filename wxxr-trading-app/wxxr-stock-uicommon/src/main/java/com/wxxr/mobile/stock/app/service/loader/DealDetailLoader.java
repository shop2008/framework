package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;

public class DealDetailLoader extends AbstractEntityLoader<String, DealDetailBean, DealDetailInfoVO> {

    private static final String COMMAND_NAME = "GetDealDetailCommand";
    @NetworkConstraint
    private static class GetDealDetailVOsCommand implements ICommand<List<DealDetailInfoVO>> {
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
        public Class<List<DealDetailInfoVO>> getResultType() {
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
    public ICommand<List<DealDetailInfoVO>> createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetDealDetailVOsCommand cmd = new GetDealDetailVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<DealDetailInfoVO> result,
            IReloadableEntityCache<String, DealDetailBean> cache) {
        boolean updated = false;
        if (cmd instanceof GetDealDetailVOsCommand) {
    	   if(result != null){
               for (DealDetailInfoVO vo : result) {
                   String key= String.valueOf(vo.getId());
                   DealDetailBean bean=cache.getEntity(key);
                   if(bean == null) {
                       bean =ConverterUtils.fromVO(vo);
                       cache.putEntity(key, bean);
                   }else{
                       ConverterUtils.updatefromVOtoBean(bean, vo);
                   }
                   updated = true;
               }
    	   }	
        }
        
        return updated;
    }

    @Override
    protected String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    protected List<DealDetailInfoVO> executeCommand(
            ICommand<List<DealDetailInfoVO>> command) throws Exception {
        GetDealDetailVOsCommand cmd = (GetDealDetailVOsCommand)command;
        DealDetailInfoVO vo = RestUtils.getRestService(ITradingResource.class).getDealDetail(cmd.getAcctID());
        if (vo!=null){
            ArrayList<DealDetailInfoVO> result=new ArrayList<DealDetailInfoVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}
