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
 * 这个服务是用来管理平衡哈希环（Balanced Hash Ring)中各节点状态，哈希环的节点是在数据库表H_RING_NODE中定义，表中一条记录代表一个节点，每个节点有一个整数的Id来表示。
 * 这个服务必须部署成集群的单例服务{@link HASingleton} : 即参与哈希环的各节点都必须部署这个服务并加入同一集群，其中有一个节点为主节点（Master），其他的为
 * 副节点（Slave）。节点状态的管理主要由这个服务中两个任务来完成。
 * <li>主节点任务{@link AbstractHashRingDaemon.MasterTask} ：这个任务每隔一分钟（可以配置）把所有在{@link NodeStatus#IN_DOUBT}状态的节点设置成{@link NodeStatus#DOWN}状态，
 * 把所有在{@link NodeStatus#UP}状态的节点设置成{@link NodeStatus#IN_DOUBT}状态。这个任务只能在主节点上运行。</li>
 * <li>副节点任务{@link AbstractHashRingDaemon.SlaveTask} ：这个任务每个10秒钟（可以配置）把自己节点的状态更新为{@link NodeStatus#UP},并从数据库中把所有节点状态读取出来，
 * 更新本地内存中的节点状态，如果有任何节点的状态发生改变，发出节点状态改变事件，通知所有事件的监听者{@link IHashRingNodeDaemon.NodeStatusChangedEventListener}。
 * 这个任务在所有节点上都必须运行。</li>
 * <p>为避免在系统启动时产生的不确定性，这个服务在启动后有一个可配置的冷静期{@link AbstractHashRingDaemon#startupSilentPeriod},在冷静期内，这个服务会正常更新数据库中本节点的状态但
 * 不接受对它接口的调用（抛{@link IllegalStateException}）。冷静期过后，这个服务会发出服务准备好事件。所有使用这个服务的代码，必须确保在服务准备好后调用它的接口方法。
 * <p>要使本地节点加入指定的哈希环，本地节点Id{@link AbstractHashRingDaemon#localNodeId}必须正确配置，并且配置的Id必须出现在数据库表H_RING_NODE中。如果没有正确配置本地节点id，这个服务将
 * 不会发出服务准备好事件。另外需要注意的是，这个服务当前并没有检查本地配置的节点Id是否与其他节点Id重复，服务配置人员必须确保每个节点Id的唯一性！
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
