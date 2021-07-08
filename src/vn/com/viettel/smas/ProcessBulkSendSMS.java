package vn.com.viettel.smas;

import vn.com.viettel.util.Parameters;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import vn.com.viettel.smas.BulkSMSApplication.*;

import com.viettel.mmserver.base.ProcessThreadMX;
import java.io.IOException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import vn.com.viettel.DAL.UtilBusnisness;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;

public class ProcessBulkSendSMS extends ProcessThreadMX {

    private Logger logMM = null;
    private Logger log = null;
    BulkSMSApplication smasApp = null;
    BulkSMSTransaction bankTransSendSMS = null;

    WrapperService wrapperService = null;

    public ProcessBulkSendSMS(String threadName, String description,
            String MBeanName) throws Exception {
        super(threadName, description);
        logMM = Logger.getLogger(MBeanName);
        log = Logger.getLogger(GlobalConstant.PROCESS_BULK_SMS);
        try {
            registerAgent(MBeanName);
            LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "Null", "Start " + MBeanName + " " + description);
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            LogUtil.ErrorExt(log, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "NULL", e);
        }

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GlobalConstant.LOG_CONFIG_PATH));
            PropertyConfigurator.configure(prop);
            LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "Null", "Load log config success");
        } catch (IOException e) {
            LogUtil.ErrorExt(log, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "NULL", e);
        }

        //Parameters.load(GlobalConstant.CONFIG_PATH);
        smasApp = new BulkSMSApplication();
        smasApp.parentProcess = this;
        smasApp.logMM = logMM;
    }

    @Override
    protected void process() {
        buStartTime = new Date();
        long normalSleepTime = Parameters.SleepTime;
        try {
            Thread.sleep(normalSleepTime);
        } catch (InterruptedException x) {
            LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", x);
        } catch (Exception e) {
            LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
        }

        if (Parameters.PROCESS1_ALLOWRUN) {

            // Gui tin nhan theo nghiep vu
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            try {
                Date fromTime = parser.parse(Parameters.FromTime);
                Date toTime = parser.parse(Parameters.ToTime);
                Date userDate = parser.parse(parser.format(cal.getTime()));
                if (userDate.after(fromTime) && userDate.before(toTime)) {
                    bankTransSendSMS = smasApp.new BulkSMSTransaction();
                    if (bankTransSendSMS.connection != null) {
                        bankTransSendSMS.doWorkWithActiveMod();
                    }
                }

                //Thuc hien xoa cau gui tin da thuc hien thanh cong
                /*if (userDate.after(Parameters.TIMER_CONFIG_START)
                        && userDate.before(Parameters.TIMER_CONFIG_END)) {
                    bankTransSendSMS = smasApp.new BulkSMSTransaction();
                    bankTransSendSMS.DeleteTimer();
                }*/
            } catch (ParseException e) {
                // Invalid date was entered
                LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
            } catch (Exception e) {
                LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
            }

            // ghi log MM cho cac tool .net
            /*if (Parameters.DBLogFlag) {
                UtilBusnisness.doSendLogMM(bankTransSendSMS.connectionLog, logMM);
            }*/
            if (bankTransSendSMS.connection != null) {
                // ghi log vao DB chung to minh con song
                logMM.info("Danh dau tien trinh van con song: " + Parameters.CheckListAppID);
                UtilBusnisness.writeLogMonitoring(logMM, "0", Parameters.CheckListAppID);

                // ghi log MM chung to minh con song
                //logger.info("SMAS SMS EDU SMAS 3 still alive");
                try {
                    bankTransSendSMS.connection.close();
                } catch (SQLException e) {
                    LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
                }
                buStartTime = null;
            } else {
                // loi ket noi db => stop service (de tool khac thuc hien thay cong
                // viec cua minh)
                //Canh bao MM
                logMM.error("Connect DB SMS error");
                logMM.error("SMAS_SMS_SENDER Can not connect DB");

                /*logMM.info("Begin stop sms edu service");
                wrapperService.stopService();
                logMM.info("end stop sms edu service");
                wrapperService.startService();*/
                wrapperService.restart();

            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    public void stopWrapper() {
        this.stop();
        wrapperService.stop(0);
    }

}
