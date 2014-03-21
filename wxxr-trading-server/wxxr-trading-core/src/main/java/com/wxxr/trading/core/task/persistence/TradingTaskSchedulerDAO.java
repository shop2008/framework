/**
 * 
 */
package com.wxxr.trading.core.task.persistence;

import java.util.Collection;
import java.util.Date;

import javax.persistence.PersistenceException;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.trading.core.task.api.ITaskHandler;
import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;
import com.wxxr.trading.core.task.persistence.command.DeleteActiveTaskOfTypeCommand;
import com.wxxr.trading.core.task.persistence.command.DoNextTaskSchedulingCommand;
import com.wxxr.trading.core.task.persistence.command.FindActiveTasksOfTypeCommand;
import com.wxxr.trading.core.task.persistence.command.FindAllTasksOfTypeCommand;
import com.wxxr.trading.core.task.persistence.command.RemoveTaskOfDateCommand;

/**
 * @author neillin
 *
 */
public interface TradingTaskSchedulerDAO {
	void add(TradingTaskInfo task) throws PersistenceException;
	void update(TradingTaskInfo task) throws PersistenceException;
	TradingTaskInfo findByPrimaryKey(Long jobId) throws PersistenceException;
	void remove(TradingTaskInfo task) throws PersistenceException;
	
	@Command(clazz=DeleteActiveTaskOfTypeCommand.class)
	int removeScheduledTasksOfType(String type) throws PersistenceException;

	
	@Command(clazz=FindActiveTasksOfTypeCommand.class)
	Collection<TradingTaskInfo> findScheduledTasksOfType(String type) throws PersistenceException;

	/**
	 * return true if there are more tasks in database
	 * @param stationId
	 * @param timeoutInSeconds
	 * @param handler
	 * @param maxTasks
	 * @return
	 * @throws PersistenceException
	 */
	@Command(clazz=DoNextTaskSchedulingCommand.class)
	boolean doNextScheduling(int timeoutInSeconds,ITaskHandler handler) throws PersistenceException;
	
	@Command(clazz=FindAllTasksOfTypeCommand.class)
	Collection<TradingTaskInfo> findAllTasksOfType(String type) throws PersistenceException;
	/**
	 * 删除指定日期之前的已完成的任务
	 */
	@Command(clazz=RemoveTaskOfDateCommand.class)
	void removeTaskOfDate(Date time) throws PersistenceException;
}
