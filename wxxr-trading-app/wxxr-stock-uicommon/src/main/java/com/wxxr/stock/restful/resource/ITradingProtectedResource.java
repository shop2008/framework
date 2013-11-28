package com.wxxr.stock.restful.resource;

import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

@Path("/secure/trading")
public interface ITradingProtectedResource{
	@GET
    @Path("/buyStock")
    @Produces({ "application/json" })
	@Consumes
    public StockResultVO buyStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception;
	@GET
    @Path("/sellStock")
    @Produces({ "application/json" })
	@Consumes
    public StockResultVO sellStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception;
	@GET
    @Path("/cancelOrder")
    @Produces({ "application/json" })
	@Consumes
    public StockResultVO cancelOrder(@QueryParam("orderID") String orderID) throws Exception;
	@GET
	@Path("/getGain")
	@Produces({ "application/json" })
	@Consumes
	public List<GainVO> getGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	@GET
	@Path("/getList")
	@Produces({ "application/json" })
	@Consumes
	public List<TradingAccInfoVO> getTradingAccountList() throws Exception;
	@GET
	@Path("/getInfo")
	@Produces({ "application/json" })
	@Consumes
	public UserCreateTradAccInfoVO getCreateStrategyInfo()throws Exception;
	@GET
	@Path("/newTraAcc")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO createTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate)throws Exception;
	@GET
	@Path("/mulTraAcc")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO mulCreateTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType)throws Exception;
	@GET
	@Path("/quickBuy")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO quickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate)throws Exception;
	@GET
	@Path("/mulQuickBuy")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO mulQuickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType) throws Exception;
	@GET
	@Path("/clearance")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO clearTradingAccount(@QueryParam("acctID")String acctID)throws Exception;
	@GET
	@Path("/totalGain")
	@Produces({ "application/json" })
	@Consumes
	public List<GainVO> getTotalGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	
	@GET
	@Path("/homeImage")
	@Produces({ "application/json" })
	@Consumes
	public List<HomePageVO> getHomeImage() throws Exception;
	@GET
	@Path("/homeList")
	@Produces({ "application/json" })
	@Consumes
	public List<HomePageVO> getHomeList(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	@GET
	@Path("/applyDrawMoney")
	@Produces({ "application/json" })
	@Consumes
	public StockResultVO drawMoney(@QueryParam("count") long count);
	@GET
	@Path("/getAcctUsable")
	@Produces({ "application/json" })
	@Consumes
	public UserAssetVO getAcctUsable()throws Exception;
	@GET
	@Path("/getGPDetails")
	@Produces({ "application/json" })
	@Consumes
	public List<GainPayDetailsVO> getGPDetails(@QueryParam("start") int start,@QueryParam("limit") int limit)throws Exception;
	@GET
	@Path("/getSelfHomePage")
	@Produces({ "application/json" })
	@Consumes
	public PersonalHomePageVO getSelfHomePage()throws Exception;
	@GET
	@Path("/getMoreHomePage")
	@Produces({ "application/json" })
	@Consumes
	public List<GainVO> getMorePersonalRecords(@QueryParam("start") int start,@QueryParam("limit") int limit,@QueryParam("virtual") boolean virtual) throws Exception ;
}
