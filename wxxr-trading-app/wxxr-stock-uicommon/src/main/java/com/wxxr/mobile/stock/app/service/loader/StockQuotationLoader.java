package com.wxxr.mobile.stock.app.service.loader;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.command.GetStockQuotationCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.db.OptionStock;
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;
/**
 * 实时行情
 * @author zhengjincheng
 *
 */
public class StockQuotationLoader extends AbstractEntityLoader<String, StockQuotationBean, StockQuotationVO, GetStockQuotationCommand> {
    @Override
    public GetStockQuotationCommand createCommand(Map<String, Object> params) {
        if (params==null){
            return null;
        }
        GetStockQuotationCommand cmd = new GetStockQuotationCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetStockQuotationCommand cmd,List<StockQuotationVO> result, IReloadableEntityCache<String, StockQuotationBean> cache) {
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
        return GetStockQuotationCommand.Name;
    }

    @Override
    protected void executeCommand(GetStockQuotationCommand command, IAsyncCallback<List<StockQuotationVO>> callback) {
        GetStockQuotationCommand cmd = (GetStockQuotationCommand)command;
        getRestService(StockResourceAsync.class, StockResource.class).getQuotation(cmd.getMarket(),cmd.getCode()).
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

