package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVos;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVOs;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.GuideResultVO;
import com.wxxr.stock.trading.ejb.api.HomePageVOs;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.SecurityAppHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVOs;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

public interface ITradingProtectedResourceAsync{
	
    public Async<StockResultVO> buyStock( String acctID, String market, String code, long price, long amount);
    
    public Async<StockResultVO> sellStock( String acctID, String market, String code, long price, long amount);
    
    public Async<StockResultVO> cancelOrder( String orderID);
	
    public Async<GainVOs> getGain( int start, int limit);
	
    public Async<TradingAccInfoVOs> getTradingAccountList();
	
    public Async<UserCreateTradAccInfoVO> getCreateStrategyInfo();
	
    public Async<StockResultVO> createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual,float depositRate);
	
    public Async<StockResultVO> mulCreateTradingAccount(Long captitalAmount, float capitalRate, boolean virtual,float depositRate,String assetType);
	
    public Async<StockResultVO> quickBuy( Long captitalAmount, float capitalRate, boolean virtual, String stockMarket, String stockCode, long stockBuyAmount,float depositRate);
	
    public Async<StockResultVO> mulQuickBuy( Long captitalAmount, float capitalRate, boolean virtual, String stockMarket, String stockCode, long stockBuyAmount,float depositRate,String assetType);
	
    public Async<StockResultVO> clearTradingAccount(String acctID);
	
    public Async<GainVOs> getTotalGain( int start, int limit);
	
	public Async<HomePageVOs> getHomeImage();
	
	public Async<HomePageVOs> getHomeList( int start, int limit);
	
	public Async<StockResultVO> drawMoney( long count);
	
	public Async<UserAssetVO> getAcctUsable();
	
	public Async<GainPayDetailsVOs> getGPDetails( int start, int limit);
	
	public Async<PersonalHomePageVO> getSelfHomePage();
	
	public Async<GainVOs> getMorePersonalRecords( int start, int limit, boolean virtual);
	
	public Async<VoucherDetailsVO> getVoucherDetails( int start, int limit);

	public Async<DrawMoneyRecordVos> drawMoneyRecords(int start, int limit);
	
	public Async<StockResultVO> newCreateTradingAccount(Long captitalAmount,float capitalRate,boolean virtual,float depositRate,String assetType,String tradingType);

	public Async<UserSignVO> userSign();

	public Async<UserSignVO> getUserSignMessage();

	public Async<StockResultVO> guideGain();
	
	public Async<GuideResultVO> checkGuideGainAllow();

	public Async<SecurityAppHomePageVO> securityAppHome();

}
