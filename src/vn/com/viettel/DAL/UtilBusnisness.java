/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;
import vn.com.viettel.util.StringUtil;

/**
 *
 * @author chiendd1
 */
public class UtilBusnisness {

    private final static String CLASS_NAME = UtilBusnisness.class.getName();
    private final static Logger loger = Logger.getLogger("SMAS_SMS_SENDER");
    public static List<String> lstMobile = null;

    /**
     * bo sung ma quoc gia vao so dien thoai truoc khi gui tin
     *
     * @param isdn
     * @return
     */
    public static String getISDN(String isdn) {
        if (isdn.equals("")) {
            return "";
        }
        if (isdn.length() >= 10) {
            String val0 = isdn.substring(0, 1);
            if (val0.equals("0")) {
                isdn = isdn.substring(1);
            }
            String val84 = isdn.substring(0, 2);
            if (val84.equals(Parameters.CountryCode) && isdn.length() >= 11) {
                isdn = isdn.substring(Parameters.CountryCode.length());
            }
        }
        return isdn;
    }

    /**
     * Ghi log MM
     *
     * @param logMM
     * @param ErrorCode
     * @param appId
     */
    public static void writeLogMonitoring(Logger logMM, String ErrorCode, String appId) {
        CallableStatement stmtLog = null;
        Connection connectionLog = null;
        try {
            if (connectionLog == null || connectionLog.isClosed()) {
                connectionLog = ConnectionPoolManager.getSMSConnection(logMM);
            }
            //connectionLog.setAutoCommit(false);
            // insert or update
            stmtLog = connectionLog.prepareCall("{call INSERT_OR_UPDATE_MONITORING (?, ?)}");
            stmtLog.setString(1, appId);
            stmtLog.setInt(2, Integer.parseInt(ErrorCode));
            stmtLog.execute();
            //connectionLog.commit();
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "writeLogMonitoring", CommonUtils.getDateNow(), "Cannot write log monitoring:" + e.getMessage(), e);
        } finally {
            try {
                if (stmtLog != null) {
                    stmtLog.close();
                }
                if (connectionLog != null) {
                    connectionLog.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "writeLogMonitoring", CommonUtils.getDateNow(), "Cannot close connect write log monitoring:" + e2.getMessage(), e2);
            }
        }
    }

    /**
     * Ghi log gui tin nhan loi va gui tin nhan canh bao
     *
     * @param appId
     * @param errorMessage, Ma loi BrandName trả ve
     * @param mobile: So dien thoai gui di bi loi
     */
    public static void writeLog(String appId, String errorMessage, String mobile) {
        CallableStatement stmtLog = null;
        Connection connectionLog = null;
        try {

            //Neu gap ma loi  DUPLICATE_MESSAGE thi khong tao tin nhan
            if (errorMessage.contains(GlobalConstant.ERR_DUPLICATE_MESSAGE)) {
                return;
            }
            if (connectionLog == null || connectionLog.isClosed()) {
                connectionLog = ConnectionPoolManager.getSMSConnection(loger);
            }

            stmtLog = connectionLog.prepareCall("{call INSERT_OR_UPDATE_MONITORING_2 (?, ?,?)}");
            stmtLog.setString(1, appId);
            stmtLog.setInt(2, 0);
            stmtLog.setString(3, errorMessage);
            stmtLog.execute();

            //Send Mobile
            if (!lstMobile.isEmpty()) {
                String content = StringUtil.createMassage(mobile, errorMessage);
                for (int i = 0; i < lstMobile.size(); i++) {
                    String mobileReceiver = lstMobile.get(i);
                    //loger.info("mobileReceiver: "+ mobileReceiver);
                    if (mobileReceiver.isEmpty()) {
                        continue;
                    }
                    MTDAL.insertMassageWarning(connectionLog, content, mobileReceiver);
                }
            }
            //connectionLog.commit();
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "writeLog", CommonUtils.getDateNow(), "Cannot write log monitoring:" + e.getMessage(), e);
        } finally {
            try {
                if (stmtLog != null) {
                    stmtLog.close();
                }
                if (connectionLog != null) {
                    connectionLog.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "writeLog", CommonUtils.getDateNow(), "Cannot close connect write log monitoring:", e2);
            }
        }
    }

    /**
     * Kiem tra App co con song hay khong
     *
     * @param connectionLog
     * @param logMM
     * @param node
     * @param appId
     * @return
     */
    public static boolean checkAppIDAnotherAlive(Connection connectionLog, Logger logMM, String node, String appId) {
        PreparedStatement statementLog = null;
        ResultSet rsLog = null;
        try {
            if (connectionLog == null || connectionLog.isClosed()) {
                connectionLog = ConnectionPoolManager.getSMSConnection(logMM);
            }

            String sql = "SELECT app_id FROM monitoring_app "
                    + "WHERE error_code_id = 0 "
                    + "AND (update_time + (?/1440)) <= sysdate "
                    + "AND app_id = ?";

            if (Parameters.DBTimeAlive == 0) {
                Parameters.DBTimeAlive = 30;
            }
            statementLog = connectionLog.prepareStatement(sql);
            statementLog.setInt(1, Parameters.DBTimeAlive);
            statementLog.setString(2, appId);
            rsLog = statementLog.executeQuery();
            //loger.info("Log Kiem tra tien trinh"+ sql);
            //Neu co du lieu chung to tien trinh bi die
            if (rsLog.next()) {
                return false;
            }
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "checkAppIDAnotherAlive", CommonUtils.getDateNow(), "NUll", e);
        } finally {
            try {
                if (rsLog != null) {
                    rsLog.close();
                }
                if (statementLog != null) {
                    statementLog.close();
                }

                if (connectionLog != null) {
                    connectionLog.close();
                }

            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "checkAppIDAnotherAlive", CommonUtils.getDateNow(), "Can not close connectionLog function checkAppIDAnotherAlive:", e2);
            }
        }
        return true;
    }

    public static void doSendLogMM(Connection connectionLog, Logger logMM) {
        PreparedStatement statementLog = null;
        ResultSet rsLog = null;
        // Xu ly giam sat MM
        try {
            if (connectionLog == null || connectionLog.isClosed()) {
                connectionLog = ConnectionPoolManager.getSMSConnection(logMM);
            }
            //connectionLog.setAutoCommit(false);

            // MM not alive
            String sql1 = "SELECT app_id FROM monitoring_app "
                    + "WHERE error_code_id = 0 "
                    + "AND (update_time + (?/1440)) < sysdate";

            statementLog = connectionLog.prepareStatement(sql1);
            statementLog.setInt(1, Parameters.DBTimeAlive);
            rsLog = statementLog.executeQuery();
            while (rsLog.next()) {
                String AppID = rsLog.getString("app_id");
                String para = AppID + " not alive";
                LogUtil.InfoExt(logMM, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "doSendLogMM", CommonUtils.getDateNow(), para, "Null");
            }

            // MM loi error
            /*String sql2 = "SELECT m.AppID, mrc.Contents FROM [Adm].[MonitoringApp] m "
                    + "JOIN [Adm].[MonitoringErrorCode] mrc on m.ErrorCodeID = mrc.MonitoringErrorCodeID "
                    + "WHERE m.ErrorCodeID <> 0 and DATEADD(MINUTE, "
                    + "?"
                    + ", m.UpdateTime) >= GETDATE()";
             */
            String sql2 = "SELECT a.app_id, b.content FROM monitoring_app a "
                    + "JOIN monitoring_error_code b on a.error_code_id = b.monitoring_error_code "
                    + "WHERE error_code_id <> 0 "
                    + "AND (update_time + (?/1440)) >= sysdate";
            statementLog = connectionLog.prepareStatement(sql2);
            statementLog.setInt(1, Parameters.DBTimeAlive);
            rsLog = statementLog.executeQuery();
            while (rsLog.next()) {
                String AppID = rsLog.getString("app_id");
                String Contents = rsLog.getString("content");
                LogUtil.InfoExt(loger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "doSendLogMM", CommonUtils.getDateNow(), AppID + " " + Contents, "Null");
            }
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "doSendLogMM", CommonUtils.getDateNow(), "Can not send log MM of .net tool:", e);

        } finally {
            try {
                if (rsLog != null) {
                    rsLog.close();
                }
                if (statementLog != null) {
                    statementLog.close();
                }

                if (connectionLog != null) {
                    connectionLog.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "doSendLogMM", CommonUtils.getDateNow(), "Can not close connectionLog", e2);
            }
        }
    }

    /**
     * Kiem tra co ap dung chuyen doi dau so tu 11 sang 10. true: chuyen doi
     *
     * @return
     */
    public static void getReceiverError() {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                connection = ConnectionPoolManager.getSMSConnection(loger);
            }

            String sql = "SELECT * FROM APP_SYS_CONFIG where SYS_CONFIG_ID=3";

            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            if (!rs.next()) {
                return;
            }

            String mobile = rs.getString("CONFIG_VALUE");
            if ("".equals(mobile)) {
                return;
            }
            lstMobile = Arrays.asList(mobile.split(","));

        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "getReceiverError", CommonUtils.getDateNow(), "NUll", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }

            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "getReceiverError", CommonUtils.getDateNow(), "Can not close connectionLog function checkAppIDAnotherAlive:", e2);
            }
        }

    }
}
