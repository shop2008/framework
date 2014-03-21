package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.DrawMoneyRecordBean;
import com.wxxr.mobile.stock.app.command.GetDrawMoneyRecordCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVo;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVos;

public class DrawMoneyRecordLoader  extends AbstractEntityLoader<Long, DrawMoneyRecordBean, DrawMoneyRecordVo, GetDrawMoneyRecordCommand> {

	@Override
    public GetDrawMoneyRecordCommand createCommand(Map<String, Object> params) {
    	GetDrawMoneyRecordCommand cmd = new GetDrawMoneyRecordCommand();
        cmd.setLimit((Integer) params.get("limit"));
        cmd.setStart((Integer) params.get("start"));
        return cmd;
    }

    @Override
    public boolean handleCommandResult(GetDrawMoneyRecordCommand command,List<DrawMoneyRecordVo> result,
            IReloadableEntityCache<Long, DrawMoneyRecordBean> cache) {
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
        return GetDrawMoneyRecordCommand.COMMAND_NAME;
    }

	@Override
	protected void executeCommand(GetDrawMoneyRecordCommand cmd,
			IAsyncCallback<List<DrawMoneyRecordVo>> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).
			drawMoneyRecords(cmd.getStart(), cmd.getLimit()).onResult(new DelegateCallback<DrawMoneyRecordVos,List<DrawMoneyRecordVo>>(callback) {

				@Override
				protected List<DrawMoneyRecordVo> getTargetValue(
						DrawMoneyRecordVos vo) {
			        if (vo!=null){
			            return vo.getDrawMoneyRecordVos();
			        }
			        return null;
				}
			});;
	}

}

