/**
 * 
 */
package com.wxxr.trading.core.task.persistence.command;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;

import com.wxxr.persistence.service.AbstractJPACommand;
import com.wxxr.trading.core.task.persistence.bean.HashRingNodeInfo;
import com.wxxr.trading.core.task.persistence.bean.NodeStatus;

/**
 * @author Neil Lin
 *
 */
public class UpdateStatusByStatusCommand extends AbstractJPACommand {

	private static final String JQL_UPDATE = "UPDATE "+HashRingNodeInfo.class.getCanonicalName()+" n SET n.status = :newStatus WHERE n.status = :oldStatus";
	/* (non-Javadoc)
	 * @see com.wxxr.persistence.service.JPACommand#execute(java.lang.reflect.Method, java.lang.Object[], javax.persistence.EntityManager)
	 */
	@Override
	public Object execute(Method method, Object[] args, EntityManager mgr)
			throws Exception {
		NodeStatus oldStatus = (NodeStatus)args[0];
		NodeStatus newStatus = (NodeStatus)args[1];
		return mgr.createQuery(JQL_UPDATE).setParameter("newStatus", newStatus).setParameter("oldStatus", oldStatus).executeUpdate();
	}

}
