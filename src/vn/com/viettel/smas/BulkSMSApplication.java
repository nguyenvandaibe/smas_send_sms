/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: anhvd9 $
 */
package vn.com.viettel.smas;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import vn.com.viettel.BO.SmsQueue;
import vn.com.viettel.DAL.MTDAL;
import vn.com.viettel.DAL.TimerDAL;
import vn.com.viettel.DAL.UtilBusnisness;
import vn.com.viettel.bulk.SmsBrandNameWs;
import vn.com.viettel.bulk.SmsBulkNonUnicodeWs;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.thread.KThreadPoolExecutor;
import vn.com.viettel.util.*;
import ws.bulkSms.impl.Result;
import ws.bulkSms.impl.ResultBO;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BulkSMSApplication {

    private final Logger log;
    public Logger logMM;
    private BulkSMSApplication instance;

    ProcessBulkSendSMS parentProcess;

    private final static String CLASS_NAME = BulkSMSApplication.class.getName();

    public BulkSMSApplication() {
        this.log = Logger.getLogger(GlobalConstant.PROCESS_BULK_SMS);
    }

    public BulkSMSApplication getInstance() {
        if (instance == null) {
            instance = new BulkSMSApplication();
        }
        return instance;
    }

    public boolean sendSMS(String serviceID, String sms, String isdn) {
        boolean res = false;
        String para = "Mobile=" + isdn + "sms=" + sms + " serviceID=" + serviceID;
        Date eventDate = CommonUtils.getDateNow();
        try {
            if (Parameters.IS_BULK_SMS) {

                ResultBO ret = SmsBulkNonUnicodeWs.sendSms("1", isdn, isdn, serviceID, sms, false);

                if (ret != null) {
                    if (new Long(1).equals(ret.getResult())) {
                        res = true;
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "sendSMS", eventDate, para, "Gui tin nhan qua BulkSMS");
                    } else {
                        String content = ret.getMessage();
                        log.error(ret.getMessage() + " Result: " + ret.getResult().toString());
                        UtilBusnisness.writeLog(Parameters.CheckListAppID, content,isdn);
                    }
                } else {
                    log.error("null result");
                }
            } else {
                //Gui tin nhan qua smsbrandName
                Result ret = SmsBrandNameWs.sendSms("1", isdn, isdn, serviceID, sms, false);

                if (ret != null) {
                    if (new Long(1).equals(ret.getResult())) {
                        res = true;
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "sendSMS", eventDate, para, "Gui tin nhan qua SMSBrandName");
                    } else {
                        String content = ret.getMessage();
                        log.error(ret.getMessage() + " Result: " + ret.getResult().toString());
                        UtilBusnisness.writeLog(Parameters.CheckListAppID, content,isdn);
                    }
                } else {
                    log.error("null result");
                }
            }
        } catch (RemoteException e) {
            //String content = e.getMessage();
            log.error(e.getMessage(), e);
            //UtilBusnisness.writeLog(Parameters.CheckListAppID, content);

        } catch (Exception e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "sendSMS", eventDate, para, e);
        }
        return res;
    }

    public boolean sendSMSProcess(String serviceID, int maxLength, String sms, String isdn, String historyID) {
        boolean res = false;
        String para = "isdn=" + isdn + " sms=" + sms + "serviceID=" + serviceID + " historyID=" + historyID;
        Date eventDate = CommonUtils.getDateNow();
        try {
            String mobile = isdn;
            if (sms.length() <= maxLength) {
                if (!sms.isEmpty()) {
                    res = sendSMS(serviceID, sms, mobile);
                }

                //Gui tin nhan den dau so chuyen doi
            } else {

                String smsStr = sms;
                String subSMS;
                boolean IsFirst = true;
                String contStr = "(tiep):";
                boolean Is1SMS = false;

                while (!("".equals(smsStr))) {
                    if (smsStr.length() > maxLength) {
                        // Neu la tin nhan dau tien
                        if (IsFirst) {
                            subSMS = smsStr.substring(0, maxLength);
                            int lastSpace = subSMS.lastIndexOf(" ");
                            if (lastSpace != -1) {
                                subSMS = subSMS.substring(0, lastSpace);
                                smsStr = smsStr.substring(lastSpace + 1,
                                        smsStr.length());
                                // Danh dau con tin
                                Is1SMS = true;
                            } else {
                                subSMS = smsStr;
                                smsStr = "";
                            }
                        } else // neu cat tin thi them dung format
                        {

                            smsStr = contStr + smsStr;
                            // Neu sau khi them noi dung vuot qua MAX_LENGTH
                            if (smsStr.length() > maxLength) {
                                subSMS = smsStr.substring(0, maxLength);
                                int lastSpace = subSMS.lastIndexOf(" ");
                                if (lastSpace != -1) {
                                    // Cat toi ky tu khoang trang cuoi cung de
                                    // lay Noi dung co nghia
                                    subSMS = subSMS.substring(0, lastSpace);
                                    smsStr = smsStr.substring(lastSpace + 1,
                                            smsStr.length());
                                    // Danh dau con tin
                                    Is1SMS = true;
                                } else {
                                    subSMS = smsStr;
                                    smsStr = "";
                                }
                            } else {
                                subSMS = smsStr;
                                smsStr = "";
                            }
                        }
                        // Danh dau het tin dau tien
                        IsFirst = false;
                    }else if (Is1SMS) { // neu con cung 1 tin
                        smsStr = contStr + smsStr;
                        // Neu sau khi them noi dung vuot qua MAX_LENGTH
                        if (smsStr.length() > maxLength) {
                            subSMS = smsStr.substring(0, maxLength);
                            int lastSpace = subSMS.lastIndexOf(" ");
                            if (lastSpace != -1) {
                                // Cat toi ky tu khoang trang cuoi cung de
                                // lay Noi dung co nghia
                                subSMS = subSMS.substring(0, lastSpace);
                                smsStr = smsStr.substring(lastSpace + 1,
                                        smsStr.length());
                                // Danh dau con tin
                                Is1SMS = true;
                            } else {
                                subSMS = smsStr;
                                smsStr = "";
                            }
                        } else {
                            subSMS = smsStr;
                            smsStr = "";
                        }
                    } else {
                        subSMS = smsStr;
                        smsStr = "";
                    }
                    if (!"".equals(subSMS)) {
                        res = sendSMS(serviceID, subSMS, mobile);
                        Thread.sleep(GlobalConstant.SLEEP_TIME_2_SMS);
                    }

                }
            }
        } catch (InterruptedException e) {

            LogUtil.ErrorExt(log, CLASS_NAME, "sendSMSProcess", eventDate, para, e);
        }
        return res;
    }

    class BulkSMSTransaction {

        public Connection connection;
        //public Connection connectionLog;
        ResultSet rs;
        ResultSet rsLog;
        PreparedStatement statement;
        PreparedStatement statementLog;
        CallableStatement stmtLog = null;

        public BulkSMSTransaction() {
            connection = ConnectionPoolManager.getSMSConnection(logMM);
            //connectionLog = ConnectionPoolManager.getSMSConnection(logMM);
        }

        public void doWorkWithActiveMod() throws ParseException {

            log.info("Node active: " + Parameters.CheckListNodes[0]);

            this.doWorkSchool(Parameters.CheckListNodes[0]);// thuc hien
            // nghiep vu
            // theo mod cua
            // minh
            /*for (int i = 1; i < Parameters.CheckListNodes.length; i++) {
                // kiem tra cac mod khac
                String node = Parameters.CheckListNodes[i];
                if (UtilBusnisness.checkAppIDAnotherAlive(connectionLog, log, node, Parameters.CheckListAppID)) {// neu mod con song
                    
                    break;// thoat ra khoi vong lap kiem tra mod
                } else {// neu mode chet                                      
                    LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "doWorkWithActiveMod", CommonUtils.getDateNow(), "node=" + node, " standby: ");
                    this.doWorkSchool(node);// thuc hien nghiep vu cua mod
                    // bi chet
                }
            }*/

        }

        public void doWorkSchool(String node) throws ParseException {
            Date eventDate = CommonUtils.getDateNow();
            String para = "Node=" + node;
            try {
                //log.info("Danh dau node "+ Parameters.CheckListAppID + " dang hoat dong");
                while (connection == null || connection.isClosed()) {
                    connection = ConnectionPoolManager.getSMSConnection(logMM);
                    if (connection == null) {
                        return;
                    }
                }

                //connection.setAutoCommit(false);
                //lay danh sach tin nhan khong dau
                List<SmsQueue> lstSMS = MTDAL.getSmsQueue(connection, statement, rs, node, GlobalConstant.SEND_NORMAL, log);
                if (lstSMS == null || lstSMS.isEmpty()) {
                    //LogUtil.writeInfoLog(logger, "Khong co du lieu gui tin");
                    LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "doWorkSchool", CommonUtils.getDateNow(), "Node=" + node, "Khong co du lieu");
                    return;
                }

//                MTDAL.UpdateStatusSMSSending(connection, lstSMS, log);
                //this.processSendSMS(lstSMS);
                SendNotification sendNotification = new SendNotification();
                for (SmsQueue queueItem : lstSMS) {
                    //LogUtil.writeInfoLog(logger, log, "Gui tin den so: " + objMt.getUSER_ID());
                    processSendSMS(queueItem);

//                    if (!"".equals(queueItem.getDEVICE_TOKEN()) && objMt.getDEVICE_TOKEN() != null) {
//                        try {
//                            log.info("Send notification");
//                            sendNotification.processSendNotification(objMt);
//                        } catch (Exception ex) {
//                            logMM.error(ex.getMessage(), ex);
//                        }
//
//                    }

                }
            } catch (SQLException | NumberFormatException e) {
                LogUtil.ErrorExt(log, CLASS_NAME, "doWorkSchool", eventDate, para, e);
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
                } catch (SQLException e) {
                    LogUtil.ErrorExt(log, CLASS_NAME, "doWorkSchool", eventDate, para, e);
                }
            }
        }

        void processSendSMS(final SmsQueue queueItem) {
            KThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    String para = queueItem.getPara();
                    Date eventDate = CommonUtils.getDateNow();
                    //Connection connectionThread = null;
                    //CallableStatement stmt = null;

                    String serviceID = Parameters.SERVICE_ID_OTHER;

                    int maxLength = Parameters.SMS_P1_MAXLENGTH_OTHER;
                    Boolean result = false;
                    //LogUtil.writeInfoLog(logger, log, "Bat dau gui tin nhan den so: " + objSms.getUSER_ID());
                    // Xoa ky tu dac biet va ky tu unicode
                    String sms = StringUtil.removeSpecialChar(queueItem.getSmsContent().trim());
                    //String phone = objSms.getUSER_ID();
                    StringBuilder strphone = new StringBuilder(String.valueOf(Parameters.CountryCode));
                    strphone.append(UtilBusnisness.getISDN(queueItem.getReceiverId()));
                    String phone = strphone.toString().trim();

//                    String historyID = objSms.getHISTORY_RAW_ID();
//                    int isNumberRouting = objSms.getIS_NUMBER_ROUTING();
//                    String currOperator = objSms.getCURR_OPERATOR();
//                    int isHome = objSms.getIS_HOME();
//
//                    if (PreNumberTelcoDAL.isVinaPhoneNumber(phone, isNumberRouting, currOperator)) {
//                        // Cat ky tu xuong dong (newline) doi voi dau so Vina
//                        sms = StringUtil.removeNewlineChar(sms);
//                        //Lay brandName
//                        if (!"".equals(objSms.getSERVICE_ID()) && objSms.getSERVICE_ID() != null) {
//                            serviceID = objSms.getSERVICE_ID();
//                        } else {
//                            serviceID = Parameters.SERVICE_ID_OTHER;
//                        }
//                    } else if (PreNumberTelcoDAL.isViettelPhoneNumber(phone, isNumberRouting, isHome)) {
//                        maxLength = Parameters.SMS_P1_MAXLENGTH_VIETTEL;
//
//                        //Lay brandName
//                        if (!"".equals(objSms.getSERVICE_ID()) && objSms.getSERVICE_ID() != null) {
//                            serviceID = objSms.getSERVICE_ID();
//                        } else {
//                            serviceID = Parameters.SERVICE_ID_VIETTEL;
//                        }
//                    } else//Mang mobi, VNmobi...
//                    if (!"".equals(queueItem.getServiceId()) && queueItem.getServiceId() != null) {
//                        serviceID = queueItem.getServiceId();
//                    } else {
//                        serviceID = Parameters.SERVICE_ID_OTHER;
//                    }
                    serviceID = queueItem.getServiceId();
                    try {

                        // Thuc hien gui tin nhan
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_END_ACTION, CLASS_NAME, "processSendSMS", eventDate, para, "start send sms");
                        //Random random = new Random();
                        //result = random.nextBoolean(); 
                        result = sendSMSProcess(serviceID, maxLength, sms, phone, UUID.randomUUID().toString());
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_END_ACTION, CLASS_NAME, "processSendSMS", eventDate, para, "end send sms, result=" + result);
                        //Cap nhat trang thai tin nhan trong history
//                        MTDAL.UpdateSMSHistory(queueItem, result, serviceID, log);
                        logMM.log(Priority.toPriority(Priority.INFO_INT), "Cap nhat trang thai tin nhan trong history");

                        LogUtil.InfoExt(log, "INFO", CLASS_NAME, "processSendSMS", CommonUtils.getDateNow(), para, "OK");
                    } catch (Exception e) {
                        LogUtil.ErrorExt(log, CLASS_NAME, "processSendSMS", CommonUtils.getDateNow(), para, e);
                    }

                    // end process send sms
                }

            });
        }

        public void DeleteTimer() throws ParseException {
            try {
                if (connection == null || connection.isClosed()) {
                    connection = ConnectionPoolManager.getSMSConnection(logMM);
                    if (connection == null) {
                        return;
                    }
                }
                TimerDAL.DeleteEndTimer(connection, log);
            } catch (SQLException | NumberFormatException e) {
                LogUtil.ErrorExt(log, CLASS_NAME, "DeleteTimer", CommonUtils.getDateNow(), "Null", e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    LogUtil.ErrorExt(log, CLASS_NAME, "DeleteTimer", CommonUtils.getDateNow(), "Null", e);
                }
            }
        }
    }
}
