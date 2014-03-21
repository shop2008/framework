package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.db.OptionStock;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;

@NetworkConstraint
public class RefreshOptionStockQuotationCommand implements ICommand<List<StockQuotationVO>> {
	
    public final static String Name = "RefreshOptionStockQuotationCommand";


    private OptionStock[] stocks;

   

    public OptionStock[] getStocks() {
		return stocks;
	}

	public void setStocks(OptionStock[] stocks) {
		this.stocks = stocks;
	}

	@Override
    public String getCommandName() {
        return Name;
    }

    @Override
    public Class<List<StockQuotationVO>>  getResultType() {
        Class clazz=List.class;
        return clazz;
    }

    @Override
    public void validate() {
    }

}