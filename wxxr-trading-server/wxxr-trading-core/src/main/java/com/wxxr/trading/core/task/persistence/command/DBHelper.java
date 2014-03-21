/**
 * 
 */
package com.wxxr.trading.core.task.persistence.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wxxr.trading.core.task.persistence.bean.TradingTaskInfo;

/**
 * @author neillin
 *
 */
public class DBHelper {
	private DBHelper(){}
	
	public static TradingTaskInfo readTaskInfo(ResultSet rs) throws SQLException {
		TradingTaskInfo info = new TradingTaskInfo();
		info.setJobId(rs.getLong("JOB_ID"));
		info.setFailureCause(rs.getString("FAILURE_CAUSE"));
		info.setJobRequest(rs.getString("JOB_REQUEST"));
		info.setJobStartTime(rs.getTimestamp("START_TIME"));
		info.setJobType(rs.getString("JOB_TYPE"));
		info.setNextSchedulingTime(rs.getTimestamp("NEXT_SCHED_TIME"));
		info.setStatus(rs.getString("STATUS"));
		info.setLastExecutionTime(rs.getTimestamp("LAST_EXECUTE_TIME"));
		return info;
	}

	public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if (rs != null)
			try {
				rs.close();
				rs = null;
			} catch (Exception sqle) {
			}

		if (pstmt != null)
			try {
				pstmt.close();
				pstmt = null;
			} catch (Exception sqle) {
			}

		if (conn != null)
			try {
				conn.close();
				conn = null;
			} catch (Exception sqle) {
			}
	}

}
