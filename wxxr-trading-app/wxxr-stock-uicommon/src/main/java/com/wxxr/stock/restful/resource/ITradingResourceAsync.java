package com.wxxr.stock.restful.resource;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.restful.json.SimpleVO;
import com.wxxr.stock.trading.ejb.api.AppHomePageListVO;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;
import com.wxxr.stock.trading.ejb.api.ClosedSummaryVO;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.HomePageVOs;
import com.wxxr.stock.trading.ejb.api.MegagameRankVOs;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVOs;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingConfigListVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVOs;
import com.wxxr.stock.trading.ejb.api.WeekRankVOs;

public interface ITradingResourceAsync{
	
    public Async<TradingAccountVO> getAccount(String acctID);
	
    public Async<TradingRecordVOs> getTradingAccountRecord( String acctID, int start, int limit);
	
	public Async<GainVOs> getGain( int start, int limit);
	
	public Async<ClosedSummaryVO> getClosedTradingSum( String tradingAccountId);

	public Async<DealDetailInfoVO> getDealDetail(String acctID);
	
	public Async<AuditInfoVO> getAuditDetail(String acctID);
	
	public Async<GainVOs> getTotalGain( int start, int limit);
	
	public Async<HomePageVOs> getHomeImage();
	
	public Async<HomePageVOs> getHomeList( int start, int limit);
	
	public String[] getFilterStocks();
	
	public Async<MegagameRankVOs> getTMegagameRank();
	
	public Async<MegagameRankVOs> getTPlusMegagameRank();
	
	public Async<RegularTicketVOs> getRegularTicketRank();
	
	public Async<WeekRankVOs> getWeekRank();
	
	public Async<PersonalHomePageVO> getOtherHomeFromWeek( String userId);
	
	public Async<PersonalHomePageVO> getOtherHomeFromTDay( String userId);
	
	public Async<PersonalHomePageVO> getOtherHomeTPlusDay( String userId);
	
	public Async<GainVOs> getMoreOtherPersonal( String userId, int start, int limit, boolean virtual);
	
	public Async<TradingConfigListVO> getStrategyConfig();

	public Async<SimpleVO> getGuideGainAmount();
	
	public Async<AppHomePageListVO> unSecurityAppHome(int start,int limit);

}
