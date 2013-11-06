package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.TradeRecordEntity;


/**
 * 个人主页-挑战交易盘每个条目布局
 * @author renwenjie
 */
@View(name="upage_item_view")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_page_item_layout")
public abstract class UPageItemView extends ViewBase implements IModelUpdater{

	/**
	 * 股票名称
	 */
	@Field(valueKey="text")
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text")
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text")
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text")
	String trade_date;
	
	
	DataField<String> stock_nameField;
	DataField<String> stock_codeField;
	DataField<String> challenge_amountField;
	DataField<String> profit_loss_amountField;
	DataField<String> trade_dateField;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof TradeRecordEntity) {
			TradeRecordEntity entity = (TradeRecordEntity) value;
			setStockName(entity.getStockName());
			setStockCode(entity.getStockCode());
			setChallengeAmount(entity.getTradeAmount());
			setProfitLoss(entity.getTradeProfit());
			setTradeDate(entity.getTradeDate());
		}
	}
	
	protected void setStockName(String stockName) {
		this.stock_name = stockName;
		this.stock_nameField.setValue(stockName);
	}
	
	protected void setStockCode(String stockCode) {
		this.stock_code = stockCode;
		this.stock_codeField.setValue(stockCode);
	}
	
	protected void setChallengeAmount(String challengeAmount) {
		this.challenge_amount = challengeAmount;
		this.challenge_amountField.setValue(challengeAmount);
	}
	
	protected void setProfitLoss(String profitLoss) {
		this.profit_loss_amount = profitLoss;
		this.profit_loss_amountField.setValue(profitLoss);
	}
	
	protected void setTradeDate(String tradeDate) {
		this.trade_date = tradeDate;
		this.trade_dateField.setValue(tradeDate);
	}
}
