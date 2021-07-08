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

import vn.com.viettel.smas.BulkSMSBrandNameApplication.*;

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

public class ProcessBrandNameSendSMS extends ProcessThreadMX {

    private Logger logMM = null;
    private Logger log = null;
    BulkSMSBrandNameApplication smasApp = null;
    BulkSMSBrandNameTransaction bulkTransSendSMS = null;

    WrapperService wrapperService = null;

    public ProcessBrandNameSendSMS(String threadName, String description,
            String MBeanName) throws Exception {
        super(threadName, description);
        logMM = Logger.getLogger(MBeanName);
        log = Logger.getLogger(GlobalConstant.PROCESS_SMS_BRANDNAME);
        try {
            registerAgent(MBeanName);
            LogUtil.InfoExt(logMM, GlobalConstant.LOG_TYPE_INFO, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "Null", "Start " + MBeanName + " " + description);
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "NULL", e);
        }

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(GlobalConstant.LOG_CONFIG_PATH));
            PropertyConfigurator.configure(prop);
            LogUtil.InfoExt(logMM, GlobalConstant.LOG_TYPE_INFO, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "Null", "Load log config success");
            LogUtil.InfoExt(log, GlobalConstant.LOG_TYPE_INFO, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "Null", "Load log config success");
        } catch (IOException e) {
            LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "NULL", e);
            LogUtil.ErrorExt(log, "ProcessBulkSendSMS", "ProcessBulkSendSMS", CommonUtils.getDateNow(), "NULL", e);
        }

        //Parameters.load(GlobalConstant.CONFIG_PATH);
        smasApp = new BulkSMSBrandNameApplication();
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
        if (Parameters.PROCESS2_ALLOWRUN) {
            // Gui tin nhan theo nghiep vu
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            try {
                Date fromTime = parser.parse(Parameters.FromTime);
                Date toTime = parser.parse(Parameters.ToTime);
                Date userDate = parser.parse(parser.format(cal.getTime()));
                if (userDate.after(fromTime) && userDate.before(toTime)) {
                    bulkTransSendSMS = smasApp.new BulkSMSBrandNameTransaction();
                    if (bulkTransSendSMS.connection != null) {
                        bulkTransSendSMS.doWorkWithActiveMod();
                    }
                }

            } catch (ParseException e) {
                // Invalid date was entered
                LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
            } catch (Exception e) {
                LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
            }

            logMM.info("Danh dau tien trinh van con song: "+ Parameters.CheckListAppID_UniCode);
            UtilBusnisness.writeLogMonitoring(logMM, "0", Parameters.CheckListAppID_UniCode);
            if (bulkTransSendSMS.connection != null) {
                try {
                    bulkTransSendSMS.connection.close();
                } catch (SQLException e) {
                    LogUtil.ErrorExt(logMM, "ProcessBulkSendSMS", "process", CommonUtils.getDateNow(), "NULL", e);
                }
                buStartTime = null;
            } else {
                //Canh bao MM
                logMM.error("Connect DB SMS error");
                logMM.error("SMAS_SMS_SENDER Can not connect DB");
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
