package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

@NetworkConstraint(allowConnectionTypes={})
public class CreateTradingAccountCommand  implements ICommand<StockResultVO>{
    public final static String Name="CreateTradingAccountCommand";
    private Long captitalAmount;
    private float capitalRate;
    private boolean virtual;
    private float depositRate;
    private String assetType;
    
    

    public CreateTradingAccountCommand(Long captitalAmount, float capitalRate, boolean virtual, float depositRate,String assetType) {
        super();
        this.captitalAmount = captitalAmount;
        this.capitalRate = capitalRate;
        this.virtual = virtual;
        this.depositRate = depositRate;
        this.assetType = assetType;

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

    public Long getCaptitalAmount() {
        return captitalAmount;
    }

    public void setCaptitalAmount(Long captitalAmount) {
        this.captitalAmount = captitalAmount;
    }

    public float getCapitalRate() {
        return capitalRate;
    }

    public void setCapitalRate(float capitalRate) {
        this.capitalRate = capitalRate;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public float getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(float depositRate) {
        this.depositRate = depositRate;
    }
    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

}