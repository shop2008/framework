package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;
import com.wxxr.stock.trading.ejb.api.ClosedSummaryVO;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;
import com.wxxr.stock.trading.ejb.api.DealDetailVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

@Path("/rest/trading")
public interface ITradingResource{
	@GET
    @Path("/getAcct")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public TradingAccountVO getAccount(@QueryParam("acctID")String acctID) throws Exception;
	
	@GET
    @Path("/getAcctRecord")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public List<TradingRecordVO> getTradingAccountRecord(@QueryParam("acctID") String acctID,@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception ;
	
	@GET
	@Path("/getGain")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<GainVO> getGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	@GET
	@Path("/getClosedSum")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public ClosedSummaryVO getClosedTradingSum(@QueryParam("tradingAccountId") String tradingAccountId)throws Exception;

	@GET
	@Path("/dealDetail")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public DealDetailInfoVO getDealDetail(@QueryParam("acctID")String acctID)throws Exception;
	@GET
	@Path("/auditDetail")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public AuditInfoVO getAuditDetail(@QueryParam("acctID")String acctID)throws Exception;
	@GET
	@Path("/totalGain")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<GainVO> getTotalGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	
	@GET
	@Path("/homeImage")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<HomePageVO> getHomeImage() throws Exception;
	@GET
	@Path("/homeList")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<HomePageVO> getHomeList(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	
	@GET
	@Path("/filterStocks")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public String[] getFilterStocks()throws Exception;
	@GET
	@Path("/getTRank")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<MegagameRankVO> getTMegagameRank()throws Exception;
	
	@GET
	@Path("/getTPlusRank")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<MegagameRankVO> getTPlusMegagameRank() throws Exception;
	
	@GET
	@Path("/getRegTic")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<RegularTicketVO> getRegularTicketRank()throws Exception;
	
	@GET
	@Path("/getWeekRank")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<WeekRankVO> getWeekRank()throws Exception;
	@GET
	@Path("/getOtherHomeFromWeek")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes({ "application/json" })
	public PersonalHomePageVO getOtherHomeFromWeek(@QueryParam("userId") String userId)throws Exception;
	@GET
	@Path("/getOtherHomeFromTDay")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public PersonalHomePageVO getOtherHomeFromTDay(@QueryParam("userId") String userId)throws Exception;
	@GET
	@Path("/getOtherHomeTPlusDay")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public PersonalHomePageVO getOtherHomeTPlusDay(@QueryParam("userId") String userId)throws Exception;
	@GET
	@Path("/getMoreOtherHomePage")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public List<GainVO> getMoreOtherPersonal(@QueryParam("userId") String userId,@QueryParam("start") int start,@QueryParam("limit") int limit,@QueryParam("virtual") boolean virtual)throws Exception;
}
