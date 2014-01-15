package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import android.R.integer;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVOs;

public class GainPayDetailLoader  extends AbstractEntityLoader<String, GainPayDetailBean, GainPayDetailsVO> {
    private static final String COMMAND_NAME = "GetGainPayDetailCommand";
    private static class GetGainPayDetailCommand implements ICommand<List<GainPayDetailsVO>> {
        private int start, limit;
        public int getStart() {
            return start;
        }
        public void setStart(int start) {
            this.start = start;
        }
        public int getLimit() {
            return limit;
        }
        public void setLimit(int limit) {
            this.limit = limit;
        }
        @Override
        public String getCommandName() {
            return COMMAND_NAME;
        }
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Class<List<GainPayDetailsVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }
        @Override
        public void validate() {
          
        }
    }
    @Override
    public ICommand<List<GainPayDetailsVO>> createCommand(Map<String, Object> params) {
        GetGainPayDetailCommand cmd = new GetGainPayDetailCommand();
        
        if(params == null) {
        	cmd.setLimit(20);
        	cmd.setStart(0);
        } else {
        	cmd.setLimit((Integer) params.get("limit"));
        	cmd.setStart((Integer) params.get("start"));
        }
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<GainPayDetailsVO> result,
            IReloadableEntityCache<String, GainPayDetailBean> cache) {
        GetGainPayDetailCommand command = (GetGainPayDetailCommand) cmd;
        boolean updated = false;

        if(result != null){
        		int i=((GetGainPayDetailCommand)cmd).getStart();
            for (GainPayDetailsVO vo : result) {
                String key=i+"";
                GainPayDetailBean bean=cache.getEntity(key);
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(key, bean);
                }else{
                    ConverterUtils.updatefromVOtoBean(bean, vo);
                }
                updated = true;
                i++;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    protected List<GainPayDetailsVO> executeCommand(
            ICommand<List<GainPayDetailsVO>> command) throws Exception {
        GetGainPayDetailCommand cmd = (GetGainPayDetailCommand) command;
        
        		
        	GainPayDetailsVOs gvos=	RestUtils.getRestService(ITradingProtectedResource.class).getGPDetails(cmd.getStart(), cmd.getLimit());
        	List<GainPayDetailsVO> result=  gvos==null?null:gvos.getGainPayDetailss();
        return result;
    }

}

