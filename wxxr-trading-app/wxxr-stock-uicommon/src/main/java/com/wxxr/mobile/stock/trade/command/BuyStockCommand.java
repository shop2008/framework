package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

@NetworkConstraint(allowConnectionTypes={})
public class BuyStockCommand implements ICommand<StockResultVO>{
    public final static String Name="BuyStockCommand";
    private String acctID; 
    private String market;
    private String code; 
    private String price; 
    private String amount;
    
    public BuyStockCommand(String acctID, String market, String code, String price, String amount) {
        super();
        this.acctID = acctID;
        this.market = market;
        this.code = code;
        this.price = price;
        this.amount = amount;
    }

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static String getName() {
        return Name;
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
        
    }

    @Override
    public String toString() {
        return "BuyStockCommand [acctID=" + acctID + ", market=" + market + ", code=" + code + ", price=" + price + ", amount=" + amount + "]";
    }
    

}
