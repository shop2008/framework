package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.TradingConfigBean;
import com.wxxr.mobile.stock.app.command.GetTradingConfigInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.TradingConfigListVO;
import com.wxxr.stock.trading.ejb.api.TradingConfigVO;

public class TradingConfigInfoLoader extends AbstractEntityLoader<String, TradingConfigBean, TradingConfigVO, GetTradingConfigInfoCommand>  {
    @Override
    public GetTradingConfigInfoCommand createCommand(Map<String, Object> params) {
        
        return new GetTradingConfigInfoCommand();
    }

    @Override
    public boolean handleCommandResult(GetTradingConfigInfoCommand cmd,List<TradingConfigVO> result, IReloadableEntityCache<String, TradingConfigBean> cache) {
        boolean updated = false;
        for (TradingConfigVO vo : result) {
        	TradingConfigBean bean = cache.getEntity(vo.getVoIdentity());
            if(bean == null ) {
                bean = ConverterUtils.fromVO(vo);
                cache.putEntity(vo.getVoIdentity(), bean);
            }else{
                ConverterUtils.setfromVO(bean,vo);
            }
            updated = true;
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return GetTradingConfigInfoCommand.COMMAND_NAME;
    }

	@Override
	protected void executeCommand(GetTradingConfigInfoCommand command,
			IAsyncCallback<List<TradingConfigVO>> callback) {
		getRestService(ITradingResourceAsync.class,ITradingResource.class).
			getStrategyConfig().onResult(new DelegateCallback<TradingConfigListVO, List<TradingConfigVO>>(callback) {

				@Override
				protected List<TradingConfigVO> getTargetValue(
						TradingConfigListVO vo) {
			        if (vo!=null){
			            return vo.getTradingConfig();
			        }
			        return null;
				}
			});;
		
	}
}
