/**
 * 
 */
package com.wxxr.trading.core.storage.trading;

import java.util.List;
import java.util.Map;

import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.model.TradingStatus;
import com.wxxr.trading.core.storage.account.persistence.IAssetAccountDAO;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.trading.bean.TradingInfo;
import com.wxxr.trading.core.storage.trading.bean.TradingPartyInfo;
import com.wxxr.trading.core.storage.trading.persistence.ITradingInfoDAO;
import com.wxxr.trading.core.storage.trading.persistence.ITradingPartyDAO;
import com.wxxr.trading.core.storage.tradingcode.persistence.ITradingCodeDAO;

/**
 * @author neillin
 *
 */
public class TradingObjectStorageImpl extends AbstractBizObjectStorage<Long,TradingObject,TradingInfo>
		implements ITradingObjectStorage {

	private ITradingInfoDAO dao;
	private ITradingPartyDAO partyDAO;
	private IAssetAccountDAO acctDAO;
	private ITradingCodeDAO tCodeDAO;
	
	private IDataAccessObject<Long,TradingInfo> baseDAO = new IDataAccessObject<Long,TradingInfo>() {
		
		@Override
		public void update(TradingInfo object) {
			getTradingInfoDAO().update(object);
		}
		
		@Override
		public void remove(TradingInfo object) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public String getType(TradingInfo object) {
			return object.getType();
		}
		
		@Override
		public String[] getQueryNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public TradingInfo findByPrimaryKey(Long key) {
			return getTradingInfoDAO().findByPrimaryKey(key);
		}
		
		@Override
		public List<TradingInfo> doQuery(String name, Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long add(TradingInfo object) {
			return getTradingInfoDAO().add(object);
		}
	};
	
	protected ITradingCodeDAO getTradingCodeDAO() {
		if(this.tCodeDAO == null){
			this.tCodeDAO = DAOFactory.getDAObject(ITradingCodeDAO.class);
		}
		return this.tCodeDAO;
	}

	protected ITradingInfoDAO getTradingInfoDAO() {
		if(this.dao == null){
			this.dao = DAOFactory.getDAObject(ITradingInfoDAO.class);
		}
		return this.dao;
	}
	
	protected ITradingPartyDAO getTradingPartyDAO() {
		if(this.partyDAO == null){
			this.partyDAO = DAOFactory.getDAObject(ITradingPartyDAO.class);
		}
		return this.partyDAO;
	}

	protected IAssetAccountDAO getAccountDAO() {
		if(this.acctDAO == null){
			this.acctDAO = DAOFactory.getDAObject(IAssetAccountDAO.class);
		}
		return this.acctDAO;
	}



	@Override
	protected IDataAccessObject<Long,TradingInfo> getBaseDAO() {
		return this.baseDAO;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.impl.AbstractBizObjectStorage#saveOrUpdate(com.wxxr.trading.core.storage.api.InheritableBizObject)
	 */
	@Override
	public <E extends TradingObject> Long saveOrUpdate(E object) {
		boolean isNew = object.getId() == null;
		Long key = super.saveOrUpdate(object);
		if(isNew){
			Long[] parties = object.getTradingParties();
			if((parties != null)&&(parties.length > 0)){
				for (Long pid : parties) {
					TradingPartyInfo pInfo = new TradingPartyInfo();
					pInfo.setTrading(getTradingInfoDAO().findByPrimaryKey(key));
					pInfo.setAccount(getAccountDAO().findByPrimaryKey(pid));
					getTradingPartyDAO().add(pInfo);
				}
			}
		}
		return key;
	}
	
	protected boolean isSame(Object obj1, Object obj2){
		if((obj1 == null)&&(obj2 == null)){
			return true;
		}
		
		if((obj1 == null)||(obj2 == null)){
			return false;
		}

		return obj1.equals(obj2);
	}

	@Override
	protected boolean updateBaseDO(TradingInfo baseDO, TradingObject bizObject) {
		boolean updated = false;
		TradingStatus status = baseDO.getStatus();
		String subStatus = baseDO.getSubStatus();
		if(!isSame(status, bizObject.getStatus())){
			baseDO.setStatus(bizObject.getStatus());
			updated = true;
		}
		if(!isSame(subStatus, bizObject.getSubStatus())){
			baseDO.setSubStatus(bizObject.getSubStatus());
			updated = true;
		}
		return updated;
	}

	@Override
	protected void updateBO(TradingObject bizObject, TradingInfo baseDO) {
		bizObject.setId(baseDO.getId());
		bizObject.setBroker(baseDO.getBroker());
		bizObject.setOwnerId(baseDO.getOwnerId());
		bizObject.setStatus(baseDO.getStatus());
		bizObject.setSubStatus(baseDO.getSubStatus());
		bizObject.setTradingCode(baseDO.getTradingCode().getId());
		bizObject.setType(baseDO.getTradingCode().getCode());
		List<TradingPartyInfo> parties = baseDO.getTradingParties();
		if((parties != null)&&(parties.size() > 0)){
			int size = parties.size();
			Long[] keys = new Long[size];
			for(int i=0 ; i < size ; i++){
				keys[i] = parties.get(i).getId();
			}
			bizObject.setTradingParties(keys);
		}
	}

	@Override
	protected TradingInfo createBaseDO(TradingObject bizObject) {
		TradingInfo info = new TradingInfo();
		info.setBroker(bizObject.getBroker());
		info.setId(bizObject.getId());
		info.setOwnerId(bizObject.getOwnerId());
		info.setStatus(bizObject.getStatus());
		info.setSubStatus(bizObject.getSubStatus());
		info.setTradingCode(getTradingCodeDAO().findByPrimaryKey(bizObject.getTradingCode()));
		return info;
	}

	@Override
	protected String getStorageName() {
		return "Trading Object Storage";
	}

}
