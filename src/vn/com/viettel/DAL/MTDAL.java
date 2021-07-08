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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import vn.com.viettel.BO.SmsMT;
import vn.com.viettel.BO.TimerConfigBO;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;

/**
 *
 * @author chiendd1
 */
public class MTDAL {

    private final static String CLASS_NAME = MTDAL.class.getName();
    private final static Logger LOGGER = Logger.getLogger("MTDAL");

    public static List<SmsMT> getSmsMt(Connection connection, PreparedStatement statement, ResultSet rs, String node, int contentType, Logger logger) throws SQLException, ParseException {

        //String sql = "";
        //String selectSt = (Parameters.MaxSMSInSession == -1) ? "" : "TOP " + Parameters.MaxSMSInSession;
        StringBuilder sqlQuey = new StringBuilder();

        sqlQuey.append("SELECT MT.* FROM SMS_MT MT ");
        sqlQuey.append("LEFT JOIN SMS_TIMER_CONFIG STC ON MT.SMS_TIMER_CONFIG_ID=STC.SMS_TIMER_CONFIG_ID ");
        sqlQuey.append(" WHERE (MT.SYNC_TIME IS NULL OR MT.SYNC_TIME<SYSDATE)");
        sqlQuey.append(" AND (MT.SMS_TIMER_CONFIG_ID IS NULL OR (MT.SMS_TIMER_CONFIG_ID IS NOT NULL AND STC.SEND_TIME<SYSDATE))");

        //sqlQuey.append((Parameters.MaxSMSInSession == -1) ? "" : "TOP " + Parameters.MaxSMSInSession);
        /*sqlQuey.append(" m.* FROM [SMS].[MT] m");
        sqlQuey.append(" LEFT JOIN SMS.TimerConfig tc on m.TimerConfigID=tc.TimerConfigID ");
        //.append(" AND (tc.TimerConfigID is null OR ").append("( tc.TimerConfigID is not null AND tc.SendTime<=GETDATE()))");

        sqlQuey.append(" WHERE m.RETRY_NUM <").append(Parameters.MaxRetryTimes);
        sqlQuey.append(" AND (m.SEND_TIME is null or m.SEND_TIME < GETDATE()) ")
                .append(" AND (tc.TimerConfigID is null OR ").append("( tc.TimerConfigID is not null AND tc.SendTime<=GETDATE()))");
         */
        int rowNum = Parameters.MaxSMSInSession > 0 ? Parameters.MaxSMSInSession : 1000;
        sqlQuey.append(" AND  ROWNUM<=").append(rowNum);
        sqlQuey.append(" AND  MT.RETRY_NUM <").append(Parameters.MaxRetryTimes);
        if (node.length() > 0) {
            sqlQuey.append(" AND MOD(TO_NUMBER(SUBSTR(MT.USER_ID,-1,1)),").append(Parameters.CheckListNodes.length).append(")");
            //sqlQuey.append(" AND CONVERT(INT, RIGHT(m.USER_ID, 1)) %").append(Parameters.CheckListNodes.length);
            sqlQuey.append(" = ").append(node);
        }

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        Date userDate = parser.parse(parser.format(cal.getTime()));

        //Neu thoi gian gui tu 
        if (contentType == GlobalConstant.SEND_UNICODE) {
            sqlQuey.append(" AND MT.CONTENT_TYPE = 1 ");
        } else if (contentType == GlobalConstant.SEND_NORMAL) {
            //Neu duoc phep gui tu dong thi lay cac ban tin duoc gui tu dong
            if (userDate.after(Parameters.StartAutoTime) && userDate.before(Parameters.EndAutoTime)) {
                sqlQuey.append(" AND (MT.CONTENT_TYPE is null or MT.CONTENT_TYPE != 1)");
            } else {
                //chi lay cac ban tin gui chu dong
                sqlQuey.append(" AND (MT.CONTENT_TYPE is  not null AND MT.CONTENT_TYPE != 1)");
            }
        }
        if (!"".equals(Parameters.SEND_MOBILE) && Parameters.SEND_MOBILE.length() > 0) {
            // sqlQuey.append(" AND m.USER_ID='").append(Parameters.SEND_MOBILE).append("'");
            sqlQuey.append("AND MT.USER_ID IN(SELECT regexp_substr('").append(Parameters.SEND_MOBILE);
            sqlQuey.append("','[^,]+', 1, level) FROM DUAL ");
            sqlQuey.append(" CONNECT BY regexp_substr('").append(Parameters.SEND_MOBILE).append("', '[^,]+', 1, level) IS NOT NULL)");
        }

        sqlQuey.append(" order by MT.TIME_SEND_REQUEST  asc ");
        //logger.info("Cau lenh thuc hien=" + sqlQuey.toString());
        List<SmsMT> lstMt = new ArrayList<>();
        SmsMT objMt;
        statement = connection.prepareStatement(sqlQuey.toString());
        rs = statement.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                objMt = new SmsMT(rs);
                lstMt.add(objMt);
            }
        }
        return lstMt;
    }

    /**
     * Cap nhat tin nhan sang trang thai dang gui
     *
     * @param connection
     * @param lstSendSMS
     * @param logger
     * @return
     * @throws SQLException
     */
    public static boolean UpdateStatusSMSSending(Connection connection, List<SmsMT> lstSendSMS, Logger logger) throws SQLException {
        PreparedStatement statement = null;
        PreparedStatement statTimer = null;
        try {
            //connection.setAutoCommit(false);
            //long start = System.currentTimeMillis();
            String sqlUpdateMT = "Update SMS_MT set SYNC_TIME = (SYSDATE+?/24) where REQUEST_ID = ?";
            statement = connection.prepareStatement(sqlUpdateMT);
            List<TimerConfigBO> lstTimer = new ArrayList<>();
            TimerConfigBO timerBO = null;
            int sendTime = Parameters.SEND_TIME == 0 ? 1 : Parameters.SEND_TIME;
            for (SmsMT objmt : lstSendSMS) {
                statement.setInt(1, sendTime);
                statement.setString(2, objmt.getREQUEST_ID());
                statement.addBatch();

                //Lay lich gui tin
                if (!"".equals(objmt.getSMS_TIMER_CONFIG_ID())) {
                    timerBO = new TimerConfigBO();
                    timerBO.setTimerConfigId(objmt.getSMS_TIMER_CONFIG_ID());
                    timerBO.setSchoolId(objmt.getUNIT_ID());
                    timerBO.setPartitionId(objmt.getUNIT_ID() % 100);
                    if (!lstTimer.contains(timerBO)) {
                        lstTimer.add(timerBO);
                    }
                }
            }
            statement.executeBatch();

            //Cap nhat trang thai hen gio gui tin
            if (!lstTimer.isEmpty()) {
                String sqlUpdateTimer = "UPDATE SMS_TIMER_CONFIG set Status=2,UPDATE_TIME=sysdate WHERE PARTITION_ID=? AND SCHOOL_ID=? and SMS_TIMER_CONFIG_ID=? and Status=1";
                statTimer = connection.prepareStatement(sqlUpdateTimer);
                for (TimerConfigBO timer : lstTimer) {
                    statTimer.setInt(1, timer.getPartitionId());
                    statTimer.setInt(2, timer.getSchoolId());
                    statTimer.setString(3, timer.getTimerConfigId());
                    statTimer.addBatch();
                }
                statTimer.executeBatch();
            }

            //connection.commit();
            //long ecuteTime = TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - start);
            //LogUtil.writeInfoLog(logger, logsms, "UpdateStatusSMSSending thoi gian thuc hien  cap nhat " + lstSendSMS.size() + " ban ghi la " + ecuteTime + " ms");
            return true;
        } catch (SQLException e) {
            connection.rollback();
            LogUtil.ErrorExt(logger, CLASS_NAME, "UpdateStatusSMSSending", CommonUtils.getDateNow(), "NULL", e);
            return false;
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (statTimer != null) {
                statTimer.close();
            }
            connection.setAutoCommit(true);
        }
    }

    /**
     * Cap nhat trang thai gui tin
     *
     * @param objMT
     * @param sendResult
     * @param serviceId
     * @param logger
     * @throws SQLException
     */
    public static void UpdateSMSHistory(SmsMT objMT, boolean sendResult, String serviceId, Logger logger) throws SQLException {
        PreparedStatement stmt = null;
        Connection connectionThread = null;
        try {
            if (connectionThread == null || connectionThread.isClosed()) {
                connectionThread = ConnectionPoolManager.getSMSConnection(LOGGER);
            }
            if (connectionThread == null) {
                LOGGER.info("UpdateSMSHistory, khong ket noi duoc CSDL");
                return;
            }
            int retryNum = objMT.getRETRY_NUM();
            String phone = objMT.getUSER_ID();
            String historyID = objMT.getHISTORY_RAW_ID();
            String requestID = objMT.getREQUEST_ID();
            int unitId = objMT.getUNIT_ID();
            Date eventDate = CommonUtils.getDateNow();
            //LogUtil.writeInfoLog(logsms, logger, "INFO: thong tin MT  requestID: " + requestID + " historyID: " + historyID + " unitId: " + unitId);

            if (sendResult == true) {
                stmt = connectionThread.prepareCall("{call SP_PROCESS_SEND_SMS(?,?,?,?,?,?,?)}");
                stmt.setString(1, requestID);
                stmt.setInt(2, 1);// @IS_SUCCESS
                stmt.setString(3, historyID);
                // status in sms.mt
                stmt.setInt(4, GlobalConstant.SEND_SUCCESS);
                stmt.setInt(5, retryNum);
                stmt.setString(6, serviceId);
                stmt.setInt(7, unitId);
                stmt.execute();
                String para = "Mobile=" + phone + "requestID: " + requestID + " historyID: " + historyID + " unitId: " + unitId;
                LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", eventDate, para, "Send Success SMS");

            } else {
                int numRetryWhenFail = retryNum + 1;

                if (numRetryWhenFail >= Parameters.MaxRetryTimes) {
                    stmt = connectionThread.prepareCall("{call SP_PROCESS_SEND_SMS(?,?,?,?,?,?,?)}");

                    stmt.setString(1, requestID);
                    stmt.setInt(2, 0);// @IS_SUCCESS
                    stmt.setString(3, historyID);
                    // Het so lan retry: STATUS MT = 4
                    stmt.setInt(4, GlobalConstant.RETRY_FAILURE);
                    stmt.setInt(5, numRetryWhenFail);
                    stmt.setString(6, serviceId);
                    stmt.setInt(7, unitId);
                    stmt.execute();
                    String para = "requestID: " + requestID + " historyID: " + historyID + " unitId: " + unitId + " -mobile: " + phone + " - " + " Retry..." + String.valueOf(numRetryWhenFail);
                    LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", eventDate, para, "Send Unsuccess SMS");
                } else {
                    // update row table SMS.MT (retry ++)
                    stmt = connectionThread.prepareCall("{call SP_PROCESS_SEND_SMS(?,?,?,?,?,?,?)}");

                    stmt.setString(1, requestID);
                    stmt.setInt(2, 0);
                    stmt.setString(3, historyID);
                    stmt.setInt(4, GlobalConstant.RETRY_NORMAL); // STATUS
                    // MT
                    stmt.setInt(5, numRetryWhenFail);
                    stmt.setString(6, serviceId);
                    stmt.setInt(7, unitId);
                    stmt.execute();

                    String para = "requestID: " + requestID + " historyID: " + historyID + " unitId: " + unitId + " -mobile: " + phone + " - " + " Retry..." + String.valueOf(numRetryWhenFail);
                    LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", eventDate, para, "Send Unsuccess SMS");
                }
            }
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage(), ex);
            LOGGER.error(ex.getStackTrace(), ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connectionThread != null) {
                    connectionThread.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(LOGGER, CLASS_NAME, "UpdateSMSHistory", CommonUtils.getDateNow(), "Cannot UpdateSMSHistory:", e2);
            }
        }

    }

    /**
     * Them tin nhan canh bao vao MT
     *
     * @param connectionLog
     * @param content
     * @param mobile
     */
    public static void insertMassageWarning(Connection connectionLog, String content, String mobile) {
        CallableStatement stmtLog = null;
        //Connection connectionLog = null;
        try {
            if (connectionLog == null || connectionLog.isClosed()) {
                connectionLog = ConnectionPoolManager.getSMSConnection(LOGGER);
            }
            if (connectionLog == null) {
                return;
            }
            stmtLog = connectionLog.prepareCall("{call SP_INSERT_SMS (?, ?)}");
            stmtLog.setString(1, content);
            stmtLog.setString(2, mobile);
            stmtLog.execute();
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(LOGGER, CLASS_NAME, "insertMassageWarning", CommonUtils.getDateNow(), "Cannot insert MT:" + e.getMessage(), e);
        } finally {
            try {
                if (stmtLog != null) {
                    stmtLog.close();
                }
                if (connectionLog != null) {
                    connectionLog.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(LOGGER, CLASS_NAME, "insertMassageWarning", CommonUtils.getDateNow(), "Cannot close connect insert MT:", e2);
            }
        }
    }
}
