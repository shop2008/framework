/*
 * @(#)ShareResource.java    2011-10-14
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;
import java.util.List;

import com.wxxr.javax.ws.rs.Consumes;
import com.wxxr.javax.ws.rs.GET;
import com.wxxr.javax.ws.rs.POST;
import com.wxxr.javax.ws.rs.Path;
import com.wxxr.javax.ws.rs.Produces;
import com.wxxr.javax.ws.rs.QueryParam;
import com.wxxr.javax.ws.rs.core.MediaType;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.BaseInfoListVO;
import com.wxxr.stock.restful.json.ComponentstocksListVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.json.PlateTaxisListVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.json.StockTaxisListVO;

/**
 * 分享：短信分享
 * 
 * @class desc A ShareResource.
 * 
 * @author zhengjincheng
 * @version v1.0
 * @created time 2011-10-14 下午02:01:16
 */
@Path("/rest/trading")
public interface StockResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public BaseInfoListVO getAllStockInfo() throws Exception ;

    /**
     * 行情数据接口
     * @param list
     * @return
     * @throws Exception
     */
    @POST
    @Path("/quotation")
    @Produces({ "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public QuotationListVO getQuotation(List<ParamVO> list) throws Exception;
    @GET
    @Path("/quo")
    @Produces({ "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })   
    public QuotationListVO getQuotation(@QueryParam("market")  String market,@QueryParam("code") String code) throws Exception;
    

    /**
     * 分时线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/minuteline")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public StockMinuteKVO getMinuteline(ParamVO paramVO) throws Exception;
    /**
     * 日K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/dayline")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public LineListVO getDayline(ParamVO paramVO) throws Exception ;
    /**
     * 五日分时线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/fivedayminuteline")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public List<StockMinuteKVO> getFiveDayMinuteline(ParamVO paramVO) throws Exception;
   
    /**
     * @GZIP
     * @param taxisvo
     * @return
     * @throws Exception
     */
    @POST
    @Path("/blocStockkHQ")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    public List<StockQuotationVO> getStockHQListByBlockId(TaxisVO taxisvo) throws Exception;
    /**
     * 周K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/weekline")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public LineListVO getWeekline(ParamVO paramVO) throws Exception;
    /**
     * 月K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/monthline")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public LineListVO getMonthline(ParamVO paramVO) throws Exception;

    /**
     * 涨跌排行接口
     * @param vo
     * @return
     * @throws Exception
     */
    @POST
    @Path("/stocktaxis")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public StockTaxisListVO getStocktaxis(TaxisVO vo) throws Exception;

    /**
     * 板块排行接口
     * @param vo
     * @return
     * @throws Exception
     */
    @POST
    @Path("/platetaxis")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public PlateTaxisListVO getPlatetaxis(TaxisVO vo) throws Exception ;
    @POST
    @Path("/refBlockHQ")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public PlateTaxisListVO getRefenceBlockHQ(List<ParamVO> list) throws Exception;
    
    /**
     * 成份股接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    @POST
    @Path("/componentstocks")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public ComponentstocksListVO getComponentstocks(ParamVO paramVO) throws Exception ;
    /**
     * 指数概览接口
     * @param vo
     * @return
     * @throws Exception
     */
    @POST
    @Path("/index")
    @Produces( { "application/json;charset=utf-8" })
    @Consumes({ "application/json;charset=utf-8" })
    //@GZIP
    public StockTaxisListVO getIndexPreview(TaxisVO vo) throws Exception;
  
    
  

}
