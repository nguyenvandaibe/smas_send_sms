/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.BO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author chiendd1
 */
public class SmsMT {

    

    private String REQUEST_ID;
    private String USER_ID;
    private String COMMAND_CODE;
    private Date TIME_SEND_REQUEST;
    private Date TIME_SENT;
    private String SERVICE_ID;
    private String SMS_CONTENT;
    private String CONTENT_TYPE;
    private int STATUS;
    private int RETRY_NUM;
    private String ISDN_TAIL;
    private int MO_HIS_ID;
    private int SUB_ID;
    private int MT_TYPE;
    private String CP_CODE;
    private int BLOCK_ID;

    private String HISTORY_RAW_ID;
    private Date SYNC_TIME;
    private int UNIT_ID;
    private String SMS_TIMER_CONFIG_ID;
    
    private String DEVICE_TOKEN;
    

    public SmsMT() {
    }

    public SmsMT(ResultSet rs) throws SQLException {
        this.REQUEST_ID = rs.getString("REQUEST_ID");
        this.USER_ID = rs.getString("USER_ID");
        this.COMMAND_CODE = rs.getString("COMMAND_CODE");
        this.TIME_SEND_REQUEST = rs.getDate("TIME_SEND_REQUEST");
        this.TIME_SENT = rs.getDate("TIME_SENT");
        this.SERVICE_ID = rs.getString("SERVICE_ID");
        this.SMS_CONTENT = rs.getString("SMS_CONTENT");
        this.CONTENT_TYPE = rs.getString("CONTENT_TYPE");
        this.STATUS = rs.getInt("STATUS");
        this.RETRY_NUM = rs.getInt("RETRY_NUM");
        this.ISDN_TAIL = rs.getString("ISDN_TAIL");
        this.MO_HIS_ID = rs.getInt("MO_HIS_ID");
        this.SUB_ID = rs.getInt("SUB_ID");
        this.MT_TYPE = rs.getInt("MT_TYPE");
        this.CP_CODE = rs.getString("CP_CODE");
        this.BLOCK_ID = rs.getInt("BLOCK_ID");
        this.HISTORY_RAW_ID = rs.getString("HISTORY_RAW_ID");
        this.SYNC_TIME = rs.getDate("SYNC_TIME");
        this.UNIT_ID = rs.getInt("UNIT_ID");
        this.SMS_TIMER_CONFIG_ID = rs.getString("SMS_TIMER_CONFIG_ID");
        this.IS_NUMBER_ROUTING=rs.getInt("IS_NUMBER_ROUTING");
        this.IS_HOME=rs.getInt("IS_HOME");
        this.CURR_OPERATOR=rs.getString("CURR_OPERATOR");
        
    }

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCOMMAND_CODE() {
        return COMMAND_CODE;
    }

    public void setCOMMAND_CODE(String COMMAND_CODE) {
        this.COMMAND_CODE = COMMAND_CODE;
    }

    public Date getTIME_SEND_REQUEST() {
        return TIME_SEND_REQUEST;
    }

    public void setTIME_SEND_REQUEST(Date TIME_SEND_REQUEST) {
        this.TIME_SEND_REQUEST = TIME_SEND_REQUEST;
    }

    public Date getTIME_SENT() {
        return TIME_SENT;
    }

    public void setTIME_SENT(Date TIME_SENT) {
        this.TIME_SENT = TIME_SENT;
    }

    public String getSERVICE_ID() {
        return SERVICE_ID;
    }

    public void setSERVICE_ID(String SERVICE_ID) {
        this.SERVICE_ID = SERVICE_ID;
    }

    public String getSMS_CONTENT() {
        return SMS_CONTENT;
    }

    public void setSMS_CONTENT(String SMS_CONTENT) {
        this.SMS_CONTENT = SMS_CONTENT;
    }

    public String getCONTENT_TYPE() {
        return CONTENT_TYPE;
    }

    public void setCONTENT_TYPE(String CONTENT_TYPE) {
        this.CONTENT_TYPE = CONTENT_TYPE;
    }

    public int getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(int STATUS) {
        this.STATUS = STATUS;
    }

    public int getRETRY_NUM() {
        return RETRY_NUM;
    }

    public void setRETRY_NUM(int RETRY_NUM) {
        this.RETRY_NUM = RETRY_NUM;
    }

    public String getISDN_TAIL() {
        return ISDN_TAIL;
    }

    public void setISDN_TAIL(String ISDN_TAIL) {
        this.ISDN_TAIL = ISDN_TAIL;
    }

    public int getMO_HIS_ID() {
        return MO_HIS_ID;
    }

    public void setMO_HIS_ID(int MO_HIS_ID) {
        this.MO_HIS_ID = MO_HIS_ID;
    }

    public int getSUB_ID() {
        return SUB_ID;
    }

    public void setSUB_ID(int SUB_ID) {
        this.SUB_ID = SUB_ID;
    }

    public int getMT_TYPE() {
        return MT_TYPE;
    }

    public void setMT_TYPE(int MT_TYPE) {
        this.MT_TYPE = MT_TYPE;
    }

    public String getCP_CODE() {
        return CP_CODE;
    }

    public void setCP_CODE(String CP_CODE) {
        this.CP_CODE = CP_CODE;
    }

    public int getBLOCK_ID() {
        return BLOCK_ID;
    }

    public void setBLOCK_ID(int BLOCK_ID) {
        this.BLOCK_ID = BLOCK_ID;
    }

    
    public String getHISTORY_RAW_ID() {
        return HISTORY_RAW_ID;
    }

    public void setHISTORY_RAW_ID(String HISTORY_RAW_ID) {
        this.HISTORY_RAW_ID = HISTORY_RAW_ID;
    }

    public Date getSYNC_TIME() {
        return SYNC_TIME;
    }

    public void setSYNC_TIME(Date SYNC_TIME) {
        this.SYNC_TIME = SYNC_TIME;
    }

    public int getUNIT_ID() {
        return UNIT_ID;
    }

    public void setUNIT_ID(int UNIT_ID) {
        this.UNIT_ID = UNIT_ID;
    }

    public String getSMS_TIMER_CONFIG_ID() {
        return SMS_TIMER_CONFIG_ID;
    }

    public void setSMS_TIMER_CONFIG_ID(String SMS_TIMER_CONFIG_ID) {
        this.SMS_TIMER_CONFIG_ID = SMS_TIMER_CONFIG_ID;
    }
    public String getDEVICE_TOKEN() {
        return DEVICE_TOKEN;
    }

    public void setDEVICE_TOKEN(String DEVICE_TOKEN) {
        this.DEVICE_TOKEN = DEVICE_TOKEN;
    }
    
    private int IS_NUMBER_ROUTING;
    public String CURR_OPERATOR;
    private int IS_HOME;

    public int getIS_NUMBER_ROUTING() {
        return IS_NUMBER_ROUTING;
    }

    public void setIS_NUMBER_ROUTING(int IS_NUMBER_ROUTING) {
        this.IS_NUMBER_ROUTING = IS_NUMBER_ROUTING;
    }

    public String getCURR_OPERATOR() {
        return CURR_OPERATOR;
    }

    public void setCURR_OPERATOR(String CURR_OPERATOR) {
        this.CURR_OPERATOR = CURR_OPERATOR;
    }

    public int getIS_HOME() {
        return IS_HOME;
    }

    public void setIS_HOME(int IS_HOME) {
        this.IS_HOME = IS_HOME;
    }
    
    
    public String getPara(){
        String para="REQUEST_ID="+this.REQUEST_ID
                + " Mobile="+this.USER_ID 
                + " SERVICE_ID="+ this.SERVICE_ID
                + " Content= "+ this.SMS_CONTENT
                + " CONTENT_TYPE= "+ this.CONTENT_TYPE
                + " HISTORY_RAW_ID= "+ this.HISTORY_RAW_ID
                + " UNIT_ID="+this.UNIT_ID;
                
        return para;
    }
}
