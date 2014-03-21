package com.wxxr.mobile.stock.client.biz;

import java.io.Serializable;
import java.util.ArrayList;

public class MinuteLine implements Serializable, Cloneable {
	public String code;// 股票代码
	public String name;// 股票名称
	public String type;// 类型(指数、A股、B股)
	public String market;// 市场代码(SZ、SH)
	public String acronym;// 拼音首字母	
	public String datetime;// 时间
	public double close;// 昨收价
	public ArrayList<Perminute> perminutes;// 每分钟数据
}
