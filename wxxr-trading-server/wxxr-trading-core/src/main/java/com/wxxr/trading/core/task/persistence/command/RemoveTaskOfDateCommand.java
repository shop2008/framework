package com.wxxr.trading.core.task.persistence.command;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import com.wxxr.persistence.IConnectionProvider;
import com.wxxr.persistence.service.JDBCCommand;

public class RemoveTaskOfDateCommand implements JDBCCommand{
	
	private static final String SQL = "DELETE T_TASKS_INFO WHERE STATUS = 'DONE' AND LAST_EXECUTE_TIME < ? ";

	@Override
	public Object execute(Method method, Object[] args, IConnectionProvider connProvider) throws Exception {
		if(args.length != 1){
			throw new IllegalArgumentException("Invalid arguments for RemoveTaskOfDateCommand !");
		}
		Date time = (Date)args[0];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connProvider.getConnection();
			pstmt = conn.prepareStatement(SQL);
			java.sql.Timestamp stamp = new java.sql.Timestamp(time.getTime());
			pstmt.setTimestamp(1, stamp);			
			return pstmt.executeUpdate();
		}finally{
			DBHelper.close(rs, pstmt, conn);
		}
	}

}
