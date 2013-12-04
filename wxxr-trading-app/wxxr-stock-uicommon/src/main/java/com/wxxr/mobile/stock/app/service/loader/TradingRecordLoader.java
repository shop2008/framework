package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.json.StockTaxisListVO;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;

public class TradingRecordLoader extends AbstractEntityLoader<String, TradingRecordBean, TradingRecordVO> {

    private static final String COMMAND_NAME = "GetTradingRecordCommand";
    
    private static class GetTradingRecordVOsCommand implements ICommand<List<TradingRecordVO>> {

        private String acctID;
        private int start;
        private int limit;

        public String getAcctID() {
            return acctID;
        }

        public void setAcctID(String acctID) {
            this.acctID = acctID;
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

        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<TradingRecordVO>> getResultType() {
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
    public ICommand<List<TradingRecordVO>> createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetTradingRecordVOsCommand cmd = new GetTradingRecordVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        cmd.setLimit((Integer) params.get("limit"));
        cmd.setStart((Integer) params.get("start"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<TradingRecordVO> result,
            IReloadableEntityCache<String, TradingRecordBean> cache) {
        GetTradingRecordVOsCommand command = (GetTradingRecordVOsCommand) cmd;
        boolean updated = false;
       
        if(result != null){
            for (TradingRecordVO vo : result) {
                String key =vo.getMarket()+vo.getCode()+vo.getDate();
                TradingRecordBean b=ConverterUtils.fromVO(vo);
                b.setAcctID(command.getAcctID());
                cache.putEntity(key,b);
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
    protected List<TradingRecordVO> executeCommand(
            ICommand<List<TradingRecordVO>> command) throws Exception {
        GetTradingRecordVOsCommand cmd = (GetTradingRecordVOsCommand)command;
        List<TradingRecordVO> result = RestUtils.getRestService(ITradingResource.class).getTradingAccountRecord(cmd.getAcctID(), cmd.getStart(), cmd.getLimit());
        return result;
    }

}
