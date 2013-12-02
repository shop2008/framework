package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class CancelOrderCommand implements ICommand<StockResultVO>{
    public final static String Name="CancelOrderCommand";
    private String orderID;

    public CancelOrderCommand(String orderID) {
        super();
        this.orderID = orderID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public String getCommandName() {
        return Name;
    }

    @Override
    public Class<StockResultVO> getResultType() {
        return StockResultVO.class;
    }

    @Override
    public void validate() {
        if(this.orderID == null){
            throw new IllegalArgumentException();
        }
    }

}
