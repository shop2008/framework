/**
 * 
 */
package com.wxxr.trading.core.task.persistence;

import java.util.Collection;

import javax.persistence.PersistenceException;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.trading.core.task.persistence.bean.HashRingNodeInfo;
import com.wxxr.trading.core.task.persistence.bean.NodeStatus;
import com.wxxr.trading.core.task.persistence.command.UpdateStatusByStatusCommand;

/**
 * @author Neil Lin
 *
 */
public interface HashRingNodeDAO {
	void add(HashRingNodeInfo node) throws PersistenceException;
	void update(HashRingNodeInfo node) throws PersistenceException;
	HashRingNodeInfo findByPrimaryKey(Integer nodeId) throws PersistenceException;
	void remove(HashRingNodeInfo task) throws PersistenceException;
	Collection<HashRingNodeInfo> findAll() throws PersistenceException;
	
	@Command(clazz=UpdateStatusByStatusCommand.class)
	int updateNodeStatusByStatus(NodeStatus currentStatus, NodeStatus newStatus) throws PersistenceException;

}
