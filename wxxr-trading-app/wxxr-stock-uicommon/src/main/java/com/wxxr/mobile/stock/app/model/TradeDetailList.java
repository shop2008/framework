package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailBean;
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradeDetailListBean")
public class TradeDetailList {

	private List<TradeDetailBean> tradeDetails;
}
