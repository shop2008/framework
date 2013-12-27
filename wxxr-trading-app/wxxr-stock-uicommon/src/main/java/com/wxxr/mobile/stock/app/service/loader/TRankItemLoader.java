/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVOs;

/**
 * @author neillin
 *
 */
public class TRankItemLoader extends AbstractEntityLoader<String, MegagameRankBean, MegagameRankVO> {

	private static final String COMMAND_NAME = "GetTRankItems";
	
	@NetworkConstraint
	private static class GetTRankItemsCommand implements ICommand<List<MegagameRankVO>> {

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
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
	
	@Override
	public ICommand<List<MegagameRankVO>> createCommand(
			Map<String, Object> params) {
		return new GetTRankItemsCommand();
	}

	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<MegagameRankVO> result,
			IReloadableEntityCache<String, MegagameRankBean> cache) {
		boolean updated = false;
		int rankNo = 1;
		if (result!=null&&result.size()>0) {
		   cache.clear();
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
        }
		return updated;
	}


	@Override
	protected List<MegagameRankVO> executeCommand(ICommand<List<MegagameRankVO>> command)
			throws Exception {
		
		MegagameRankVOs vos=getRestService(ITradingResource.class).getTMegagameRank();
		return vos==null?null:vos.getMegagameRanks();
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}



}
