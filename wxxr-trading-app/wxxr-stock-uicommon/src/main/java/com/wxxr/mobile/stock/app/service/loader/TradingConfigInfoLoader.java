package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.TradingConfigBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.TradingConfigListVO;
import com.wxxr.stock.trading.ejb.api.TradingConfigVO;

public class TradingConfigInfoLoader extends AbstractEntityLoader<String, TradingConfigBean, TradingConfigVO>  {
    private static final String COMMAND_NAME = "GetTradingConfigInfoCommand";
    @NetworkConstraint
    private static class GetTradingConfigInfoCommand implements ICommand<List<TradingConfigVO>> {

        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<TradingConfigVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
        }
        
    }
    @Override
    public ICommand<List<TradingConfigVO>> createCommand(Map<String, Object> params) {
        
        return new GetTradingConfigInfoCommand();
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<TradingConfigVO> result, IReloadableEntityCache<String, TradingConfigBean> cache) {
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
        return COMMAND_NAME;
    }

    @Override
    protected List<TradingConfigVO> executeCommand(ICommand<List<TradingConfigVO>> command) throws Exception {
        TradingConfigListVO vo=getRestService(ITradingResource.class).getStrategyConfig();
        if (vo!=null){
            return vo.getTradingConfig();
        }
        return null;
    }
}
