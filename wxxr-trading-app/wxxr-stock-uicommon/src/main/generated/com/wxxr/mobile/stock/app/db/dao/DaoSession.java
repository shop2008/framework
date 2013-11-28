package com.wxxr.mobile.stock.app.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import com.wxxr.mobile.dao.AbstractDao;
import com.wxxr.mobile.dao.AbstractDaoSession;
import com.wxxr.mobile.dao.identityscope.IdentityScopeType;
import com.wxxr.mobile.dao.internal.DaoConfig;

import com.wxxr.mobile.stock.app.db.StockInfo;

import com.wxxr.mobile.stock.app.db.dao.StockInfoDao;


/**
 * {@inheritDoc}
 * 
 * @see com.wxxr.mobile.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig stockInfoDaoConfig;


    private final StockInfoDao stockInfoDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);
    stockInfoDaoConfig = daoConfigMap.get(StockInfoDao.class).clone();
	stockInfoDaoConfig.initIdentityScope(type);
   
        stockInfoDao = new StockInfoDao(stockInfoDaoConfig, this);

		registerDao(StockInfo.class,stockInfoDao);
     
    }
    
    public void clear() {
        stockInfoDaoConfig.getIdentityScope().clear();
    }

    public StockInfoDao getStockInfoDao() {
        return stockInfoDao;
    }

}
