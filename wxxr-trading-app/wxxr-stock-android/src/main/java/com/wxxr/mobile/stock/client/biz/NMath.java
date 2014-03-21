/*
 * @(#)NMath.java	 2012-6-29
 *
 * Copyright 2004-2012 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.client.biz;

import java.math.BigDecimal;

public class NMath {
	/**
	 * FIXME 提供小数位四舍五入处理。如果不是科学计算，任何商业计算都应该使用BigDecimal而不是double，因为double类型不精确。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后“最多”保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * FIXME “#”是可以为空，“0”是不够添0占位，“，”是分隔符;例如“#，##0.0#”
	 * 
	 * @param v
	 *            需要格式化的数字
	 * @param scale
	 *            小数点后“至少”保留几位
	 * @return
	 */
	public static String formatDouble(double v, int scale) {
		String temp = "0.";
		for (int i = 0; i < scale; i++) {
			temp += "0";
		}
		return new java.text.DecimalFormat(temp).format(v);
	}

	/**
	 * FIXME 小数转化为百分数,保留2位小数(有四舍五入)
	 * 
	 * @param -0.12345
	 * @return -12.35%
	 */
	public static String formatToPercent(double value) {
		Double ret = null;
		value = value * 100;
		int precision = 2;
		try {
			double factor = Math.pow(10, precision);
			ret = new Double(Math.floor(value * factor + 0.5) / factor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String tmp = String.valueOf(ret);
		if (tmp.substring(tmp.indexOf('.') + 1).length() < 2) {
			tmp = tmp + "0";
		}
		return tmp + "%";
	}
}
