package com.wxxr.mobile.stock.trade.entityloader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.command.GetTradingAccInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVOs;

public class TradingAccInfoLoader extends AbstractEntityLoader<String,TradingAccInfoBean,TradingAccInfoVO, GetTradingAccInfoCommand>{
    
    @Override
    public GetTradingAccInfoCommand createCommand(Map<String, Object> params) {
        return new GetTradingAccInfoCommand();
    }

    @Override
    public boolean handleCommandResult(GetTradingAccInfoCommand cmd,List<TradingAccInfoVO> result, IReloadableEntityCache<String, TradingAccInfoBean> cache) {
        boolean updated = false;
        for (TradingAccInfoVO vo : result) {
            String accId = String.valueOf(vo.getAcctID());
            
            TradingAccInfoBean bean = (TradingAccInfoBean)cache.getEntity(accId);
            if(bean == null) {
                bean =  ConverterUtils.fromVO(vo);
                cache.putEntity(accId, bean);
                updated = true;
            }else{
               ConverterUtils.updatefromVO(vo,bean);
               updated = true;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return GetTradingAccInfoCommand.Name;
    }

    @Override
    protected void executeCommand(GetTradingAccInfoCommand command,IAsyncCallback<List<TradingAccInfoVO>> callback) {
    	getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getTradingAccountList().onResult(new DelegateCallback<TradingAccInfoVOs, List<TradingAccInfoVO>>(callback) {

			@Override
			protected List<TradingAccInfoVO> getTargetValue(TradingAccInfoVOs vos) {
		        return vos==null?null:vos.getTradingAccInfos();
			}
		});
    }
   

}
