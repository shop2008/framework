package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.command.GetTradingAccountInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;

public class TradingAccountInfoLoader extends AbstractEntityLoader<String, TradingAccountBean,TradingAccountVO, GetTradingAccountInfoCommand>{

    @Override
    public GetTradingAccountInfoCommand createCommand(Map<String, Object> params) {
    	if(params == null || params.get("acctID") == null)
    		return null;
        GetTradingAccountInfoCommand cmd = new GetTradingAccountInfoCommand();
        cmd.setAcctID((String)params.get("acctID"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetTradingAccountInfoCommand cmd,List<TradingAccountVO> result, IReloadableEntityCache<String, TradingAccountBean> cache) {
        boolean updated = false;
        for (TradingAccountVO vo : result) {
            String accId = String.valueOf(vo.getId());
            TradingAccountBean bean = (TradingAccountBean)cache.getEntity(vo.getId());
            if(bean == null ) {
                bean = ConverterUtils.fromVO(vo);
                cache.putEntity(accId, bean);
                updated = true;
            }else{
                ConverterUtils.UpdatefromVO(bean, vo);
                updated = true;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return GetTradingAccountInfoCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetTradingAccountInfoCommand command, IAsyncCallback<List<TradingAccountVO>> callback) {
        GetTradingAccountInfoCommand cmd = (GetTradingAccountInfoCommand)command;
        getRestService(ITradingResourceAsync.class, ITradingResource.class).getAccount(cmd.getAcctID()).
        onResult(new DelegateCallback<TradingAccountVO, List<TradingAccountVO>>(callback) {

			@Override
			protected List<TradingAccountVO> getTargetValue(
					TradingAccountVO vo) {
		        if (vo!=null){
		            List<TradingAccountVO> vos=new ArrayList<TradingAccountVO>();
		            vos.add(vo);
		            return vos;
		        }
		         return null;
			}
		});
    } 

}

