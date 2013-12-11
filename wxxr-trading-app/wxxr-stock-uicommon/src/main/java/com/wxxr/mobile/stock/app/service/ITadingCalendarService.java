package com.wxxr.mobile.stock.app.service;


public interface ITadingCalendarService {
    //判断当前是否正常交易时间
    boolean isTradingTime(String market);
}
