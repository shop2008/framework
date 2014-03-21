/**
 * 
 */
package com.wxxr.trading.core.task.persistence.command;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.wxxr.persistence.IConnectionProvider;
import com.wxxr.persistence.service.JDBCCommand;
import com.wxxr.trading.core.task.api.ITaskHandler;
import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;

/**
 * 
 * @author Neil Lin
 *
 */
public class DoNextTaskSchedulingCommand implements JDBCCommand {
	private static final String QUERY = "SELECT JOB_ID,JOB_TYPE, STATUS, NEXT_SCHED_TIME, START_TIME, JOB_REQUEST,FAILURE_CAUSE,LAST_EXECUTE_TIME FROM T_TASKS_INFO "+
			"WHERE (NEXT_SCHED_TIME IS NULL OR NEXT_SCHED_TIME <= SYSDATE) AND " +
			"(STATUS IS NULL OR ((STATUS <> 'DONE' AND STATUS <> 'ABORTED') " +
			"AND (LAST_EXECUTE_TIME+?) < SYSDATE)) order by NEXT_SCHED_TIME";
	/* (non-Javadoc)
	 * @see com.wxxr.persistence.service.JDBCCommand#execute(java.lang.reflect.Method, java.lang.Object[], com.wxxr.persistence.IConnectionProvider)
	 */
	@Override
	public Object execute(Method m, Object[] args, IConnectionProvider connProvider)
			throws Exception {
		if(args[4]==null){
			return false;
		}
		int total=1;
		int timeout = (Integer)args[0];
		ITaskHandler handler = (ITaskHandler)args[1];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connProvider.getConnection();
			pstmt = conn.prepareStatement(QUERY);
			float f = timeout/(24*60*60.0f);
			pstmt.setFloat(1, f);
			rs = pstmt.executeQuery();
			while(rs.next()){
				TradingTaskInfo taskInfo = DBHelper.readTaskInfo(rs);
				switch(handler.handleTask(taskInfo)){
				case OK:
					total++;
					break;
				case SKIP:
					break;
				case STOP:
					return true;
				}
			}
			return false;
		}finally{
			DBHelper.close(rs, pstmt, conn);
		}
	}
	


}
