package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;

public class GetStockTaxisVOsCommand implements ICommand<List<StockTaxisVO>> {

	public static final String COMMAND_NAME = "GetStockTaxisVOs";

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