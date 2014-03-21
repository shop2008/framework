package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.command.GetDayStockLineCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockLineVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;

public class DayStockLineLoader extends AbstractEntityLoader<String, StockLineBean, StockLineBean,GetDayStockLineCommand> {

    @Override
    public GetDayStockLineCommand createCommand(Map<String, Object> params) {
        if (params==null){
            return null;
        }
        GetDayStockLineCommand cmd = new GetDayStockLineCommand();
        cmd.setCode((String) params.get("code"));
        cmd.setMarket((String) params.get("market"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetDayStockLineCommand cmd,List<StockLineBean> result, IReloadableEntityCache<String, StockLineBean> cache) {
        boolean updated = false;
        if(result!=null && !result.isEmpty()){
            for (StockLineBean vo : result) {
                StockLineBean bean=cache.getEntity(vo.getMarket()+vo.getCode()+vo.getDate());
                if(bean == null) {
                    cache.putEntity(vo.getMarket()+vo.getCode()+vo.getDate(), vo);
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
        return GetDayStockLineCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetDayStockLineCommand command,final IAsyncCallback<List<StockLineBean>> callback)  {
        final GetDayStockLineCommand cmd = (GetDayStockLineCommand)command;
        ParamVO vo=new  ParamVO();
        vo.setCode(cmd.getCode());
        vo.setMarket(cmd.getMarket());
        List<ParamVO> ps= new ArrayList<ParamVO>();
        ps.add(vo);
        vo.setStart(0L);
        vo.setLimit(50L);
        getRestService(StockResourceAsync.class,StockResource.class).getDayline(vo).onResult(new DelegateCallback<LineListVO,List<StockLineBean>>(callback) {
			
			@Override
			public List<StockLineBean> getTargetValue(LineListVO svos) {
				List<StockLineBean> result= null;
		        if (svos!=null){
		            result=new ArrayList<StockLineBean>();
		            List<StockLineVO> stockLineVOs=svos.getList();
		            for (StockLineVO item:stockLineVOs ){
		                StockLineBean bean=ConverterUtils.fromVO(item);
		                bean.setCode(cmd.getCode());
		                bean.setMarket(cmd.getMarket());
		                result.add(bean);
		            }
		        }
		        return result;
			}
		});
    }

}
