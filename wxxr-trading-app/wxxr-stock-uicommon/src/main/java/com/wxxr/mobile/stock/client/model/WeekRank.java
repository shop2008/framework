package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="WeekRankBean")
public class WeekRank{
	private String nickName;//昵称
	private int gainCount;//正收益个数1
	private Long totalGain;//总盈亏3
	private String gainRate;//总盈亏率2
	private int gainRates;
	private String dates;//周期时间
	private String userId;//用户id
	private int rankSeq;//
}
