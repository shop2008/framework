package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.command.GetDealDetailVOsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;

public class DealDetailLoader extends AbstractEntityLoader<String, DealDetailBean, DealDetailInfoVO, GetDealDetailVOsCommand> {

    
    @Override
    public GetDealDetailVOsCommand createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetDealDetailVOsCommand cmd = new GetDealDetailVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetDealDetailVOsCommand command,List<DealDetailInfoVO> result,
            IReloadableEntityCache<String, DealDetailBean> cache) {
        boolean updated = false;
       
        if(result != null){
            for (DealDetailInfoVO vo : result) {
                String key= String.valueOf(vo.getId());
                DealDetailBean bean=cache.getEntity(key);
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
    protected String getCommandName() {
        return GetDealDetailVOsCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetDealDetailVOsCommand command,final IAsyncCallback<List<DealDetailInfoVO>> callback) {
        GetDealDetailVOsCommand cmd = (GetDealDetailVOsCommand)command;
        getRestService(ITradingResourceAsync.class,ITradingResource.class).getDealDetail(cmd.getAcctID()).onResult(new DelegateCallback<DealDetailInfoVO,List<DealDetailInfoVO>>(callback) {
			
			@Override
			public List<DealDetailInfoVO> getTargetValue(DealDetailInfoVO vo) {
	            ArrayList<DealDetailInfoVO> result=null;
		        if (vo!=null){
		        	result = new ArrayList<DealDetailInfoVO>();
		            result.add(vo);
		        }
		       return result;
			}
			
		});
    }

}


