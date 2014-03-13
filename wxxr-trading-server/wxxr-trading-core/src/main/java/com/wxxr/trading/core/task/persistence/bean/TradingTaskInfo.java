/**
 * 
 */
package com.wxxr.trading.core.task.persistence.bean;

import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.trading.core.task.api.ITaskInfo;
import com.wxxr.trading.core.task.persistence.TradingTaskSchedulerDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name = "T_TASKS_INFO")
@Persistence(daoClass = TradingTaskSchedulerDAO.class)
@SequenceGenerator(name = "seq_t_task_id", sequenceName = "seq_t_task_id")
public class TradingTaskInfo implements ITaskInfo{
	private Long jobId;
	private String jobType;
	private String jobRequest;
	private Date nextSchedulingTime;
	private String status;
	private Date jobStartTime;
	private Date jobDoneTime;
	private Date lastExecutionTime;
	private String failureCause;
	
	
	/**
	 * @return the jobId
	 */
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_t_task_id")
	@Column(name = "JOB_ID", nullable = false, length=20)
	public Long getJobId() {
		return jobId;
	}
	
	/**
	 * @return the jobType
	 */
	@Column(name = "JOB_TYPE", nullable = false, length=40)
	public String getJobType() {
		return jobType;
	}
	
	/**
	 * @return the jobRequest
	 */
	@Column(name = "JOB_REQUEST", nullable = true, length=2000)
	public String getJobRequest() {
		return jobRequest;
	}
	
	/**
	 * @return the nextScheduledTime
	 */
	@Column(name = "NEXT_SCHED_TIME", nullable = true)
	public Date getNextSchedulingTime() {
		return nextSchedulingTime;
	}
	
	/**
	 * @return the status
	 */
	@Column(name = "STATUS", nullable = true,length=40)
	public String getStatus() {
		return status;
	}
	
	/**
	 * @return the jobStartTime
	 */
	@Column(name = "START_TIME", nullable = true)
	public Date getJobStartTime() {
		return jobStartTime;
	}
	
	/**
	 * @return the jobDoneTime
	 */
	@Column(name = "DONE_TIME", nullable = true)
	public Date getJobDoneTime() {
		return jobDoneTime;
	}
	
	/**
	 * @return the failureCause
	 */
	@Column(name = "FAILURE_CAUSE", nullable = true, length=2000)
	public String getFailureCause() {
		return failureCause;
	}
	
	
	/**
	 * @return the lastExecutionTime
	 */
	@Column(name = "LAST_EXECUTE_TIME", nullable = true, length=2000)
	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}

	/**
	 * @param lastExecutionTime the lastExecutionTime to set
	 */
	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	/**
	 * @param jobType the jobType to set
	 */
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	/**
	 * @param jobRequest the jobRequest to set
	 */
	public void setJobRequest(String jobRequest) {
		this.jobRequest = jobRequest;
	}
	
	/**
	 * @param nextScheduledTime the nextScheduledTime to set
	 */
	public void setNextSchedulingTime(Date scheduledStartTime) {
		this.nextSchedulingTime = scheduledStartTime;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @param jobStartTime the jobStartTime to set
	 */
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}
	
	/**
	 * @param jobDoneTime the jobDoneTime to set
	 */
	public void setJobDoneTime(Date jobDoneTime) {
		this.jobDoneTime = jobDoneTime;
	}
	
	/**
	 * @param failureCause the failureCause to set
	 */
	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradingTaskInfo [jobId=" + jobId + ", jobType=" + jobType
				+ ", jobRequest=" + jobRequest + ", nextScheduledTime="
				+ nextSchedulingTime + ", status=" + status + ", jobStartTime="
				+ jobStartTime + ", jobDoneTime=" + jobDoneTime
				+ ", failureCause=" + failureCause + "]";
	}
	
	
}
