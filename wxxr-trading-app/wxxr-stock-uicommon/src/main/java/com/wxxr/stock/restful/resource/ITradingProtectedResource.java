package com.wxxr.stock.restful.resource;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVos;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVOs;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.HomePageVOs;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVOs;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

@Path("/secure/trading")
public interface ITradingProtectedResource{
	@GET
    @Path("/buyStock")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public StockResultVO buyStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception;
	@GET
    @Path("/sellStock")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public StockResultVO sellStock(@QueryParam("acctID") String acctID,@QueryParam("market") String market,@QueryParam("code") String code,@QueryParam("price") long price,@QueryParam("amount") long amount) throws Exception;
	@GET
    @Path("/cancelOrder")
    @Produces({ "application/json;charset=utf-8" })
	@Consumes
    public StockResultVO cancelOrder(@QueryParam("orderID") String orderID) throws Exception;
	@GET
	@Path("/getGain")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public GainVOs getGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	@GET
	@Path("/getList")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public TradingAccInfoVOs getTradingAccountList() throws Exception;
	@GET
	@Path("/getInfo")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public UserCreateTradAccInfoVO getCreateStrategyInfo()throws Exception;
	@GET
	@Path("/newTraAcc")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO createTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate)throws Exception;
	@GET
	@Path("/mulTraAcc")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO mulCreateTradingAccount(@QueryParam("captitalAmount")Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType)throws Exception;
	@GET
	@Path("/quickBuy")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO quickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate)throws Exception;
	@GET
	@Path("/mulQuickBuy")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO mulQuickBuy(@QueryParam("captitalAmount") Long captitalAmount,@QueryParam("capitalRate") float capitalRate,@QueryParam("virtual") boolean virtual,@QueryParam("stockMarket") String stockMarket,@QueryParam("stockCode") String stockCode,@QueryParam("stockBuyAmount") long stockBuyAmount,@QueryParam("depositRate")float depositRate,@QueryParam("assetType")String assetType) throws Exception;
	@GET
	@Path("/clearance")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO clearTradingAccount(@QueryParam("acctID")String acctID)throws Exception;
	@GET
	@Path("/totalGain")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public GainVOs getTotalGain(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	
	@GET
	@Path("/homeImage")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public HomePageVOs getHomeImage() throws Exception;
	@GET
	@Path("/homeList")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public HomePageVOs getHomeList(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
	@GET
	@Path("/applyDrawMoney")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public StockResultVO drawMoney(@QueryParam("count") long count);
	@GET
	@Path("/getAcctUsable")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public UserAssetVO getAcctUsable()throws Exception;
	@GET
	@Path("/getGPDetails")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public GainPayDetailsVOs getGPDetails(@QueryParam("start") int start,@QueryParam("limit") int limit)throws Exception;
	@GET
	@Path("/getSelfHomePage")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public PersonalHomePageVO getSelfHomePage()throws Exception;
	@GET
	@Path("/getMoreHomePage")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public GainVOs getMorePersonalRecords(@QueryParam("start") int start,@QueryParam("limit") int limit,@QueryParam("virtual") boolean virtual) throws Exception ;
	
	@GET
	@Path("/getVouDetails")
	@Produces({  "application/json;charset=utf-8" })
	@Consumes
	public VoucherDetailsVO getVoucherDetails(@QueryParam("start") int start,@QueryParam("limit") int limit)throws Exception;
	@GET
	@Path("/drawMoneyRecords")
	@Produces({ "application/json;charset=utf-8" })
	@Consumes
	public DrawMoneyRecordVos drawMoneyRecords(@QueryParam("start") int start,@QueryParam("limit") int limit) throws Exception;
}
