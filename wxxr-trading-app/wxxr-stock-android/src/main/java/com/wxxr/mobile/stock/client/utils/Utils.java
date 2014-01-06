package com.wxxr.mobile.stock.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

	private static Utils instance;

	private Utils() {

	}

	public static Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		return instance;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
		return sdf.format(new Date());
	}

	public static String getCurrentTime(String format) {
		SimpleDateFormat sdf;
		try {
			sdf = new SimpleDateFormat(format);
		} catch (NullPointerException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
		return sdf.format(new Date());
	}

	public static String getDate(long data) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
		if (data > 0) {
			String date = sdf.format(data);
			return date;
		}
		return null;
	}

	public static String getTime(long data) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (data > 0) {
			String teim = sdf.format(data);
			return teim;
		}
		return null;
	}

	public static String parseFloat(Float f) {

		if (f > 0) {
			return "+" + String.valueOf(f);
		} else {
			return "-" + String.valueOf(f);
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
		if (view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);

		return location;
	}

	public static int[] getViewRBPoint(View view) {
		if (view == null)
			return null;
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		location[0] += view.getWidth();
		location[1] += view.getHeight();

		return location;
	}

	public static int[] getViewCentrePoint(View view) {
		if (view == null)
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
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
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

	public static String formatNumber(Float val, int n) {
		String data = null;
		if (val > 10000000000.0) {
			if (n == 1) {
				data = String.format("%.1f亿", val / 10000000000.0);
			}
			if (n == 2) {
				data = String.format("%.1f亿", val / 10000000000.0 / 2);
			}
		} else if (val > 1000000.0) {
			if (n == 1) {
				data = String.format("%.1f万", val / 1000000.0);
			}
			if (n == 2) {
				data = String.format("%.1f万", val / 1000000.0 / 2);
			}
		} else {
			if (n == 1) {
				data = String.format("%.0f", val / 100);
			}
			if (n == 2) {
				data = String.format("%.0f", val / 200);
			}
		}
		return data;
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
	public String formatDouble(float v, int scale) {
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
	public String formatToPercent(float value) {
		Float ret = null;
		value = value * 100;
		int precision = 2;
		try {
			double factor = Math.pow(10, precision);
			ret = new Float(Math.floor(value * factor + 0.5) / factor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String tmp = String.valueOf(ret);
		if (tmp.substring(tmp.indexOf('.') + 1).length() < 2) {
			tmp = tmp + "0";
		}
		return tmp + "%";
	}

	/**
	 * 处理已排序的数据
	 * 
	 * @param data
	 *            要处理的数据
	 * @param method
	 *            方法的名称，这里是标签数据在相应Bean里的getter方法名
	 * @return 分组后的数据
	 */
	public static Object[] getSortedGroupData(List<Object> data, String method) {

		if (data == null) {
			return null;
		}

		if (data.size() <= 0) {
			return data.toArray();
		}
		List<Object> retList = new ArrayList<Object>();
		Object curLabelObj = null;
		for (int i = 0; i < data.size(); i++) {
			Object obj = data.get(i);
			try {
				Method m = obj.getClass().getMethod(method, null);
				m.setAccessible(true);
				Object labelObj = m.invoke(obj, null);

				if (i == 0) {
					curLabelObj = labelObj;
					if (obj instanceof RemindMessageBean)
						retList.add(remindStringTimeFormat(curLabelObj
								.toString()));
					else if (obj instanceof PullMessageBean) {
						retList.add(pullStringTimeFormat(curLabelObj.toString()));
					}
					retList.add(obj);
				} else {
					if (obj instanceof RemindMessageBean) {
						if (remindStringTimeFormat(labelObj.toString()).equals(
								remindStringTimeFormat(curLabelObj.toString()))) {
							retList.add(obj);
						} else {
							curLabelObj = labelObj;

							retList.add(remindStringTimeFormat(curLabelObj
									.toString()));

							retList.add(obj);
						}
					}

					if (obj instanceof PullMessageBean) {
						if (pullStringTimeFormat(labelObj.toString()).equals(
								pullStringTimeFormat(curLabelObj.toString()))) {
							retList.add(obj);
						} else {
							curLabelObj = labelObj;
							retList.add(pullStringTimeFormat(curLabelObj
									.toString()));
							retList.add(obj);
						}
					}
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return retList.toArray();
	}

	private static String pullStringTimeFormat(String time)
			throws NumberFormatException {
		Long lTime = Long.parseLong(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date(lTime);
		return formatter.format(date);
	}

	private static String remindStringTimeFormat(String time)
			throws ParseException {
		long lTime = stringTime2Long(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(lTime);
		return formatter.format(date);
	}

	/**
	 * 处理未排序的数据--按时间排序
	 * 
	 * @param data
	 *            要处理的数据
	 * @param method
	 *            方法的名称，这里是标签数据在相应Bean里的getter方法名
	 * @return 分组后的数据
	 */
	public static Object[] getUnSortedGroupData(List<Object> data,
			final String method) {

		if (data == null || data.size() <= 0) {
			return null;
		}

		Collections.sort(data, new Comparator<Object>() {

			@Override
			public int compare(Object lhs, Object rhs) {
				try {
					Method lMethod = lhs.getClass().getMethod(method, null);
					Method rMethod = rhs.getClass().getMethod(method, null);
					Object lValue = lMethod.invoke(lhs, null);
					Object rValue = rMethod.invoke(rhs, null);

					if (lValue instanceof String || rValue instanceof String) {
						long lTime = stringTime2Long((String) lValue);
						long rTime = stringTime2Long((String) rValue);
						if (lTime > rTime) {
							return -1;
						} else if (lTime == rTime) {
							return 0;
						} else {
							return 1;
						}
					}

					if (lValue instanceof Long || rValue instanceof Long) {
						long ltime = (Long) lValue;
						long rtime = (Long) rValue;
						if (ltime > rtime) {
							return -1;
						} else if (ltime == rtime) {
							return 0;
						} else {
							return 1;
						}
					}

				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		List<Object> retList = new ArrayList<Object>();
		Object curLabelObj = null;
		for (int i = 0; i < data.size(); i++) {
			Object obj = data.get(i);
			try {
				Method m = obj.getClass().getMethod(method, null);
				m.setAccessible(true);
				Object labelObj = m.invoke(obj, null);

				if (i == 0) {
					curLabelObj = labelObj;
					retList.add(stringTimeFormat(curLabelObj.toString()));
					retList.add(obj);
				} else {
					if (labelObj.equals(curLabelObj)) {
						retList.add(obj);
					} else {
						curLabelObj = labelObj;
						retList.add(stringTimeFormat(curLabelObj.toString()));
						retList.add(obj);
					}
				}

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return retList.toArray();
	}

	private static Long stringTime2Long(String time) throws ParseException {
		SimpleDateFormat formatter = null;
		if (time.length() == 8) {
			formatter = new SimpleDateFormat("yyyyMMdd");
			Date date = formatter.parse(time);
			return date.getTime();
		} else {
			return 0l;
		}

	}

	private static String stringTimeFormat(String time) throws ParseException {
		long lTime = stringTime2Long(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(lTime);
		return formatter.format(date);
	}

	/**
	 * 处理已排序的数据-交易记录使用
	 * 
	 * @param data
	 *            要处理的数据
	 * @param method
	 *            方法的名称，这里是标签数据在相应Bean里的getter方法名
	 * @return 分组后的数据
	 */
	public static Object[] getSortedGroupDataLongTime(List<Object> data,
			String method) {

		if (data == null) {
			return null;
		}

		if (data.size() <= 0) {
			return data.toArray();
		}
		List<Object> retList = new ArrayList<Object>();
		Object curLabelObj = null;
		for (int i = 0; i < data.size(); i++) {
			Object obj = data.get(i);
			try {
				Method m = obj.getClass().getMethod(method, null);
				m.setAccessible(true);
				Object labelObj = m.invoke(obj, null);
				if (labelObj instanceof Long) {
					labelObj = longTimeFormat((Long) labelObj);
				}
				if (i == 0) {
					curLabelObj = labelObj;
					retList.add(curLabelObj);
					retList.add(obj);
				} else {
					if (labelObj.equals(curLabelObj)) {
						retList.add(obj);
					} else {
						curLabelObj = labelObj;
						retList.add(curLabelObj);
						retList.add(obj);
					}
				}

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return retList.toArray();
	}

	/**
	 * 处理已排序的数据-周赛排名使用
	 * 
	 * @param data
	 *            要处理的数据
	 * @param method
	 *            方法的名称，这里是标签数据在相应Bean里的getter方法名
	 * @return 分组后的数据
	 */
	public static Object[] getSortedGroupTimeData(List<Object> data,
			String method) {

		if (data == null) {
			return null;
		}

		if (data.size() <= 0) {
			return data.toArray();
		}
		List<Object> retList = new ArrayList<Object>();
		Object curLabelObj = null;
		for (int i = 0; i < data.size(); i++) {
			Object obj = data.get(i);
			try {
				Method m = obj.getClass().getMethod(method, null);
				m.setAccessible(true);
				Object labelObj = m.invoke(obj, null);

				if (i == 0) {
					curLabelObj = labelObj;
					retList.add(curLabelObj.toString());
					retList.add(obj);
				} else {
					if (labelObj.equals(curLabelObj)) {
						retList.add(obj);
					} else {
						curLabelObj = labelObj;
						retList.add(curLabelObj.toString());
						retList.add(obj);
					}
				}

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return retList.toArray();
	}

	private static String longTimeFormat(long time) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			Date date = new Date(time);
			return formatter.format(date);
		} catch (NullPointerException e) {

		} catch (IllegalArgumentException e) {

		}
		return "";
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
