/*
 * @(#)ShareResource.java    2011-10-14
 *
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.stock.restful.resource;
import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVOs;
import com.wxxr.stock.hq.ejb.api.TaxisVO;
import com.wxxr.stock.restful.json.BaseInfoListVO;
import com.wxxr.stock.restful.json.ComponentstocksListVO;
import com.wxxr.stock.restful.json.LineListVO;
import com.wxxr.stock.restful.json.ParamVO;
import com.wxxr.stock.restful.json.PlateTaxisListVO;
import com.wxxr.stock.restful.json.QuotationListVO;
import com.wxxr.stock.restful.json.StockParamsVo;
import com.wxxr.stock.restful.json.StockTaxisListVO;
import com.wxxr.stock.trading.ejb.api.StockQuotationVOs;

/**
 * 分享：短信分享
 * 
 * @class desc A ShareResource.
 * 
 * @author zhengjincheng
 * @version v1.0
 * @created time 2011-10-14 下午02:01:16
 */
public interface StockResourceAsync {

    public Async<BaseInfoListVO> getAllStockInfo();

    /**
     * 行情数据接口
     * @param list
     * @return
     * @throws Exception
     */
    public Async<QuotationListVO> getQuotation(StockParamsVo stockParamsVos);
    
    public Async<QuotationListVO> getQuotation(  String market, String code);
    

    /**
     * 分时线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<StockMinuteKVO> getMinuteline(ParamVO paramVO);
    /**
     * 日K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<LineListVO> getDayline(ParamVO paramVO);
    /**
     * 五日分时线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<StockMinuteKVOs> getFiveDayMinuteline(ParamVO paramVO);
   
    /**
     * @GZIP
     * @param taxisvo
     * @return
     * @throws Exception
     */
    public Async<StockQuotationVOs> getStockHQListByBlockId(TaxisVO taxisvo);
    /**
     * 周K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<LineListVO> getWeekline(ParamVO paramVO);
    /**
     * 月K线数据接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<LineListVO> getMonthline(ParamVO paramVO);

    /**
     * 涨跌排行接口
     * @param vo
     * @return
     * @throws Exception
     */
    public Async<StockTaxisListVO> getStocktaxis(TaxisVO vo);

    /**
     * 板块排行接口
     * @param vo
     * @return
     * @throws Exception
     */
    public Async<PlateTaxisListVO> getPlatetaxis(TaxisVO vo);
    
    public Async<PlateTaxisListVO> getRefenceBlockHQ(StockParamsVo vos);
    
    /**
     * 成份股接口
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Async<ComponentstocksListVO> getComponentstocks(ParamVO paramVO);
    /**
     * 指数概览接口
     * @param vo
     * @return
     * @throws Exception
     */
    public Async<StockTaxisListVO> getIndexPreview(TaxisVO vo);
  
    
  

}
