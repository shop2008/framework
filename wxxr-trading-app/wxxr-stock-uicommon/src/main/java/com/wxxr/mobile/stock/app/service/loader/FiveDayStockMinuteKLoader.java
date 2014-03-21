package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.command.GetFiveDayStockMinuteCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVOs;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;

public class FiveDayStockMinuteKLoader extends AbstractEntityLoader<String, StockMinuteKBean, StockMinuteKBean,GetFiveDayStockMinuteCommand> {

    @Override
    public GetFiveDayStockMinuteCommand createCommand(Map<String, Object> params) {
        GetFiveDayStockMinuteCommand cmd = new GetFiveDayStockMinuteCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetFiveDayStockMinuteCommand cmd,List<StockMinuteKBean> result, IReloadableEntityCache<String, StockMinuteKBean> cache) {
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
        return GetFiveDayStockMinuteCommand.Name;
    }

    @Override
    protected void executeCommand(GetFiveDayStockMinuteCommand command,IAsyncCallback<List<StockMinuteKBean>> callback) {
        final GetFiveDayStockMinuteCommand cmd = (GetFiveDayStockMinuteCommand)command;
        ParamVO p=new ParamVO();
        p.setMarket(cmd.getMarket());
        p.setCode(cmd.getCode());
        getRestService(StockResourceAsync.class,StockResource.class).getFiveDayMinuteline(p).onResult(new DelegateCallback<StockMinuteKVOs, List<StockMinuteKBean>>(callback) {

			@Override
			protected List<StockMinuteKBean> getTargetValue(
					StockMinuteKVOs vos) {
		        if (vos!=null &&vos.getStockMinuteKVOs()!=null ){
		            List<StockMinuteKBean> result= new ArrayList<StockMinuteKBean>();
		            for (StockMinuteKVO vo:vos.getStockMinuteKVOs()){
		            StockMinuteKBean bean =ConverterUtils.fromVO(vo);
		            bean.setMarket(cmd.getMarket());
		            bean.setCode(cmd.getCode());
		                result.add(bean);
		            }
		            return result;
		        }
		        return null;
			}
		});
    }
}

