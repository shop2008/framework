package com.wxxr.mobile.stock.client.utils;

import java.math.BigDecimal;
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
    
    /**
	 * FIXME 提供小数位四舍五入处理。如果不是科学计算，任何商业计算都应该使用BigDecimal而不是double，因为double类型不精确。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后“最多”保留几位
	 * @return 四舍五入后的结果
	 */
	public static float roundHalfUp(float v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Float.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	  /**
		 * FIXME 提供小数位四舍五入处理。如果不是科学计算，任何商业计算都应该使用BigDecimal而不是double，因为double类型不精确。
		 * 
		 * @param v
		 *            需要四舍五入的数字
		 * @param scale
		 *            小数点后“最多”保留几位
		 * @return 四舍五入后的结果
		 */
	public static float roundUp(float v, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b = new BigDecimal(Float.toString(v));
			BigDecimal one = new BigDecimal("1");
			return b.divide(one, scale, BigDecimal.ROUND_UP).floatValue();
		}
	
	public static String formatNumber(Float val,int n){
		String data = null;
		if(val > 10000000000.0){
			if(n==1){
				data = String.format("%.1f亿", val/10000000000.0);
			}
			if(n==2){
				data = String.format("%.1f亿", val/10000000000.0/2);
			}
		}else if(val > 1000000.0){
			if(n==1){
				data = String.format("%.1f万", val/1000000.0);
			}
			if(n==2){
				data = String.format("%.1f万", val/1000000.0/2);
			}
		}else{
			if(n==1){
				data = String.format("%.0f", val/100);
			}
			if(n==2){
				data = String.format("%.0f", val/200);
			}
		}
		return data;
	}	
}
