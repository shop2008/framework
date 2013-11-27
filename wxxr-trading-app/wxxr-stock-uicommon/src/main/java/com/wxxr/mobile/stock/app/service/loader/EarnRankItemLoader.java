/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.HomePageVO;

/**
 * @author neillin
 *
 */
@SuppressWarnings("rawtypes")
public class EarnRankItemLoader extends AbstractEntityLoader<String,EarnRankItemBean,HomePageVO>{

	final static String COMMAND_NAME = "GetEarnRankItems";

	@NetworkConstraint
	private static class GetEarnRankItemsCommand implements ICommand<List<HomePageVO>> {
		
		private int start, limit;

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class<List<HomePageVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
			if((start < 0)||(limit <= 0)){
				throw new IllegalArgumentException("start and limit parameters must large than 0");
			}
		}

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @return the limit
		 */
		public int getLimit() {
			return limit;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @param limit the limit to set
		 */
		public void setLimit(int limit) {
			this.limit = limit;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public List<HomePageVO> execute(ICommand<List<HomePageVO>> command) throws Exception {
		GetEarnRankItemsCommand cmd = (GetEarnRankItemsCommand)command;
		return cmdCtx.getKernelContext().getService(
				IRestProxyService.class).getRestService(
				TradingResourse.class).getHomeList(cmd.getStart(), cmd.getLimit());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<HomePageVO>> createCommand(Map<String, Object> params) {
		GetEarnRankItemsCommand cmd = new GetEarnRankItemsCommand();
		cmd.setStart((Integer)params.get("start"));
		cmd.setLimit((Integer)params.get("limit"));
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.lang.Object, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(List<HomePageVO> result,
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
			bean.setImgUrl(vo.getUrl());
			bean.setTitle(vo.getWordage());
		}
		return updated;
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

}
