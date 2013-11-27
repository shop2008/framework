/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;

/**
 * @author wangyan
 *
 */
public class RightGainLoader extends AbstractEntityLoader<Long, GainBean, GainVO> {

	private static final String COMMAND_NAME = "GetRightGain";

	
	private static class GetRightGainCommand implements ICommand<List<GainVO>>{

		private int start,limit;
		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
		 */
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
		 */
		@Override
		public Class getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			
		}

		/**
		 * @return the start
		 */
		public int getStart() {
			return start;
		}

		/**
		 * @param start the start to set
		 */
		public void setStart(int start) {
			this.start = start;
		}

		/**
		 * @return the limit
		 */
		public int getLimit() {
			return limit;
		}

		/**
		 * @param limit the limit to set
		 */
		public void setLimit(int limit) {
			this.limit = limit;
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<GainVO>> createCommand(Map<String, Object> params) {
		GetRightGainCommand command=new GetRightGainCommand();
		command.setStart((Integer)params.get("start"));
		command.setLimit((Integer)params.get("limit"));
		return command;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(List<GainVO> result,
			IReloadableEntityCache<Long, GainBean> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
			for (GainVO vo : result) {
				Long accId = vo.getTradingAccountId();
				
				GainBean bean = cache.getEntity(accId);
				if(bean == null) {
					bean = new GainBean();
					ConverterUtils.fromVO(vo);
					cache.putEntity(accId, bean);
				}
				ConverterUtils.fromVO(vo, bean);
				updated = true;
			}
		}
		return updated;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<GainVO> executeCommand(ICommand<List<GainVO>> command)
			throws Exception {
		GetRightGainCommand cmd=(GetRightGainCommand)command;
		return getRestService(TradingResourse.class).getGain(cmd.getStart(), cmd.getLimit());
	}

}
