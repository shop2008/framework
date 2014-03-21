/**
 * 
 */
package com.wxxr.trading.core.storage.tradingcode;

import java.util.List;
import java.util.Map;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.tradingcode.persistence.ITradingCodeDAO;
import com.wxxr.trading.core.storage.tradingcode.persistence.bean.TradingCodeInfo;

/**
 * @author wangyan
 *
 */
@ServiceMBean
public class TradingCodeStorageImpl extends AbstractBizObjectStorage<Integer, TradingCodeObject, TradingCodeInfo> implements ITradingCodeStorage{

	
	private ITradingCodeDAO tradingCodeDAO;
	private IDataAccessObject<Integer,TradingCodeInfo> baseDao=new IDataAccessObject<Integer, TradingCodeInfo>() {

		@Override
		public TradingCodeInfo findByPrimaryKey(Integer key) {
			return getTradingCodeDAO().findByPrimaryKey(key);
		}

		@Override
		public void update(TradingCodeInfo object) {
			getTradingCodeDAO().update(object);
			
		}

		@Override
		public void remove(TradingCodeInfo object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Integer add(TradingCodeInfo object) {
			return getTradingCodeDAO().add(object);
		}

		@Override
		public String getType(TradingCodeInfo object) {
			return object.getCode();
		}

		@Override
		public String[] getQueryNames() {
			return null;
		}

		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List<TradingCodeInfo> doQuery(String name,
				Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	@Override
	protected IDataAccessObject<Integer, TradingCodeInfo> getBaseDAO() {
		return baseDao;
	}

	@Override
	protected boolean updateBaseDO(TradingCodeInfo baseDO,
			TradingCodeObject bizObject) {
		if(baseDO.getId()==null){
			baseDO.setCode(bizObject.getCode());
			baseDO.setDescription(bizObject.getDescription());
			return true;
		}
	
		return false;
	}

	@Override
	protected void updateBO(TradingCodeObject bizObject, TradingCodeInfo baseDO) {
		bizObject.setId(baseDO.getId());
		bizObject.setCode(baseDO.getCode());
		bizObject.setDescription(baseDO.getDescription());
	}

	@Override
	protected TradingCodeInfo createBaseDO(TradingCodeObject bizObject) {
		TradingCodeInfo tradingCodeInfo=new TradingCodeInfo();
		tradingCodeInfo.setCode(bizObject.getCode());
		tradingCodeInfo.setDescription(bizObject.getDescription());
		return null;
	}

	@Override
	protected String getStorageName() {
		return "Trading Code Object Storage";
	}

	/**
	 * @return the tradingCodeDAO
	 */
	public ITradingCodeDAO getTradingCodeDAO() {
		if(tradingCodeDAO==null){
			tradingCodeDAO=DAOFactory.getDAObject(ITradingCodeDAO.class);
		}
		return tradingCodeDAO;
	}

	public void start(IKernelContext ctx) {
		ctx.registerService(ITradingCodeStorage.class, this);
	}

	public void stop(IKernelContext ctx) {
		ctx.unregisterService(ITradingCodeStorage.class, this);
	}

	
}
