package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.command.GetGainPayDetailCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVOs;

public class GainPayDetailLoader  extends AbstractEntityLoader<String, GainPayDetailBean, GainPayDetailsVO, GetGainPayDetailCommand> {
    @Override
    public GetGainPayDetailCommand createCommand(Map<String, Object> params) {
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
    public boolean handleCommandResult(GetGainPayDetailCommand cmd,List<GainPayDetailsVO> result,
            IReloadableEntityCache<String, GainPayDetailBean> cache) {
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
        return GetGainPayDetailCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetGainPayDetailCommand command,IAsyncCallback<List<GainPayDetailsVO>> callback) {
        GetGainPayDetailCommand cmd = (GetGainPayDetailCommand) command;
        getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getGPDetails(cmd.getStart(), cmd.getLimit()).
    	onResult(new DelegateCallback<GainPayDetailsVOs, List<GainPayDetailsVO>>(callback) {

			@Override
			protected List<GainPayDetailsVO> getTargetValue(
					GainPayDetailsVOs gvos) {
				return gvos==null?null:gvos.getGainPayDetailss();
			}
		});
    }

}


