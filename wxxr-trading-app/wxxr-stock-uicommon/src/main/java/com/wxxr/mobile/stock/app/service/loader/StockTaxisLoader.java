package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;
import com.wxxr.stock.restful.resource.StockResource;

public class StockTaxisLoader extends AbstractEntityLoader<String, StockTaxisBean, StockTaxisVO> {

    private final static String COMMAND_NAME = "GetStockTaxis";

    @NetworkConstraint
    private static class GetStockTaxisCommand implements ICommand<List<StockTaxisVO>> {
        private String taxis;
        private String orderby;
        private Long start;
        private Long limit; 
        private Long blockId;
        
        public String getTaxis() {
            return taxis;
        }

        public void setTaxis(String taxis) {
            this.taxis = taxis;
        }

        public String getOrderby() {
            return orderby;
        }

        public void setOrderby(String orderby) {
            this.orderby = orderby;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getLimit() {
            return limit;
        }

        public void setLimit(Long limit) {
            this.limit = limit;
        }

        public Long getBlockId() {
            return blockId;
        }

        public void setBlockId(Long blockId) {
            this.blockId = blockId;
        }


        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<StockTaxisVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }

        @Override
        public void validate() {
        }
        
    }
    
    @Override
    public ICommand<List<StockTaxisVO>> createCommand(
            Map<String, Object> params) {
        return new GetStockTaxisCommand();
    }

    @Override
    public boolean handleCommandResult(List<StockTaxisVO> result,
            IReloadableEntityCache<String, StockTaxisBean> cache) {
        boolean updated = false;
        int rankNo = 1;
        for (StockTaxisVO vo : result) {
            String id = String.valueOf(vo.getMarket()+vo.getCode());
            StockTaxisBean bean = cache.getEntity(id);
            if(bean == null) {
                bean = ConverterUtils.fromVO(vo);
                cache.putEntity(id, bean);
                updated = true;
            }
        }
        return updated;
    }


    @Override
    protected List<StockTaxisVO> executeCommand(ICommand<List<StockTaxisVO>> command)
            throws Exception {
         if (command instanceof GetStockTaxisCommand){
             GetStockTaxisCommand g=(GetStockTaxisCommand) command;
             TaxisVO p=new TaxisVO();
             p.setBlockId(g.getBlockId());
             p.setLimit(g.getLimit());
             p.setOrderby(g.getOrderby());
             p.setStart(g.getStart());
             p.setTaxis(g.getTaxis());
             StockTaxisListVO result= getRestService(StockResource.class).getStocktaxis(p);
            return result!=null?result.getList():null;
         }
        return null;
    }

    @Override
    protected String getCommandName() {
        return COMMAND_NAME;
    }


}

