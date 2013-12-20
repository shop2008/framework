package com.wxxr.mobile.stock.client.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class StartSystemService {

	private Context mContext;
	
	public StartSystemService(Context c) {
		this.mContext = c;
	}
	
	public void callPhone(String mobile){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ mobile));
		mContext.startActivity(intent);
	}
	
	public void dialPhone(String mobile){
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ mobile));
		mContext.startActivity(intent);
	}
	
	public void startBrowse(String url) {
		Uri uri = Uri.parse(url); 
		Intent intent  = new Intent(Intent.ACTION_VIEW,uri); 
		mContext.startActivity(intent);
	}
}
