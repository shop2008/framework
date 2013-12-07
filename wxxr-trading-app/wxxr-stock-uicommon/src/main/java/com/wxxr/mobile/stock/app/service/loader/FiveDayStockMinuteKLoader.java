package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;

public class FiveDayStockMinuteKLoader extends AbstractEntityLoader<String, StockMinuteKBean, StockMinuteKBean> {
    public final static String Name = "GetFiveDayStockMinuteCommand";
    @NetworkConstraint
    public class GetFiveDayStockMinuteCommand implements ICommand<List<StockMinuteKBean>> {
        private String code;
        private String market;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMarket() {
            return market;
        }

        public void setMarket(String market) {
            this.market = market;
        }

        @Override
        public String getCommandName() {
            return Name;
        }

        @Override
        public Class<List<StockMinuteKBean>>  getResultType() {
            Class clazz=List.class;
            return clazz;
        }

        @Override
        public void validate() {}

    }

    @Override
    public ICommand<List<StockMinuteKBean>> createCommand(Map<String, Object> params) {
        GetFiveDayStockMinuteCommand cmd = new GetFiveDayStockMinuteCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<StockMinuteKBean> result, IReloadableEntityCache<String, StockMinuteKBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockMinuteKBean vo : result) {
                String key=vo.getMarket()+vo.getCode()+vo.getDate();
                StockMinuteKBean bean=cache.getEntity(key);
                if(bean == null) {
                    cache.putEntity(key, vo);
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
        return Name;
    }

    @Override
    protected List<StockMinuteKBean> executeCommand(ICommand<List<StockMinuteKBean>> command) throws Exception {
        GetFiveDayStockMinuteCommand cmd = (GetFiveDayStockMinuteCommand)command;
        ParamVO p=new ParamVO();
        p.setMarket(cmd.getMarket());
        p.setCode(cmd.getCode());
        List<StockMinuteKVO> vos= getRestService(StockResource.class).getFiveDayMinuteline(p);
        if (vos!=null && !vos.isEmpty() ){
            List<StockMinuteKBean> result= new ArrayList<StockMinuteKBean>();
            for (StockMinuteKVO vo:vos){
            StockMinuteKBean bean =ConverterUtils.fromVO(vo);
            bean.setMarket(cmd.getMarket());
            bean.setCode(cmd.getCode());
                result.add(bean);
            }
            return result;
        }
        return null;
    }
}

