package com.wxxr.trading.core.task.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;

import com.hygensoft.common.util.AnotherThreadPoolExecutor;
import com.hygensoft.common.util.Trace;
import com.wxxr.common.jmx.annotation.ManagedAttribute;
import com.wxxr.common.jmx.annotation.ManagedOperation;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.common.util.AuditableResource;
import com.wxxr.common.util.CommonUtils;
import com.wxxr.common.util.LRUList;
import com.wxxr.trading.core.task.api.HandlerStatus;
import com.wxxr.trading.core.task.api.IDistributedTaskExecutor;
import com.wxxr.trading.core.task.api.ISchedulerContext;
import com.wxxr.trading.core.task.api.ITaskExecutor;
import com.wxxr.trading.core.task.api.ITaskHandler;
import com.wxxr.trading.core.task.api.ITaskInfo;
import com.wxxr.trading.core.task.api.ITaskScheduler;
import com.wxxr.trading.core.task.api.ReschedulingReqiredException;
import com.wxxr.trading.core.task.common.IHashRingNodeDaemon.NodeStatusChangedEventListener;

/**
 * 这个实现是基于一致Hash环算法的分布式集群任务调度服务。每个任务都有指定的任务类型{@link ITaskInfo#getJobType()},
 * 每种任务类型必须有对应的任务执行器{@link ITaskExecutor},在这个实现中任务执行器必须是分布式类型{@link IDistributedTaskExecutor},
 * 每个分布式任务执行器必须提供它自己对任务的Hash计算策略{@link IDistributedTaskExecutor#getHashStrategy()},计算出的Hash值，
 * 将通过节点Hash环管理服务{@link IHashRingNodeDaemon#getAliveNodeId4Hash(long)}计算出应该执行该任务的节点号。
 * 这种算法可以有效避免了多个节点对同一种任务竞争，提高任务的执行效率。因为不存在竞争，所以在这个实现中，我们将到期要执行的任务尽可能多的预先
 * 加载到线程池的任务队列中，并且在任务调度中可以不需要对任务状态作任何改变，只要在任务成功或失败时才需要对任务状态进行更新。
 * @author neillin
 *
 */
public abstract class AbstractHashDistributedTaskScheduler extends AuditableResource implements
		ITaskScheduler {
	private static final Trace log = Trace.register(AbstractHashDistributedTaskScheduler.class);
	
	private class TaskWorker implements Runnable {

		private final ITaskExecutor executor;
		private final ITaskInfo taskInfo;
		
		public TaskWorker(ITaskExecutor exec,ITaskInfo info) {
			this.executor = exec;
			this.taskInfo = info;
		}
		@Override
		public void run() {
			try {
				this.executor.executeTask(getSchedulerContext(), taskInfo.getJobId(), taskInfo.getJobRequest());
				handleTaskDone(taskInfo);
			} catch (ReschedulingReqiredException e){
				handleRescheduleRequest(taskInfo,e);
			} catch (Throwable e) {
				handleExecutionFailure(taskInfo,e);
			}	
		}
		
	}

	private class TaskScheduler implements Runnable {

		private Thread currentThread;
		private volatile boolean keepAlive,waiting,checkAgain,listenerRegistered;

		@Override
		public void run() {
			this.currentThread = Thread.currentThread();
			this.currentThread.setName(getThreadName());
			try {
				Thread.sleep(30 * 1000L);
			} catch (InterruptedException e1) {
			}
			while (keepAlive) {
				try {
					boolean moreTask = false;
					IHashRingNodeDaemon daemon = getHashRingNodeDaemon();
					if((daemon != null)&&daemon.isServiceReady()&&hasExecutorRegistered()){
						if(!listenerRegistered){
							daemon.addNodeStatusChangedEventListener(listener);
							listenerRegistered = true;
						}
						moreTask = doScheduling(new ITaskHandler() {

								@Override
								public HandlerStatus handleTask(ITaskInfo task) {
									IDistributedTaskExecutor executor = getTaskExecutor(task
											.getJobType());
									if (executor == null) {
										return HandlerStatus.SKIP;
									}
									IHashRingNodeDaemon daemon = context.getService(IHashRingNodeDaemon.class);
									if(! daemon.getAliveNodeId4Hash(executor.getHashStrategy().calculateHash(task)).equals(daemon.getLocalNodeId())){
										return HandlerStatus.SKIP;
									}
									if(scheduledTasks.contains(task.getJobId())){
										return HandlerStatus.SKIP;
									}
									TaskWorker worker = new TaskWorker(executor, task);
									if (log.isInfoEnabled()) {
										log.info("Find job :"
												+ task
												+ ", going to schedule the task ...");
									}
									lock.lock();
									try {
										getExecutor().execute(worker);
										scheduledTasks.put(task.getJobId());
										return HandlerStatus.OK;
									} catch (RejectedExecutionException e) {
										log.info("Job :"
												+ task
												+ " was rejected by thread pool, will retry later !");
										return HandlerStatus.STOP;
									} finally {
										lock.unlock();
									}
								}

							});
					}
					if(checkAgain) {
						checkAgain = false;
						continue;
					}
					long interval = moreTask ? busyTaskCheckIntervalInSeconds*1000L : idleTaskCheckIntervalInSeconds*1000L;
					waiting = true;
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
					}
					waiting = false;
				} catch (Throwable t) {
					addException(t);
					log.error("Failed to find and execute scheduled task !", t);
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
					}
				}
			}
			if(listenerRegistered){
				IHashRingNodeDaemon daemon = context.getService(IHashRingNodeDaemon.class);
				if(daemon != null){
					daemon.removeNodeStatusChangedEventListener(listener);
					listenerRegistered = false;
				}
			}
		}
		
		public void forceCheck() {
			if((this.currentThread == null)||(!keepAlive)){
				return;
			}
			if(this.waiting){
				this.currentThread.interrupt();
			}else{
				this.checkAgain = true;
			}
		}
		
		public void start() {
			this.keepAlive = true;
			new Thread(this).start();
			if (log.isInfoEnabled()) {
				log.info("Startup task scheduler ");
			}
		}

		public void stop() {
			this.keepAlive = false;
			if (this.currentThread != null) {
				this.currentThread.interrupt();
				try {
					this.currentThread.join(1000L);
				} catch (InterruptedException e) {
				}
				this.currentThread = null;
			}
			if (log.isInfoEnabled()) {
				log.info("Stop task scheduler !");
			}

		}
	}
	
	private int busyTaskCheckIntervalInSeconds = 1,idleTaskCheckIntervalInSeconds = 5, maxExecutionTimeInSeconds = 300;
	private Map<String,IDistributedTaskExecutor> schedulers = new HashMap<String,IDistributedTaskExecutor>();
	private TaskScheduler scheduler;
	private LRUList<Long> scheduledTasks;
	private int maxThreads = 30,taskQueueSize = 1000, minThreads = 5;
	private IKernelContext context;
	private AnotherThreadPoolExecutor executor;
	private ReentrantLock lock = new ReentrantLock();
	private IHashRingNodeDaemon hashRingDaemon;
	private NodeStatusChangedEventListener listener = new NodeStatusChangedEventListener() {
		
		@Override
		public void serviceReady(IHashRingNodeDaemon service) {
			
		}
		
		@Override
		public void nodesChanged(IHashRingNodeDaemon service,
				Integer[] upServerIds, Integer[] downServerIds) {
			lock.lock();
			try {
				if(executor != null){
					executor.getQueue().clear();
				}
				if(scheduledTasks != null){
					scheduledTasks.clear();
				}
				if(scheduler != null){
					scheduler.forceCheck();
				}
			}finally {
				lock.unlock();
			}
		}
	};
	
	protected IHashRingNodeDaemon getHashRingNodeDaemon() {
		if(this.hashRingDaemon == null){
			this.hashRingDaemon = CommonUtils.getService(IHashRingNodeDaemon.class);
		}
		return this.hashRingDaemon;
	}
		
	
	protected boolean hasExecutorRegistered() {
		synchronized(this.schedulers){
			return this.schedulers.size() > 0;
		}
	}
	protected ITaskExecutor removeExecutor(ITaskExecutor executor){
		ITaskExecutor scheduler = null;
		synchronized(this.schedulers){
			ITaskExecutor sched = schedulers.get(executor.getAcceptableTaskType());
				if(sched == executor){
					scheduler = sched;
				}
		}
		if(scheduler != null){
			this.schedulers.remove(executor.getAcceptableTaskType());
		}
		return scheduler;
	}
	
	protected void addExecutor(IDistributedTaskExecutor executor){
		synchronized(this.schedulers){
			this.schedulers.put(executor.getAcceptableTaskType(),executor);
		}
	}
	
	protected IDistributedTaskExecutor getTaskExecutor(String jobType){
		synchronized(this.schedulers){
			return this.schedulers.get(jobType);
		}
	}

	protected Executor getExecutor() {
		return executor;
	}
	
	protected boolean rmeoveScheduledTaskId(ITaskInfo taskInfo){
		lock.lock();
		try {
			return this.scheduledTasks.remove(taskInfo.getJobId());
		}finally {
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.info.scheduler.ICollectorTaskScheduler#registerTaskExecutor(com.wxxr.stock.info.scheduler.ITaskExecutor)
	 */
	@Override
	public void registerTaskExecutor(ITaskExecutor executor) {
		if(log.isDebugEnabled()){
			log.debug("Going to add executor :"+executor);
		}
		if(executor instanceof IDistributedTaskExecutor){
			addExecutor((IDistributedTaskExecutor)executor);
		}else{
			throw new IllegalArgumentException("executor must be type of IDistributedTaskExecutor");
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.info.scheduler.ICollectorTaskScheduler#unregisterTaskExecutor(com.wxxr.stock.info.scheduler.ITaskExecutor)
	 */
	@Override
	public boolean unregisterTaskExecutor(ITaskExecutor executor) {
		if(log.isDebugEnabled()){
			log.debug("Going to remove executor :"+executor);
		}
		return removeExecutor(executor) != null;
	}


	public void start(IKernelContext ctx) {
		this.context = ctx;
		this.scheduledTasks = new LRUList<Long>(taskQueueSize*10);
		this.executor = new AnotherThreadPoolExecutor(minThreads, maxThreads, 1, TimeUnit.MINUTES, 
				new LinkedBlockingDeque<Runnable>(taskQueueSize),
				new ThreadFactory() {
					private AtomicInteger seqNo = new AtomicInteger();
					
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "Distributed Task Scheduler Thread -- "+seqNo.getAndIncrement());
					}
				});
		this.scheduler = new TaskScheduler();
		this.scheduler.start();

	}

	public void stop(IKernelContext ctx) {
		if(this.scheduler != null){
			this.scheduler.stop();
			this.scheduler = null;
		}
		if(this.executor != null){
			this.executor.shutdownNow();
			this.executor = null;
		}
		if(this.scheduledTasks != null){
			this.scheduledTasks.clear();
			this.scheduledTasks = null;
		}
		this.context = null;
	}


	/**
	 * @return the maxExecutionTimeInSeconds
	 */
	@ManagedAttribute(persistPolicy="OnUpdate")
	public int getMaxExecutionTimeInSeconds() {
		return maxExecutionTimeInSeconds;
	}


	/**
	 * @param maxExecutionTimeInSeconds the maxExecutionTimeInSeconds to set
	 */
	@ManagedAttribute
	public void setMaxExecutionTimeInSeconds(int maxExecutionTimeInSeconds) {
		this.maxExecutionTimeInSeconds = maxExecutionTimeInSeconds;
	}


	
	@ManagedOperation
	public String listAllRegisteredTaskTypes() {
		synchronized(this.schedulers){
			return this.schedulers.size() > 0 ? StringUtils.join(this.schedulers.keySet().iterator(),"<br/>") : null;
		}
	}
	
	

	/**
	 * @return the busyTaskCheckIntervalInSeconds
	 */
	@ManagedAttribute
	public int getBusyTaskCheckIntervalInSeconds() {
		return busyTaskCheckIntervalInSeconds;
	}
	/**
	 * @return the idleTaskCheckIntervalInSeconds
	 */
	@ManagedAttribute
	public int getIdleTaskCheckIntervalInSeconds() {
		return idleTaskCheckIntervalInSeconds;
	}
	/**
	 * @param busyTaskCheckIntervalInSeconds the busyTaskCheckIntervalInSeconds to set
	 */
	@ManagedAttribute(persistPolicy="OnUpdate",description="当系统有更多的任务等待时，两次调度执行之间等待的时间间隔")
	public void setBusyTaskCheckIntervalInSeconds(int busyTaskCheckIntervalInSeconds) {
		this.busyTaskCheckIntervalInSeconds = busyTaskCheckIntervalInSeconds;
	}
	/**
	 * @param idleTaskCheckIntervalInSeconds the idleTaskCheckIntervalInSeconds to set
	 */
	@ManagedAttribute(persistPolicy="OnUpdate",description="当系统没有已知的任务等待时，两次调度执行之间等待的时间间隔")
	public void setIdleTaskCheckIntervalInSeconds(int idleTaskCheckIntervalInSeconds) {
		this.idleTaskCheckIntervalInSeconds = idleTaskCheckIntervalInSeconds;
	}
	/**
	 * return true if there are more tasks waiting for execution
	 * @param taskHandler
	 * @return
	 */
	protected abstract boolean doScheduling(ITaskHandler taskHandler);

	/**
	 * @param e
	 */
	protected abstract void handleExecutionFailure(ITaskInfo info,Throwable e);

	
	protected abstract ISchedulerContext getSchedulerContext();
	/**
	 * 
	 */
	protected abstract void handleTaskDone(ITaskInfo info);
	

	protected abstract void handleRescheduleRequest(ITaskInfo taskInfo, ReschedulingReqiredException e);

	protected String  getThreadName() {
		return "Collector Task Scheduler thread";
	}
	
	protected String[] getAllRegisteredTaskTypes(){
		synchronized(this.schedulers){
			return this.schedulers.size() > 0 ?schedulers.keySet().toArray(new String[0]):null;
		}
	}

	
}
