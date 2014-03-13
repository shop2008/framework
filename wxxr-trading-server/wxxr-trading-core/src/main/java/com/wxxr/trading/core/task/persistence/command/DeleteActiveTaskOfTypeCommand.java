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

/**
 * @author neillin
 *
 */
public class DeleteActiveTaskOfTypeCommand implements JDBCCommand {
	private static final String SQL = "DELETE T_TASKS_INFO "+
			"WHERE (STATUS IS NULL OR (STATUS <> 'DONE' AND STATUS <> 'ABORTED')) AND JOB_TYPE = ? ";

	/* (non-Javadoc)
	 * @see com.wxxr.persistence.service.JDBCCommand#execute(java.lang.reflect.Method, java.lang.Object[], com.wxxr.persistence.IConnectionProvider)
	 */
	@Override
	public Object execute(Method m, Object[] args, IConnectionProvider connProvider)
			throws Exception {
		if(args.length != 1){
			throw new IllegalArgumentException("Invalid arguments for DeleteActiveTaskOfTypeCommand !");
		}
		String type = (String)args[0];
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connProvider.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, type);			
			return pstmt.executeUpdate();
		}finally{
			DBHelper.close(rs, pstmt, conn);
		}
	}
	

}
