/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author wangyan
 *
 */
public abstract class AbstractMTreeDataSyncClient implements IMTreeDataSyncClient {
	private Map<String,IDataConsumer> consumers=new ConcurrentHashMap<String,IDataConsumer>();
	private Map<String,MTreeRoot> trees =new ConcurrentHashMap<String,MTreeRoot>();
	protected Trace log=Trace.register(AbstractMTreeDataSyncClient.class);
	private long lastSuccessTime, lastFailedTime;
	
	private Checker checker=new Checker();
	class Checker implements Runnable{

		private Thread currentThread;
		private volatile boolean keepAlive;
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			currentThread=Thread.currentThread();
			currentThread.setName("MTreeDataSync consumers checker");
			while(keepAlive&&currentThread!=null){
				if(isOK2CheckerMTreeServer()){
					try{
						for(Map.Entry<String,IDataConsumer> entry: consumers.entrySet()){
							walk(entry.getKey(),entry.getValue());
							entry.getValue().allDataReceived();
						}
						lastSuccessTime = System.currentTimeMillis();
					
					}catch (Throwable e) {
						log.error("",e);
						lastFailedTime = System.currentTimeMillis();
					}
				}
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
				}
			}
			
		}
		public void start(){
			keepAlive=true;
			new Thread(this).start();
		}
		public void stop(){
			keepAlive=false;
			if(this.currentThread != null){
				this.currentThread.interrupt();
				try {
					this.currentThread.join(1000L);
				} catch (InterruptedException e) {
				}
				this.currentThread = null;
			}
		}
	}
	
	protected abstract boolean isOK2CheckerMTreeServer();
	
	public void walk(String key,IDataConsumer consumer) throws IOException {
		if(log.isDebugEnabled()){
			log.debug("Walking MTree of :"+key);
		}
		MTreeRoot root = this.trees.get(key);
		if(root == null){
			if(log.isInfoEnabled()){
				log.debug("MTree of :"+key+" is not existing in cache,going to create a new one");
			}
			root = new MTreeRoot();
			root.setRoot(new MTreeNode("/"));
			this.trees.put(key, root);
		}else{
			if(log.isDebugEnabled()){
				log.debug("Going to update node digest of MTree :"+key);
			}
			updateBranchDigest(root.getRoot(),consumer);
		}
		walk(key, root.getRoot(),consumer);
	}

	
	public void walk(String key,MTreeNode node,IDataConsumer consumer) throws IOException {
		byte[] clientDigest=node.getDigest();
		String nodePath = node.getPath();
		UNodeDescriptor unode=getConnector().isDataChanged(key, node.getPath(), clientDigest);
		if(unode == null){
			if(log.isTraceEnabled()){
				log.debug("Node digest of node :"+node.getPath()+" has not changed, finish walking !");
			}

			return ;
		}
		if(log.isDebugEnabled()){
			log.debug("Node digest of node :"+node.getPath()+" has changed, UNodeDescriptor :"+unode);
		}
		if(unode.isLeaf()){
			Object grpId = ((MTreeLeaf)node).getDataGroupId();
			if(log.isDebugEnabled()){
				log.debug("Going to receiving data of node :"+node.getPath()+" ,group id :"+grpId);
			}

			consumer.dateReceiving(grpId);
			try{
				byte[] bytes=getConnector().getNodeData(key, nodePath);
				if(log.isDebugEnabled()){
					log.debug("Receive data of node :"+node.getPath()+" ,group id :"+grpId+", data length :"+(bytes != null ? bytes.length : 0));
				}
				consumer.dataReceived(grpId,bytes);
			}catch (Throwable e) {
				log.warn("Failed to received data of node :"+node.getPath()+" ,group id :"+grpId,e);
				consumer.dataReceivingFailed(grpId);
			}
		}else{
			MNodeDescriptor[] mnodes=unode.getChildren();
			if(mnodes==null){
				return;
			}
			for(MNodeDescriptor mnode:mnodes){
				MTreeNode child = node.getChild(mnode.getNodeId());
				if(child == null){
					child = mnode.isLeaf() ? new MTreeLeaf(mnode.getNodeId()) : new MTreeNode(mnode.getNodeId());
					if(mnode.isLeaf()){
						if(log.isDebugEnabled()){
							log.debug("Going to create leaf node for node :"+node.getPath());
						}
						Object grpId = consumer.getGroupId(mnode.getLeafPayload());
						byte[] data = consumer.getReceivedData(grpId);
						((MTreeLeaf)child).setDataGroupId(grpId);
						((MTreeLeaf)child).setDigest(calculateDigest(data));
					}
					node.addChild(child);
					if(log.isDebugEnabled()){
						log.debug("Child node for node :"+node.getPath()+" was created :["+child+"]");
					}
				}
				byte[] serverHash = mnode.getDigest();
				byte[] clientHash = child.getDigest();
				if((serverHash == null)&&(clientDigest == null)){
					continue;
				}
				if(!Arrays.equals(serverHash, clientHash)){
					walk(key,child,consumer);
				}
			}
		}
	}
	
	protected byte[] calculateDigest(byte[] data){
		if(data == null){
			return null;
		}
		try {
			return MessageDigest.getInstance("SHA1").digest(data);
		} catch (NoSuchAlgorithmException e) {
			log.error("Cannot found message digester of SHA1", e);
			throw new RuntimeException("Cannot found message digester of SHA1", e);
		}
	}
	protected void updateBranchDigest(MTreeNode node,IDataConsumer consumer){
		calculateNodeDigest(node,consumer);
	}
	
	protected byte[] calculateNodeDigest(MTreeNode node,IDataConsumer consumer) {
		if(node instanceof MTreeLeaf){
			byte[] data = consumer.getReceivedData(((MTreeLeaf)node).getDataGroupId());
			if(data != null){
				node.setDigest(calculateDigest(data));
			}else{
				node.setDigest(null);
			}
			return node.getDigest();
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			boolean hasData = false;
			digest.reset();
			if(node.getChildrenSize() == 0){
				node.setDigest(null);
			}else{
				for(MTreeNode n : MTreeNode.getSortedChildren(node)){
					byte[] bytes = calculateNodeDigest(n,consumer);
					if((bytes != null)&&(bytes.length > 0)){
						digest.update(bytes);
						hasData = true;
					}
				}
			}
			if(hasData){
				byte[] d=digest.digest();
				node.setDigest(d);
				return d;
			}else{
				node.setDigest(null);
				return null;
			}
		} catch (NoSuchAlgorithmException e) {
			log.error("Cannot found message digester of SHA1", e);
			throw new RuntimeException("Cannot found message digester of SHA1", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.IMTreeDataSyncClient#clearCache(java.lang.String)
	 */
	public void clearCache(String key) {
		this.trees.remove(key);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.IMTreeDataSyncClient#registerConsumer(java.lang.String, com.wxxr.stock.utils.sync.IDataConsumer)
	 */
	public void registerConsumer(String key, IDataConsumer consumer) {
		synchronized (consumers) {
			consumers.put(key, consumer);
			this.trees.remove(key);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.utils.sync.IMTreeDataSyncClient#unregisterConsumer(java.lang.String, com.wxxr.stock.utils.sync.IDataConsumer)
	 */
	public boolean unregisterConsumer(String key, IDataConsumer consumer) {
		consumers.remove(key);
		this.trees.remove(key);
		return true;
	}
	/**
	 * @return the server
	 */
	protected abstract IMTreeDataSyncServerConnector getConnector();

	public void start(){
		checker.start();
	}
	public void stop(){
		checker.stop();
	}

	/**
	 * @return the lastSuccessTime
	 */
	public long getLastSuccessTime() {
		return lastSuccessTime;
	}

	/**
	 * @return the lastFailedTime
	 */
	public long getLastFailedTime() {
		return lastFailedTime;
	}
}
