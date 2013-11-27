package com.wxxr.mobile.stock.client.utils;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.View;

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
	
	public static String parseFloat(Float f) { 
		
		if (f > 0) {
			return "+"+String.valueOf(f);
		} else {
			return "-"+String.valueOf(f);
		}
	}
	
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int[] getViewLTPoint(View view) {
		if(view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);

		return location;
	}

	public static int[] getViewRBPoint(View view) {
		if(view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		location[0] += view.getWidth();
		location[1] += view.getHeight();

		return location;
	}

	public static int[] getViewCentrePoint(View view) {
		if(view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		location[0] += view.getWidth() / 2;
		location[1] += view.getHeight() / 2;

		return location;
	}
	
	/** 
     * 获取状态栏高度 
     *  
     * @return 
     */  
    public static int getStatusBarHeight(Context context)  
    {  
        Class<?> c = null;  
        Object obj = null;  
        java.lang.reflect.Field field = null;  
        int x = 0;  
        int statusBarHeight = 0;  
        try  
        {  
            c = Class.forName("com.android.internal.R$dimen");  
            obj = c.newInstance();  
            field = c.getField("status_bar_height");  
            x = Integer.parseInt(field.get(obj).toString());  
            statusBarHeight = context.getResources().getDimensionPixelSize(x);  
            return statusBarHeight;  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        return statusBarHeight;  
    }  
}
