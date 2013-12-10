package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;

public class VoucherDetailsLoader  extends AbstractEntityLoader<String, VoucherDetailsBean, VoucherDetailsVO> {
    private static final String COMMAND_NAME = "GetVoucherDetailsCommand";
    private static class GetVoucherDetailsCommand implements ICommand<List<VoucherDetailsVO>> {
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
        public Class<List<VoucherDetailsVO>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }
        @Override
        public void validate() {
          
        }
    }
    @Override
    public ICommand<List<VoucherDetailsVO>> createCommand(Map<String, Object> params) {
        GetVoucherDetailsCommand cmd = new GetVoucherDetailsCommand();
        cmd.setLimit((Integer) params.get("limit"));
        cmd.setLimit((Integer) params.get("start"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<VoucherDetailsVO> result,
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
        return COMMAND_NAME;
    }

    @Override
    protected List<VoucherDetailsVO> executeCommand(
            ICommand<List<VoucherDetailsVO>> command) throws Exception {
        GetVoucherDetailsCommand cmd = (GetVoucherDetailsCommand) command;
        VoucherDetailsVO vo=null;
        try {
            vo = RestUtils.getRestService(ITradingProtectedResource.class).getVoucherDetails(cmd.getStart(), cmd.getLimit());
        } catch (Throwable e) {
            throw new Exception(e.getMessage());
        }
        if (vo!=null){
            List<VoucherDetailsVO> result=new ArrayList<VoucherDetailsVO>();
            result.add(vo);
            return result;
        }
        return null;
    }

}

