package vn.com.viettel.util;

/**
 *
 * @author chiendd1
 */
public class GlobalConstant {

    public static int UNPROCESS = 0; // chua gui
    public static int SEND_SUCCESS = 1;// Thanh cong
    public static int RETRY_SUCCESS = 2;// retry thanh cong
    public static int RETRY_NORMAL = 3;// cac lan retry tiep theo chua phai lan gui cuoi
    public static int RETRY_FAILURE = 4;// gui khong thanh cong
    public static int RETRY_LAST = 5;  //cac lan retry cuoi

    
     //Ma nha mang chuyen mang: 01 – Mobifone, 02 – Vinaphone, 04 – Viettel, 05 – Vietnammobile, 07 – Gtel Mobile
    public static final String CURR_OPERATOR_VIETTEL="04";
    
    public static final String CURR_OPERATOR_MOBI="01";
    
    public static final String CURR_OPERATOR_VINA="02";
    
    public static final String CURR_OPERATOR_VIETNAMMOBILE="05";
    
    public static final String CURR_OPERATOR_GTEL="07";
    
     /**
     * Gui tin nhan co dau
     */
    public final static Integer SEND_UNICODE = 1;

    /**
     * Gui tin nhan khong dau
     */
    public final static Integer SEND_NORMAL = 0;
    
    /**
     * Thoi gian cho gui ban tin tiep theo khi tin nhan bi cat thanh nhieu tin
     */
     
    public final static long SLEEP_MAX_SMS=10000;
    
    public static final String LOG_TYPE_START_ACTION = "START_ACTION";
    public static final String LOG_TYPE_END_ACTION = "END_ACTION";
    public static final String LOG_TYPE_START_CONNECT = "START_CONNECT";
    public static final String LOG_TYPE_END_CONNECT = "END_CONNECT";
    public static final String LOG_TYPE_INFO = "INFO";
    public static final String LOG_ERROR = "ERROR";
    public static final String APP_CODE = "SMAS_SMS_SENDER";
    public final static String PROCESS_BULK_SMS="SMS_BRANDNAME_NONE_UNICODE";
    public final static String PROCESS_SMS_BRANDNAME="SMS_BRANDNAME_UNICODE";
    
    public final static String LOG_CONFIG_PATH="D:\\ITSOL\\Viettel\\smas_send_sms\\etc\\log4j.conf";
     //public final static String CONFIG_PATH="../config/general.cfg";
//     public static final String GENERAL_CONFIG_PATH = "..\\..\\..\\..\\..\\etc\\general.cfg";
//     public static final String SMAS_CORE_CONFIG_PATH = "..\\..\\..\\..\\..\\etc\\oracle_smas_core.conf";
    public final static String GENERAL_CONFIG_PATH="D:\\ITSOL\\Viettel\\smas_send_sms\\etc\\general.cfg";
    public final static String SMAS_CORE_CONFIG_PATH="D:\\ITSOL\\Viettel\\smas_send_sms\\etc\\oracle_smas_core.conf";

    public final static String ERR_DUPLICATE_MESSAGE="DUPLICATE MESSAGE";
     
    /**
    * Thoi gian nghi 10s cho cac tin nhan bi cat thanh nhieu tin
    */
    public final static long SLEEP_TIME_2_SMS=10000;
}
