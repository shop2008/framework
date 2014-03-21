/**
 * 
 */
package com.wxxr.trading.core.task.common;

/**
 * @author Neil Lin
 *
 */
public interface IHashRingNodeDaemon {
	public interface NodeStatusChangedEventListener {
		void nodesChanged(IHashRingNodeDaemon service,Integer[] upServerIds,Integer[] downServerIds);
		void serviceReady(IHashRingNodeDaemon service);
	}	
	Integer[] getConfiguredNodeIds();
	Integer[] getUpServerIds();
	Integer[] getDownNodeIds();
	Integer getLocalNodeId();
	Integer getAliveNodeId4Hash(long hash);
	boolean isServiceReady();
	
	void addNodeStatusChangedEventListener(NodeStatusChangedEventListener listener);
	boolean removeNodeStatusChangedEventListener(NodeStatusChangedEventListener listener);
	
}
