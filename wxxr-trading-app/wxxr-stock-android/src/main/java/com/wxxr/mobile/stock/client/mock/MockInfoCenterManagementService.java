/**
 * 
 */
package com.wxxr.mobile.stock.client.mock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.service.impl.InfoCenterManagementServiceImpl;

/**
 * @author wangxuyang
 *
 */
public class MockInfoCenterManagementService extends InfoCenterManagementServiceImpl {

	@Override
	public LineListBean getDayline(String code, String market) {
		return loadLocalSettings();
	}
	
	private LineListBean loadLocalSettings() {
		InputStream in = null;
		try {
			in = context.getApplication().getAndroidApplication().getAssets().open("klines");
			BufferedReader br = null;
			ByteArrayOutputStream baos = null;
			String s = null;
			br = new BufferedReader(new InputStreamReader(in));
			baos = new ByteArrayOutputStream();
			while ((s = br.readLine()) != null) {
				baos.write(s.trim().getBytes());
			}
			return parseJson(baos.toString());
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
		}
		return null;
	}
	
	protected LineListBean parseJson(String data) {
		LineListBean lineList = new LineListBean();
		List<StockLineBean> day_list = new ArrayList<StockLineBean>();
		StockLineBean stockLine;
		try {
			JSONObject strJson = new JSONObject(data);
			if (strJson.has("list"))
			{
				strJson = strJson.getJSONObject("list");
			}
			JSONArray arrJson = strJson.getJSONArray("list");
			for(int i=0;i<arrJson.length();i++){
				stockLine = new StockLineBean();
				JSONObject o = arrJson.getJSONObject(i);
				stockLine.setClose(o.getLong("close"));
				stockLine.setDate(String.valueOf(o.getString("date")));
				stockLine.setHigh(o.getLong("high"));
				stockLine.setLow(o.getLong("low"));
				stockLine.setPrice(o.getLong("price"));
				stockLine.setSecuamount(o.getLong("secuamount"));
				stockLine.setSecuvolume(o.getLong("secuvolume"));
				stockLine.setOpen(o.getLong("open"));
				day_list.add(stockLine);
			}
			lineList.setDay_list(day_list);
			return lineList;
		} catch (JSONException e) {
		}
		return null;
	}
}
