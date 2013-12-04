/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.db.StockInfo;
import com.wxxr.mobile.stock.app.db.dao.StockInfoDao;
import com.wxxr.mobile.stock.app.service.IDBService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.sync.client.api.IDataConsumer;
import com.wxxr.mobile.sync.client.api.IMTreeDataSyncServerConnector;
import com.wxxr.mobile.sync.client.impl.MTreeDataSyncClientService;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author wangxuyang
 * 
 */
public class StockInfoSyncServiceImpl extends AbstractModule<IStockAppContext>
		implements IStockInfoSyncService {
	private static final Trace log = Trace.register(StockInfoSyncServiceImpl.class);
	private StockInfoDao dao;

	protected StockInfoDao getStockInfoDao() {
		if (dao == null) {
			dao = getService(IDBService.class).getDaoSession()
					.getStockInfoDao();
		}
		return dao;
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IDBService.class);
		addRequiredService(IMTreeDataSyncServerConnector.class);
	}

	@Override
	protected void startService() {
		if (log.isDebugEnabled()) {
			log.debug("Loading local stock infos...");
		}
		loadLocalDatas();
		if (log.isDebugEnabled()) {
			log.debug(String.format("%d stocks loaded.", cache.size()));
		}
		context.registerService(IStockInfoSyncService.class, this);
		context.getExecutor().execute(new Runnable() {
			public void run() {
				restartSync();// 启动同步组件
			}
		});
	}

	@Override
	protected void stopService() {
		if (syncStarted) {
			syncClient.stop();
			syncStarted = false;
		}
		cache.clear();
		context.unregisterService(IStockInfoSyncService.class, this);
	}

	// =================股票信息同步================
	private MTreeDataSyncClientService syncClient = new MTreeDataSyncClientService(){
		protected boolean isInDebugMode() {
			return context.getApplication().isInDebugMode();
		}
	};
	private Map<Object, List<String>> datas = new HashMap<Object, List<String>>();
	private Set<Object> receiving = new HashSet<Object>();
	private Set<Object> receiveFailed = new HashSet<Object>();
	private Map<String/**code*/,StockBaseInfo> cache = new HashMap<String, StockBaseInfo>();
	private Map<String/**code*/,Long/**db id*/> ids = new HashMap<String/**code*/,Long/**db id*/>();
	private boolean syncStarted = false;
	private boolean dataChanged = false;
	private IDataConsumer consumer = new IDataConsumer() {
		public String[] getAllReceivedDataKeys() {
			return datas.keySet().toArray(new String[datas.size()]);
		}

		public void dataReceived(Object key, byte[] data) {
			try {
				List<StockBaseInfo> datas = fromBytes(data);
				if (log.isDebugEnabled()) {
					log.debug("Received data for the key:" + key);
					log.debug(String.format("key:[%s]", key));
				}
				processReceivedData(key, datas);
				dataChanged = true;
				receiving.remove(key);
				receiveFailed.remove(key);
			} catch (Exception e) {
				log.warn("Failed to deserilize the data", e);
			}

		}

		public void dateReceiving(Object key) {
			if (log.isDebugEnabled()) {
				log.debug("Receiving data for the key:" + key);
			}
			receiving.add(key);
		}

		public void dataReceivingFailed(Object key) {
			if (log.isDebugEnabled()) {
				log.debug("Failed to Receive data for the key:" + key);
			}
			receiving.remove(key);
			receiveFailed.add(key);
		}

		public void allDataReceived() {
			if (log.isDebugEnabled()) {
				log.debug("All data Received.");
			}
			processAllDataReceived();

		}

		public byte[] removeReceivedData(Object key) {
			List<String> ids = datas.remove(key);
			return toBytes(getStockInfos(ids));
		}

		public byte[] getReceivedData(Object key) {
			List<String> ids = datas.get(key);
			return toBytes(getStockInfos(ids));
		}
		
		public List<StockBaseInfo> fromBytes(byte[] data) throws Exception {
			if (data == null) {
				return null;
			}
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bis);
				StockBaseInfo[] msgs = (StockBaseInfo[]) ois.readObject();
				return Arrays.asList(msgs);
			} catch (Exception e) {
				log.warn("Error when read stock info from bytes", e);
				throw e;
			} finally {
				try {
					if (ois != null) {
						ois.close();
					}
					bis.close();
				} catch (IOException e) {
				}
			}

		}
		
		public Object getGroupId(byte[] leafPayload) {
			if (leafPayload == null) {
				return null;
			}
			
			ByteBuffer bf = ByteBuffer.wrap(leafPayload);
			Object grpId = bf.getLong();
			List<String> list = null;
			if (bf.hasRemaining()) {
				byte[] data = new byte[bf.remaining()];
				try {
					bf.get(data);
					list = getLeafPayloadfromBytes(data);
				} catch (Exception e) {
					log.warn("Failed to get leaf payload data", e);
				}
			}
			datas.put(grpId, list);
			return grpId;
		}
	};
	public List<String> getLeafPayloadfromBytes(byte[] data) throws Exception {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			List<String> ids = (List<String>) ois.readObject();
			return ids;
		} catch (Exception e) {
			log.warn("Error when read stock info from bytes", e);
			throw e;
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				bis.close();
			} catch (IOException e) {
			}
		}

	}
	private void processReceivedData(Object key, List<StockBaseInfo> data) {
		if (data != null) {
			for (StockBaseInfo stockInfo : data) {
				saveOrUpdate(stockInfo);
			}
			if (log.isDebugEnabled()) {
				log.debug("Group info:"+data.toString());
			}
		} else {
			datas.remove(key);
		}
	}

	private Set<String> loadAllStockCodes() {
		return cache.keySet();
	}
	private void loadLocalDatas() {
		if (log.isDebugEnabled()) {
			log.debug(String.format("%d stock infos before load from db", cache.size()));
		}
		List<StockInfo> list = getStockInfoDao().loadAll();
		if (log.isDebugEnabled()) {
			log.debug(String.format("%d stock infos in db", list.size()));
		}
		synchronized (cache) {
			cache.clear();
			ids.clear();
			if (list!=null&&list.size()>0) {
				for (StockInfo stockInfo : list) {
					cache.put(stockInfo.getCode()+"."+stockInfo.getCode(),fromPO(stockInfo));
					ids.put(stockInfo.getCode()+"."+stockInfo.getCode(), stockInfo.getId());
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(String.format("%d stock infos loaded from db", cache.size()));
		}
	}
	private StockBaseInfo fromPO(StockInfo stockInfo){
		if (stockInfo==null) {
			return null;
		}
		StockBaseInfo stock = new StockBaseInfo();
		stock.setAbbr(stockInfo.getAbbr());
		stock.setCapital(stockInfo.getCapital());
		stock.setCode(stockInfo.getCode());
		stock.setCorpCode(stockInfo.getCorpCode());
		stock.setEps(stockInfo.getEps());
		stock.setEps_report_date(stockInfo.getEps_report_date());
		stock.setMarketCapital(stockInfo.getMarketCapital());
		stock.setMc(stockInfo.getMc());
		stock.setName(stockInfo.getName());
		stock.setType(stockInfo.getType());
		return stock;
	}
	private void saveOrUpdate(StockBaseInfo info){	
		if (log.isDebugEnabled()) {
			log.debug("SaveOrUpdate data:"+info);
		}
		if (info==null) {
			return ;
		}
		Long id = ids.get(info.getCode());
		StockInfo stockInfo = null;
		if (id!=null) {
			stockInfo = getStockInfoDao().load(id);
		}
		if (stockInfo==null) {
			stockInfo = new StockInfo();
		}		
		
		stockInfo.setAbbr(info.getAbbr());
		stockInfo.setCapital(info.getCapital());
		stockInfo.setCode(info.getCode());
		stockInfo.setCorpCode(info.getCorpCode());
		stockInfo.setEps(info.getEps());
		stockInfo.setEps_report_date(info.getEps_report_date());
		stockInfo.setMarketCapital(info.getMarketCapital());
		stockInfo.setMc(info.getMc());
		stockInfo.setName(info.getName());
		stockInfo.setType(info.getType());
		id = getStockInfoDao().insertOrReplace(stockInfo);
		if (id!=-1) {
			cache.put(info.getCode()+"."+info.getCode(), info);
			if (log.isDebugEnabled()) {
				log.debug(String.format("Update data for code[%s],db_id[%s],stock info:%s", info.getCode(),id,stockInfo.toString()));
			}
			ids.put(info.getCode(), id);
		}
	}
	private void processAllDataReceived() {
		Set<String> all_ids = new HashSet<String>();
		if (!receiving.isEmpty() || !receiveFailed.isEmpty()) {
			return;
		}
		synchronized (datas) {
			for (Entry<Object, List<String>> entry : datas.entrySet()) {
				all_ids.addAll(entry.getValue());
			}
		}
		List<String> list = new ArrayList<String>();
		list.addAll(all_ids);
		Set<String> local_storage_ids = loadAllStockCodes();
		if (local_storage_ids != null) {
			local_storage_ids.removeAll(all_ids);// 获取本地有，服务器端没有的集合
			if (!local_storage_ids.isEmpty()) {
				for (String code : local_storage_ids) {
					try {
						dataChanged = true;
						removeStockInfo(code);
					} catch (Exception e) {
						log.warn("Error when delete stock", e);
					}
				}
			}
		}
		if (dataChanged) {		
			loadLocalDatas();
			dataChanged = false;
		}
	}
	private void removeStockInfo(String code) {
		 StockBaseInfo info = cache.remove(code);
		 Long id = ids.remove(code);
		 if (info!=null&&id!=null) {
			 try {
				getStockInfoDao().deleteByKey(id);
			} catch (Exception e) {
				log.warn("Failed to delete the stock", e);
				cache.put(code, info);
				ids.put(code, id);
			}
		 }
	}

	public byte[] toBytes(List<StockBaseInfo> data) {
		if (data == null) {
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		StockBaseInfo[] infos = data.toArray(new StockBaseInfo[data.size()]);
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(infos);
			return bos.toByteArray();
		} catch (IOException e) {
			log.warn("Error when serilize the stocks", e);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				bos.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	private List<StockBaseInfo> getStockInfos(List<String> codes){
		List<StockBaseInfo> list = new LinkedList<StockBaseInfo>();
		if (codes!=null&&codes.size()>0) {
			Collections.sort(codes);
			for (String code : codes) {
				StockBaseInfo stock = cache.get(code);
				if (stock!=null) {
					list.add(stock);
				}else{					
					if (log.isDebugEnabled()) {
						log.debug(String.format("***size***===stock for code[%s] not found",code));
					}
				}
			}
		}
		return list;
	}
	private boolean isNetworkConnected() {
		return context.getService(IDataExchangeCoordinator.class).checkAvailableNetwork() > 0;
	}

	private void clear() {
		datas.clear();
		receiving.clear();
		receiveFailed.clear();

	}

	private void restartSync() {
		if (log.isDebugEnabled()) {
			log.debug("Restarting sshx msg sync service ...");
		}
		if (syncStarted) {
			syncClient.stop();
			syncClient.destory();
			clear();
			syncStarted = false;
		}
		while (!syncStarted) {
			try {
				if (!isNetworkConnected()) {
					continue;
				}
				syncClient.registerConsumer("STOCK_BASE/",consumer);
				syncClient.init(context);
				syncClient.start();
				syncStarted = true;
				if (log.isDebugEnabled()) {
					log.debug("Stock info sync service started successfully.");
				}
			} finally {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {

				}
			}

		}

	}

	@Override
	public List<StockBaseInfo> getStockInfos(IEntityFilter<StockBaseInfo> filter) {
		List<StockBaseInfo> ret = new ArrayList<StockBaseInfo>();
		if (filter==null) {
			ret.addAll(cache.values());
			return ret;
		}
		for (StockBaseInfo stockBaseInfo : cache.values()) {
			if (filter.doFilter(stockBaseInfo)) {
				ret.add(stockBaseInfo);
			}
		}
		return ret;
	}


	public StockBaseInfo getStockBaseInfoByCode(String code,String marketCode) {
		if (StringUtils.isBlank(code)||StringUtils.isBlank(marketCode)) {
			return null;
		}
		return cache.get(code+"."+marketCode);
	}
	
	
}
