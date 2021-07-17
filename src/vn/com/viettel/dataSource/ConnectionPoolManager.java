/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: doanhcdm $
 */

package vn.com.viettel.dataSource;

import org.apache.log4j.Logger;

import java.sql.Connection;

public class ConnectionPoolManager {
	
	public static Connection getSMSConnection(Logger logMM) {

		Connection connection = SmasMysqlConnectionPool.getInstance().getConnection(logMM);

		return connection;
	}

}