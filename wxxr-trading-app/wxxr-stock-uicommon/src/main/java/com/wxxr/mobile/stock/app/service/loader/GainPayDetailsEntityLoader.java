package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;

public class GainPayDetailsEntityLoader  extends AbstractEntityLoader<String, GainPayDetailBean, GainPayDetailsVO> {
    private static final String COMMAND_NAME = "GainPayDetailCommand";
    private static class GainPayDetailCommand implements ICommand<List<GainPayDetailsVO>> {
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
        return COMMAND_NAME;
    }
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<GainPayDetailsVO> result,IReloadableEntityCache<String, GainPayDetailBean> cache) {
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
	protected List<GainPayDetailsVO> executeCommand(ICommand<List<GainPayDetailsVO>> command) throws Exception {
		GainPayDetailCommand cmd = (GainPayDetailCommand) command;
	        VoucherDetailsVO vo=null;
	        try {
	            vo = RestUtils.getRestService(ITradingProtectedResource.class).getVoucherDetails(cmd.getStart(), cmd.getLimit());
	        } catch (Throwable e) {
	            throw new Exception(e.getMessage());
	        }
	        if (vo!=null){
	        	return vo.getList();
	        }
	        return null;
	}
}

