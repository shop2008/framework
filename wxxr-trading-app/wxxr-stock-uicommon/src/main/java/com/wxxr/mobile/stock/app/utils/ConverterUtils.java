/**
 * 
 */
package com.wxxr.mobile.stock.app.utils;

import java.util.ArrayList;
import java.util.List;

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
        if(bean.getMaxAmount()!=null && bean.getMaxAmount()>0){
            List<String> x=new ArrayList<String>();
            long max = bean.getMaxAmount()/10000/100;
            for(int i=0; i<max;i++){
                x.add(String.valueOf(i+1)+"万");
            }
            bean.setRequestamount(x);
        }
        
        dataResolution(bean);
    }
    
    public static void dataResolution(UserCreateTradAccInfoBean bean){
        String rateString = bean.getRateString();
        if(rateString==null){
            return ;
        }
        String[] data = rateString.split(",");
        if(data!=null&&data.length>0){
            int index;
            index = data[0].indexOf(";");
            bean.setRateData1(Float.parseFloat(data[0].substring(0, index))); 
            bean.setRateString1("-"+String.format("%.0f", bean.getRateData1()*100)+"%");
           bean.setDeposit1(Float.parseFloat(data[0].substring(index+1, data[0].length()))) ;
//            
            index = data[1].indexOf(";");
            bean.setRateData2(Float.parseFloat(data[1].substring(0, index))); 
            bean.setRateString2("-"+String.format("%.0f", bean.getRateData2()*100)+"%");
            bean.setDeposit2(Float.parseFloat(data[1].substring(index+1, data[1].length())));
//            
            index = data[2].indexOf(";");
            bean.setRateData3(Float.parseFloat(data[2].substring(0, index))); 
            bean.setRateString3("-"+String.format("%.0f", bean.getRateData3()*100)+"%");
          bean.setDeposit3(Float.parseFloat(data[2].substring(index+1, data[2].length())));
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
