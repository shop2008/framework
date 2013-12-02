package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockLineVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;

public class DayStockLineLoader extends AbstractEntityLoader<String, StockLineBean, StockLineBean> {
   public  final static String COMMAND_NAME = "GetDayStockLineCommand";

    public class GetDayStockLineCommand implements ICommand<List<StockLineBean>> {
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
            return COMMAND_NAME;
        }

        @Override
        public Class<List<StockLineBean>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {

        }

    }

    @Override
    public ICommand<List<StockLineBean>> createCommand(Map<String, Object> params) {
        if (params==null){
            return null;
        }
        GetDayStockLineCommand cmd = new GetDayStockLineCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<StockLineBean> result, IReloadableEntityCache<String, StockLineBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockLineBean vo : result) {
                StockLineBean bean=cache.getEntity(vo.getMarket()+vo.getCode());
                if(bean == null) {
                    cache.putEntity(vo.getMarket()+vo.getCode(), vo);
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
    protected List<StockLineBean> executeCommand(ICommand<List<StockLineBean>> command) throws Exception {
        GetDayStockLineCommand cmd = (GetDayStockLineCommand)command;
        ParamVO vo=new  ParamVO();
        vo.setCode(cmd.getCode());
        vo.setMarket(cmd.getMarket());
        List<ParamVO> ps= new ArrayList<ParamVO>();
        ps.add(vo);
        LineListVO svos= getRestService(StockResource.class).getDayline(vo);
        
        if (svos!=null && svos.getList()!=null){
            List<StockLineVO> stockLineVOs=svos.getList();
            for (StockLineVO item:stockLineVOs ){
                StockLineBean bean=ConverterUtils.fromVO(item);
            }
        }
        return null;
    }

}
