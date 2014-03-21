package com.wxxr.trading.core.task.common;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.hygensoft.common.util.Trace;
import com.wxxr.common.jmx.annotation.ManagedAttribute;
import com.wxxr.common.jmx.annotation.ManagedOperation;
import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.task.api.IDistributedTaskExecutor;
import com.wxxr.trading.core.task.api.ISchedulerContext;
import com.wxxr.trading.core.task.api.ITaskHandler;
import com.wxxr.trading.core.task.api.ITaskHashStrategy;
import com.wxxr.trading.core.task.api.ITaskInfo;
import com.wxxr.trading.core.task.api.ReschedulingReqiredException;
import com.wxxr.trading.core.task.persistence.TradingTaskSchedulerDAO;
import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;

/**
 * @author neillin
 *
 */
@ServiceMBean
public class TradingTaskSchedulerImpl extends AbstractHashDistributedTaskScheduler implements
		ITradingTaskScheduler, ISchedulerContext {
	
	private static final Trace log = Trace.register(TradingTaskSchedulerImpl.class);
	private static final String DELETE_TASK = "DELETE_TASK";
	
	private TradingTaskSchedulerDAO dao;
	private int delDays = 30;
	
	protected TradingTaskSchedulerDAO getScheduledTaskDAO() {
		if(this.dao == null){
			this.dao = DAOFactory.getDAObject(TradingTaskSchedulerDAO.class);
		}
		return this.dao;
	}
	

	/* (non-Javadoc)
	 * @see com.wxxr.stock.info.scheduler.ICollectorTaskScheduler#scheduleTask(java.lang.String, java.lang.String, java.lang.String, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Long scheduleTask(String taskType, String jobRequest,
			long delay, TimeUnit unit) {
		return scheduleTask(taskType, jobRequest, new Date(System.currentTimeMillis()+TimeUnit.MILLISECONDS.convert(delay, unit)));
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.info.scheduler.ICollectorTaskScheduler#scheduleTask(java.lang.String, java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public Long scheduleTask(String taskType, String jobRequest,
			Date afterTime) {
		if(log.isDebugEnabled()){
			log.debug("Going to schdule a task with type:"+taskType+", jobRequest :"+jobRequest);
		}
		try {
			TradingTaskInfo taskInfo = new TradingTaskInfo();
			taskInfo.setJobRequest(jobRequest);
			taskInfo.setJobType(taskType);
			taskInfo.setNextSchedulingTime(afterTime);
			getScheduledTaskDAO().add(taskInfo);
			return taskInfo.getJobId();
		}catch(PersistenceException e){
			addException(e);
			log.warn("Failed to schedule collector task with  type :"+taskType+", request :"+jobRequest, e);
			throw e;
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.info.scheduler.ICollectorTaskScheduler#unscheduleTask(java.lang.String)
	 */
	@Override
	@ManagedOperation
	public boolean unscheduleTask(Long jobId) {
		log.fatal("对于不完整业务数据的更正，unscheduleTask任务，jobId:"+jobId);
		try {
			TradingTaskInfo taskInfo = null;
			try {
				taskInfo = getScheduledTaskDAO().findByPrimaryKey(jobId);
			}catch(EntityNotFoundException e){}
			if(taskInfo != null){
				getScheduledTaskDAO().remove(taskInfo);
				return true;
			}
			return false;
		}catch(PersistenceException e){
			addException(e);
			log.warn("Failed to unschedule collector task with id :"+jobId, e);
			throw e;
		}
	}

	@Override
	public boolean unscheduleTasksOfType(String jobType) {
		try {
			int c = getScheduledTaskDAO().removeScheduledTasksOfType(jobType);
			return c > 0;
		}catch(PersistenceException e){
			addException(e);
			log.warn("Failed to unschedule collector task with type :"+jobType, e);
			throw e;
		}
	}

	@Override
	public void start(IKernelContext ctx) {
		super.start(ctx);
//		doStart(ctx);
		registerTaskExecutor(deleteTaskExecutor);
		ctx.registerService(ITradingTaskScheduler.class, this);
	}

	@Override
	public void stop(IKernelContext ctx) {
		super.stop(ctx);
		unregisterTaskExecutor(deleteTaskExecutor);
		ctx.unregisterService(ITradingTaskScheduler.class, this);
	}


	@Override
	public Collection<TradingTaskInfo> findActiveTasksOfType(String type) {
		return getScheduledTaskDAO().findScheduledTasksOfType(type);
	}
	
	@ManagedOperation
	public String listActiveTasksOfType(String taskType) {
		Collection<TradingTaskInfo> tasks = getScheduledTaskDAO().findScheduledTasksOfType(taskType);
		if((tasks != null)&&(tasks.size() > 0)){
			return StringUtils.join(tasks.iterator(), "<br/>");
		}
		return null;
	}
	
	@ManagedOperation
	public String listAllTasksOfType(String taskType) {
		Collection<TradingTaskInfo> tasks = getScheduledTaskDAO().findAllTasksOfType(taskType);
		if((tasks != null)&&(tasks.size() > 0)){
			return StringUtils.join(tasks.iterator(), "<br/>");
		}
		return null;
	}
	
	
	protected boolean doScheduling(ITaskHandler taskHandler) {
			long startTime = System.currentTimeMillis();
			boolean scheduling = getScheduledTaskDAO().doNextScheduling(getMaxExecutionTimeInSeconds(), taskHandler);
			addProcessTime("doNextScheduling", System.currentTimeMillis()
					- startTime);
			return scheduling;
	}

	/**
	 * @param e
	 */
	protected void handleExecutionFailure(ITaskInfo info,Throwable e) {
		TradingTaskInfo taskInfo = getScheduledTaskDAO().findByPrimaryKey(info.getJobId());
		log.error("Failed to execute scheduled task :"+taskInfo, e);
		taskInfo.setStatus("FAILED");
		String cause = ExceptionUtils.getStackTrace(e);
		if(cause.length() > 2000){
			cause = cause.substring(0,2000);
		}
		taskInfo.setFailureCause(cause);
		addException(e);
		updateTaskInfo(taskInfo);
	}

	/**
	 * 
	 */
	protected void handleTaskDone(ITaskInfo info) {
		TradingTaskInfo taskInfo = getScheduledTaskDAO().findByPrimaryKey(info.getJobId());
		taskInfo.setStatus("DONE");
		taskInfo.setJobDoneTime(new Date(System.currentTimeMillis()));
		if(log.isDebugEnabled()){
			log.debug("Task :"+taskInfo+" was executed successfully !");
		}
		taskInfo.setFailureCause(null);
		updateTaskInfo(taskInfo);
	}

	protected void handleRescheduleRequest(ITaskInfo info, ReschedulingReqiredException e){
		TradingTaskInfo taskInfo = getScheduledTaskDAO().findByPrimaryKey(info.getJobId());
		taskInfo.setStatus(null);
		taskInfo.setNextSchedulingTime(new Date(e.getNextExecutionTime()));
		if(log.isDebugEnabled()){
			log.debug("Task :"+taskInfo+" was executed successfully !");
		}
		taskInfo.setFailureCause(null);
		updateTaskInfo(taskInfo);
	}
	
	
	private IDistributedTaskExecutor deleteTaskExecutor = new IDistributedTaskExecutor() {

		private ITaskHashStrategy hashStrategy = new ITaskHashStrategy() {
			
			@Override
			public long calculateHash(ITaskInfo info) {
				return 1;
			}
		};
		
		@Override
		public String getAcceptableTaskType() {
			return DELETE_TASK;
		}

		@Override
		public void executeTask(ISchedulerContext ctx, Long jobId, String jobRequest) throws Exception {
			String message = StringUtils.trimToNull(jobRequest);
			if (log.isInfoEnabled()) {
				log.debug("Going to execute delete task, message is :" + message);
			}
			try {
				deleteTaskOfDate(delDays);
			} catch(ReschedulingReqiredException e){
				throw e;
			} catch (Exception t) {
				addException(t);
				log.error("execute delete task error!!!",t);
				throw new ReschedulingReqiredException(System.currentTimeMillis()+24*60*60*1000L);
			}
		}

		@Override
		public ITaskHashStrategy getHashStrategy() {
			return this.hashStrategy;
		}
	};
	
	
	protected void updateTaskInfo(ITaskInfo info) {
		TradingTaskInfo taskInfo = (TradingTaskInfo)info;
		try{
			getScheduledTaskDAO().update(taskInfo);
		}catch(Throwable t){
			log.warn("Failed to update task status of ["+taskInfo+"]", t);
		}
	}
	
//	protected void doStart(IKernelContext ctx) {
//		Collection<TradingTaskInfo> deleteTask = getScheduledTaskDAO().findAllTasksOfType(DELETE_TASK);
//		boolean haveTask = false;
//		if(deleteTask!=null && !deleteTask.isEmpty()){
//			for(TradingTaskInfo t:deleteTask){
//				if(!"DONE".equals(t.getStatus())){
//					haveTask = true;
//				}
//			}
//		}
//		if(!haveTask){
//			scheduleTask(DELETE_TASK, "DEL_T_DAYS:"+delDays, 10 , TimeUnit.MINUTES);
//		}
//	}
	
	/**
	 * 删除指定日期之前的所有已经完成的任务记录
	 * @param days 
	 * @throws Exception
	 */
    protected void deleteTaskOfDate(int days) throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		Date time = cal.getTime();
		getScheduledTaskDAO().removeTaskOfDate(time);
		throw new ReschedulingReqiredException(System.currentTimeMillis()+24*60*60*1000L);
    }

	@ManagedAttribute(persistPolicy="OnUpdate")
	public int getDelDays() {
		return delDays;
	}

	@ManagedAttribute
	public void setDelDays(int delDays) {
		this.delDays = delDays;
	}


	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.impl.AbstractTaskScheduler#getThreadName()
	 */
	@Override
	protected String getThreadName() {
		return "Trading Task Scheduler thread";
	}
	
	@ManagedOperation
	public Long scheduleTask(String taskType, String jobRequest,
			long delay) {
		log.fatal("对于不完整业务数据的更正，添加scheduleTask任务，taskType:"+taskType+",jobRequest:"+jobRequest);
		return 	scheduleTask(taskType, jobRequest, delay,TimeUnit.SECONDS);
	}


	@Override
	protected ISchedulerContext getSchedulerContext() {
		return this;
	}
}
