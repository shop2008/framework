/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.db.dao.DaoSession;


/**
 * @author wangxuyang
 *
 */
public interface IDBService {	
	DaoSession newDaoSession();
	DaoSession getDaoSession();
}
