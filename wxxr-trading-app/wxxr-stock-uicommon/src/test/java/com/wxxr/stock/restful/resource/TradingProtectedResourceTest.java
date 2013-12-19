package com.wxxr.stock.restful.resource;

import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVOs;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.HomePageVOs;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVOs;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

public class TradingProtectedResourceTest extends TestCase {
	ITradingProtectedResource tradingProtectedResource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}

	protected void init() {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
		MockApplication app = new MockApplication() {
			ExecutorService executor = Executors.newFixedThreadPool(3);

			@Override
			public ExecutorService getExecutorService() {
				return executor;
			}

			@Override
			protected void initModules() {

			}

		};
		IKernelContext context = app.getContext();
		context.registerService(IUserAuthManager.class, new IUserAuthManager() {
			@Override
			public IUserAuthCredential getAuthCredential(String host,
					String realm) {
				return new IUserAuthCredential() {

					@Override
					public String getUserName() {
						return "13500001009";
					}

					@Override
					public String getAuthPassword() {
						return "404662";
					}

				};
			}
		});
		service.startup(context);
		context.registerService(ISiteSecurityService.class,
				new ISiteSecurityService() {

					@Override
					public KeyStore getTrustKeyStore() {
						return null;
					}

					@Override
					public KeyStore getSiteKeyStore() {
						return null;
					}

					@Override
					public HostnameVerifier getHostnameVerifier() {
						return null;
					}
				});

		MockRestClient builder = new MockRestClient();
		builder.init(context);
		tradingProtectedResource = builder.getRestService(
				ITradingProtectedResource.class,
				"http://192.168.123.44:8480/mobilestock2");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tradingProtectedResource = null;
	}

	// public StockResultVO buyStock( String acctID, String market, String code,
	// long price, long amount) throws Exception;
	public void testBuyStock() throws Exception {
		StockResultVO a = tradingProtectedResource.buyStock("100", "SH",
				"600000", 1000, 1000);
		System.out.println(a);
	}

	// public StockResultVO sellStock( String acctID, String market, String
	// code, long price, long amount) throws Exception;
	public void testSellStock() throws Exception {
		StockResultVO a = tradingProtectedResource.sellStock("100", "SH",
				"600000", 1000, 1000);
		System.out.println(a);
	}

	// public StockResultVO cancelOrder( String orderID) throws Exception;
	public void testCancelOrder() throws Exception {
		StockResultVO a = tradingProtectedResource.cancelOrder("100");
		System.out.println(a);
	}

	// public List<GainVO> getGain( int start, int limit) throws Exception;
	public void testGetGain() throws Exception {
		GainVOs a = tradingProtectedResource.getGain(0, 10);
		System.out.println(a);
	}

	// public List<TradingAccInfoVO> getTradingAccountList() throws Exception;
	public void testGetTradingAccountList() throws Exception {
		TradingAccInfoVOs a = tradingProtectedResource
				.getTradingAccountList();
		System.out.println(a);
	}

	// public UserCreateTradAccInfoVO getCreateStrategyInfo()throws Exception;
	public void testGetCreateStrategyInfo() throws Exception {
		UserCreateTradAccInfoVO a = tradingProtectedResource
				.getCreateStrategyInfo();
		System.out.println(a);
	}

	// public StockResultVO createTradingAccount(Long captitalAmount, float
	// capitalRate, boolean virtual,float depositRate)throws Exception;
	public void testCreateTradingAccount() throws Exception {
		StockResultVO a = tradingProtectedResource.createTradingAccount(
				100000L, 0.05F, true, 0.08F);
		System.out.println(a);
	}

	// public StockResultVO mulCreateTradingAccount(Long captitalAmount, float
	// capitalRate, boolean virtual,float depositRate,String assetType)throws
	// Exception;
	public void testMulCreateTradingAccount() throws Exception {
		StockResultVO a = tradingProtectedResource.mulCreateTradingAccount(
				100000L, 0.05F, true, 0.08F, "CASH");
		System.out.println(a);
	}

	// public StockResultVO quickBuy( Long captitalAmount, float capitalRate,
	// boolean virtual, String stockMarket, String stockCode, long
	// stockBuyAmount,float depositRate)throws Exception;
	public void testQuickBuy() throws Exception {
		StockResultVO a = tradingProtectedResource.quickBuy(100000L, 0.05F,
				true, "SH", "600000", 10000l, 0.08F);
		System.out.println(a);
	}

	// public StockResultVO mulQuickBuy( Long captitalAmount, float capitalRate,
	// boolean virtual, String stockMarket, String stockCode, long
	// stockBuyAmount,float depositRate,String assetType) throws Exception;
	public void testMulQuickBuy() throws Exception {
		StockResultVO a = tradingProtectedResource.mulQuickBuy(100000L, 0.05F,
				true, "SH", "600000", 10000l, 0.08F, "CASH");
		System.out.println(a);
	}

	// public StockResultVO clearTradingAccount(String acctID)throws Exception;
	public void testClearTradingAccount() throws Exception {
		StockResultVO a = tradingProtectedResource
				.clearTradingAccount("600000");
		System.out.println(a);
	}

	// public List<GainVO> getTotalGain( int start, int limit) throws Exception;
	public void testGetTotalGain() throws Exception {
		GainVOs a = tradingProtectedResource.getTotalGain(0, 10);
		System.out.println(a);
	}

	// public List<HomePageVO> getHomeImage() throws Exception;
	public void testGetHomeImage() throws Exception {
		HomePageVOs a = tradingProtectedResource.getHomeImage();
		System.out.println(a);
	}

	// public List<HomePageVO> getHomeList( int start, int limit) throws
	// Exception;
	public void testGetHomeList() throws Exception {
		HomePageVOs a = tradingProtectedResource.getHomeList(0, 10);
		System.out.println(a);
	}

	// public StockResultVO drawMoney( long count);
	public void testDrawMoney() throws Exception {
		StockResultVO a = tradingProtectedResource.drawMoney(100);
		System.out.println(a);
	}

	// public UserAssetVO getAcctUsable()throws Exception;
	public void testGetAcctUsable() throws Exception {
		UserAssetVO a = tradingProtectedResource.getAcctUsable();
		System.out.println(a);
	}

	// public List<GainPayDetailsVO> getGPDetails( int start, int limit)throws
	// Exception;
	public void testGetGPDetails() throws Exception {
		GainPayDetailsVOs a = tradingProtectedResource.getGPDetails(0, 10);
		System.out.println(a);
	}

	// public PersonalHomePageVO getSelfHomePage()throws Exception;
	public void testGetSelfHomePage() throws Exception {
		PersonalHomePageVO a = tradingProtectedResource.getSelfHomePage();
		System.out.println(a);
	}

	// public List<GainVO> getMorePersonalRecords( int start, int limit, boolean
	// virtual) throws Exception ;
	public void testGetMorePersonalRecords() throws Exception {
		GainVOs a = tradingProtectedResource.getMorePersonalRecords(0, 10,
				true);
		System.out.println(a);
	}

//	public VoucherDetailsVO getVoucherDetails(@QueryParam("start") int start,@QueryParam("limit") int limit)throws Throwable;
	public void testGetVoucherDetails() throws Throwable {
		VoucherDetailsVO a = tradingProtectedResource.getVoucherDetails(1, 10);
		System.out.println(a);
	}
}
