/**
 * 
 */
package com.wxxr.trading.core.task.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.hygensoft.common.util.Trace;
import com.wxxr.common.jmx.annotation.HASingleton;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.task.persistence.HashRingNodeDAO;
import com.wxxr.trading.core.task.persistence.bean.HashRingNodeInfo;
import com.wxxr.trading.core.task.persistence.bean.NodeStatus;

/**
 * �����������������ƽ���ϣ����Balanced Hash Ring)�и��ڵ�״̬����ϣ���Ľڵ��������ݿ��H_RING_NODE�ж��壬����һ����¼����һ���ڵ㣬ÿ���ڵ���һ��������Id����ʾ��
 * ���������벿��ɼ�Ⱥ�ĵ�������{@link HASingleton} : �������ϣ���ĸ��ڵ㶼���벿��������񲢼���ͬһ��Ⱥ��������һ���ڵ�Ϊ���ڵ㣨Master����������Ϊ
 * ���ڵ㣨Slave�����ڵ�״̬�Ĺ�����Ҫ�����������������������ɡ�
 * <li>���ڵ�����{@link AbstractHashRingDaemon.MasterTask} ���������ÿ��һ���ӣ��������ã���������{@link NodeStatus#IN_DOUBT}״̬�Ľڵ����ó�{@link NodeStatus#DOWN}״̬��
 * ��������{@link NodeStatus#UP}״̬�Ľڵ����ó�{@link NodeStatus#IN_DOUBT}״̬���������ֻ�������ڵ������С�</li>
 * <li>���ڵ�����{@link AbstractHashRingDaemon.SlaveTask} ���������ÿ��10���ӣ��������ã����Լ��ڵ��״̬����Ϊ{@link NodeStatus#UP},�������ݿ��а����нڵ�״̬��ȡ������
 * ���±����ڴ��еĽڵ�״̬��������κνڵ��״̬�����ı䣬�����ڵ�״̬�ı��¼���֪ͨ�����¼��ļ�����{@link IHashRingNodeDaemon.NodeStatusChangedEventListener}��
 * ������������нڵ��϶��������С�</li>
 * <p>Ϊ������ϵͳ����ʱ�����Ĳ�ȷ���ԣ������������������һ�������õ��侲��{@link AbstractHashRingDaemon#startupSilentPeriod},���侲���ڣ��������������������ݿ��б��ڵ��״̬��
 * �����ܶ����ӿڵĵ��ã���{@link IllegalStateException}�����侲�ڹ����������ᷢ������׼�����¼�������ʹ���������Ĵ��룬����ȷ���ڷ���׼���ú�������Ľӿڷ�����
 * <p>Ҫʹ���ؽڵ����ָ���Ĺ�ϣ�������ؽڵ�Id{@link AbstractHashRingDaemon#localNodeId}������ȷ���ã��������õ�Id������������ݿ��H_RING_NODE�С����û����ȷ���ñ��ؽڵ�id���������
 * ���ᷢ������׼�����¼���������Ҫע����ǣ��������ǰ��û�м�鱾�����õĽڵ�Id�Ƿ��������ڵ�Id�ظ�������������Ա����ȷ��ÿ���ڵ�Id��Ψһ�ԣ�
 * @author Neil Lin
 *
 */
public abstract class AbstractHashRingDaemon implements IHashRingNodeDaemon{
	private static final Trace log = Trace.register(AbstractHashRingDaemon.class);
	
	private HashRingNodeDAO dao;
	private Integer[] allNodes;
	private Integer[] hashRing;
	private int startupSilentPeriod = 60;  // in seconds, 60s by default
	private int slaveUpdateInterval = 10;   // in seconds, 5s by default
	private int masterUpdateInterval = 60;	// in seconds 60s by default
	private Integer localNodeId;
	private volatile boolean ready;
	private MasterTask master;
	private SlaveTask salve;
	private ArrayList<Integer> downNodes = new ArrayList<Integer>();
	private ArrayList<NodeStatusChangedEventListener> listeners = new ArrayList<IHashRingNodeDaemon.NodeStatusChangedEventListener>();
	
	private class SlaveTask implements Runnable {
		private Thread currentThread;
		private volatile boolean keepAlive;
		private long startupTime;
		
		public void run() {
			this.startupTime = System.currentTimeMillis();
			this.currentThread = Thread.currentThread();
			this.currentThread.setName("HashRing Daemon Slave Thread");
			if(localNodeId == null){
				log.warn("Local Node id was not properly configured !!! this node will not join the Hash Ring !");
			}else if(!hasConfiguredNode(localNodeId)){
				log.warn("Local Node id is not in configured Hash Ring, this node will not be able to join the Hash Ring !");
			}
			while(this.keepAlive){
				try {
					Thread.sleep(slaveUpdateInterval*1000L);
				} catch (InterruptedException e) {
				}
				if((localNodeId == null)||(!hasConfiguredNode(localNodeId))){
					continue;
				}
				try {
					updateLocalNodeStatus();
				}catch(Throwable t){
					log.error("Failed to update local node status, local node id :"+localNodeId, t);
				}
				try {
					if(!ready){
						if((System.currentTimeMillis() - this.startupTime) >= (startupSilentPeriod*1000L)){
							ready = true;
							updateAllNodeStatus();
							notifyServerReady();
						}
					}else {
						updateAllNodeStatus();
					}
				}catch(Throwable t){
					log.error("Failed to update all node status, local node id :"+localNodeId, t);
				}
			}
		}
		
		public void start() {
			this.keepAlive = true;
			new Thread(this).start();
		}
		
		public void stop() {
			this.keepAlive = false;
			if(this.currentThread != null){
				this.currentThread.interrupt();
				try {
					this.currentThread.join(2000L);
				} catch (InterruptedException e) {
				}
				this.currentThread = null;
			}
		}
	};
	
	private class MasterTask implements Runnable {
	
		private Thread currentThread;
		private volatile boolean keepAlive;
		
		public void run() {
			this.currentThread = Thread.currentThread();
			this.currentThread.setName("HashRing Daemon Master Thread");
			while(this.keepAlive){
				try {
					Thread.sleep(masterUpdateInterval*1000L);
				} catch (InterruptedException e) {
					continue;
				}
				try {
					getDAO().updateNodeStatusByStatus(NodeStatus.IN_DOUBT, NodeStatus.DOWN);
					getDAO().updateNodeStatusByStatus(NodeStatus.UP, NodeStatus.IN_DOUBT);
				}catch(Throwable t){
					log.error("Master failed to update node status, local node id :"+localNodeId, t);
				}
			}
		}

		public void start() {
			this.keepAlive = true;
			new Thread(this).start();
		}
		
		public void stop() {
			this.keepAlive = false;
			if(this.currentThread != null){
				this.currentThread.interrupt();
				try {
					this.currentThread.join(2000L);
				} catch (InterruptedException e) {
				}
				this.currentThread = null;
			}
		}
	}
	
	protected void updateLocalNodeStatus() {
		if(this.localNodeId == null){
			return;
		}
		HashRingNodeInfo nodeInfo = getDAO().findByPrimaryKey(localNodeId);
		if(nodeInfo != null){
			nodeInfo.setStatus(NodeStatus.UP);
			nodeInfo.setLastUpdatedDate(new Date());
			getDAO().update(nodeInfo);
		}
	}
	
	protected boolean hasConfiguredNode(Integer nodeId) {
		for (Integer id : this.allNodes) {
			if(id.equals(nodeId)){
				return true;
			}
		}
		return false;
	}
	@Override
	public Integer[] getConfiguredNodeIds() {
		return this.allNodes;
	}

	@Override
	public Integer[] getUpServerIds() {
		checkIfServiceReady();
		ArrayList<Integer> result = new ArrayList<Integer>();
		synchronized(this.downNodes){
			for (Integer id : this.allNodes) {
				if(!this.downNodes.contains(id)){
					result.add(id);
				}
			}
		}
		return result.isEmpty() ? null : result.toArray(new Integer[0]);
	}

	@Override
	public Integer[] getDownNodeIds() {
		checkIfServiceReady();
		synchronized(this.downNodes){
			return this.downNodes.isEmpty() ? null : this.downNodes.toArray(new Integer[0]);
		}
	}

	@Override
	public Integer getLocalNodeId() {
		return this.localNodeId;
	}

	@Override
	public Integer getAliveNodeId4Hash(long hash) {
		checkIfServiceReady();
		synchronized(this.downNodes){
			return HashRingAlgUtils.findAliveNodeId4Hash(hashRing, downNodes, hash);
		}
	}

	/**
	 * 
	 */
	protected void checkIfServiceReady() {
		if(!ready){
			throw new IllegalStateException("System is not ready yet !");
		}
	}

	protected HashRingNodeDAO getDAO() {
		if(this.dao == null){
			this.dao = DAOFactory.getDAObject(HashRingNodeDAO.class);
		}
		return this.dao;
	}
	
	protected void notifyServerReady() {
		NodeStatusChangedEventListener[] vals = getAllListeners();
		for (NodeStatusChangedEventListener l : vals) {
			l.serviceReady(this);
		}
	}

	/**
	 * @return
	 */
	protected NodeStatusChangedEventListener[] getAllListeners() {
		NodeStatusChangedEventListener[] vals = null;
		synchronized(this.listeners){
			vals = this.listeners.toArray(new NodeStatusChangedEventListener[0]);
		}
		return vals;
	}
	
	protected void notifyNodeStatusChanged(){
		NodeStatusChangedEventListener[] vals = getAllListeners();
		if(vals.length == 0){
			return;
		}
		Integer[] upNodes = getUpServerIds();
		Integer[] downNodes = getDownNodeIds();
		for (NodeStatusChangedEventListener l : vals) {
			l.nodesChanged(this, upNodes, downNodes);
		}
	}
	
	
	protected void updateAllNodeStatus() {
		boolean nodeStatusChanged = false;
		Collection<HashRingNodeInfo> nodes = getDAO().findAll();
		synchronized(this.downNodes){
			for (HashRingNodeInfo info : nodes) {
				Integer id = info.getNodeId();
				if(info.getStatus() == NodeStatus.DOWN){
					if(!downNodes.contains(id)){
						downNodes.add(id);
						nodeStatusChanged = true;
					}
				}else if(downNodes.contains(id)){
					downNodes.remove(id);
					nodeStatusChanged = true;
				}
			}
		}
		if(nodeStatusChanged){
			notifyNodeStatusChanged();
		}
		
	}
	
	protected void initHashRing() {
		Collection<HashRingNodeInfo> nodes = getDAO().findAll();
		if((nodes == null)||(nodes.size() == 0)){
			throw new IllegalStateException("There is no any node configured in database !!!");
		}
		Integer[] allNodeIds = new Integer[nodes.size()];
		int idx = 0;
		for (HashRingNodeInfo node : nodes) {
			allNodeIds[idx++] = node.getNodeId();
		}
		Arrays.sort(allNodeIds);
		this.allNodes = allNodeIds;
		this.hashRing = HashRingAlgUtils.generateHashRing(this.allNodes);
		startSlave();
	}

	protected void startMaster() {
		if(this.master == null){
			this.master = new MasterTask();
			this.master.start();
		}
	}
	
	protected void stopMaster() {
		if(this.master != null){
			this.master.stop();
			this.master = null;
		}
	}
	
	protected void startSlave() {
		if(this.salve == null){
			this.salve = new SlaveTask();
			this.salve.start();
		}
	}
	
	protected void stopSlave() {
		if(this.salve != null){
			this.salve.stop();
			this.salve = null;
		}
	}

	/**
	 * @return the startupSilentPeriod
	 */
	public int getStartupSilentPeriod() {
		return startupSilentPeriod;
	}

	/**
	 * @return the slaveUpdateInterval
	 */
	public int getSlaveUpdateInterval() {
		return slaveUpdateInterval;
	}

	/**
	 * @return the masterUpdateInterval
	 */
	public int getMasterUpdateInterval() {
		return masterUpdateInterval;
	}

	/**
	 * @return the ready
	 */
	public boolean isServiceReady() {
		return ready;
	}

	/**
	 * @param startupSilentPeriod the startupSilentPeriod to set
	 */
	public void setStartupSilentPeriod(int startupSilentPeriod) {
		this.startupSilentPeriod = startupSilentPeriod;
	}

	/**
	 * @param slaveUpdateInterval the slaveUpdateInterval to set
	 */
	public void setSlaveUpdateInterval(int slaveUpdateInterval) {
		this.slaveUpdateInterval = slaveUpdateInterval;
	}

	/**
	 * @param masterUpdateInterval the masterUpdateInterval to set
	 */
	public void setMasterUpdateInterval(int masterUpdateInterval) {
		this.masterUpdateInterval = masterUpdateInterval;
	}

	/**
	 * @param localNodeId the localNodeId to set
	 */
	public void setLocalNodeId(int localNodeId) {
		this.localNodeId = localNodeId;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.task.common.IHashRingNodeDaemon#addNodeStatusChangedEventListener(com.wxxr.trading.core.task.common.IHashRingNodeDaemon.NodeStatusChangedEventListener)
	 */
	@Override
	public void addNodeStatusChangedEventListener(
			NodeStatusChangedEventListener listener) {
		synchronized(this.listeners){
			if(!this.listeners.contains(listener)){
				this.listeners.add(listener);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.task.common.IHashRingNodeDaemon#removeNodeStatusChangedEventListener(com.wxxr.trading.core.task.common.IHashRingNodeDaemon.NodeStatusChangedEventListener)
	 */
	@Override
	public boolean removeNodeStatusChangedEventListener(
			NodeStatusChangedEventListener listener) {
		synchronized(this.listeners){
			return this.listeners.remove(listener);
		}
	}

}
