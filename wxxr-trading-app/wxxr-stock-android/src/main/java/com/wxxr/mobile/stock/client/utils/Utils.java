package com.wxxr.mobile.stock.client.utils;

import java.text.SimpleDateFormat;

public class Utils {

	private static Utils instance;
	
	private Utils(){
		
	}
	
	public static Utils getInstance() {
		if(instance==null){
			instance = new Utils();
		}
		return instance;
	}
	
	public static String getDate(long data){
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		if(data>0){
			String date = sdf.format(data);
			return date;
		}
		return null;
	}
	
	public static String getTime(long data){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if(data>0){
			String teim = sdf.format(data);
			return teim;
		}
		return null;
	}
	
}
