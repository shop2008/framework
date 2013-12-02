/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;
import com.wxxr.stock.restful.resource.StockResource;

/**
 * @author neillin
 *
 */
public class StockTaxisVOLoader extends AbstractEntityLoader<String, StockTaxisVO, StockTaxisVO> {

	private static final String COMMAND_NAME = "GetStockTaxisVOs";
	
	private static class GetStockTaxisVOsCommand implements ICommand<List<StockTaxisVO>> {

		private TaxisVO taxis;
		
		/**
		 * @return the taxis
		 */
		public TaxisVO getTaxis() {
			return taxis;
		}

		/**
		 * @param taxis the taxis to set
		 */
		public void setTaxis(TaxisVO taxis) {
			this.taxis = taxis;
		}

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class<List<StockTaxisVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
			if(this.taxis == null){
				throw new IllegalArgumentException();
			}
		}
		
	}
	
	private TaxisVO previousSearchCriteria;
	
	@Override
	public ICommand<List<StockTaxisVO>> createCommand(Map<String, Object> params) {
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
	public boolean handleCommandResult(ICommand<?> cmd,List<StockTaxisVO> result,
			IReloadableEntityCache<String, StockTaxisVO> cache) {
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
				cache.putEntity(key, vo);
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
	protected List<StockTaxisVO> executeCommand(
			ICommand<List<StockTaxisVO>> command) throws Exception {
		GetStockTaxisVOsCommand cmd = (GetStockTaxisVOsCommand)command;
		StockTaxisListVO result = RestUtils.getRestService(StockResource.class).getStocktaxis(cmd.getTaxis());
		return result.getList();
	}

}
