package vn.com.viettel.DAL;


import org.apache.log4j.Logger;
import vn.com.viettel.BO.SmsQueue;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;

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

public class QueueDAL {
    private final static String CLASS_NAME = QueueDAL.class.getName();
    private final static Logger LOGGER = Logger.getLogger("MTDAL");

    /**
     * Lấy ra các hàng trong bảng SmsQueue
     *
     * @param connection
     * @param statement
     * @param rs
     * @param node
     * @param contentType
     * @param logger
     * @return
     * @throws SQLException
     */
    public static List<SmsQueue> getSmsQueue(Connection connection, PreparedStatement statement, ResultSet rs, String node, int contentType, Logger logger) throws SQLException, ParseException {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT sq.* FROM SmsQueue sq");
        sql.append(" LEFT JOIN SmsTimerConfig stc ON sq.SmsTimerConfigId=stc.Id ");
        sql.append(" WHERE (sq.SyncTime IS NULL OR sq.SyncTime < SYSDATE()) ");

        sql.append(" AND (sq.SmsTimerConfigId IS NULL OR (sq.SmsTimerConfigId IS NULL AND stc.SendTime < SYSDATE()))");

        sql.append(" AND sq.RetryNum <= ").append(Parameters.MaxRetryTimes);

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        Date userDate = parser.parse(parser.format(cal.getTime()));

        //Neu thoi gian gui tu
        if (contentType == GlobalConstant.SEND_UNICODE) {
            sql.append(" AND sq.ContentType = 1 ");
        } else if (contentType == GlobalConstant.SEND_NORMAL) {
            //Neu duoc phep gui tu dong thi lay cac ban tin duoc gui tu dong
            if (userDate.after(Parameters.StartAutoTime) && userDate.before(Parameters.EndAutoTime)) {
                sql.append(" AND (sq.ContentType IS NULL or sq.ContentType != 1)");
            } else {
                //chi lay cac ban tin gui chu dong
                sql.append(" AND (sq.ContentType IS NOT NULL AND sq.ContentType != 1)");
            }
        }

        if (!"".equals(Parameters.SEND_MOBILE) && Parameters.SEND_MOBILE.length() > 0) {
            sql.append("AND sq.UserId IN (").append(Parameters.SEND_MOBILE).append(")");
        }

        sql.append(" ORDER BY sq.CreationTime DESC");
        int rowNum = Parameters.MaxSMSInSession > 0 ? Parameters.MaxSMSInSession : 1000;
        sql.append(" LIMIT ").append(rowNum).append(" OFFSET " + 0);

        List<SmsQueue> listSmsQueue = new ArrayList<>();
        SmsQueue queueItem;
        statement = connection.prepareStatement(sql.toString());
        rs = statement.executeQuery();
        if (rs != null) {
            while (rs.next()) {
                queueItem = new SmsQueue(rs);
                listSmsQueue.add(queueItem);
            }
        }
        return listSmsQueue;
    }

    /**
     * Cập nhật trạng thái đang gửi tin
     *
     * @param connection
     * @param listSendSms
     * @param logger
     * @return
     * @throws SQLException
     */
    public static boolean UpdateStatusSMSSending(Connection connection, List<SmsQueue> listSendSms, Logger logger) throws SQLException {
        PreparedStatement statement = null;
        PreparedStatement statTimer = null;

        LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", CommonUtils.getDateNow(), "", "Cap nhat trang thai dang gui");
        return true;
        /*
        try {
            String sqlUpdateSmsQueue = "Update SmsQueue set SyncTime = (SYSDATE+?/24) where Id = ?";
            statement = connection.prepareStatement(sqlUpdateSmsQueue);
            List<TimerConfigBO> lstTimer = new ArrayList<>();
            TimerConfigBO timerBO = null;
            int sendTime = Parameters.SEND_TIME == 0 ? 1 : Parameters.SEND_TIME;
            for (SmsQueue objSmsQueue : listSendSms) {
                statement.setInt(1, sendTime);
                statement.setString(2, objSmsQueue.getId());
                statement.addBatch();

                //Lay lich gui tin
                if (!"".equals(objSmsQueue.getSmsTimerConfigId())) {
                    timerBO = new TimerConfigBO();
                    timerBO.setTimerConfigId(objSmsQueue.getSmsTimerConfigId());
//                    timerBO.setSchoolId(objSmsQueue.getSenderUnitId());
//                    timerBO.setPartitionId(objmt.getUNIT_ID() % 100);
                    if (!lstTimer.contains(timerBO)) {
                        lstTimer.add(timerBO);
                    }
                }
            }
            statement.executeBatch();

            //Cap nhat trang thai hen gio gui tin
            if (!lstTimer.isEmpty()) {
                String sqlUpdateTimer = "UPDATE SmsTimerConfig set Status=2, LastModificationTime=sysdate WHERE SchoolId=? and Id=? and Status=1";
                statTimer = connection.prepareStatement(sqlUpdateTimer);
                for (TimerConfigBO timer : lstTimer) {
//                    statTimer.setInt(1, timer.getPartitionId());
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
         */
    }

    /**
     * Cập nhật trạng thái tin đã gửi
     *
     * @param objSmsQueue
     * @param sendResult
     * @param serviceId
     * @param logger
     */
    public static void UpdateSmsHistory(SmsQueue objSmsQueue, boolean sendResult, String serviceId, Logger logger) {
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
            int retryNum = objSmsQueue.getRetryNum();
            String phone = objSmsQueue.getMobile();
            String requestID = objSmsQueue.getId();
            String unitId = objSmsQueue.getSenderUnitId();
            Date eventDate = CommonUtils.getDateNow();

            if (sendResult == true) {
                String para = "Mobile=" + phone + "requestID: " + requestID + " unitId: " + unitId;
                LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", eventDate, para, "Send Success SMS");
            } else {
                LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "UpdateSMSHistory", eventDate, null, "Send SMS failed");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connectionThread != null) {
                    connectionThread.close();
                }
            } catch (SQLException e2) {
                LogUtil.ErrorExt(LOGGER, CLASS_NAME, "UpdateSmsHistory", CommonUtils.getDateNow(), "Cannot UpdateSMSHistory:", e2);
            }
        }
    }
}
