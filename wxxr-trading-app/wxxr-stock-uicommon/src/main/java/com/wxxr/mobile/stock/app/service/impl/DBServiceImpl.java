/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import android.database.sqlite.SQLiteDatabase;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.db.DaoMaster;
import com.wxxr.mobile.stock.app.db.DaoMaster.DevOpenHelper;
import com.wxxr.mobile.stock.app.db.dao.DaoSession;
import com.wxxr.mobile.stock.app.service.IDBService;

/**
 * @author wangxuyang
 *
 */
public class DBServiceImpl extends AbstractModule<IStockAppContext> implements
		IDBService {
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private final static String DB_NAME = "wxxr-finance.db";
	@Override
	protected void initServiceDependency() {
	}
	@Override
	protected void startService() {
	    DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplication().getAndroidApplication().getApplicationContext(), DB_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        DaoMaster.createAllTables(db, true);
		context.registerService(IDBService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IDBService.class, this);
	}


	@Override
	public DaoSession newDaoSession() {
		return daoMaster.newSession();
	}


	@Override
	public DaoSession getDaoSession() {
		if (daoSession==null) {
			daoSession = newDaoSession();
		}
		return daoSession;
	}
}
