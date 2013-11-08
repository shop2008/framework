/**
 * 
 */
package com.wxxr.stock.restful.json;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wuzhangyue
 * 
 */
@XmlRootElement(name = "param")
public class ParamVO {
	@XmlElement(name = "code")
	private String code; // 股票代码
	@XmlElement(name = "market")
	private String market;// 市场代码
	@XmlElement(name = "start")
	private Long start;// 起始值
	@XmlElement(name = "limit")
	private Long limit;// 偏移量
	@XmlElement(name = "date")
	private String date;
	@XmlElement(name = "startTime")
	private Long startTime;// 开始时间戳，如：1329918250
	@XmlElement(name = "endTime")
	private Long endTime;// 结束时间戳
	@XmlElement(name = "page")
	private int page;// 第几页
	@XmlElement(name = "game_id")
	private String game_id;//
	@XmlElement(name = "date_type")
	private String date_type;// 指定近几日达人/红人排行,如5代表近5日(默认5)
	@XmlElement(name = "fund")
	private String fund;// false int
						// 投注的雪球币数，最少500，最多1万，以100递增。注：创建投注微计划时（vp_type:13）传入。
	@XmlElement(name = "type")
	private String type; // 类型指定创建微计划的类型。13：大赛或投注，15：策略

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getDate_type() {
		return date_type;
	}

	public void setDate_type(String date_type) {
		this.date_type = date_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
