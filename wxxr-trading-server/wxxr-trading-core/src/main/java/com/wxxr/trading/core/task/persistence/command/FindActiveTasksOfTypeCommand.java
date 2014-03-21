/**
 * 
 */
package com.wxxr.trading.core.task.persistence.command;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import com.wxxr.persistence.IConnectionProvider;
import com.wxxr.persistence.service.JDBCCommand;
import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;

/**
 * @author neillin
 *
 */
public class FindActiveTasksOfTypeCommand implements JDBCCommand {
	private static final String QUERY = "SELECT JOB_ID,JOB_TYPE, STATUS, NEXT_SCHED_TIME, START_TIME, JOB_REQUEST,FAILURE_CAUSE,LAST_EXECUTE_TIME FROM T_TASKS_INFO "+
			"WHERE JOB_TYPE = ? AND (STATUS IS NULL OR (STATUS <> 'DONE' AND STATUS <> 'ABORTED'))";
	/* (non-Javadoc)
	 * @see com.wxxr.persistence.service.JDBCCommand#execute(java.lang.reflect.Method, java.lang.Object[], com.wxxr.persistence.IConnectionProvider)
	 */
	@Override
	public Object execute(Method m, Object[] args, IConnectionProvider connProvider)
			throws Exception {
		if(args.length != 1){
			throw new IllegalArgumentException("Invalid arguments for DoNextTaskSchedulingCommand !");
		}
		String type = (String)args[0];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connProvider.getConnection();
			pstmt = conn.prepareStatement(QUERY,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
			pstmt.setString(1, type);
			rs = pstmt.executeQuery();
			LinkedList<TradingTaskInfo> result = new LinkedList<TradingTaskInfo>();
			while(rs.next()){
				result.add(DBHelper.readTaskInfo(rs));
				
			}
			return result;
		}finally{
			DBHelper.close(rs, pstmt, conn);
		}
	}
	
}
