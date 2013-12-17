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
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.service.IContentManager;
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
	private static final Trace log = Trace.register("com.wxxr.mobile.stock.app.service.impl.StockInfoSyncServiceImpl");
	private static final String TYPE_STOCK_DATA_BLOCK = "stock_block";
	@Override
	protected void initServiceDependency() {
		addRequiredService(IContentManager.class);
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
	private Map<String/**code*/,StockBaseInfo> cache = new ConcurrentHashMap<String, StockBaseInfo>();
	private boolean syncStarted = false;
	private boolean dataChanged = false;
	private IDataConsumer consumer = new IDataConsumer() {
		public String[] getAllReceivedDataKeys() {
			return datas.keySet().toArray(new String[datas.size()]);
		}
		public void dataReceived(Object key, byte[] data) {
			try {
				if (log.isDebugEnabled()) {
					log.debug("Received data for the key:" + key);
				}
				processReceivedData(key, data);
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
			return loadStockDataBlock(String.valueOf(key));
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
	private void processReceivedData(Object key, byte[] data) throws Exception {
		if (data!=null) {
			getService(IContentManager.class).saveContent(TYPE_STOCK_DATA_BLOCK, String.valueOf(key), data);
		}else{
			getService(IContentManager.class).delete(TYPE_STOCK_DATA_BLOCK, String.valueOf(key));
			this.datas.remove(key);
		}		
	}

	private Set<String> loadAllStockCodes() {		
		return cache.keySet();
	}
	private void loadLocalDatas() {
		if (log.isDebugEnabled()) {
			log.debug(String.format("%d stock infos in cache before load from local storage", cache.size()));
		}
		String[] ids =  null;
		try {
			ids = getService(IContentManager.class).queryContentIds(TYPE_STOCK_DATA_BLOCK, null, null);
		} catch (IOException e) {
			log.warn("Failed to load stock data block ids", e);
		}
		List<StockBaseInfo> list = new ArrayList<StockBaseInfo>();
		if (ids!=null&&ids.length>0) {
			for (String id : ids) {
				byte[] data =  null;
				try {
					data = getService(IContentManager.class).getContent(TYPE_STOCK_DATA_BLOCK, id);
					if (data!=null) {
						list.addAll(fromBytes(data));
					}
				} catch (Exception e) {
					log.warn(String.format("Failed to load stock block id[%s]", id),e);
				}
				
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(String.format("Total %d stock infos in local storage", list.size()));
		}
		synchronized (cache) {
			cache.clear();
			if (list!=null&&list.size()>0) {
				for (StockBaseInfo stockInfo : list) {
					if (log.isDebugEnabled()) {
						log.debug(String.format("Putting stock[%s]  to cache...", getStockKey(stockInfo.getCode(), stockInfo.getMc())));
					}
					cache.put(getStockKey(stockInfo.getCode(), stockInfo.getMc()),stockInfo);
				}
			}
		}
		if (log.isDebugEnabled()) {
			log.debug(String.format("Total %d stock infos loaded from local storage", cache.size()));
		}
	}
	private String getStockKey(String code,String marketCode){
		return code+"."+marketCode;
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
		Set<String> local_storage_ids = new HashSet<String>();
		local_storage_ids.addAll(loadAllStockCodes());
		if (local_storage_ids != null) {
			local_storage_ids.removeAll(all_ids);// 获取本地有，服务器端没有的集合
			if (!local_storage_ids.isEmpty()) {
				dataChanged = true;
			}
		}
		if (dataChanged) {		
			loadLocalDatas();
			dataChanged = false;
		}
	}
	public byte[] loadStockDataBlock(String blockId){
		byte[] data = null;
		try {
			data = getService(IContentManager.class).getContent(TYPE_STOCK_DATA_BLOCK, blockId);
		} catch (IOException e) {
			log.error("Failed to load stock block data for blockId:"+blockId,e);
		}
		return data;
	}
	public byte[] toBytes(List<StockBaseInfo> data) {
		if (data != null&&data.size()>0) {
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
		}
		return null;
	}
	private List<StockBaseInfo> getStockInfos(List<String> codes){
		List<StockBaseInfo> list = new LinkedList<StockBaseInfo>();
		if (codes!=null&&codes.size()>0) {
			Collections.sort(codes);
			for (int i = 0; i < codes.size(); i++) {
			StockBaseInfo stock = cache.get(codes.get(i));
				if (stock!=null) {
					list.add(stock);
				}else{					
					if (log.isDebugEnabled()) {
						log.debug(String.format("***size***===stock for code[%s] not found",codes.get(i)));
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
		return cache.get(getStockKey(code,marketCode));
	}

	@Override
	public String getStockName(String code, String marketCode) {
		StockBaseInfo stock = getStockBaseInfoByCode(code, marketCode);
		String stockName = null;
		if (stock!=null) {
			stockName = stock.getName();
		}
		return stockName;
	}
	
	
}
