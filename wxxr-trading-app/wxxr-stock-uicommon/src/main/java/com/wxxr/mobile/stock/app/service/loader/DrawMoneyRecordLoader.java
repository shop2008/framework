package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.DrawMoneyRecordBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVo;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVos;

public class DrawMoneyRecordLoader  extends AbstractEntityLoader<Long, DrawMoneyRecordBean, DrawMoneyRecordVo> {
    private static final String COMMAND_NAME = "GetDrawMoneyRecordCommand";
    private static class GetDrawMoneyRecordCommand implements ICommand<List<DrawMoneyRecordVo>> {
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
        public Class<List<DrawMoneyRecordVo>> getResultType() {
            Class clazz = List.class;
            return clazz;
        }
        @Override
        public void validate() {
          
        }
    }
    @Override
    public ICommand<List<DrawMoneyRecordVo>> createCommand(Map<String, Object> params) {
    	GetDrawMoneyRecordCommand cmd = new GetDrawMoneyRecordCommand();
        cmd.setLimit((Integer) params.get("limit"));
        cmd.setStart((Integer) params.get("start"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(ICommand<?> cmd,List<DrawMoneyRecordVo> result,
            IReloadableEntityCache<Long, DrawMoneyRecordBean> cache) {
    	GetDrawMoneyRecordCommand command = (GetDrawMoneyRecordCommand) cmd;
        boolean updated = false;

        if(result != null){
            for (DrawMoneyRecordVo vo : result) {
            	DrawMoneyRecordBean bean=cache.getEntity(vo.getId());
                if(bean == null) {
                    bean =ConverterUtils.fromVO(vo);
                    cache.putEntity(vo.getId(), bean);
                }else{
                    ConverterUtils.updatefromVO(bean, vo);
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
    protected List<DrawMoneyRecordVo> executeCommand(
            ICommand<List<DrawMoneyRecordVo>> command) throws Exception {
    	GetDrawMoneyRecordCommand cmd = (GetDrawMoneyRecordCommand) command;
    	DrawMoneyRecordVos vo=null;
        try {
            vo = RestUtils.getRestService(ITradingProtectedResource.class).drawMoneyRecords(cmd.getStart(), cmd.getLimit());
        } catch (Throwable e) {
            throw new Exception(e.getMessage());
        }
        if (vo!=null){
            return vo.getDrawMoneyRecordVos();
        }
        return null;
    }

}

