package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.command.GetVoucherDetailsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;

public class VoucherDetailsLoader  extends AbstractEntityLoader<String, VoucherDetailsBean, VoucherDetailsVO, GetVoucherDetailsCommand> {
     @Override
    public GetVoucherDetailsCommand createCommand(Map<String, Object> params) {
        GetVoucherDetailsCommand cmd = new GetVoucherDetailsCommand();
        cmd.setLimit((Integer) params.get("limit"));
        cmd.setStart((Integer) params.get("start"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetVoucherDetailsCommand cmd,List<VoucherDetailsVO> result,
            IReloadableEntityCache<String, VoucherDetailsBean> cache) {
        GetVoucherDetailsCommand command = (GetVoucherDetailsCommand) cmd;
        boolean updated = false;

        if(result != null){
            for (VoucherDetailsVO vo : result) {
                String key="key";
                VoucherDetailsBean bean=cache.getEntity(key);
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
        return GetVoucherDetailsCommand.COMMAND_NAME;
    }

    @Override
    protected void executeCommand(GetVoucherDetailsCommand command, IAsyncCallback<List<VoucherDetailsVO>> callback) {
        GetVoucherDetailsCommand cmd = (GetVoucherDetailsCommand) command;
        getRestService(ITradingProtectedResourceAsync.class, ITradingProtectedResource.class).getVoucherDetails(cmd.getStart(), cmd.getLimit()).
        onResult(new DelegateCallback<VoucherDetailsVO, List<VoucherDetailsVO>>(callback) {

			@Override
			protected List<VoucherDetailsVO> getTargetValue(
					VoucherDetailsVO vo) {
		        if (vo!=null){
		            List<VoucherDetailsVO> result=new ArrayList<VoucherDetailsVO>();
		            result.add(vo);
		            return result;
		        }
		        return null;
			}
		});
    }

}


