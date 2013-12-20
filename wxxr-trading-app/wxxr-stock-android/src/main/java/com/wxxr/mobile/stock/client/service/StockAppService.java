package com.wxxr.mobile.stock.client.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StockAppService extends Service {

	@Override  
    public IBinder onBind(Intent intent) {
    	return null;  
    }  

    @Override  
    public void onCreate() {
    	super.onCreate();  
    }  

    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {
    	flags = START_REDELIVER_INTENT; 
    	return super.onStartCommand(intent, flags, startId);  
    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
    
}