package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.command.GetAuditDetailVOsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;

public class AuditDetailLoader extends AbstractEntityLoader<String, AuditDetailBean, AuditInfoVO, GetAuditDetailVOsCommand> {
    
    
    @Override
    public GetAuditDetailVOsCommand createCommand(Map<String, Object> params) {
        if((params == null)||params.isEmpty()){
            return null;
        }
        GetAuditDetailVOsCommand cmd = new GetAuditDetailVOsCommand();
        cmd.setAcctID((String)params.get("acctID"));
        
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetAuditDetailVOsCommand command,List<AuditInfoVO> result,
            IReloadableEntityCache<String, AuditDetailBean> cache) {
        boolean updated = false;
       
        if(result != null){
            for (AuditInfoVO vo : result) {
                AuditDetailBean bean=cache.getEntity(vo.getId());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(vo.getId(), bean);
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
        return GetAuditDetailVOsCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetAuditDetailVOsCommand cmd,final IAsyncCallback<List<AuditInfoVO>> callback) {
       getRestService(ITradingResourceAsync.class,ITradingResource.class).getAuditDetail(cmd.getAcctID()).onResult(
    		   
    		   
    	 new DelegateCallback<AuditInfoVO,List<AuditInfoVO>>(callback) {
			
			@Override
			protected List<AuditInfoVO> getTargetValue(AuditInfoVO result) {
				if(result != null){
					ArrayList<AuditInfoVO> list=new ArrayList<AuditInfoVO>();
					list.add(result);
					return list;
				}else{
					return null;
				}
			}
			
		});
    }

 }



