/**
 * 
 */
package com.wxxr.trading.core.storage.common;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hygensoft.common.util.Trace;
import com.wxxr.smbiz.bizobject.INumberKeyObject;
import com.wxxr.stock.common.service.AbstractModule;
import com.wxxr.trading.core.storage.api.IBizObjectConverter;
import com.wxxr.trading.core.storage.api.IBizObjectStorage;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.api.IExtStrategy;
import com.wxxr.trading.core.storage.api.InheritableBizObject;
import com.wxxr.trading.core.storage.api.InvalidDataTypeException;

/**
 * 
 * BB : base BO type, BD : base DO type
 * @author neillin
 *
 */
public abstract class AbstractBizObjectStorage<K extends Number,BB extends InheritableBizObject<K>,BD extends INumberKeyObject<K>> extends AbstractModule implements IBizObjectStorage<K,BB,BD>{
	private static final Trace log = Trace.register(AbstractBizObjectStorage.class);
	
	private Map<String, IExtStrategy<K,BB, ? extends BB, BD,? extends INumberKeyObject<K>>> extStrategies = new HashMap<String, IExtStrategy<K,BB,? extends BB, BD,? extends INumberKeyObject<K>>>();
	
	@SuppressWarnings("unchecked")
	public <E extends BB> E load(K key) {
		IDataAccessObject<K,BD> baseLoader = getBaseDAO();
		BD baseDO = baseLoader.findByPrimaryKey(key);
		String type = baseLoader.getType(baseDO);
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy == null){
			log.warn("Cannot find extension strategy of type :"+type+ "for :"+getStorageName());
			return null;
		}
		IDataAccessObject<K,INumberKeyObject<K>> extLoader = getExtDAO(type);
		INumberKeyObject<K> extData = extLoader.findByPrimaryKey(key);
		IBizObjectConverter<K,BB, BD, INumberKeyObject<K>> converter = getConverter(type);
		E val = (E)strategy.createBizObject(null);
		updateBO(val, baseDO);
		converter.updateExtBO(val, extData);
		return val;
	}
	
	
	@SuppressWarnings("unchecked")
	protected <E extends BB> E loadByBaseDO(BD baseDO) {
		IDataAccessObject<K,BD> baseLoader = getBaseDAO();
		String type = baseLoader.getType(baseDO);
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy == null){
			log.warn("Cannot find extension strategy of type :"+type+ "for :"+getStorageName());
			return null;
		}
		IDataAccessObject<K,INumberKeyObject<K>> extLoader = getExtDAO(type);
		INumberKeyObject<K> extData = extLoader.findByPrimaryKey(baseDO.getId());
		IBizObjectConverter<K,BB, BD, INumberKeyObject<K>> converter = getConverter(type);
		E val = (E)strategy.createBizObject(null);
		updateBO(val, baseDO);
		converter.updateExtBO(val, extData);
		return val;
	}
	
	
	@SuppressWarnings("unchecked")
	protected <E extends BB> E loadByExtDO(INumberKeyObject<K> extDO) {
		IDataAccessObject<K,BD> baseLoader = getBaseDAO();
		BD baseDO = baseLoader.findByPrimaryKey(extDO.getId());
		String type = baseLoader.getType(baseDO);
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy == null){
			log.warn("Cannot find extension strategy of type :"+type+ "for :"+getStorageName());
			return null;
		}
		IBizObjectConverter<K,BB, BD, INumberKeyObject<K>> converter = getConverter(type);
		E val = (E)strategy.createBizObject(null);
		updateBO(val, baseDO);
		converter.updateExtBO(val, extDO);
		return val;
	}

	
	
	public <E extends BB> K saveOrUpdate(E object) {
		K key = object.getId();
		String type = object.getType();
		IDataAccessObject<K,BD> baseLoader = getBaseDAO();
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy == null){
			throw new InvalidDataTypeException("Invalid ext data type :"+type);
		}
		IBizObjectConverter<K,BB,BD,INumberKeyObject<K>> converter = getConverter(type);
		BD baseDO = key != null ?  baseLoader.findByPrimaryKey(key) : null;
		if(baseDO != null){
			if(updateBaseDO(baseDO, object)){
				baseLoader.update(baseDO);
			}
		}else{
			baseDO = createBaseDO(object);
			object.setId(baseLoader.add(baseDO));
		}
		IDataAccessObject<K,INumberKeyObject<K>> extLoader = getExtDAO(type);
		INumberKeyObject<K> extData = key != null ? extLoader.findByPrimaryKey(key) : null;
		if(extData != null){
			if(converter.updateExtDO(extData, object)){
				extLoader.update(baseDO);
			}
		}else{
			extData = converter.createExtDO(object);
			extLoader.add(baseDO);
		}
		return key;
	}
	
	protected abstract IDataAccessObject<K,BD> getBaseDAO();
	
	protected abstract boolean updateBaseDO(BD baseDO, BB bizObject);
	
	protected abstract void updateBO(BB bizObject, BD baseDO);

	protected abstract BD createBaseDO(BB bizObject);
	
	protected abstract String getStorageName();

	protected IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> getExtStrategy(String type) {
		synchronized(this.extStrategies){
			return this.extStrategies.get(type);
		}
	}

	@SuppressWarnings("unchecked")
	protected IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>>[] getAllExtStrategy() {
		synchronized(this.extStrategies){
			if(this.extStrategies.isEmpty()){
				return null;
			}
			return this.extStrategies.values().toArray(new IExtStrategy[0]);
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected IDataAccessObject<K,INumberKeyObject<K>> getExtDAO(String type) {
		synchronized(this.extStrategies){
			IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject> ext = this.extStrategies.get(type);
			IDataAccessObject obj = ext != null ? ext.getDAO() : null;
			return obj;
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected IBizObjectConverter<K,BB, BD, INumberKeyObject<K>> getConverter(String type) {
		synchronized(this.extStrategies){
			IExtStrategy<K,BB, ? extends BB,BD, ? extends INumberKeyObject> ext = this.extStrategies.get(type);
			IBizObjectConverter obj = ext != null ? ext.getConverter() : null;
			return obj;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#registerExtStrategy(java.lang.String, com.wxxr.trading.core.storage.api.IExtStrategy)
	 */
	@Override
	public void registerExtStrategy(String type,
			IExtStrategy<K,BB,? extends BB, BD, ? extends INumberKeyObject<K>> strategy) {
		synchronized(this.extStrategies){
			this.extStrategies.put(type, strategy);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#unregisterExtStrategy(java.lang.String, com.wxxr.trading.core.storage.api.IExtStrategy)
	 */
	@Override
	public boolean unregisterExtStrategy(String type,
			IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy) {
		synchronized(this.extStrategies){
			IExtStrategy<K,BB, ? extends BB,BD, ? extends INumberKeyObject<K>> old = this.extStrategies.get(type);
			if(old == strategy){
				this.extStrategies.remove(type);
				return true;
			}
		}
		return false;
	}



	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#newObject(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E extends BB> E newObject(String type, Map<String, Object> params) {
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy == null){
			throw new InvalidDataTypeException("Invalid ext data type :"+type);
		}
		return (E)strategy.createBizObject(params);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#doQuery(java.lang.String, java.util.Map, com.wxxr.trading.core.storage.api.IObjectFilter)
	 */
	@Override
	public List<BB> doQuery(String queryName, Map<String, Object> criteria) {
		IDataAccessObject<K,? extends INumberKeyObject<K>> loader = null;
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>>[] exts = getAllExtStrategy();
		if(exts != null){
			for (IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> ext : exts) {
				if(ext.getDAO().hasQuery(queryName)){
					loader = ext.getDAO();
					break;
				}
			}
		}
		if(loader == null){
			if(getBaseDAO().hasQuery(queryName)){
				loader = getBaseDAO();
			}
		}
		if(loader == null){
			throw new IllegalArgumentException("Invalid query name :"+queryName);
		}
		final List<? extends INumberKeyObject<K>> list = loader.doQuery(queryName, criteria);
		final boolean isBaseLoader = (loader == getBaseDAO());
		if((list == null)||list.isEmpty()){
			return null;
		}
		return new AbstractList<BB>() {

			@SuppressWarnings("unchecked")
			@Override
			public BB get(int index) {
				INumberKeyObject<K> val = list.get(index);
				return isBaseLoader ? loadByBaseDO((BD)val) : loadByExtDO(val);
			}

			@Override
			public int size() {
				return list.size();
			}
		};
	}


	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#get(java.lang.Number)
	 */
	@Override
	public <E extends BB> E get(K key) {
		return load(key);
	}


	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.api.IBizObjectStorage#remove(java.lang.Number)
	 */
	@Override
	public boolean remove(K key) {
		IDataAccessObject<K,BD> baseLoader = getBaseDAO();
		BD baseDO = baseLoader.findByPrimaryKey(key);
		if(baseDO == null){
			return false;
		}
		String type = baseLoader.getType(baseDO);
		IExtStrategy<K,BB, ? extends BB, BD, ? extends INumberKeyObject<K>> strategy = getExtStrategy(type);
		if(strategy != null){
			IDataAccessObject<K,INumberKeyObject<K>> extLoader = getExtDAO(type);
			INumberKeyObject<K> extData = extLoader.findByPrimaryKey(key);
			extLoader.remove(extData);
		}
		baseLoader.remove(baseDO);
		return true;
	}
}
