/*

Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)     
                                                                           
This file is part of greenDAO Generator.                                   
                                                                           
greenDAO Generator is free software: you can redistribute it and/or modify 
it under the terms of the GNU General Public License as published by       
the Free Software Foundation, either version 3 of the License, or          
(at your option) any later version.                                        
greenDAO Generator is distributed in the hope that it will be useful,      
but WITHOUT ANY WARRANTY; without even the implied warranty of             
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              
GNU General Public License for more details.                               
                                                                           
You should have received a copy of the GNU General Public License          
along with greenDAO Generator.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.wxxr.mobile.stock.app.db.dao;


import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.dao.AbstractDao;
import com.wxxr.mobile.dao.Property;
import com.wxxr.mobile.dao.internal.SqlUtils;
import com.wxxr.mobile.stock.app.db.dao.DaoSession;
import com.wxxr.mobile.dao.internal.DaoConfig;
import com.wxxr.mobile.dao.query.Query;
import com.wxxr.mobile.dao.query.QueryBuilder;

import com.wxxr.mobile.stock.app.db.StockInfo;


/** 
 * DAO for table stock_info.
*/
public class StockInfoDao extends AbstractDao<StockInfo, Long> {
	private static final Trace log = Trace.register(StockInfoDao.class);
    public static final String TABLENAME = "stock_info";

    /**
     * Properties of entity StockInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
				public final static Property id = new Property(0, Long.class, "id", true, "_id");
				public final static Property code = new Property(1, String.class, "code", false, "CODE");
				public final static Property name = new Property(2, String.class, "name", false, "NAME");
				public final static Property mc = new Property(3, String.class, "mc", false, "MC");
				public final static Property abbr = new Property(4, String.class, "abbr", false, "ABBR");
				public final static Property type = new Property(5, Integer.class, "type", false, "TYPE");
				public final static Property capital = new Property(6, Long.class, "capital", false, "CAPITAL");
				public final static Property marketCapital = new Property(7, Long.class, "marketCapital", false, "MARKET_CAPITAL");
				public final static Property eps = new Property(8, Long.class, "eps", false, "EPS");
				public final static Property eps_report_date = new Property(9, java.util.Date.class, "eps_report_date", false, "EPS_REPORT_DATE");
				public final static Property corpCode = new Property(10, String.class, "corpCode", false, "CORP_CODE");
		};
    private DaoSession daoSession;	

    public StockInfoDao(DaoConfig config) {
        super(config);
    }
    
    public StockInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }


    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'stock_info' (" + //
				                "'_id' INTEGER  PRIMARY KEY  ," +  // 0: id
				                "'CODE' TEXT  NOT NULL UNIQUE  ," +  // 1: code
				                "'NAME' TEXT  NOT NULL UNIQUE  ," +  // 2: name
				                "'MC' TEXT  NOT NULL  ," +  // 3: mc
				                "'ABBR' TEXT  NOT NULL  ," +  // 4: abbr
				                "'TYPE' INTEGER  ," +  // 5: type
				                "'CAPITAL' INTEGER  ," +  // 6: capital
				                "'MARKET_CAPITAL' INTEGER  ," +  // 7: marketCapital
				                "'EPS' INTEGER  ," +  // 8: eps
				                "'EPS_REPORT_DATE' INTEGER  ," +  // 9: eps_report_date
				                "'CORP_CODE' TEXT   );");  // 10: corpCode
						upgradeTable(db, ifNotExists);
    }

	private static void upgradeTable(SQLiteDatabase db, boolean ifNotExists) {
       Cursor cursor = db.rawQuery("select sql from sqlite_master sm where sm.type='table' and sm.tbl_name='stock_info';",null);
       if (cursor.getCount()<=0) {
    	   System.out.println("no result");
		   return ;
	   }
       String sql = cursor.moveToFirst()?cursor.getString(0):null;
       cursor.close();
       Map<String,String> map = new HashMap<String, String>();
	          map.put("_id", "INTEGER");
              map.put("CODE", "TEXT");
              map.put("NAME", "TEXT");
              map.put("MC", "TEXT");
              map.put("ABBR", "TEXT");
              map.put("TYPE", "INTEGER");
              map.put("CAPITAL", "INTEGER");
              map.put("MARKET_CAPITAL", "INTEGER");
              map.put("EPS", "INTEGER");
              map.put("EPS_REPORT_DATE", "INTEGER");
              map.put("CORP_CODE", "TEXT");
              for (Entry<String,String> entry : map.entrySet()) {
		 if (!sql.contains(entry.getKey())) {
			db.execSQL("alter table "+TABLENAME+" add "+entry.getKey()+" "+entry.getValue()+";");
			log.info(String.format("Added column [%s] on Table[%s].", entry.getKey(),TABLENAME));
		}
       }
      
    }
    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'stock_info'";
        db.execSQL(sql);
    }

	
	    /** @inheritdoc */
    @Override
    public Long getKey(StockInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }
	
	 /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
				return false;
	    }
	  /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, StockInfo entity, int offset) {
       entity.setId(cursor.isNull(offset + 0) ? null:   cursor.getLong(offset + 0));
	          entity.setCode(   cursor.getString(offset + 1));
	          entity.setName(   cursor.getString(offset + 2));
	          entity.setMc(   cursor.getString(offset + 3));
	          entity.setAbbr(   cursor.getString(offset + 4));
	          entity.setType(cursor.isNull(offset + 5) ? null:   cursor.getInt(offset + 5));
	          entity.setCapital(cursor.isNull(offset + 6) ? null:   cursor.getLong(offset + 6));
	          entity.setMarketCapital(cursor.isNull(offset + 7) ? null:   cursor.getLong(offset + 7));
	          entity.setEps(cursor.isNull(offset + 8) ? null:   cursor.getLong(offset + 8));
	          entity.setEps_report_date(cursor.isNull(offset + 9) ? null:  new java.util.Date( cursor.getLong(offset + 9)));
	          entity.setCorpCode(cursor.isNull(offset + 10) ? null:   cursor.getString(offset + 10));
	        }	

	   /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(StockInfo entity, long rowId) {
			        entity.setId(rowId);        return rowId;
	    }
    @Override
    public StockInfo readEntity(Cursor cursor, int offset) {
        StockInfo entity = new StockInfo();
        readEntity(cursor, entity, offset);
        return entity;
    }
	 
  
	
    @Override
    protected void bindValues(SQLiteStatement stmt, StockInfo entity) {
        stmt.clearBindings();
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
	        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }
	        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
	        String mc = entity.getMc();
        if (mc != null) {
            stmt.bindString(4, mc);
        }
	        String abbr = entity.getAbbr();
        if (abbr != null) {
            stmt.bindString(5, abbr);
        }
	        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(6, type);
        }
	        Long capital = entity.getCapital();
        if (capital != null) {
            stmt.bindLong(7, capital);
        }
	        Long marketCapital = entity.getMarketCapital();
        if (marketCapital != null) {
            stmt.bindLong(8, marketCapital);
        }
	        Long eps = entity.getEps();
        if (eps != null) {
            stmt.bindLong(9, eps);
        }
	        java.util.Date eps_report_date = entity.getEps_report_date();
        if (eps_report_date != null) {
            stmt.bindLong(10, eps_report_date.getTime());
        }
	        String corpCode = entity.getCorpCode();
        if (corpCode != null) {
            stmt.bindString(11, corpCode);
        }
	    }
	
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null :  cursor.getLong(offset + 0)	;
    }  
}