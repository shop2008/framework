/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.command.GetStockTaxisVOsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;
import com.wxxr.stock.restful.resource.StockResource;
import com.wxxr.stock.restful.resource.StockResourceAsync;

/**
 * @author neillin
 *
 */
public class StockTaxisLoader extends AbstractEntityLoader<String, StockTaxisBean, StockTaxisVO, GetStockTaxisVOsCommand> {

	
	private TaxisVO previousSearchCriteria;
	
	@Override
	public GetStockTaxisVOsCommand createCommand(Map<String, Object> params) {
		if((params == null)||params.isEmpty()){
			return null;
		}
		GetStockTaxisVOsCommand cmd = new GetStockTaxisVOsCommand();
		TaxisVO vo = new TaxisVO();
		if(params.containsKey("start")){
			vo.setStart((Long)params.get("start"));
		}
		if(params.containsKey("limit")){
			vo.setLimit((Long)params.get("limit"));
		}
		if(params.containsKey("taxis")){
			vo.setTaxis((String)params.get("taxis"));
		}
		if(params.containsKey("orderby")){
			vo.setOrderby((String)params.get("orderby"));
		}
		if(params.containsKey("blockId")){
			vo.setBlockId((Long)params.get("blockId"));
		}
		cmd.setTaxis(vo);
		return cmd;
	}

	@Override
	public boolean handleCommandResult(GetStockTaxisVOsCommand cmd,List<StockTaxisVO> result,
			IReloadableEntityCache<String, StockTaxisBean> cache) {
		TaxisVO criteria = ((GetStockTaxisVOsCommand)cmd).getTaxis();
		boolean updated = false;
		if(!criteria.hasSameSearchConditions(previousSearchCriteria)){
			cache.clear();
			previousSearchCriteria = criteria;
			updated = true;
		}
		if(result != null){
			for (StockTaxisVO vo : result) {
				String key = vo.getCode()+vo.getMarket();
				cache.putEntity(key, ConverterUtils.fromVO(vo));
				updated = true;
			}
		}
		return updated;
	}

	@Override
	protected String getCommandName() {
		return GetStockTaxisVOsCommand.COMMAND_NAME;
	}

	@Override
	protected void executeCommand(GetStockTaxisVOsCommand command, IAsyncCallback<List<StockTaxisVO>> callback) {
		GetStockTaxisVOsCommand cmd = (GetStockTaxisVOsCommand)command;
		getRestService(StockResourceAsync.class, StockResource.class).getStocktaxis(cmd.getTaxis()).
		onResult(new DelegateCallback<StockTaxisListVO, List<StockTaxisVO>>(callback) {

			@Override
			protected List<StockTaxisVO> getTargetValue(StockTaxisListVO result) {
				return result != null ? result.getList() : null;
			}
		});
	}

}

