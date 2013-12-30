package com.wxxr.mobile.stock.trade.entityloader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVOs;

public class TradingAccInfoLoader extends AbstractEntityLoader<String,TradingAccInfoBean,TradingAccInfoVO>{
    
    @NetworkConstraint(allowConnectionTypes={})
    @SecurityConstraint(allowRoles={})
    public class GetTradingAccInfoCommand implements ICommand<List<TradingAccInfoVO>> {
        public final static String Name="GetTradingAccInfoCommand";
        @Override
        public String getCommandName() {
            return Name;
        }

        @Override
        public Class getResultType() {
            return List.class;
        }

        @Override
        public void validate() {
            
        }

    }
    @Override
    public ICommand<List<TradingAccInfoVO>> createCommand(Map<String, Object> params) {
        return new GetTradingAccInfoCommand();
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<TradingAccInfoVO> result, IReloadableEntityCache<String, TradingAccInfoBean> cache) {
        boolean updated = false;
        for (TradingAccInfoVO vo : result) {
            String accId = String.valueOf(vo.getAcctID());
            
            TradingAccInfoBean bean = (TradingAccInfoBean)cache.getEntity(accId);
            if(bean == null) {
                bean =  ConverterUtils.fromVO(vo);
                cache.putEntity(accId, bean);
                updated = true;
            }else{
               ConverterUtils.updatefromVO(vo,bean);
               updated = true;
            }
        }
        return updated;
    }

    @Override
    protected String getCommandName() {
        return GetTradingAccInfoCommand.Name;
    }

    @Override
    protected List<TradingAccInfoVO> executeCommand(ICommand<List<TradingAccInfoVO>> command) throws Exception {
    	TradingAccInfoVOs vos = null;
    	if ( this.cmdCtx.getKernelContext().getAttribute("currentUser")!=null) {//用户登录后方可获取列表信息
    		 vos=getRestService(ITradingProtectedResource.class).getTradingAccountList();
		}
        return vos==null?null:vos.getTradingAccInfos();
    }
   

}
