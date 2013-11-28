/**
 * 
 */
package com.wxxr.mobile.stock.sync.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

/**
 * @author wangxuyang
 *
 */
public class StockBaseInfo implements Externalizable{
	private String name; //股票或指数 名称
	private String mc;//市场代码： SH，SZ各代表上海，深圳。
	private String abbr;// 股票名称的中文拼音的首字母 如：“新大陆”  为 “xdl”
	private String code;  //股票或指数 代码
	private Long capital;// 总股本
	private Long marketCapital;// 流通盘
	private int type;
	private Long eps;//
	private Date eps_report_date;//报告时间
	private String corpCode;// 上市公司代码 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMc() {
		return mc;
	}
	public void setMc(String mc) {
		this.mc = mc;
	}
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getCapital() {
		return capital;
	}
	public void setCapital(Long capital) {
		this.capital = capital;
	}
	public Long getMarketCapital() {
		return marketCapital;
	}
	public void setMarketCapital(Long marketCapital) {
		this.marketCapital = marketCapital;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getEps() {
		return eps;
	}
	public void setEps(Long eps) {
		this.eps = eps;
	}
	public Date getEps_report_date() {
		return eps_report_date;
	}
	public void setEps_report_date(Date eps_report_date) {
		this.eps_report_date = eps_report_date;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	@Override
	public String toString() {
		return "StockBaseInfo [name=" + name + ", mc=" + mc + ", abbr=" + abbr
				+ ", code=" + code + ", capital=" + capital
				+ ", marketCapital=" + marketCapital + ", type=" + type
				+ ", eps=" + eps + ", eps_report_date=" + eps_report_date
				+ ", corpCode=" + corpCode + "]";
	}
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		 	out.writeUTF(this.code);
		 	out.writeUTF(this.name);
		 	out.writeUTF(this.abbr);
		 	out.writeUTF(this.mc);
		 	if (this.corpCode!=null) {
		 		out.write(1);
		 		out.writeUTF(this.corpCode);
			}else{
				out.write(0);
			}
		 	if (this.capital!=null) {
		 		out.write(1);
		 		out.writeLong(this.capital);
			}else{
				out.write(0);
			}
		 	if (this.eps!=null) {
		 		out.write(1);
		 		out.writeLong(this.eps);
			}else{
				out.write(0);
			}
	        if (this.eps_report_date!=null) {
	        	out.write(1);
	        	out.writeLong(this.eps_report_date.getTime());
			}else{
				out.write(0);
			}
	        if (this.marketCapital!=null) {
		 		out.write(1);
		 		out.writeLong(this.marketCapital);
			}else{
				out.write(0);
			}
	        out.writeInt(this.type);
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.code = in.readUTF();
		this.name = in.readUTF();
		this.abbr = in.readUTF();
		this.mc = in.readUTF();
		if (in.read()!=0) {
			this.corpCode = in.readUTF();
		}
		if (in.read()!=0) {
			this.capital = in.readLong();
		}
		if (in.read()!=0) {
			this.eps = in.readLong();
		}
        if (in.read()!=0) {
        	this.eps_report_date = new Date(in.readLong());
		}
        if (in.read()!=0) {
        	this.marketCapital = in.readLong();
		}
        this.type = in.readInt();
	}
}
