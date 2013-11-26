/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.common.IEntityLoader;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.HomePageVO;

/**
 * @author neillin
 *
 */
@SuppressWarnings("rawtypes")
public class EarnRankItemLoader implements IEntityLoader<String,EarnRankItemBean,List>, ICommandHandler<List> {
	
	private static class GetEarnRankItemsCommand implements ICommand<List> {
		
		private int start, limit;

		@Override
		public String getCommandName() {
			return "GetEarnRankItems";
		}

		@Override
		public Class<List> getResultType() {
			return List.class;
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

	private ICommandExecutionContext cmdCtx;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public List execute(ICommand<List> command) throws Exception {
		GetEarnRankItemsCommand cmd = (GetEarnRankItemsCommand)command;
		return cmdCtx.getKernelContext().getService(
				IRestProxyService.class).getRestService(
				TradingResourse.class).getHomeList(cmd.getStart(), cmd.getLimit());
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext ctx) {
		this.cmdCtx = ctx;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#destroy()
	 */
	@Override
	public void destroy() {
		this.cmdCtx = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List> createCommand(Map<String, Object> params) {
		GetEarnRankItemsCommand cmd = new GetEarnRankItemsCommand();
		cmd.setStart((Integer)params.get("start"));
		cmd.setLimit((Integer)params.get("limit"));
		return cmd;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.lang.Object, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean handleCommandResult(List result,
			IReloadableEntityCache<String, EarnRankItemBean> cache) {
		List<HomePageVO> volist = (List<HomePageVO>)result;
		for (HomePageVO vo : volist) {
			String accId = vo.getAccID();
			
			EarnRankItemBean bean = (EarnRankItemBean)cache.getEntity(accId);
			if(bean == null) {
				bean = new EarnRankItemBean();
				bean.setAcctId(accId);
				cache.putEntity(accId, bean);
			}
			bean.setAcctId(vo.getAccID());
			bean.setImgUrl(vo.getUrl());
			bean.setTitle(vo.getWordage());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#registerCommandHandler(com.wxxr.mobile.core.command.api.ICommandExecutor)
	 */
	@Override
	public void registerCommandHandler(ICommandExecutor executor) {
		executor.registerCommandHandler("GetEarnRankItems", this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#unregisterCommandHandler(com.wxxr.mobile.core.command.api.ICommandExecutor)
	 */
	@Override
	public void unregisterCommandHandler(ICommandExecutor executor) {
		executor.unregisterCommandHandler("GetEarnRankItems", this);
	}

}
