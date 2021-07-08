/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: doanhcdm $
 */

package vn.com.viettel.dataSource;

import java.sql.Connection;
import org.apache.log4j.Logger;

public class ConnectionPoolManager {
	
	public static Connection getSMSConnection(Logger logMM) {
		Connection connection =SmasOracleConnectionPool.getInstance().getConnection(logMM);
                
		return connection;
	}

}