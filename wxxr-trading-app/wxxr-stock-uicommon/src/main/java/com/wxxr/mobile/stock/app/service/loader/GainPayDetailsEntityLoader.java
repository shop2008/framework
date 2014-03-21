package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;

public class GainPayDetailsEntityLoader  extends AbstractEntityLoader<String, GainPayDetailBean, GainPayDetailsVO, GainPayDetailCommand> {
    @Override
    public GainPayDetailCommand createCommand(Map<String, Object> params) {
    	GainPayDetailCommand cmd = new GainPayDetailCommand();
    	if(params == null) {
    		//params.put(key, value)
    		cmd.setLimit(20);
            cmd.setStart(0);
    	} else {
	        cmd.setLimit((Integer) params.get("limit"));
	        cmd.setStart((Integer) params.get("start"));
        }
        return cmd;
    }



    @Override
    protected String getCommandName() {
        return GainPayDetailCommand.COMMAND_NAME;
    }
	@Override
	public boolean handleCommandResult(GainPayDetailCommand cmd,List<GainPayDetailsVO> result,IReloadableEntityCache<String, GainPayDetailBean> cache) {
		  boolean updated = false;
	      if(result != null){
	            for (GainPayDetailsVO vo : result) {
	                String key= String.valueOf(vo.getTime());
	                GainPayDetailBean bean=cache.getEntity(key);
	                if(bean == null) {
	                    bean =ConverterUtils.fromVO(vo);
	                    cache.putEntity(key, bean);
	                }else{
	                    ConverterUtils.updatefromVOtoBean(bean, vo);
	                }
	                updated = true;
	            }
	        }
	        return updated;
	}
	
	@Override
	protected void executeCommand(GainPayDetailCommand command,IAsyncCallback<List<GainPayDetailsVO>> callback) {
		GainPayDetailCommand cmd = (GainPayDetailCommand) command;
	    getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getVoucherDetails(cmd.getStart(), cmd.getLimit()).
	    onResult(new DelegateCallback<VoucherDetailsVO, List<GainPayDetailsVO>>(callback) {

			@Override
			protected List<GainPayDetailsVO> getTargetValue(
					VoucherDetailsVO vo) {
				return vo != null ? vo.getList() : null;
			}
		});
	}
}


