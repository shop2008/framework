package com.wxxr.mobile.stock.client.biz;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 工具箱
 * 
 * @author LiuZhongnan
 * 
 */

public class Tools
{

	/**
	 * FIXME 保留两位小数(没有逗号)(常用) 小数点前最少显示数字用("0.00")例如0.23显示0.23
	 * 可以不显示数字用("#.00")例如0.23显示 .23
	 * 
	 * @param -123456.395
	 * @return -123456.40
	 */
	private static DecimalFormat df = new DecimalFormat("0.00");

	public static String formatDouble(double value)
	{

		return df.format(NMath.round(value, 2));
	}

	public static String formatDouble3(double value)
	{
		return NMath.formatDouble(NMath.round(value, 3), 3);
		// return String.valueOf(NMath.round(value, 3));
	}

	public static String formatDouble4(double value)
	{
		return String.valueOf(NMath.round(value, 4));
	}

	/**
	 * FIXME 小数转化为百分数,保留2位小数(有四舍五入)
	 * 
	 * @param -0.12345
	 * @return -12.35%
	 */
	public static String formatToPercent(double value)
	{
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2); // 小数部分最多显示2位
		nf.setMinimumFractionDigits(2); // 小数部分最少显示2位
		return nf.format(value);
	}

	/**
	 * FIXME 数字加万、亿
	 * 
	 * @param val
	 * @return
	 */
	public static String formatNum(double value)
	{
		// 成交量纵坐标小于1万，直接显示，如9453；
		// 大于1万，加万，保留1位小数，如1140.5万；
		// 大于1亿，加亿，保留1位小数，如1.5亿。
		if (value >= 100000000000d)
		{// 大于等于1千亿
			return NMath.round(value / 100000000, 1) + "亿";// 6,666亿
		}
		else if (value >= 100000000)
		{// 大于等于1亿
			return NMath.round((value / 100000000), 1) + "亿";// 666.6亿
		}
		else if (value > 10000000 && value < 100000000)
		{// 大于1千万小于1亿
			return NMath.round(value / 10000, 1) + "万";// 6666万
		}
		else if (value >= 10000)
		{// 大于等于一万
			return NMath.round((value / 10000), 1) + "万";// 666.6万
		}
		else
		{// 小于一万
			return String.valueOf(NMath.round(value, 1));// 6,666
		}
	}
	
	
	

	/**
	 * FIXME 数字加万、亿(盘口数据专用)
	 * 
	 * @param val
	 * @return
	 */
	// public static String formatNum2(double value) {
	// // 大于1千万小于1亿的，显示万手，不保留小数。
	// // 大于等于1千亿，显示亿手，不保留小数。
	// // 其余情况保留两位小数
	// if (value >= 100000000000d) {// 大于等于1千亿
	// return NMath.round(value / 100000000, 0) + "亿";// 6,666亿
	// } else if (value > 10000000 && value < 100000000) {// 大于1千万小于1亿
	// return NMath.round(value / 10000, 0) + "万";// 6666万
	// } else {// 小于一万
	// return String.valueOf(NMath.round(value, 2));// 6,666
	// }
	// }
	/** 是否是竖屏 */
	public static boolean isPortrait(Context c)
	{
		if (c.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public static String getdatebynumber(Date date, int num)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, num);
		return dateToString(c.getTime());
	}

	/** 将Date转化成String2012-06-24 */
	public static String dateToString(Date date)
	{
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
		return d.format(date);
	}

	/** 获得今天的日期2012-06-24 */
	public static String getToday()
	{
		Calendar c = Calendar.getInstance();
		return dateToString(c.getTime());
	}

	/** 获得日期 */
	public static Date getDate()
	{
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

	/**
	 * 得到年和月
	 * @param date
	 * @return
	 */
	public static String getYM(String strDate){
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		SimpleDateFormat format = null;
		try {
			date = d.parse(strDate);
			format = new SimpleDateFormat("yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format.format(date);
	}
	
	/**
	 * 得到时和分
	 * @param context
	 * @return
	 */
	
	public static String getHM(String strDate){
		SimpleDateFormat d = new SimpleDateFormat("HHmmss");
		SimpleDateFormat format = null;
		Date date = null;
		try {
			 date = d.parse(strDate);
			 format = new SimpleDateFormat("HH:mm");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format.format(date);
	}
	
	public static boolean isWifi(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// State mobile =
		// manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		// 如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
		// if(mobile == State.CONNECTED||mobile==State.CONNECTING)
		// return;
		if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			return true;
		else
			return false;

	}

	public static void showToast(Context c, String str)
	{
		Toast toast = Toast.makeText(c, str, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.TOP, 0, 220);
		toast.show();
	}

	/**
	 * 弹出时间稍长的吐司
	 * 
	 * @param c
	 * @param str
	 */
	public static void showToastLong(Context c, String str)
	{

		Toast.makeText(c, str, Toast.LENGTH_LONG).show();
	}

	public static boolean hasNetwork(Context context)
	{
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networInfo = conManager.getActiveNetworkInfo(); // 在取得相關資訊
		if (networInfo == null || !networInfo.isAvailable())
		{
			return false;
		}
		return true;
	}

	/**
	 * FIXME 让列表长度最大化以适应滚动
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			// pre-condition
			return;
		}
		// 得到所有条目的高度
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		// 得到布局文件 的参数
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		// 总条目的高度+分隔线的高度
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 将textview中的字符全角化。
	 */

	public static String ToDBC(String input)
	{
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 12288)
			{
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 自动刷新只需在上午9：10-11：35 下午12：55-15：05起作用
	 */

	public static boolean isTime()
	{
		// 9:10
		Calendar c_9_10 = Calendar.getInstance();
		c_9_10.set(Calendar.HOUR_OF_DAY, 9);
		c_9_10.set(Calendar.MINUTE, 10);
		c_9_10.set(Calendar.SECOND, 0);
		c_9_10.set(Calendar.MILLISECOND, 0);
		long t_9_10 = c_9_10.getTimeInMillis();
		// 11:35
		Calendar c_11_35 = Calendar.getInstance();
		c_11_35.set(Calendar.HOUR_OF_DAY, 11);
		c_11_35.set(Calendar.MINUTE, 35);
		c_11_35.set(Calendar.SECOND, 59);
		c_11_35.set(Calendar.MILLISECOND, 999);
		long t_11_35 = c_11_35.getTimeInMillis();
		// 12:55
		Calendar c_12_55 = Calendar.getInstance();
		c_12_55.set(Calendar.HOUR_OF_DAY, 12);
		c_12_55.set(Calendar.MINUTE, 55);
		c_12_55.set(Calendar.SECOND, 0);
		c_12_55.set(Calendar.MILLISECOND, 0);
		long t_12_55 = c_12_55.getTimeInMillis();
		// 15:05
		Calendar c_15_5 = Calendar.getInstance();
		c_15_5.set(Calendar.HOUR_OF_DAY, 15);
		c_15_5.set(Calendar.MINUTE, 5);
		c_15_5.set(Calendar.SECOND, 59);
		c_15_5.set(Calendar.MILLISECOND, 999);
		long t_15_5 = c_15_5.getTimeInMillis();

		long currTime = System.currentTimeMillis();
		boolean b = false;

		if ((currTime < t_11_35 && currTime > t_9_10) || (currTime < t_15_5 && currTime > t_12_55))
		{
			b = true;
		}
		return b;
	}
	
	/**
	 * 9：15无成交明细
	 * @param context
	 * @return
	 */
	public static boolean isNT(){
		Calendar c_9_15 = Calendar.getInstance();
		c_9_15.set(Calendar.HOUR_OF_DAY, 9);
		c_9_15.set(Calendar.MINUTE, 15);
		c_9_15.set(Calendar.SECOND, 00);
		c_9_15.set(Calendar.MILLISECOND, 000);
		long t_9_15 = c_9_15.getTimeInMillis();
		long currTime = System.currentTimeMillis();
		
		boolean bool = false;
		if(currTime < t_9_15)
			bool = true;
		return bool;
	}
	
	public static boolean isKaiPanTime(){
		Calendar c_9_15 = Calendar.getInstance();
		c_9_15.set(Calendar.HOUR_OF_DAY, 9);
		c_9_15.set(Calendar.MINUTE, 30);
		c_9_15.set(Calendar.SECOND, 00);
		c_9_15.set(Calendar.MILLISECOND, 000);
		long t_9_15 = c_9_15.getTimeInMillis();
		long currTime = System.currentTimeMillis();
		
		boolean bool = false;
		if(currTime > t_9_15)
			bool = true;
		return bool;
	}
	
	

	public static String getAndroidid(Context context)
	{
		String androidid  = null;
		if (androidid == null)
		{
			String android_id = Settings.Secure.getString(context.getContentResolver(), "android_id");
			if (android_id == null || isEmulator())
			{
				android_id = md5("emulator");
			}
			else
			{
				android_id = md5(android_id);
			}
			if (android_id == null)
			{
				return null;
			}
			androidid = android_id.toUpperCase();
		}
		return androidid;
	}

	public static boolean isEmulator()
	{
		return ("unknown".equals(Build.BOARD)) && ("generic".equals(Build.DEVICE)) && ("generic".equals(Build.BRAND));
	}

	public static String md5(String s)
	{
		if ((s != null) && (s.length() > 0))
		{
			try
			{
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(s.getBytes(), 0, s.length());
				return String.format("%032X", new BigInteger(1, md5.digest()));
			}
			catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
			{
				return s.substring(0, 32);
			}
		}
		return s;
	}
	
	public static void printLog(String title,String content)
	{
		Log.e("--"+title+"--", content);
		
	}
	
}
