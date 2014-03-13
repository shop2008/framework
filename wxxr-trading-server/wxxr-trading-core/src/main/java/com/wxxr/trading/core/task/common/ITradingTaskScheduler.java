package com.wxxr.trading.core.task.common;


import java.util.Collection;

import com.wxxr.trading.core.task.api.ITaskScheduler;
import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;

/**
 * @author neillin
 *
 */
public interface ITradingTaskScheduler extends ITaskScheduler {
	
	Collection<TradingTaskInfo> findActiveTasksOfType(String type);
}
