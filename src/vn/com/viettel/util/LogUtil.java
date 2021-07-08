/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author chiendd1
 */
public class LogUtil {

    /**
     * Ghi log info ra file MMlog va SMSLog
     *
     * @param logMM
     * @param logSMS
     * @param content
     */
    /*public static void writeInfoLog(Logger logMM, Logger logSMS, String content) {
     if (logMM != null) {
     logMM.info(content);
     }
     if (logSMS != null) {
     logSMS.info(content);
     }
     }*/
    /**
     * Ghi log ra file MMlog va SMSLog
     *
     * @param logger
     * @param logSMS
     * @param content
     * @param ex
     */
    /*public static void writeErrorLog(Logger logger, Logger logSMS, String content, Exception ex) {
     if (ex != null) {
     logSMS.error(content, ex);
     logger.error(content, ex);
     } else {
     logSMS.error(content, ex);
     logger.error(content);
     }
     }*/
    public static String getIp() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            return "";
        }
    }

    public static void InfoExt(Logger logger, String logType, String className, String methodName, Date eventDate, String paraList, String description) {
        String sDate = CommonUtils.convertDateToString(eventDate, "YYYY/MM/DD HH:MM:SS");
        Date dateNow = CommonUtils.getDateNow();
        long duration = dateNow.getTime() - eventDate.getTime();

        StringBuilder retVal = new StringBuilder();
        retVal = retVal.append(logType).append("|")
                .append(GlobalConstant.APP_CODE).append("|")
                .append(sDate).append("|")
                .append("Null").append("|")
                .append(getIp()).append("|")
                .append("Null").append("|")
                .append(methodName).append("|")
                .append(paraList).append("|")
                .append(className).append("|")
                .append(duration).append("|")
                .append(description).append("|");
        logger.info(retVal.toString());
    }

    /**
     * Luu log loi theo chuan 168 cua tap doan
     *
     * @param logger
     * @param className
     * @param methodName
     * @param eventDate
     * @param paraList
     * @param ex
     */
    public static void ErrorExt(Logger logger, String className, String methodName, Date eventDate, String paraList, Exception ex) {
        String sDate = eventDate.toString();
        StringBuilder retVal = new StringBuilder();
        retVal = retVal.append(sDate).append("|")
                .append(className).append("|")
                .append(methodName).append("|")
                .append(ex.getMessage()).append("|")
                .append(paraList);
        logger.error(retVal.toString(),ex);
    }

}
