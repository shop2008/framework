package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.stock.hq.ejb.api.StockBaseInfoVO;
import com.wxxr.stock.trading.ejb.api.AuditDetailVO;
import com.wxxr.stock.trading.ejb.api.ClosedSumInfoVO;
import com.wxxr.stock.trading.ejb.api.DealDetailVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

@Path("/trading/resourse")
public interface TradingResourse  {
	
	@GET
    @Path("/getAcct")
    @Produces({ "application/json" })
    public TradingAccountVO getAccount(@QueryParam("acctID")String acctID) throws Exception ;
	
	@GET
    @Path("/getAcctRecord")
    @Produces({ "application/json" })
    public List<TradingRecordVO> getTradingAccountRecord(@QueryParam("acctID") String acctID,@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception ;
	
	@GET
    @Path("/buyStock")
    @Produces({ "application/json" })
    public StockResultVO buyStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception ;
	
	@GET
    @Path("/sellStock")
    @Produces({ "application/json" })
    public StockResultVO sellStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception ;
	
	@GET
    @Path("/cancelOrder")
    @Produces({ "application/json" })
    public StockResultVO cancelOrder(@QueryParam("orderID") String orderID) throws Exception ;
	
	@GET
	@Path("/getGain")
	@Produces({ "application/json" })
	public List<GainVO> getGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Throwable;
	@GET
	@Path("/getList")
	@Produces({ "application/json" })
	public List<TradingAccInfoVO> getTradingAccountList() throws Throwable;
	
	@GET
	@Path("/getClosedSum")
	@Produces({ "application/json" })
	public ClosedSumInfoVO getClosedTradingSum(@QueryParam("tradingAccountId") String tradingAccountId)throws Throwable;
	
	@GET
	@Path("/getInfo")
	@Produces({ "application/json" })
	public UserCreateTradAccInfoVO getCreateStrategyInfo()throws Throwable;
	
	@GET
	@Path("/newTraAcc")
	@Produces({ "application/json" })
	public StockResultVO createTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate)throws Throwable;
	
	@GET
	@Path("/mulTraAcc")
	@Produces({ "application/json" })
	public StockResultVO mulCreateTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType)throws Throwable;
	@GET
	@Path("/quickBuy")
	@Produces({ "application/json" })
	public StockResultVO quickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate)throws Throwable;
	@GET
	@Path("/mulQuickBuy")
	@Produces({ "application/json" })
	public StockResultVO mulQuickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType)throws Throwable;
	@GET
	@Path("/dealDetail")
	@Produces({ "application/json" })
	public DealDetailVO getDealDetail(@QueryParam("acctID")String acctID)throws Throwable;
	@GET
	@Path("/auditDetail")
	@Produces({ "application/json" })
	public AuditDetailVO getAuditDetail(@QueryParam("acctID")String acctID)throws Throwable;
	@GET
	@Path("/clearance")
	@Produces({ "application/json" })
	public StockResultVO clearTradingAccount(@QueryParam("acctID")String acctID)throws Throwable;
	
	@Deprecated
	@GET
	@Path("/unuseStocks")
	@Produces({ "application/json" })
	public List<StockBaseInfoVO> getUnusebleStocks()throws Throwable;
	

	@GET
	@Path("/totalGain")
	@Produces({ "application/json" })
	public List<GainVO> getTotalGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Throwable;
	
	@GET
	@Path("/homeImage")
	@Produces({ "application/json" })
	public List<HomePageVO> getHomeImage() throws Throwable;
	@GET
	@Path("/homeList")
	@Produces({ "application/json" })
	public List<HomePageVO> getHomeList(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Throwable;
	
	@GET
	@Path("/filterStocks")
	@Produces({ "application/json" })
	public String[] getFilterStocks()throws Throwable;
	
	@GET
	@Path("/applyDrawMoney")
	@Produces({ "application/json" })
	public StockResultVO drawMoney(@QueryParam("count") long count);
	@GET
	@Path("/getAcctUsable")
	@Produces({ "application/json" })
	public UserAssetVO getAcctUsable()throws Throwable;
	
	@GET
	@Path("/getGPDetails")
	@Produces({ "application/json" })
	public List<GainPayDetailsVO> getGPDetails(@QueryParam("start") int start,@QueryParam("limit") int limit)throws Throwable;
	
	@GET
	@Path("/getTRank")
	@Produces({ "application/json" })
	public List<MegagameRankVO> getTMegagameRank()throws Throwable;
	@GET
	@Path("/getTPlusRank")
	@Produces({ "application/json" })
	public List<MegagameRankVO> getTPlusMegagameRank()throws Throwable;
	@GET
	@Path("/getRegTic")
	@Produces({ "application/json" })
	public List<RegularTicketVO> getRegularTicketRank()throws Throwable;
	
	@GET
	@Path("/getWeekRank")
	@Produces({ "application/json" })
	public List<WeekRankVO> getWeekRank()throws Throwable;
	@GET
	@Path("/getSelfHomePage")
	@Produces({ "application/json" })
	public PersonalHomePageVO getSelfHomePage()throws Throwable;
	
	@GET
	@Path("/getOtherHomeFromWeek")
	@Produces({ "application/json" })
	public PersonalHomePageVO getOtherHomeFromWeek(@QueryParam("userId") String userId)throws Throwable;
	@GET
	@Path("/getOtherHomeFromTDay")
	@Produces({ "application/json" })
	public PersonalHomePageVO getOtherHomeFromTDay(@QueryParam("userId") String userId)throws Throwable;
	@GET
	@Path("/getOtherHomeTPlusDay")
	@Produces({ "application/json" })
	public PersonalHomePageVO getOtherHomeTPlusDay(@QueryParam("userId") String userId)throws Throwable;
	
	@GET
	@Path("/getMoreHomePage")
	@Produces({ "application/json" })
	public List<GainVO> getMorePersonalRecords(@QueryParam("start") int start,@QueryParam("limit") int limit,@QueryParam("virtual") boolean virtual) throws Exception;
	
	@GET
	@Path("/getMoreOtherHomePage")
	@Produces({ "application/json" })
	public List<GainVO> getMoreOtherPersonal(@QueryParam("userId") String userId,@QueryParam("start") int start,@QueryParam("limit") int limit,@QueryParam("virtual") boolean virtual)throws Throwable;
}
