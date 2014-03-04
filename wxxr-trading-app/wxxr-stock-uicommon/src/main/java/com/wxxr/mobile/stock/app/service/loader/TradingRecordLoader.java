package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.command.GetTradingRecordVOsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVOs;

public class TradingRecordLoader extends AbstractEntityLoader<String, TradingRecordBean, TradingRecordVO, GetTradingRecordVOsCommand> {

     
    @Override
    public GetTradingRecordVOsCommand createCommand(Map<String, Object> params) {
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
    public boolean handleCommandResult(GetTradingRecordVOsCommand cmd,List<TradingRecordVO> result,
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
        return GetTradingRecordVOsCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetTradingRecordVOsCommand command, IAsyncCallback<List<TradingRecordVO>> callback) {
        GetTradingRecordVOsCommand cmd = (GetTradingRecordVOsCommand)command;
        getRestService(ITradingResourceAsync.class, ITradingResource.class).getTradingAccountRecord(cmd.getAcctID(), cmd.getStart(), cmd.getLimit()).
        onResult(new DelegateCallback<TradingRecordVOs, List<TradingRecordVO>>(callback) {

			@Override
			protected List<TradingRecordVO> getTargetValue(
					TradingRecordVOs vos) {
		        return vos==null?null:vos.getTradingRecords();
			}
		});
    }

}


