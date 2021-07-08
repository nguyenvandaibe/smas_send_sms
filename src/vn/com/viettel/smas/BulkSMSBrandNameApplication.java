/*
 *	Copyright (c)  2014. All rights reserved.
 * 
 *  $Author: anhvd9 $
 */
package vn.com.viettel.smas;

import vn.com.viettel.util.Parameters;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import vn.com.viettel.BO.SmsMT;
import vn.com.viettel.DAL.MTDAL;
import vn.com.viettel.DAL.PreNumberTelcoDAL;
import vn.com.viettel.DAL.UtilBusnisness;

import vn.com.viettel.bulk.SmsBrandNameWs;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.util.StringUtil;
import vn.com.viettel.thread.KThreadPoolExecutor;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import ws.bulkSms.impl.Result;

public class BulkSMSBrandNameApplication {
    
    private final Logger log = Logger.getLogger(GlobalConstant.PROCESS_SMS_BRANDNAME);
    public Logger logMM;
    private final static String CLASS_NAME = BulkSMSBrandNameApplication.class.getName();
    private BulkSMSBrandNameApplication instance;
    ProcessBrandNameSendSMS parentProcess;
    
    public BulkSMSBrandNameApplication getInstance() {
        if (instance == null) {
            instance = new BulkSMSBrandNameApplication();
        }
        return instance;
    }
    
    public boolean sendSMS(String serviceID, String sms, String isdn) {
        boolean res = false;
        String para = "Mobile=" + isdn + " Content=" + sms + " serviceID=" + serviceID;
        Date eventDate = CommonUtils.getDateNow();
        try {
            LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_START_ACTION, CLASS_NAME, "sendSMS", eventDate, para, "Begin sendSMS phone: " + isdn);
            Result ret = SmsBrandNameWs.sendSms("1", isdn, isdn, serviceID, sms, true);
            
            if (ret != null) {
                if (new Long(1).equals(ret.getResult())) {
                    res = true;
                    LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "sendSMS", eventDate, para, "function sendSmS to: " + isdn);
                } else {
                    String content = ret.getMessage();
                    log.error(ret.getMessage() + " Result: " + ret.getResult().toString());
                    
                    UtilBusnisness.writeLog(Parameters.CheckListAppID_UniCode, content,isdn);
                }
            } else {
                log.error("#Error Message: null result");
            }
        } catch (RemoteException e) {
            //String content = e.getMessage();
            log.error(e.getMessage(), e);
            //UtilBusnisness.writeLog(Parameters.CheckListAppID_UniCode, content);

        } catch (Exception e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "sendSMS", eventDate, para, e);
        }
        return res;
        
    }
    
    public boolean sendSMSProcess(String serviceID, int maxLength, String sms, String isdn, String historyID) {
        boolean res = false;
        String para = "Mobile=" + isdn + " Content=" + sms + " serviceID=" + serviceID + " historyID=" + historyID;
        Date eventDate = CommonUtils.getDateNow();
        try {
            String mobile = isdn;
            
            if (sms.length() <= maxLength) {
                res = sendSMS(serviceID, sms, mobile);
            } else {
                
                String smsStr = sms;
                String subSMS;
                boolean IsFirst = true;
                String contStr = "(tiếp):";
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
                    } else if (Is1SMS) { // neu con cung 1 tin
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
                    //res = sendSMS(serviceID, subSMS, mobile);
                     if (!"".equals(subSMS)) {
                        res = sendSMS(serviceID, subSMS, mobile);
                        Thread.sleep(GlobalConstant.SLEEP_TIME_2_SMS);
                    }
                    
                }
            }
        } catch (InterruptedException e) {
            LogUtil.ErrorExt(log, CLASS_NAME, "sendSMS", eventDate, para, e);
        }
        return res;
    }
    
    class BulkSMSBrandNameTransaction {
        
        public Connection connection;
        //public Connection connectionLog;
        ResultSet rs;
        ResultSet rsLog;
        PreparedStatement statement;
        PreparedStatement statementLog;
        CallableStatement stmtLog = null;
        
        public BulkSMSBrandNameTransaction() {
            connection = ConnectionPoolManager.getSMSConnection(logMM);
            //connectionLog = ConnectionPoolManager.getSMSConnection(logMM);
        }
        
        public void doWorkWithActiveMod() throws ParseException {
            
            this.doWorkSchool(Parameters.CheckListNodes[0]);
            
           
        }
        
        public void doWorkSchool(String node) throws ParseException {
            try {
                
                while (connection == null || connection.isClosed()) {
                    connection = ConnectionPoolManager.getSMSConnection(logMM);
                    if (connection == null) {
                        return;
                    }
                }
                //Lay danh sach tin nhan co dau
                List<SmsMT> lstSMS = MTDAL.getSmsMt(connection, statement, rs, node, GlobalConstant.SEND_UNICODE, log);
                if (lstSMS == null || lstSMS.isEmpty()) {
                    LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "doWorkSchool", CommonUtils.getDateNow(), "Node=" + node, "Khong co du lieu");
                    return;
                }
                //Cap nhat tin nhan sang trang thai dang gui
                MTDAL.UpdateStatusSMSSending(connection, lstSMS, log);
                SendNotification sendNotification = new SendNotification();
                for (SmsMT objMt : lstSMS) {
                    //LogDAL.writeInfoLog(logger, log, "Gui tin den so: " + objMt.getUSER_ID());
                    processSendSMS(objMt);
                    
                    if (!"".equals(objMt.getDEVICE_TOKEN()) && objMt.getDEVICE_TOKEN() != null) {
                        try {
                            log.info("Send notification");
                            sendNotification.processSendNotification(objMt);
                        } catch (Exception ex) {
                            logMM.error(ex.getMessage(), ex);
                        }
                        
                    }
                }
                
            } catch (SQLException | NumberFormatException e) {
                LogUtil.ErrorExt(log, CLASS_NAME, "doWorkSchool", CommonUtils.getDateNow(), "Node=" + node, e);
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
                } catch (SQLException ex) {
                    LogUtil.ErrorExt(log, CLASS_NAME, "doWorkSchool", CommonUtils.getDateNow(), "Node=" + node, ex);
                }
            }
        }
        
        void processSendSMS(final SmsMT objSms) {
            KThreadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String para = objSms.getPara();
                    Date eventDate = CommonUtils.getDateNow();

                    //Connection connectionThread = null;
                    //CallableStatement stmt = null;
                    String serviceID = Parameters.SERVICE_ID_OTHER;
                    int maxLength = Parameters.SMS_P2_MAXLENGTH_OTHER;
                    Boolean result = false;
                    
                    String sms = objSms.getSMS_CONTENT();
                    //String phone = objSms.getUSER_ID();
                    StringBuilder strphone = new StringBuilder(String.valueOf(Parameters.CountryCode));
                    strphone.append(UtilBusnisness.getISDN(objSms.getUSER_ID()));
                    String phone = strphone.toString().trim();
                    
                    String historyID = objSms.getHISTORY_RAW_ID();
                    
                    int isNumberRouting=objSms.getIS_NUMBER_ROUTING();
                    String currOperator=objSms.getCURR_OPERATOR();
                    int isHome=objSms.getIS_HOME();
                    
                    if (PreNumberTelcoDAL.isVinaPhoneNumber(phone, isNumberRouting,currOperator)) {
                        // Cat ky tu xuong dong (newline) doi voi dau so Vina
                        sms = StringUtil.removeNewlineChar(sms);
                        //Lay brandName
                        if (!"".equals(objSms.getSERVICE_ID()) && objSms.getSERVICE_ID() != null) {
                            serviceID = objSms.getSERVICE_ID();
                        } else {
                            serviceID = Parameters.SERVICE_ID_OTHER;
                        }
                        
                    } else if (PreNumberTelcoDAL.isViettelPhoneNumber(phone, isNumberRouting,isHome)) {
                        //serviceID = Parameters.SERVICE_ID_VIETTEL;
                        maxLength = Parameters.SMS_P2_MAXLENGTH_VIETTEL;
                        //Lay brandName
                        if (!"".equals(objSms.getSERVICE_ID()) && objSms.getSERVICE_ID() != null) {
                            serviceID = objSms.getSERVICE_ID();
                        } else {
                            serviceID = Parameters.SERVICE_ID_VIETTEL;
                        }
                    } else//Mang mobi, VNmobi...
                    if (!"".equals(objSms.getSERVICE_ID()) && objSms.getSERVICE_ID() != null) {
                        serviceID = objSms.getSERVICE_ID();
                    } else {
                        serviceID = Parameters.SERVICE_ID_OTHER;
                    }
                    
                    try {
                        
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_START_ACTION, CLASS_NAME, "processSendSMS", eventDate, para, "start send sms");
                        result = sendSMSProcess(serviceID, maxLength, sms, phone, historyID);
                        LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_END_ACTION, CLASS_NAME, "processSendSMS", eventDate, para, "end send sms, result=" + result);

                        //Cap nhat trang thai tin nhan trong history
                        MTDAL.UpdateSMSHistory(objSms, result, serviceID, log);
                        
                    } catch (SQLException e) {
                        LogUtil.ErrorExt(log, CLASS_NAME, "processSendSMS", CommonUtils.getDateNow(), para, e);
                    }
                    // end process send sms
                }
            });
        }
    }
}
