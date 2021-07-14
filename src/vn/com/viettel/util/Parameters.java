package vn.com.viettel.util;

import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Parameters {

    private final static Logger logger = Logger.getLogger("SMAS_SMS_SENDER");
    private static boolean loaded = false;

    //public static String DBServerPath;
    //public static String DBDatabaseName;
    //public static String DBUser;
    //public static String DBPass;
    public static int MaxRetryTimes;
    public static String CountryCode;
    public static long SleepTime;
    public static String FromTime;
    public static String ToTime;

    public static int MaxSMSInSession = 1000;

    public static boolean Encrypt = false;
    //public static String DBServerPathLog;
    //public static String DBDatabaseNameLog;

    //public static String DBUserLog;
    //public static String DBPassLog;
    public static int DBTimeAlive = 30;
    public static String DBTimeError = "5";
    public static boolean DBLogFlag = false;
    public static String CheckListAppID = "";
    public static String CheckListAppID_UniCode = "";
    public static String CheckListAppIDPre = "";
    public static String CheckListNodesStr = "";
    public static String[] CheckListNodes = {};
    public static int CheckListLenMod = 0;

    public static String VIETNAMOBILE_SUBSCRIBER;
    public static String GMOBILE_SUBSCRIBER;
    public static String VINA_SUBSCRIBER;
    public static String MOBI_SUBSCRIBER;
    public static String VIETTEL_SUBSCRIBER;
    public static String SUPPORT_SUBSCRIBER;
    public static int NUM_SMS_RETRY_LEVEL1;
    public static int NUM_SMS_RETRY_LEVEL2;

    public static boolean PROCESS1_ALLOWRUN;
    public static boolean PROCESS2_ALLOWRUN;

    public static int CONNECTION_MINPOOLSIZE;
    public static int CONNECTION_ACQUIREINCREMENT;
    public static int CONNECTION_MAXPOOLSIZE;
    public static int CONNECTION_MAXSTATEMENTS;
    public static int CONNECTION_TIMEOUT;
    public static int SEND_TIME;
    public static int SMS_P1_MAXLENGTH_OTHER;
    public static int SMS_P1_MAXLENGTH_VIETTEL;
    public static int SMS_P2_MAXLENGTH_OTHER;
    public static int SMS_P2_MAXLENGTH_VIETTEL;

    public static String SERVICE_ID_VIETTEL;
    public static String SERVICE_ID_OTHER;

    public static String SEND_MOBILE;
    //Thoi gian gui tin tu dong
    public static Date StartAutoTime;
    public static Date EndAutoTime;

    public static Date TIMER_CONFIG_START;
    public static Date TIMER_CONFIG_END;
    public static int TIMER_CONFIG_DAY;
    public static boolean IS_BULK_SMS = true;

    //public static boolean TRANS_NUMBER_11_TO_10 = false;
    
  

    public static boolean load(String url) throws Exception {
        FileInputStream inputStream = null;
        try {

            //String path = CommonUtils.GetPhysicalFile(url);
            //logger.info("Path config: " + path);
            inputStream = new FileInputStream(url);
            Properties properties = new Properties();
            properties.load(inputStream);

            // 30.07.2012
            Encrypt = Boolean.parseBoolean(properties.getProperty("Encrypt"));
            /*if (Encrypt) {
                DBServerPath = PassTranformer.decrypt(properties.getProperty("DB.ServerPath"));
                DBDatabaseName = PassTranformer.decrypt(properties.getProperty("DB.DatabaseName"));
                DBUser = PassTranformer.decrypt(properties.getProperty("DB.User"));
                DBPass = PassTranformer.decrypt(properties.getProperty("DB.Pass"));

            } else {
                DBServerPath = properties.getProperty("DB.ServerPath");
                DBDatabaseName = properties.getProperty("DB.DatabaseName");
                DBUser = properties.getProperty("DB.User");
                DBPass = properties.getProperty("DB.Pass");
            }*/
            MaxRetryTimes = Integer.parseInt(properties
                    .getProperty("MAS.RETRY.TIMES"));
            CountryCode = properties.getProperty("COUNTRY.CODE");
            SleepTime = Long.parseLong(properties.getProperty("SLEEP.TIME"));
            if (SleepTime == 0) {
                SleepTime = 5000;
            }
            FromTime = properties.getProperty("FROM.TIME");
            ToTime = properties.getProperty("TO.TIME");
//            IS_BULK_SMS = Boolean.parseBoolean(properties.getProperty("SMS.BULK_SMS"));
            IS_BULK_SMS = true;
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            try {

                //Thoi gian gui tin nhan tu dong
                StartAutoTime = parser.parse(properties.getProperty("TIME.StartAutoSMS"));
                EndAutoTime = parser.parse(properties.getProperty("TIME.EndAutoSMS"));
                SEND_MOBILE = properties.getProperty("SEND.MOBILE");

                TIMER_CONFIG_START = parser.parse(properties.getProperty("TimerConfig.StartTime"));
                TIMER_CONFIG_END = parser.parse(properties.getProperty("TimerConfig.EndTime"));
                TIMER_CONFIG_DAY = Integer.parseInt(properties.getProperty("TimerConfig.DAY"));

            } catch (ParseException | NumberFormatException ex) {
                StartAutoTime = parser.parse("6:00");
                EndAutoTime = parser.parse("23:00");
                TIMER_CONFIG_START = parser.parse("0:15");
                TIMER_CONFIG_END = parser.parse("3:15");
                TIMER_CONFIG_DAY = 5;

            }
            MaxSMSInSession = Integer.parseInt(properties
                    .getProperty("MAX.SMS.IN.SESSION"));

            //DBServerPathLog = properties.getProperty("DBLog.ServerPath");
            //DBDatabaseNameLog = properties.getProperty("DBLog.DatabaseName");
            //DBUserLog = properties.getProperty("DBLog.User");
            //DBPassLog = properties.getProperty("DBLog.Pass");
            DBTimeAlive = Integer.parseInt(properties.getProperty("DBLog.Time.Alive"));
            DBTimeError = properties.getProperty("DBLog.Time.Error");
            DBLogFlag = Boolean.parseBoolean(properties
                    .getProperty("DBLog.Flag"));

            CheckListNodesStr = properties.getProperty("CheckList.Nodes");
            CheckListNodes = CheckListNodesStr.split(",");
            CheckListLenMod = CheckListNodes.length;
            CheckListAppID = properties.getProperty("CheckList.AppID");
            CheckListAppIDPre = properties.getProperty("CheckList.AppIDPre");
            CheckListAppID_UniCode = CheckListAppID + "_Unicode";
            VIETNAMOBILE_SUBSCRIBER = properties
                    .getProperty("Phone.VIETNAMOBILE");
            GMOBILE_SUBSCRIBER = properties.getProperty("Phone.GMOBILE");
            VINA_SUBSCRIBER = properties.getProperty("Phone.VINA");
            MOBI_SUBSCRIBER = properties.getProperty("Phone.MOBI");
            VIETTEL_SUBSCRIBER = properties.getProperty("Phone.VIETTEL");
            SUPPORT_SUBSCRIBER = properties
                    .getProperty("Phone.SUPPORT.SUBSCRIBER");

            NUM_SMS_RETRY_LEVEL1 = Integer.parseInt(properties
                    .getProperty("NUM_SMS_RETRY_LEVEL1"));
            NUM_SMS_RETRY_LEVEL2 = Integer.parseInt(properties
                    .getProperty("NUM_SMS_RETRY_LEVEL2"));

            PROCESS1_ALLOWRUN = Boolean
                    .parseBoolean(properties.getProperty("SMS.Process1.allowRun"));
            PROCESS2_ALLOWRUN = Boolean
                    .parseBoolean(properties.getProperty("SMS.Process2.allowRun"));

            CONNECTION_MINPOOLSIZE = Integer.parseInt(properties
                    .getProperty("Connection.MinPoolSize"));
            CONNECTION_ACQUIREINCREMENT = Integer.parseInt(properties
                    .getProperty("Connection.AcquireIncrement"));
            CONNECTION_MAXPOOLSIZE = Integer.parseInt(properties
                    .getProperty("Connection.MaxPoolSize"));
            CONNECTION_MAXSTATEMENTS = Integer.parseInt(properties
                    .getProperty("Connection.MaxStatements"));
            CONNECTION_TIMEOUT = Integer.parseInt(properties
                    .getProperty("Connection.timeout"));
            if(CONNECTION_TIMEOUT==0){
                CONNECTION_TIMEOUT=60000;
            }
            SEND_TIME = Integer.parseInt(properties.getProperty("send.time"));
            SMS_P1_MAXLENGTH_OTHER = Integer.parseInt(properties
                    .getProperty("sms.p1.max.length.other"));
            SMS_P1_MAXLENGTH_VIETTEL = Integer.parseInt(properties
                    .getProperty("sms.p1.max.length.viettel"));
            SMS_P2_MAXLENGTH_OTHER = Integer.parseInt(properties
                    .getProperty("sms.p2.max.length.other"));
            SMS_P2_MAXLENGTH_VIETTEL = Integer.parseInt(properties
                    .getProperty("sms.p2.max.length.viettel"));

            SERVICE_ID_VIETTEL = properties.getProperty("service.id.viettel");
            SERVICE_ID_OTHER = properties.getProperty("service.id.other");

            loaded = true;
            return true;
        } catch (IOException | NumberFormatException | ParseException e) {
            loaded = false;
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    public static String Descrypt(String enString) {
        try {
            DESKeySpec keySpec = new DESKeySpec(
                    "Vietnam@124002".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not
            // thread
            // safe

            byte[] encrypedPwdBytes = javax.xml.bind.DatatypeConverter
                    .parseBase64Binary(enString);
            cipher = Cipher.getInstance("DES");// cipher is not thread safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);
            String Pwd = new String(plainTextPwdBytes, "UTF-8");
            // System.out.println(Pwd);
            return Pwd;
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            return "";
        }
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
