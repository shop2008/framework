package com.wxxr.stock.restful.resource;

import java.util.List;

import junit.framework.TestCase;

import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;
import com.wxxr.stock.trading.ejb.api.AuditDetailVO;
import com.wxxr.stock.trading.ejb.api.ClosedSumInfoVO;
import com.wxxr.stock.trading.ejb.api.DealDetailVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;


public class TradingResourceTest extends TestCase{
	private IRestProxyService restService=new ResteasyRestClientService();
	
//  public TradingAccountVO getAccount(String acctID) throws Exception;
    public void testGetAccount()throws Exception{
    	TradingAccountVO a = restService.getRestService(ITradingResource.class)
				.getAccount("100");
	}

//  public List<TradingRecordVO> getTradingAccountRecord( String acctID, int start, int limit) throws Exception ;
    public void testGetTradingAccountRecord()throws Exception{
    	List<TradingRecordVO> a = restService.getRestService(ITradingResource.class)
				.getTradingAccountRecord("100",0,10);
	}
//	public List<GainVO> getGain( int start, int limit) throws Exception;
    public void testGetGain()throws Exception{
    	List<GainVO> a = restService.getRestService(ITradingResource.class)
				.getGain(0,10);
	}
	
	
	
//	public ClosedSumInfoVO getClosedTradingSum( String tradingAccountId)throws Exception;
    public void testGetClosedTradingSum()throws Exception{
    	ClosedSumInfoVO a = restService.getRestService(ITradingResource.class)
				.getClosedTradingSum("1000");
	}

//	public DealDetailVO getDealDetail(String acctID)throws Exception;
    public void testGetDealDetail()throws Exception{
    	DealDetailVO a = restService.getRestService(ITradingResource.class)
				.getDealDetail("1000");
	}

//  public AuditDetailVO getAuditDetail(String acctID)throws Exception;
    public void testGetAuditDetail()throws Exception{
    	AuditDetailVO a = restService.getRestService(ITradingResource.class)
				.getAuditDetail("1000");
	}

//	public List<GainVO> getTotalGain( int start, int limit) throws Exception;
    public void testGetTotalGain()throws Exception{
    	List<GainVO> a = restService.getRestService(ITradingResource.class)
				.getTotalGain(0,10);
	}

//	public List<HomePageVO> getHomeImage() throws Exception;
    public void testGetHomeImage()throws Exception{
    	List<HomePageVO> a = restService.getRestService(ITradingResource.class)
				.getHomeImage();
	}

//	public List<HomePageVO> getHomeList( int start, int limit) throws Exception;
    public void testGetHomeList()throws Exception{
    	List<HomePageVO> a = restService.getRestService(ITradingResource.class)
				.getHomeList(1,10);
	}

//	public String[] getFilterStocks()throws Exception;
    public void testGetFilterStocks()throws Exception{
    	String[]  a = restService.getRestService(ITradingResource.class)
				.getFilterStocks();
	}

//	public List<MegagameRankVO> getTMegagameRank()throws Exception;
    public void testGetTMegagameRank()throws Exception{
    	 List<MegagameRankVO> a = restService.getRestService(ITradingResource.class)
				.getTMegagameRank();
	}

//	public List<MegagameRankVO> getTPlusMegagameRank() throws Exception;
	public void testGetTPlusMegagameRank()throws Exception{
   	 List<MegagameRankVO> a = restService.getRestService(ITradingResource.class)
				.getTPlusMegagameRank();
	}

//	public List<RegularTicketVO> getRegularTicketRank()throws Exception;
	public void testGetRegularTicketRank()throws Exception{
	   	 List<RegularTicketVO> a = restService.getRestService(ITradingResource.class)
					.getRegularTicketRank();
	}

//	public List<WeekRankVO> getWeekRank()throws Exception;
	public void testGetWeekRank()throws Exception{
	   	 List<WeekRankVO> a = restService.getRestService(ITradingResource.class)
					.getWeekRank();
	}

//	public PersonalHomePageVO getOtherHomeFromWeek( String userId)throws Exception;
	public void testGetOtherHomeFromWeek()throws Exception{
		PersonalHomePageVO a = restService.getRestService(ITradingResource.class)
					.getOtherHomeFromWeek("13500001009");
	}

//	public PersonalHomePageVO getOtherHomeFromTDay( String userId)throws Exception;
	public void testGetOtherHomeFromTDay()throws Exception{
		PersonalHomePageVO a = restService.getRestService(ITradingResource.class)
					.getOtherHomeFromTDay("13500001009");
	}

//	public PersonalHomePageVO getOtherHomeTPlusDay( String userId)throws Exception;
	public void testGetOtherHomeTPlusDay()throws Exception{
		PersonalHomePageVO a = restService.getRestService(ITradingResource.class)
					.getOtherHomeTPlusDay("13500001009");
	}
	
//	public List<GainVO> getMoreOtherPersonal( String userId, int start, int limit, boolean virtual)throws Exception;
	public void testGetMoreOtherPersonal()throws Exception{
		List<GainVO> a = restService.getRestService(ITradingResource.class)
					.getMoreOtherPersonal("13500001009",0,10,true);
	}
}
