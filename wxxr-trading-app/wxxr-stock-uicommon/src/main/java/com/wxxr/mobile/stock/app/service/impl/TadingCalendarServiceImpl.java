package com.wxxr.mobile.stock.app.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.TradingTimeInvalidateException;
import com.wxxr.mobile.stock.app.service.ITadingCalendarService;


public class TadingCalendarServiceImpl extends AbstractModule<IStockAppContext> implements ITadingCalendarService {
    private TadingtimeStrategy t;
    public class TimePair{
        private String startDate;
        private String endDate;
        
        public TimePair(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
        public String getStartDate() {
            return startDate;
        }
        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
        public String getEndDate() {
            return endDate;
        }
        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    } 
    public class TadingtimeStrategy{
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        private String market;
        private List<TimePair> times=new ArrayList<TimePair>();
        
        public TadingtimeStrategy(String market) {
            this.market = market;
        }
        public String getMarket() {
            return market;
        }
        public void setMarket(String market) {
            this.market = market;
        }        
        public List<TimePair> getTimes() {
            return times;
        }
        public void setTimes(List<TimePair> times) {
            this.times = times;
        }
        public void add(TimePair t){
            times.add(t);
        }
        private boolean isTradingTime(Date current){
            String currentDate = sdf.format(current);
            Date currDate;
            try {
                currDate = sdf.parse(currentDate);
                for (TimePair t: times){
                    Date startDate = sdf.parse(t.getStartDate());
                    Date endDate = sdf.parse(t.getEndDate());
                    if (currDate.before(startDate) || currDate.after(endDate)){
                        throw new TradingTimeInvalidateException("不在时间"+startDate+"-"+endDate+"范围内");
//                        return false;
                    }
                }
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
    
    
    @Override
    public boolean isTradingTime(String market) {
        TadingtimeStrategy tadingtimeStrategy = getTadingtimeStrategy(market);
        return tadingtimeStrategy.isTradingTime(new Date());
    }

    protected TadingtimeStrategy getTadingtimeStrategy(String market) {
        //暂时不区分SH，SZ
        return t;
    }

    @Override
    protected void initServiceDependency() {
        
    }

    @Override
    protected void startService() {
        context.registerService(ITadingCalendarService.class, this);
        
        t=new TadingtimeStrategy("SH");
        t.add(new TimePair("9:30","11:30"));
        t.add(new TimePair("13:00","14:55"));
    }

    @Override
    protected void stopService() {
        t=null;
        context.unregisterService(ITadingCalendarService.class, this);
       
    }

}
