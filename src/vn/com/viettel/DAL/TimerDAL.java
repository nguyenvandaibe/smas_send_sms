/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;

/**
 *
 * @author chiendd1
 */
public class TimerDAL {

    private final static String CLASS_NAME = TimerDAL.class.getName();

    /**
     * Xoa cac cau hinh gui tin da hoan thanh truoc do.
     *
     * @param connection
     * @param logger
     * @return
     * @throws SQLException
     */
    public static boolean DeleteEndTimer(Connection connection, Logger logger) throws SQLException {
        //connection.setAutoCommit(false);
        try (PreparedStatement statTimer = connection.prepareCall("{call [SMS].[TimerConfigMoveHistory] (?)}")) {

            statTimer.setInt(1, Parameters.TIMER_CONFIG_DAY);
            boolean execute = statTimer.execute();
            return execute;
        } catch (SQLException e) {
            //connection.setAutoCommit(true);
            connection.rollback();
            LogUtil.ErrorExt(logger, CLASS_NAME, "DeleteEndTimer", CommonUtils.getDateNow(), "NULL", e);
            return false;
        }

    }
}
