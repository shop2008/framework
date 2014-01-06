package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
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
    	if(getCaptitalAmount()==null || getCaptitalAmount()<=0){
    		throw new StockAppBizException("请输入申购金额！");
    	}
    	UserBean user = KUtils.getService(IUserManagementService.class).getMyUserInfo();
    	if (user==null||StringUtils.isBlank(user.getNickName())) {
    		throw new StockAppBizException("请先设置昵称！");
		}
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