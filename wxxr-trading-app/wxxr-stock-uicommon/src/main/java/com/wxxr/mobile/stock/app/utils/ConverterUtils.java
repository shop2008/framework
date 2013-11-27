/**
 * 
 */
package com.wxxr.mobile.stock.app.utils;

import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockTradingOrderVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * @author wangxuyang
 * 
 */
public class ConverterUtils {
    public static  void setfromVO(UserCreateTradAccInfoBean bean,UserCreateTradAccInfoVO vo) {
        if (vo == null || bean==null) {
            return ;
        }
        bean.setCapitalRate(vo.getCapitalRate());
        bean.setCostRate(vo.getCostRate());
        bean.setDepositRate(vo.getDepositRate());
        bean.setMaxAmount(vo.getMaxAmount());
        bean.setRateString(vo.getRateString());
        bean.setUserId(vo.getUserId());
        bean.setVoucherCostRate(vo.getVoucherCostRate());        
    }
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
		bean.setMaxStockName(vo.getMaxStockName());
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
		return bean;
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
		return bean;
	}
	
	/**
	 * @param vo
	 * @return
	 */
	public static void fromVO(GainVO vo,GainBean bean) {
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
}
