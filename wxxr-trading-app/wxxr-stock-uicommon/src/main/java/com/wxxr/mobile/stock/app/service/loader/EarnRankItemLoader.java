/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.command.GetEarnRankItemsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.Utils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.HomePageVOs;

/**
 * @author neillin
 *
 */
@SuppressWarnings("rawtypes")
public class EarnRankItemLoader extends AbstractEntityLoader<String,EarnRankItemBean,HomePageVO,GetEarnRankItemsCommand>{


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetEarnRankItemsCommand createCommand(Map<String, Object> params) {
		GetEarnRankItemsCommand cmd = new GetEarnRankItemsCommand();
		cmd.setStart((Integer)params.get("start"));
		cmd.setLimit((Integer)params.get("limit"));
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.lang.Object, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetEarnRankItemsCommand cmd,List<HomePageVO> result,
			IReloadableEntityCache<String, EarnRankItemBean> cache) {
		boolean updated = false;
		for (HomePageVO vo : result) {
			String accId = vo.getAccID();
			
			EarnRankItemBean bean = (EarnRankItemBean)cache.getEntity(accId);
			if(bean == null) {
				bean = new EarnRankItemBean();
				bean.setAcctId(accId);
				cache.putEntity(accId, bean);
				updated = true;
			}
			bean.setImgUrl(Utils.getAbsoluteURL(vo.getUrl()));
			bean.setTitle(vo.getWordage());
		}
		return updated;
	}

	@Override
	protected String getCommandName() {
		return GetEarnRankItemsCommand.COMMAND_NAME;
	}

	@Override
	protected void executeCommand(GetEarnRankItemsCommand command,
			IAsyncCallback<List<HomePageVO>> callback) {
		GetEarnRankItemsCommand cmd = (GetEarnRankItemsCommand)command;
		getRestService(ITradingResourceAsync.class,ITradingResource.class).getHomeList(cmd.getStart(), cmd.getLimit()).
			onResult(new DelegateCallback<HomePageVOs, List<HomePageVO>>(callback) {

				@Override
				protected List<HomePageVO> getTargetValue(HomePageVOs vos) {
					 return vos==null? null: vos.getHomePages();
				}
		});
	}

}


