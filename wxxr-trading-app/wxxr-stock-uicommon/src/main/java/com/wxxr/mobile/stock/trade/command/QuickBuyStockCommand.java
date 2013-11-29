package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
@NetworkConstraint(allowConnectionTypes={})
public class QuickBuyStockCommand implements ICommand<StockResultVO>{
    public final static String Name="QuickBuyStockCommand";
    private Long captitalAmount;
    private String capitalRate;
    private boolean virtual;
    private String stockMarket;
    private String stockCode;
    private String stockBuyAmount;
    private String depositRate;

    public QuickBuyStockCommand(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate) {
        super();
        this.captitalAmount = captitalAmount;
        this.capitalRate = capitalRate;
        this.virtual = virtual;
        this.stockMarket = stockMarket;
        this.stockCode = stockCode;
        this.stockBuyAmount = stockBuyAmount;
        this.depositRate = depositRate;
    }

    public Long getCaptitalAmount() {
        return captitalAmount;
    }

    public void setCaptitalAmount(Long captitalAmount) {
        this.captitalAmount = captitalAmount;
    }

    public String getCapitalRate() {
        return capitalRate;
    }

    public void setCapitalRate(String capitalRate) {
        this.capitalRate = capitalRate;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public String getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(String stockMarket) {
        this.stockMarket = stockMarket;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockBuyAmount() {
        return stockBuyAmount;
    }

    public void setStockBuyAmount(String stockBuyAmount) {
        this.stockBuyAmount = stockBuyAmount;
    }

    public String getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(String depositRate) {
        this.depositRate = depositRate;
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

   
   
    
}
