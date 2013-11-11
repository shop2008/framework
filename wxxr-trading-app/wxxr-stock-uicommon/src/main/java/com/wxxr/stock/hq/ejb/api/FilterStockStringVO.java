package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "FILTER_STOCK")
public class FilterStockStringVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name="szIncludes")
	private String szIncludes;
	@XmlElement(name="szExcludes")
	private String szExcludes;
	@XmlElement(name="shIncludes")
	private String shIncludes;
	@XmlElement(name="shExcludes")
	private String shExcludes;

	/**
	 * @return the szIncludes
	 */
	
	public String getSzIncludes() {
		return szIncludes;
	}

	/**
	 * @param szIncludes the szIncludes to set
	 */
	public void setSzIncludes(String szIncludes) {
		this.szIncludes = szIncludes;
	}

	/**
	 * @return the szExcludes
	 */
	
	public String getSzExcludes() {
		return szExcludes;
	}

	/**
	 * @param szExcludes the szExcludes to set
	 */
	public void setSzExcludes(String szExcludes) {
		this.szExcludes = szExcludes;
	}

	/**
	 * @return the shIncludes
	 */
	
	public String getShIncludes() {
		return shIncludes;
	}

	/**
	 * @param shIncludes the shIncludes to set
	 */
	public void setShIncludes(String shIncludes) {
		this.shIncludes = shIncludes;
	}

	/**
	 * @return the shExcludes
	 */
	
	public String getShExcludes() {
		return shExcludes;
	}

	/**
	 * @param shExcludes the shExcludes to set
	 */
	public void setShExcludes(String shExcludes) {
		this.shExcludes = shExcludes;
	}

	@Override
	public String toString() {
		return "FilterStockStringVO [szIncludes=" + szIncludes + ", szExcludes=" + szExcludes + ", shIncludes=" + shIncludes + ", shExcludes=" + shExcludes
				+ "]";
	}
}
