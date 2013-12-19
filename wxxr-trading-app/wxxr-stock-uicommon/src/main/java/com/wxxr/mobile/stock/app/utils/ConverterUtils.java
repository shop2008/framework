/**
 * 
 */
package com.wxxr.mobile.stock.app.utils;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.stock.hq.ejb.api.StockLineVO;
import com.wxxr.stock.hq.ejb.api.StockMinuteKVO;
import com.wxxr.stock.hq.ejb.api.StockMinuteLineVO;
import com.wxxr.stock.hq.ejb.api.StockQuotationVO;
import com.wxxr.stock.hq.ejb.api.StockTaxisVO;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;
import com.wxxr.stock.restful.json.VoucherDetailsVO;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockTradingOrderVO;
import com.wxxr.stock.trading.ejb.api.TradeRecordVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * @author wangxuyang
 * 
 */
public class ConverterUtils {
    public static void updatefromVOtoBean(StockQuotationBean b,StockQuotationVO vo){
        b.setCode(vo.getCode());
        b.setMarket(vo.getMarket());
        b.setDatetime(vo.getDatetime());
        b.setClose(vo.getClose());
        b.setOpen(vo.getOpen());
        b.setHigh(vo.getHigh());
        b.setLow(vo.getLow());
        b.setNewprice(vo.getNewprice());
        b.setAverageprice(vo.getAverageprice());
        b.setSecuamount(vo.getSecuamount());
        b.setSecuvolume(vo.getSecuvolume());
        b.setLb(vo.getLb());
        b.setProfitrate(vo.getProfitrate());
        b.setHandrate(vo.getHandrate());
        b.setRisefallrate(vo.getRisefallrate());
        b.setMarketvalue(vo.getMarketvalue());
        b.setCapital(vo.getCapital());
        
        b.setBuyprice1(vo.getBuyprice1());
        b.setBuyprice2(vo.getBuyprice2());
        b.setBuyprice3(vo.getBuyprice3());
        b.setBuyprice4(vo.getBuyprice4());
        b.setBuyprice5(vo.getBuyprice5());
       
        b.setBuyvolume1(vo.getBuyvolume1());
        b.setBuyvolume2(vo.getBuyvolume2());
        b.setBuyvolume3(vo.getBuyvolume3());
        b.setBuyvolume4(vo.getBuyvolume4());
        b.setBuyvolume5(vo.getBuyvolume5());
        
        b.setSellprice1(vo.getSellprice1());
        b.setSellprice2(vo.getSellprice2());
        b.setSellprice3(vo.getSellprice3());
        b.setSellprice4(vo.getSellprice4());
        b.setSellprice5(vo.getSellprice5());
        
        b.setSellvolume1(vo.getSellvolume1());
        b.setSellvolume2(vo.getSellvolume2());
        b.setSellvolume3(vo.getSellvolume3());
        b.setSellvolume4(vo.getSellvolume4());
        b.setSellvolume5(vo.getSellvolume5());
        
        b.setPpjs(vo.getPpjs());
        b.setSzjs(vo.getSzjs());
        b.setXdjs(vo.getXdjs());
        
        b.setSellsum(vo.getSellsum());
        b.setBuysum(vo.getBuysum());
        b.setStatus(vo.getStatus());
        b.setChange(vo.getChange());
    }
    public static StockQuotationBean fromVO(StockQuotationVO vo){
        if (vo == null) {
            return null;
        }
        StockQuotationBean b=new StockQuotationBean();
        updatefromVOtoBean(b,vo);
        return b;
    } 
    public static void  UpdatefromVO(TradingAccountBean b,TradingAccountVO vo){
        b.setId(vo.getId());
        b.setApplyFee(vo.getApplyFee());
        b.setAvalibleFee(vo.getAvalibleFee());
        b.setBuyDay(vo.getBuyDay());
        b.setFrozenVol(vo.getFrozenVol());
        b.setGainRate(vo.getGainRate());
        b.setLossLimit(vo.getLossLimit());
        b.setMaxStockCode(vo.getMaxStockCode());
        b.setMaxStockMarket(vo.getMaxStockMarket());
        b.setOver(vo.getOver());
        b.setSellDay(vo.getSellDay());
        b.setStatus(vo.getStatus());
        b.setTotalGain(vo.getTotalGain());
        b.setType(vo.getType());
        b.setUsedFee(vo.getUsedFee());
        b.setVirtual(vo.isVirtual());
        List<StockTradingOrderVO> orderVos = vo.getTradingOrders();
        if (orderVos != null) {
            List<StockTradingOrderBean> list = new ArrayList<StockTradingOrderBean>();
            for (StockTradingOrderVO order : orderVos) {
                list.add(ConverterUtils.fromVO(order));
            }
            b.setTradingOrders(list);
        }
        
    }
    
    public static TradingAccountBean fromVO(TradingAccountVO vo) {
        if (vo == null) {
            return null;
        }
        TradingAccountBean b=new TradingAccountBean();
        UpdatefromVO(b,vo);
        return b;
    }

    public static void setfromVO(UserCreateTradAccInfoBean bean, UserCreateTradAccInfoVO vo) {
        if (vo == null || bean == null) {
            return;
        }
        bean.setCapitalRate(vo.getCapitalRate());
        bean.setCostRate(vo.getCostRate());
        bean.setDepositRate(vo.getDepositRate());
        bean.setMaxAmount(vo.getMaxAmount());
        bean.setRateString(vo.getRateString());
        bean.setUserId(vo.getUserId());
        bean.setVoucherCostRate(vo.getVoucherCostRate());
        if (bean.getMaxAmount() != null && bean.getMaxAmount() > 0) {
            List<String> x = new ArrayList<String>();
            long max = bean.getMaxAmount() / 10000 / 100;
            for (int i = 0; i < max; i++) {
                x.add(String.valueOf(i + 1) + "万");
            }
            bean.setRequestamount(x);
        }

        dataResolution(bean);
    }

    public static void dataResolution(UserCreateTradAccInfoBean bean) {
        String rateString = bean.getRateString();
        if (rateString == null) {
            return;
        }
        String[] data = rateString.split(",");
        if (data != null && data.length > 0) {
            int index;
            index = data[0].indexOf(";");
            bean.setRateData1(Float.parseFloat(data[0].substring(0, index)));
            bean.setRateString1("-" + String.format("%.0f", bean.getRateData1() * 100) + "%");
            bean.setDeposit1(Float.parseFloat(data[0].substring(index + 1, data[0].length())));
            //            
            index = data[1].indexOf(";");
            bean.setRateData2(Float.parseFloat(data[1].substring(0, index)));
            bean.setRateString2("-" + String.format("%.0f", bean.getRateData2() * 100) + "%");
            bean.setDeposit2(Float.parseFloat(data[1].substring(index + 1, data[1].length())));
            //            
            index = data[2].indexOf(";");
            bean.setRateData3(Float.parseFloat(data[2].substring(0, index)));
            bean.setRateString3("-" + String.format("%.0f", bean.getRateData3() * 100) + "%");
            bean.setDeposit3(Float.parseFloat(data[2].substring(index + 1, data[2].length())));
        }
    }

    //    private void rateData(){
    //        String rateString1 = "-"+String.format("%.0f", _rate1*100)+"%";
    //        String rateString2 = "-"+String.format("%.0f", _rate2*100)+"%";
    //        String rateString3 = "-"+String.format("%.0f", _rate3*100)+"%"; 
    //                   
    //    }
    public static UserCreateTradAccInfoBean fromVO(UserCreateTradAccInfoVO vo) {
        if (vo == null) {
            return null;
        }
        UserCreateTradAccInfoBean bean = new UserCreateTradAccInfoBean();
        bean.setCapitalRate(vo.getCapitalRate());
        bean.setCostRate(vo.getCostRate());
        bean.setDepositRate(vo.getDepositRate());
        bean.setMaxAmount(vo.getMaxAmount());
        bean.setRateString(vo.getRateString());
        bean.setUserId(vo.getUserId());
        bean.setVoucherCostRate(vo.getVoucherCostRate());
        return bean;
    }

    public static TradingAccInfoBean fromVO(TradingAccInfoVO vo) {
        if (vo == null) {
            return null;
        }
        TradingAccInfoBean bean = new TradingAccInfoBean();
        bean.setAcctID(vo.getAcctID());
        bean.setCreateDate(vo.getCreateDate());
        bean.setMaxStockCode(vo.getMaxStockCode());
        bean.setMaxStockMarket(vo.getMaxStockMarket());
        String stockName = vo.getMaxStockName();
        if (StringUtils.isBlank(stockName)) {
        	StockBaseInfo stock = KUtils.getService(IStockInfoSyncService.class).getStockBaseInfoByCode(vo.getMaxStockCode(), vo.getMaxStockMarket());
        	if (stock!=null) {
        		stockName = stock.getName();
			}
        }
        bean.setMaxStockName(stockName);
        bean.setOver(vo.getOver());
        bean.setStatus(vo.getStatus());
        bean.setSum(vo.getSum());
        bean.setTotalGain(vo.getTotalGain());
        bean.setVirtual(vo.isVirtual());
        
        return bean;
    }

    public static StockTradingOrderBean fromVO(StockTradingOrderVO vo) {
        if (vo == null) {
            return null;
        }
        StockTradingOrderBean bean = new StockTradingOrderBean();
        bean.setAmount(vo.getAmount());
        bean.setBuy(vo.getBuy());
        bean.setChangeRate(vo.getChangeRate());
        bean.setCurrentPirce(vo.getCurrentPirce());
        bean.setGain(vo.getGain());
        bean.setGainRate(vo.getGainRate());
        bean.setId(vo.getId());
        bean.setMarketCode(vo.getMarketCode());
        bean.setStatus(vo.getStatus());
        bean.setStockCode(vo.getStockCode());
        return bean;
    }

    public static WeekRankBean fromVO(WeekRankVO vo) {
        if (vo == null) {
            return null;
        }
        WeekRankBean bean = new WeekRankBean();
        bean.setDates(vo.getDates());
        bean.setGainCount(vo.getGainCount());
        bean.setGainRate(vo.getGainRate());
        bean.setGainRates(vo.getGainRates());
        bean.setNickName(vo.getNickName());
        bean.setTotalGain(vo.getTotalGain());
        bean.setUserId(vo.getUesrId());
        return bean;
    }

    public static MegagameRankBean fromVO(MegagameRankVO vo) {
        if (vo == null) {
            return null;
        }
        MegagameRankBean bean = new MegagameRankBean();
        bean.setAcctID(vo.getAcctID());
        bean.setGainRate(vo.getGainRate());
        bean.setGainRates(vo.getGainRates());
        bean.setMaxStockCode(vo.getMaxStockCode());
        bean.setMaxStockMarket(vo.getMaxStockMarket());
        bean.setNickName(vo.getNickName());
        bean.setOver(vo.getOver());
        bean.setStatus(vo.getStatus());
        bean.setTotalGain(vo.getTotalGain());
        bean.setUserId(vo.getUesrId());
        return bean;
    }

    public static RegularTicketBean fromVO(RegularTicketVO vo) {
        if (vo == null) {
            return null;
        }
        RegularTicketBean bean = new RegularTicketBean();
        bean.setGainCount(vo.getGainCount());
        bean.setNickName(vo.getNickName());
        bean.setRegular(vo.getRegular());
        return bean;
    }

    /**
     * @param vo
     * @return
     */
    public static GainBean fromVO(GainVO vo) {
        if (vo == null) {
            return null;
        }
        GainBean bean = new GainBean();
        updatefromVOtoBean(bean,vo);
        return bean;
    }
    public static void updatefromVOtoBean(GainBean bean, GainVO vo) {
        bean.setCloseTime(vo.getCloseTime());
        bean.setMaxStockCode(vo.getMaxStockCode());
        bean.setMaxStockMarket(vo.getMaxStockMarket());
        String stockName = vo.getMaxStockName();
        if (StringUtils.isBlank(stockName)) {
        	StockBaseInfo stock = KUtils.getService(IStockInfoSyncService.class).getStockBaseInfoByCode(vo.getMaxStockCode(), vo.getMaxStockMarket());
        	if (stock!=null) {
        		stockName = stock.getName();
			}
        }
        bean.setMaxStockName(stockName);
        bean.setOver(vo.getOver());
        bean.setStatus(vo.getStatus());
        bean.setSum(vo.getSum());
        bean.setTotalGain(vo.getTotalGain());
        bean.setTradingAccountId(vo.getTradingAccountId());
        bean.setUserGain(vo.getUserGain());
        bean.setVirtual(vo.isVirtual());        
    }
    public static TradingRecordBean fromVO(TradeRecordVO vo) {
        if (vo == null) {
            return null;
        }
        TradingRecordBean bean = new TradingRecordBean();
        bean.setAmount(vo.getAmount());
        bean.setBeDone(vo.isBeDone());
        bean.setBrokerage(vo.getBrokerage());
        bean.setCode(vo.getCode());
        bean.setDate(vo.getDate());
        bean.setDay(vo.getDay());
        bean.setDescribe(vo.getDescribe());
        bean.setFee(vo.getFee());
        bean.setMarket(vo.getMarket());
        bean.setPrice(vo.getPrice());
        bean.setTax(vo.getTax());
        bean.setVol(vo.getVol());
        bean.setId(vo.getId());
        return bean;
    }

    /**
     * @param vo
     * @return
     */
    public static void fromVO(GainVO vo, GainBean bean) {
        if (vo == null) {
            return;
        }
        bean.setCloseTime(vo.getCloseTime());
        bean.setMaxStockCode(vo.getMaxStockCode());
        bean.setMaxStockMarket(vo.getMaxStockMarket());
        bean.setMaxStockName(vo.getMaxStockName());
        bean.setOver(vo.getOver());
        bean.setStatus(vo.getStatus());
        bean.setSum(vo.getSum());
        bean.setTotalGain(vo.getTotalGain());
        bean.setTradingAccountId(vo.getTradingAccountId());
        bean.setUserGain(vo.getUserGain());
        bean.setVirtual(vo.isVirtual());
    }
    public static StockMinuteKBean fromVO(StockMinuteKVO vo) {
        if (vo == null){
            return null;
        }
        StockMinuteKBean b=new StockMinuteKBean();
        b.setClose(vo.getClose());
        b.setDate(vo.getDate());
        List<StockMinuteLineVO> mvos=vo.getList();
        if (mvos!=null && !mvos.isEmpty()){
            List< StockMinuteLineBean> smlbs=new ArrayList< StockMinuteLineBean>();
            for  (StockMinuteLineVO item:mvos){
                StockMinuteLineBean smlb=fromVO(item);
                smlbs.add(smlb);
            }
            b.setList(smlbs);
        }
        return b;
    }
    private static StockMinuteLineBean fromVO(StockMinuteLineVO vo) {
        if (vo == null){
            return null;
        }
        StockMinuteLineBean bean = new StockMinuteLineBean();
        bean.setAvgChangeRate(vo.getAvgChangeRate());
        bean.setAvprice(vo.getAvprice());
        bean.setHqTime(vo.getHqTime());
        bean.setPrice(vo.getPrice());
        bean.setSecuamount(vo.getSecuamount());
        bean.setSecuvolume(vo.getSecuvolume());
        return bean;
    }
    public static void updatefromVOtoBean(StockMinuteKBean bean, StockMinuteKBean b) {
        bean.setClose(b.getClose());
        bean.setDate(b.getDate());
        bean.setMarket(b.getMarket());
        bean.setCode(b.getCode());
        List<StockMinuteLineBean> mvos=b.getList();
        bean.setList(mvos);
    }
    public static void updatefromVOtoBean(StockLineBean bean, StockLineBean vo) {
        bean.setClose(vo.getClose());
        bean.setCode(vo.getCode());
        bean.setDate(vo.getDate());
        bean.setHigh(vo.getHigh());
        bean.setLimit(vo.getLimit());
        bean.setLow(vo.getLow());
        bean.setMarket(vo.getMarket());
        bean.setOpen(vo.getOpen());
        bean.setPrice(vo.getPrice());
        bean.setSecuamount(vo.getSecuamount());
        bean.setSecuvolume(vo.getSecuvolume());
        bean.setStart(vo.getStart());
        bean.setTime(vo.getTime());
    }
    public static StockLineBean fromVO(StockLineVO vo) {
        if (vo == null){
            return null;
        }
        StockLineBean bean=new StockLineBean();
        bean.setClose(vo.getClose());
        bean.setDate(vo.getDate());
        bean.setHigh(vo.getHigh());
        bean.setLimit(vo.getLimit());
        bean.setLow(vo.getLow());
        bean.setOpen(vo.getOpen());
        bean.setPrice(vo.getPrice());
        bean.setSecuamount(vo.getSecuamount());
        bean.setSecuvolume(vo.getSecuvolume());
        bean.setStart(vo.getStart());
        bean.setTime(vo.getTime());
        return bean;
    }
    public static StockTaxisBean fromVO(StockTaxisVO vo) {
        if (vo == null){
            return null;
        }
        StockTaxisBean b=new StockTaxisBean();
        b.setCode(vo.getCode());
        b.setHandrate(vo.getHandrate());
        b.setLb(vo.getLb());
        b.setMarket(vo.getMarket());
        b.setMarketvalue(vo.getMarketvalue());
        b.setName(vo.getName());
        b.setNewprice(vo.getNewprice());
        b.setProfitrate(vo.getProfitrate());
        b.setRisefallrate(vo.getRisefallrate());
        b.setSecuamount(vo.getSecuamount());
        b.setSecuvolume(vo.getSecuvolume());
        return b;
    }
    public static  void updatefromVOtoBean(DealDetailBean b,DealDetailInfoVO vo){
        b.setFund(vo.getFund());
        b.setPlRisk(vo.getPlRisk());
        b.setUserGain(Float.valueOf(vo.getUserGain()));
        b.setTotalGain(Float.valueOf(vo.getTotalGain()));
        b.setImgUrl(Utils.getAbsoluteURL(vo.getImgUrl()));
        List<TradeRecordVO> volist = vo.getTradingRecords();
        if (volist != null && volist.size() > 0) {
            List<TradingRecordBean> beans = new ArrayList<TradingRecordBean>();
            for (TradeRecordVO tradingRecordVO : volist) {
                beans.add(fromVO(tradingRecordVO));
            }
            b.setTradingRecords(beans);
        }
    }
    public static DealDetailBean fromVO(DealDetailInfoVO vo) {
        if (vo == null){
            return null;
        }
        DealDetailBean b=new DealDetailBean();
        updatefromVOtoBean(b,vo);
        return b;
    }
    public static AuditDetailBean fromVO(AuditInfoVO vo) {
        if (vo == null){
            return null;
        }
        AuditDetailBean b=new AuditDetailBean();
        updatefromVOtoBean(b,vo);
        return b;
    }
    public static  void updatefromVOtoBean(AuditDetailBean b,AuditInfoVO vo) {
        b.setAccountPay(vo.getAccountPay());
        b.setBuyDay(vo.getBuyDay());
        b.setBuyAverage(vo.getBuyAverage());
        b.setCapitalRate(vo.getCapitalRate());
        b.setCost(vo.getCost());
        b.setDeadline(vo.getDeadline());
        b.setFrozenAmount(vo.getFrozenAmount());
        b.setFund(vo.getFund());
        b.setId(vo.getId());
        b.setPayOut(vo.getPayOut());
        b.setPlRisk(vo.getPlRisk());
        b.setSellAverage(vo.getSellAverage());
        b.setTotalGain(vo.getTotalGain());
        b.setTradingCost(vo.getTradingCost());
        b.setTradingDate(vo.getTradingDate());
        b.setType(vo.getType());
        b.setUnfreezeAmount(vo.getFrozenAmount());
        b.setUserGain(vo.getUserGain());
        b.setVirtual(vo.isVirtual());    
    }
    public static PersonalHomePageBean fromVO(PersonalHomePageVO vo) {
        if (vo == null){
            return null;
        }
        PersonalHomePageBean b=new PersonalHomePageBean();
        updatefromVOtoBean(b,vo);
        return b;
    }
    public static void updatefromVOtoBean(PersonalHomePageBean b, PersonalHomePageVO vo) {
        b.setActualCount(vo.getActualCount());
        b.setVirtualCount(vo.getVirtualCount());
        b.setTotalProfit(vo.getTotalProfit());
        b.setVoucherVol(vo.getVoucherVol());
        List<GainVO> volist = vo.getActualList();
        if (volist!=null&&volist.size()>0) {
            List<GainBean> bean_list = new ArrayList<GainBean>(); 
            for (GainVO acVO : volist) {
                bean_list.add(ConverterUtils.fromVO(acVO));
            }
            b.setActualList(bean_list);
        }
        volist = vo.getVirtualList();
        if (volist!=null&&volist.size()>0) {
            List<GainBean> bean_list = new ArrayList<GainBean>(); 
            for (GainVO acVO : volist) {
                bean_list.add(ConverterUtils.fromVO(acVO));
            }
            b.setVirtualList(bean_list);
        }
    }
    public static GainPayDetailBean fromVO(GainPayDetailsVO vo) {
        if (vo == null){
            return null;
        }
        GainPayDetailBean b=new GainPayDetailBean();
        updatefromVOtoBean(b,vo);
        return b;
    }
    public static void updatefromVOtoBean(GainPayDetailBean bean, GainPayDetailsVO vo) {
        bean.setAmount(vo.getAmount());
        bean.setId(vo.getId());
        bean.setTime(vo.getTime());
        bean.setComment(vo.getComment());
    }
    public static TradingRecordBean fromVO(TradingRecordVO vo) {
        if (vo == null) {
            return null;
        }
        TradingRecordBean bean = new TradingRecordBean();
        bean.setAmount(vo.getAmount());
        bean.setBeDone(vo.isBeDone());
        bean.setBrokerage(vo.getBrokerage());
        bean.setCode(vo.getCode());
        bean.setDate(vo.getDate());
        bean.setDay(vo.getDay());
        bean.setDescribe(vo.getDescribe());
        bean.setFee(vo.getFee());
        bean.setMarket(vo.getMarket());
        bean.setPrice(vo.getPrice());
        bean.setTax(vo.getTax());
        bean.setVol(vo.getVol());
//        bean.setId(vo.getId());
        return bean;
    }
    public static VoucherDetailsBean fromVO(VoucherDetailsVO vo) {
        if (vo == null) {
            return null;
        } 
        VoucherDetailsBean b=new VoucherDetailsBean();
        updatefromVOtoBean(b,vo);
        return b;
    }
    public static void updatefromVOtoBean(VoucherDetailsBean bean, VoucherDetailsVO vo) {
        bean.setAddToday(vo.getAddToday());
        bean.setBal(vo.getBal());
        bean.setReduceToday(vo.getReduceToday());
        if (vo.getList()!=null && !vo.getList().isEmpty()){
            List<GainPayDetailBean> bs=new ArrayList<GainPayDetailBean> ();
            for (GainPayDetailsVO item:vo.getList()){
                GainPayDetailBean b=fromVO(item);
                bs.add(b);
            }
            bean.setList(bs);
        }
    }
    public static void updatefromVO(TradingAccInfoVO vo,TradingAccInfoBean bean) {
       if (vo == null) {
           return;
       }
       bean.setAcctID(vo.getAcctID());
       bean.setCreateDate(vo.getCreateDate());
       bean.setMaxStockCode(vo.getMaxStockCode());
       bean.setMaxStockMarket(vo.getMaxStockMarket());
       String stockName = vo.getMaxStockName();
       if (StringUtils.isBlank(stockName)) {
           StockBaseInfo stock = KUtils.getService(IStockInfoSyncService.class).getStockBaseInfoByCode(vo.getMaxStockCode(), vo.getMaxStockMarket());
           if (stock!=null) {
               stockName = stock.getName();
           }
       }
       bean.setMaxStockName(stockName);
       bean.setOver(vo.getOver());
       bean.setStatus(vo.getStatus());
       bean.setSum(vo.getSum());
       bean.setTotalGain(vo.getTotalGain());
       bean.setVirtual(vo.isVirtual());
   }

}
