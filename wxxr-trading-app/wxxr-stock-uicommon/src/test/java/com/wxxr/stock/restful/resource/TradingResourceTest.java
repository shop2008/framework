package com.wxxr.stock.restful.resource;

import java.security.KeyStore;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;
import com.wxxr.stock.trading.ejb.api.ClosedSummaryVO;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;


public class TradingResourceTest extends TestCase{
	
	ITradingResource tradingResource;

	@Override
	protected void setUp() throws Exception {
		AbstractHttpRpcService service = new AbstractHttpRpcService();
		service.setEnablegzip(false);
		MockApplication app = new MockApplication(){
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
		context.registerService(ISiteSecurityService.class, new ISiteSecurityService() {
			
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
		tradingResource=builder.getRestService(ITradingResource.class,"http://192.168.123.44:8480/mobilestock2");
	}

	@Override
	protected void tearDown() throws Exception {

		tradingResource=null;
	}

	
//  public TradingAccountVO getAccount(String acctID) throws Exception;
    public void testGetAccount()throws Exception{
    	TradingAccountVO a = tradingResource
				.getAccount("2661");
    	System.out.println(a);
	}

//  public List<TradingRecordVO> getTradingAccountRecord( String acctID, int start, int limit) throws Exception ;
    public void testGetTradingAccountRecord()throws Exception{
    	List<TradingRecordVO> a = tradingResource
				.getTradingAccountRecord("100",0,10);
	}
//	public List<GainVO> getGain( int start, int limit) throws Exception;
    public void testGetGain()throws Exception{
    	List<GainVO> a = tradingResource
				.getGain(0,10);
	}
	
	
	
//	public ClosedSumInfoVO getClosedTradingSum( String tradingAccountId)throws Exception;
    public void testGetClosedTradingSum()throws Exception{
    	ClosedSummaryVO a = tradingResource
				.getClosedTradingSum("1000");
	}

//	public DealDetailVO getDealDetail(String acctID)throws Exception;
    public void testGetDealDetail()throws Exception{
    	DealDetailInfoVO a = tradingResource
				.getDealDetail("1000");
	}

//  public AuditDetailVO getAuditDetail(String acctID)throws Exception;
    public void testGetAuditDetail()throws Exception{
    	AuditInfoVO a = tradingResource
				.getAuditDetail("1000");
	}

//	public List<GainVO> getTotalGain( int start, int limit) throws Exception;
    public void testGetTotalGain()throws Exception{
    	List<GainVO> a = tradingResource
				.getTotalGain(0,10);
	}

//	public List<HomePageVO> getHomeImage() throws Exception;
    public void testGetHomeImage()throws Exception{
    	List<HomePageVO> a = tradingResource
				.getHomeImage();
	}

//	public List<HomePageVO> getHomeList( int start, int limit) throws Exception;
    public void testGetHomeList()throws Exception{
    	List<HomePageVO> a = tradingResource
				.getHomeList(1,10);
	}

//	public String[] getFilterStocks()throws Exception;
    public void testGetFilterStocks()throws Exception{
    	String[]  a = tradingResource
				.getFilterStocks();
	}

//	public List<MegagameRankVO> getTMegagameRank()throws Exception;
    public void testGetTMegagameRank()throws Exception{
    	 List<MegagameRankVO> a = tradingResource
				.getTMegagameRank();
	}

//	public List<MegagameRankVO> getTPlusMegagameRank() throws Exception;
	public void testGetTPlusMegagameRank()throws Exception{
   	 List<MegagameRankVO> a = tradingResource
				.getTPlusMegagameRank();
	}

//	public List<RegularTicketVO> getRegularTicketRank()throws Exception;
	public void testGetRegularTicketRank()throws Exception{
	   	 List<RegularTicketVO> a = tradingResource
					.getRegularTicketRank();
	}

//	public List<WeekRankVO> getWeekRank()throws Exception;
	public void testGetWeekRank()throws Exception{
	   	 List<WeekRankVO> a = tradingResource
					.getWeekRank();
	}

//	public PersonalHomePageVO getOtherHomeFromWeek( String userId)throws Exception;
	public void testGetOtherHomeFromWeek()throws Exception{
		PersonalHomePageVO a = tradingResource
					.getOtherHomeFromWeek("13500001009");
	}

//	public PersonalHomePageVO getOtherHomeFromTDay( String userId)throws Exception;
	public void testGetOtherHomeFromTDay()throws Exception{
		PersonalHomePageVO a = tradingResource
					.getOtherHomeFromTDay("13500001009");
	}

//	public PersonalHomePageVO getOtherHomeTPlusDay( String userId)throws Exception;
	public void testGetOtherHomeTPlusDay()throws Exception{
		PersonalHomePageVO a = tradingResource
					.getOtherHomeTPlusDay("13500001009");
	}
	
//	public List<GainVO> getMoreOtherPersonal( String userId, int start, int limit, boolean virtual)throws Exception;
	public void testGetMoreOtherPersonal()throws Exception{
		List<GainVO> a = tradingResource
					.getMoreOtherPersonal("13500001009",0,10,true);
	}
}
