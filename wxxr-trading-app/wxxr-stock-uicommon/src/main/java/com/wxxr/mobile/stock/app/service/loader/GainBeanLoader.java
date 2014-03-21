package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.command.GetGainBeanCommand;
import com.wxxr.mobile.stock.app.command.GetOtherUserGainBeanCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.GainVOs;

public class GainBeanLoader  extends AbstractEntityLoader<String, GainBean, GainVO, GetGainBeanCommand> {

    @Override
    public GetGainBeanCommand createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        if (params.get("userId")!=null){
            GetOtherUserGainBeanCommand cmd = new GetOtherUserGainBeanCommand();
            cmd.setUserId((String) params.get("userId"));
            cmd.setStart((Integer) params.get("start"));
            cmd.setLimit((Integer) params.get("limit"));
            cmd.setVirtual((Boolean) params.get("virtual"));
            return cmd;
        }else{
            GetGainBeanCommand cmd = new GetGainBeanCommand();
            cmd.setStart((Integer) params.get("start"));
            cmd.setLimit((Integer) params.get("limit"));
            cmd.setVirtual((Boolean) params.get("virtual"));
            return cmd;
        }
    }

    @Override
    public boolean handleCommandResult(GetGainBeanCommand cmd,List<GainVO> result,
            IReloadableEntityCache<String, GainBean> cache) {
        boolean updated = false;
       
        if(result != null){
            for (GainVO vo : result) {
                String key= String.valueOf(vo.getTradingAccountId());
                GainBean bean=cache.getEntity(key);
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    if (cmd instanceof GetOtherUserGainBeanCommand){
                        bean.setUserId(((GetOtherUserGainBeanCommand) cmd).getUserId());
                    }else{
                    	bean.setUserId(KUtils.getService(IUserIdentityManager.class).getUserId());
                    }
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
        return GetGainBeanCommand.COMMAND_NAME;
    }

    @Override
    protected void  executeCommand(GetGainBeanCommand command, IAsyncCallback<List<GainVO>> callback) {
    		Async<GainVOs> async=null;
        if ( command instanceof GetOtherUserGainBeanCommand){
            GetOtherUserGainBeanCommand cmd =(GetOtherUserGainBeanCommand) command;
            async = getRestService(ITradingResourceAsync.class,ITradingResource.class).getMoreOtherPersonal(cmd.getUserId(), cmd.getStart(), cmd.getLimit(), cmd.isVirtual());
        }else if (command instanceof GetGainBeanCommand){
            GetGainBeanCommand cmd =(GetGainBeanCommand) command;
            async=getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getMorePersonalRecords(cmd.getStart(), cmd.getLimit(), cmd.isVirtual());           		            		
        }
        async.onResult(new DelegateCallback<GainVOs, List<GainVO>>(callback) {

			@Override
			protected List<GainVO> getTargetValue(GainVOs vos) {
		        return vos==null?null:vos.getGains();
			}
		});
    }

}


