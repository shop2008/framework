/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.command.GetT1RankItemsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVOs;

/**
 * @author neillin
 *
 */
public class T1RankItemLoader extends AbstractEntityLoader<String, MegagameRankBean, MegagameRankVO, GetT1RankItemsCommand> {

	
	@Override
	public GetT1RankItemsCommand createCommand(
			Map<String, Object> params) {
		return new GetT1RankItemsCommand();
	}

	@Override
	public boolean handleCommandResult(GetT1RankItemsCommand cmd,List<MegagameRankVO> result,
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
                }else{
                   bean.setRankSeq(rankNo++);
                   bean.setAcctID(vo.getAcctID());
                   bean.setGainRate(vo.getGainRate());
                   bean.setGainRates(vo.getGainRates());
                   bean.setMaxStockCode(vo.getMaxStockCode());
                   bean.setMaxStockMarket(vo.getMaxStockMarket());
                   bean.setNickName(vo.getNickName());
                   bean.setOver(vo.getOver());
                   bean.setStatus(vo.getStatus());
                   bean.setTotalGain(vo.getTotalGain());
                   bean.setUserId(vo.getUesrId());
                }
            }
           
        }
		return updated;
	}


	@Override
	protected void executeCommand(GetT1RankItemsCommand cmd, IAsyncCallback<List<MegagameRankVO>> callback) {
		getRestService(ITradingResourceAsync.class, ITradingResource.class).getTPlusMegagameRank().
		onResult(new DelegateCallback<MegagameRankVOs, List<MegagameRankVO>>(callback) {

			@Override
			protected List<MegagameRankVO> getTargetValue(MegagameRankVOs vos) {
				return vos==null?null:vos.getMegagameRanks();
			}
		});
	}

	@Override
	protected String getCommandName() {
		return GetT1RankItemsCommand.COMMAND_NAME;
	}


}

