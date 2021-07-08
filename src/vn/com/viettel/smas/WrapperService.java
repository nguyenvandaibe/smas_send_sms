package vn.com.viettel.smas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openide.util.Exceptions;

import org.tanukisoftware.wrapper.WrapperManager;
import org.tanukisoftware.wrapper.WrapperListener;
import vn.com.viettel.DAL.PreNumberTelcoDAL;
import vn.com.viettel.DAL.UtilBusnisness;
import vn.com.viettel.dataSource.SmasOracleConnectionPool;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;
import vn.com.viettel.util.Parameters;
import vn.com.viettel.util.StringUtil;

public class WrapperService implements WrapperListener {

    ProcessBulkSendSMS bankProcess = null;
    ProcessBrandNameSendSMS bulkProcess = null;

    final String ELEVATE = "../lib/elevate.exe";
    final String SERVICE_NAME = "SMAS_SMS_SENDER";
    private final Logger logger = Logger.getLogger("SMAS_SMS_SENDER");
    private final static String CLASS_NAME = "WrapperService";

    public static void main(String[] args) {
        WrapperManager.start(new WrapperService(), args);
    }

    @Override
    public void controlEvent(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer start(String[] arg0) {

        // TODO Auto-generated method stub        
        try {

            //String mobile=UtilBusnisness.getISDN("841242888888");
            if (!Parameters.load(GlobalConstant.GENERAL_CONFIG_PATH)) {// load cac tham so cho chuong trinh
                LogUtil.InfoExt(logger, GlobalConstant.LOG_TYPE_INFO, CLASS_NAME, "start", CommonUtils.getDateNow(), "Null", "Khong the load duoc cac tham so");
            }
            SmasOracleConnectionPool.getInstance().initC3P0();// ket noi
            BasicConfigurator.configure();

            UtilBusnisness.getReceiverError();
            //PreNumberTelcoDAL.lstPreNumTrans=PreNumberTelcoDAL.GetPreNumberTrans();
            PreNumberTelcoDAL.lstPreNumUsing = PreNumberTelcoDAL.GetPreNumberUsing();

        } catch (Exception e) {

            LogUtil.ErrorExt(logger, CLASS_NAME, "start", CommonUtils.getDateNow(), "", e);
        }
        try {
            bankProcess = new ProcessBulkSendSMS(GlobalConstant.PROCESS_BULK_SMS, "SENDER " + GlobalConstant.PROCESS_BULK_SMS, "process:name=" + GlobalConstant.PROCESS_BULK_SMS);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        bankProcess.wrapperService = this;

        try {
            bulkProcess = new ProcessBrandNameSendSMS(GlobalConstant.PROCESS_SMS_BRANDNAME, "SENDER " + GlobalConstant.PROCESS_SMS_BRANDNAME, "process:name=" + GlobalConstant.PROCESS_SMS_BRANDNAME);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        bulkProcess.wrapperService = this;

        try {
            bankProcess.start();
            bulkProcess.start();
        } catch (Exception e) {
            logger.error("Loi trong qua trinh start cac process thread: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public int stop(int arg0) {
        bankProcess.stop();
        bulkProcess.stop();
        return 0;
    }

    public int restart() {
        String[] stArray = {"0"};
        bankProcess.stop();
        bulkProcess.stop();
        start(stArray);
        return 0;
    }

    public void stopService() {
        try {
            String[] stop = {ELEVATE, "cmd.exe", "/c", "sc", "stop",
                SERVICE_NAME};
            Process p = Runtime.getRuntime().exec(stop);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException | InterruptedException ex) {
        }
    }

    public void startService() {
        try {
            String[] start = {ELEVATE, "cmd.exe", "/c", "sc", "start",
                SERVICE_NAME};
            Process p = Runtime.getRuntime().exec(start);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException | InterruptedException ex) {
        }
    }

    public String getStatusService() {
        try {
            Process p = Runtime.getRuntime().exec("sc query " + SERVICE_NAME);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String line = reader.readLine();
            while (line != null) {
                if (line.trim().startsWith("STATE")) {
                    switch (line.trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()) {
                        case "1":
                            return "Stopped";
                        case "2":
                            return "Startting";
                        case "3":
                            return "Stopping";
                        case "4":
                            return "Running";
                    }

                }
                line = reader.readLine();
            }

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

}
