package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;

public class TradingAccountInfoLoader extends AbstractEntityLoader<String, TradingAccountBean,TradingAccountVO>{
    private final static String COMMAND_NAME = "GetTradingAccountInfo";

    @NetworkConstraint
    private static class GetTradingAccountInfoCommand implements ICommand<List<TradingAccountVO>> {
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
        public Class<List<TradingAccountVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
        }
        
    }
    @Override
    public ICommand<List<TradingAccountVO>> createCommand(Map<String, Object> params) {
        GetTradingAccountInfoCommand cmd = new GetTradingAccountInfoCommand();
        cmd.setAcctID((String)params.get("acctID"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(List<TradingAccountVO> result, IReloadableEntityCache<String, TradingAccountBean> cache) {
        boolean updated = false;
        for (TradingAccountVO vo : result) {
            String accId = String.valueOf(vo.getId());
            TradingAccountBean bean = (TradingAccountBean)cache.getEntity(vo.getId());
            if(bean == null ) {
                bean = ConverterUtils.fromVO(vo);
                cache.putEntity(accId, bean);
                updated = true;
            }else{
                bean.setId(vo.getId());
                bean.setApplyFee(vo.getApplyFee());
                bean.setAvalibleFee(vo.getAvalibleFee());
                bean.setBuyDay(vo.getBuyDay());
                bean.setFrozenVol(vo.getFrozenVol());
                bean.setGainRate(vo.getGainRate());
                bean.setLossLimit(vo.getLossLimit());
                bean.setMaxStockCode(vo.getMaxStockCode());
                bean.setMaxStockMarket(vo.getMaxStockMarket());
                bean.setOver(vo.getOver());
                bean.setSellDay(vo.getSellDay());
                bean.setStatus(vo.getStatus());
                bean.setTotalGain(vo.getTotalGain());
                bean.setType(vo.getType());
                bean.setUsedFee(vo.getUsedFee());
                bean.setVirtual(vo.isVirtual());
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
    protected List<TradingAccountVO> executeCommand(ICommand<List<TradingAccountVO>> command) throws Exception {
        GetTradingAccountInfoCommand cmd = (GetTradingAccountInfoCommand)command;
        TradingAccountVO vo= getRestService(ITradingResource.class).getAccount(cmd.getAcctID());
        if (vo!=null){
            List<TradingAccountVO> vos=new ArrayList<TradingAccountVO>();
            vos.add(vo);
            return vos;
        }
         return null;
    } 

}
