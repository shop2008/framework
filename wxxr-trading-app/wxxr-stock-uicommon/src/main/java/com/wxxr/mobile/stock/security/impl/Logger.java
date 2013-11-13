/*
 * @(#)Logger.java 2011-8-25
 * 
 * Copyright 2004-2011 WXXR Network Technology Co. Ltd. All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.security.impl;

public class Logger {
	private String classname;
	private int level = 5;

	private Logger(Class c) {
		this.classname = c.getName();
	}

	public static Logger getLogger(Class c) {
		Logger l = new Logger(c);
		return l;
	}

	public void debug(String info) {
		if (level > 4) {
			System.out.println("[" + classname + "]" + info);
		}
	}

	public void info(String info) {
		if (level > 3) {

			System.out.println("[" + classname + "]" + info);
		}
	}

	public void warning(String info) {
		if (level > 2) {
			System.err.println("[" + classname + "]" + info);
		}
	}

	public void error(String info) {
		if (level > 1) {
			System.err.println("[" + classname + "]" + info);
		}
	}
}
