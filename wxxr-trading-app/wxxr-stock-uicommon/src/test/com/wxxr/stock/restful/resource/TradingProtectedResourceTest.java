package com.wxxr.stock.restful.resource;

import java.util.List;

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.stock.article.ejb.api.ArticleVO;
import com.wxxr.stock.restful.json.NewsQueryBO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;


public class TradingProtectedResourceTest extends TestCase{
	private IRestProxyService restService=new ResteasyRestClientService();
	
//  public StockResultVO buyStock( String acctID, String market, String code, long price, long amount) throws Exception;
	public void testBuyStock()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class).buyStock("100","SH","600000",1000,1000);
	}

//  public StockResultVO sellStock( String acctID, String market, String code, long price, long amount) throws Exception;
	public void testSellStock()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class).sellStock("100","SH","600000",1000,1000);
	}
    
//  public StockResultVO cancelOrder( String orderID) throws Exception;
	public void testCancelOrder()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class).cancelOrder("100");
	}

//	public List<GainVO> getGain( int start, int limit) throws Exception;
	public void testGetGain()throws Exception{
		List<GainVO> a = restService.getRestService(ITradingProtectedResource.class).getGain(0,10);
	}

//	public List<TradingAccInfoVO> getTradingAccountList() throws Exception;
	public void testGetTradingAccountList()throws Exception{
		List<TradingAccInfoVO> a = restService.getRestService(ITradingProtectedResource.class).getTradingAccountList();
	}

//	public UserCreateTradAccInfoVO getCreateStrategyInfo()throws Exception;
	public void testGetCreateStrategyInfo()throws Exception{
		UserCreateTradAccInfoVO a = restService.getRestService(ITradingProtectedResource.class).getCreateStrategyInfo();
	}

//	public StockResultVO createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual,float depositRate)throws Exception;
	public void testCreateTradingAccount()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.createTradingAccount(100000L,0.05F,true,0.08F);
	}

//	public StockResultVO mulCreateTradingAccount(Long captitalAmount, float capitalRate, boolean virtual,float depositRate,String assetType)throws Exception;
	public void testMulCreateTradingAccount()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.mulCreateTradingAccount(100000L,0.05F,true,0.08F,"CASH");
	}

//	public StockResultVO quickBuy( Long captitalAmount, float capitalRate, boolean virtual, String stockMarket, String stockCode, long stockBuyAmount,float depositRate)throws Exception;
	public void testQuickBuy()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.quickBuy(100000L,0.05F,true,"SH","600000",10000l,0.08F);
	}

//	public StockResultVO mulQuickBuy( Long captitalAmount, float capitalRate, boolean virtual, String stockMarket, String stockCode, long stockBuyAmount,float depositRate,String assetType) throws Exception;
	public void testMulQuickBuy()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.mulQuickBuy(100000L,0.05F,true,"SH","600000",10000l,0.08F,"CASH");
	}

//	public StockResultVO clearTradingAccount(String acctID)throws Exception;
	public void testClearTradingAccount()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.clearTradingAccount("600000");
	}

//	public List<GainVO> getTotalGain( int start, int limit) throws Exception;
	public void testGetTotalGain()throws Exception{
		List<GainVO> a = restService.getRestService(ITradingProtectedResource.class)
				.getTotalGain(0,10);
	}

//	public List<HomePageVO> getHomeImage() throws Exception;
	public void testGetHomeImage()throws Exception{
		List<HomePageVO> a = restService.getRestService(ITradingProtectedResource.class)
				.getHomeImage();
	}

//	public List<HomePageVO> getHomeList( int start, int limit) throws Exception;
	public void testGetHomeList()throws Exception{
		List<HomePageVO> a = restService.getRestService(ITradingProtectedResource.class)
				.getHomeList(0,10);
	}

//	public StockResultVO drawMoney( long count);
	public void testDrawMoney()throws Exception{
		StockResultVO a = restService.getRestService(ITradingProtectedResource.class)
				.drawMoney(10);
	}

//	public UserAssetVO getAcctUsable()throws Exception;
	public void testGetAcctUsable()throws Exception{
		UserAssetVO a = restService.getRestService(ITradingProtectedResource.class)
				.getAcctUsable();
	}
	
//	public List<GainPayDetailsVO> getGPDetails( int start, int limit)throws Exception;
	public void testGetGPDetails()throws Exception{
		List<GainPayDetailsVO> a = restService.getRestService(ITradingProtectedResource.class)
				.getGPDetails(0,10);
	}

//	public PersonalHomePageVO getSelfHomePage()throws Exception;
	public void testGetSelfHomePage()throws Exception{
		PersonalHomePageVO a = restService.getRestService(ITradingProtectedResource.class)
				.getSelfHomePage();
	}

//	public List<GainVO> getMorePersonalRecords( int start, int limit, boolean virtual) throws Exception ;
	public void testGetMorePersonalRecords()throws Exception{
		List<GainVO> a = restService.getRestService(ITradingProtectedResource.class)
				.getMorePersonalRecords(0,10,true);
	}
}
