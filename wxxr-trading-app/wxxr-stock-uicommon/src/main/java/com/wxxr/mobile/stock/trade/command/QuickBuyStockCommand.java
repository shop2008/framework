package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
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
    private  String assetType;

   

    public QuickBuyStockCommand(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate,String assetType) {
        super();
        this.captitalAmount = captitalAmount;
        this.capitalRate = capitalRate;
        this.virtual = virtual;
        this.stockMarket = stockMarket;
        this.stockCode = stockCode;
        this.stockBuyAmount = stockBuyAmount;
        this.depositRate = depositRate;
        this.assetType = assetType;

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
    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    @Override
    public Class<StockResultVO> getResultType() {
        return StockResultVO.class;
    }

    @Override
    public void validate() {
    	if(getCaptitalAmount()==null){
        	throw new StockAppBizException("申请额度不能为空！");
        }else if(getCaptitalAmount()<=0){
        	throw new StockAppBizException("申请额度不能为0！");
        }else if(getStockBuyAmount()==null || StringUtils.isEmpty(getStockBuyAmount())){
        	throw new StockAppBizException("买入股票数量不能为空");
        }
    }
}
