package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.command.RefreshOptionStockQuotationCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.OptionStock;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.json.StockParamVo;
import com.wxxr.stock.restful.json.StockParamsVo;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;
/**
 * 实时行情
 * @author zhengjincheng
 *
 */
public class OptionStockQuotationLoader extends AbstractEntityLoader<String, StockQuotationBean, StockQuotationVO, RefreshOptionStockQuotationCommand> {
    @Override
    public RefreshOptionStockQuotationCommand createCommand(Map<String, Object> params) {
        if (params==null){
            return null;
        }
        RefreshOptionStockQuotationCommand cmd = new RefreshOptionStockQuotationCommand();
        Object obj = null;
        if ((obj=params.get("options"))!=null) {
        	cmd.setStocks((OptionStock[])obj);
		}
        return cmd;
    }

    @Override
    public boolean handleCommandResult(RefreshOptionStockQuotationCommand cmd,List<StockQuotationVO> result, IReloadableEntityCache<String, StockQuotationBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockQuotationVO vo : result) {
                StockQuotationBean bean=cache.getEntity(vo.getMarket()+vo.getCode());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(vo.getMarket()+vo.getCode(), bean);
                    
                }else{
                    ConverterUtils.updatefromVOtoBean(bean, vo);
                }
                OptionStock stock =  KUtils.getService(IOptionStockManagementService.class).find(vo.getCode(), vo.getCode());
                if (stock!=null) {
					stock.setNewprice(vo.getNewprice()==null?0:vo.getNewprice());
					stock.setRisefallrate(vo.getRisefallrate()==null?0:vo.getRisefallrate());
					stock.setLastUpdate(new Date());
					KUtils.getService(IOptionStockManagementService.class).update(stock);
				}
                updated = true;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return RefreshOptionStockQuotationCommand.Name;
    }

    @Override
    protected void executeCommand(RefreshOptionStockQuotationCommand command, IAsyncCallback<List<StockQuotationVO>> callback) {
    	RefreshOptionStockQuotationCommand cmd = (RefreshOptionStockQuotationCommand)command;
    	OptionStock[] stocks = cmd.getStocks();
    	if (stocks!=null&&stocks.length>0) {
    		List<StockParamVo> list = new ArrayList<StockParamVo>();
    		for (OptionStock optionStock : stocks) {
    			StockParamVo p = new StockParamVo();
        		p.setMarket(optionStock.getMc());
        		p.setCode(optionStock.getStockCode());
        		list.add(p);
			}
    		StockParamsVo ps = new StockParamsVo();
    		ps.setStocks(list);
    		getRestService(StockResourceAsync.class, StockResource.class).getQuotation(ps).
            onResult(new DelegateCallback<QuotationListVO, List<StockQuotationVO>>(callback) {

    			@Override
    			protected List<StockQuotationVO> getTargetValue(
    					QuotationListVO svos) {
    		        if (svos!=null && svos.getList()!=null){
    		            return svos.getList();
    		        }
    		        return null;
    			}
    		});
		}
        
    }
}

