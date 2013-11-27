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
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.common.IEntityLoader;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;

/**
 * @author neillin
 *
 */
public class TRankItemLoader implements IEntityLoader<String, MegagameRankBean, MegagameRankVO>, ICommandHandler<List<MegagameRankVO>> {

	private static class GetTRankItemsCommand implements ICommand<List<MegagameRankVO>> {

		@Override
		public String getCommandName() {
			return "GetTRankItems";
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class<List<MegagameRankVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
		}
		
	}
	
	private ICommandExecutionContext cmdCtx;
	
	@Override
	public ICommand<List<MegagameRankVO>> createCommand(
			Map<String, Object> params) {
		return new GetTRankItemsCommand();
	}

	@Override
	public boolean handleCommandResult(List<MegagameRankVO> result,
			IReloadableEntityCache<String, MegagameRankBean> cache) {
		boolean updated = false;
		int rankNo = 1;
		for (MegagameRankVO vo : result) {
			String id = String.valueOf(vo.getAcctID());
			MegagameRankBean bean = cache.getEntity(id);
			if(bean == null) {
				bean = ConverterUtils.fromVO(vo);
				bean.setRankSeq(rankNo++);
				cache.putEntity(id, bean);
				updated = true;
			}
		}
		return updated;
	}

	@Override
	public void registerCommandHandler(ICommandExecutor executor) {
		executor.registerCommandHandler("GetTRankItems", this);
	}

	@Override
	public void unregisterCommandHandler(ICommandExecutor executor) {
		executor.unregisterCommandHandler("GetTRankItems", this);
	}

	@Override
	public List<MegagameRankVO> execute(ICommand<List<MegagameRankVO>> command)
			throws Exception {
		List<MegagameRankVO> volist = this.cmdCtx.getKernelContext().getService(
		IRestProxyService.class).getRestService(
		TradingResourse.class).getTPlusMegagameRank();
		return volist;
	}

	@Override
	public void init(ICommandExecutionContext ctx) {
		this.cmdCtx = ctx;
	}

	@Override
	public void destroy() {
		this.cmdCtx = null;
	}

}
