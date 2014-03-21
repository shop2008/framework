package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.command.GetStockMinuteCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;

public class StockMinuteKLoader extends AbstractEntityLoader<String, StockMinuteKBean, StockMinuteKBean, GetStockMinuteCommand> {
    @Override
    public GetStockMinuteCommand createCommand(Map<String, Object> params) {
        GetStockMinuteCommand cmd = new GetStockMinuteCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetStockMinuteCommand cmd,List<StockMinuteKBean> result, IReloadableEntityCache<String, StockMinuteKBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockMinuteKBean vo : result) {
                StockMinuteKBean bean=cache.getEntity(vo.getMarket()+vo.getCode());
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
        return GetStockMinuteCommand.Name;
    }

    @Override
    protected void executeCommand(GetStockMinuteCommand command, IAsyncCallback<List<StockMinuteKBean>> callback) {
        final GetStockMinuteCommand cmd = (GetStockMinuteCommand)command;
        ParamVO p=new ParamVO();
        p.setMarket(cmd.getMarket());
        p.setCode(cmd.getCode());
        getRestService(StockResourceAsync.class, StockResource.class).getMinuteline(p).
        onResult(new DelegateCallback<StockMinuteKVO, List<StockMinuteKBean>>(callback) {

			@Override
			protected List<StockMinuteKBean> getTargetValue(
					StockMinuteKVO vo) {
		        if (vo!=null ){
		            StockMinuteKBean bean =ConverterUtils.fromVO(vo);
		            bean.setMarket(cmd.getMarket());
		            bean.setCode(cmd.getCode());
		            List<StockMinuteKBean> result= new ArrayList<StockMinuteKBean>();
		            result.add(bean);
		            return result;
		        }
		        return null;
			}
		});
    }
}

