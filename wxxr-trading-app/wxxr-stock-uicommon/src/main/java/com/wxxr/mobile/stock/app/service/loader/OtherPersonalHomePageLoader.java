package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

public class OtherPersonalHomePageLoader  extends AbstractEntityLoader<String, PersonalHomePageBean, PersonalHomePageVO, GetOtherPersonalHomePageLoader> {
    static final String COMMAND_NAME = "GetOtherPersonalHomePageLoaderCommand";
    
    @Override
    public GetOtherPersonalHomePageLoader createCommand(Map<String, Object> params) {
        GetOtherPersonalHomePageLoader cmd = new GetOtherPersonalHomePageLoader();
        cmd.setUserId((String) params.get("userId"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetOtherPersonalHomePageLoader cmd,List<PersonalHomePageVO> result,
            IReloadableEntityCache<String, PersonalHomePageBean> cache) {
        GetOtherPersonalHomePageLoader command = (GetOtherPersonalHomePageLoader) cmd;
        boolean updated = false;

        if(result != null){
            for (PersonalHomePageVO vo : result) {
                String key=command.getUserId();
                PersonalHomePageBean bean=cache.getEntity(key);
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
        return COMMAND_NAME;
    }

    @Override
    protected void executeCommand(
    		GetOtherPersonalHomePageLoader command, IAsyncCallback<List<PersonalHomePageVO>> callback) {
    	GetOtherPersonalHomePageLoader cmd = (GetOtherPersonalHomePageLoader)command;
    	String userId=cmd.getUserId();
    	getRestService(ITradingResourceAsync.class,ITradingResource.class).getOtherHomeFromTDay(userId).
    	onResult(new DelegateCallback<PersonalHomePageVO, List<PersonalHomePageVO>>(callback) {

    		@Override
    		protected List<PersonalHomePageVO> getTargetValue(
    				PersonalHomePageVO vo) {
    			if (vo!=null){
    				ArrayList<PersonalHomePageVO> result=new ArrayList<PersonalHomePageVO>();
    				result.add(vo);
    				return result;
    			}
    			return null;
    		}
    	});
    }

}

