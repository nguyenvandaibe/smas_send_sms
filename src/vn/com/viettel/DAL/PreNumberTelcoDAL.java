/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import vn.com.viettel.BO.PreNumberTelco;
import vn.com.viettel.dataSource.ConnectionPoolManager;
import vn.com.viettel.util.CommonUtils;
import vn.com.viettel.util.GlobalConstant;
import vn.com.viettel.util.LogUtil;

/**
 *
 * @author Administrator
 */
public class PreNumberTelcoDAL {

    private final static String CLASS_NAME = PreNumberTelcoDAL.class.getName();
    private final static Logger loger = Logger.getLogger("PreNumberTelcoDAL");
    //public static List<PreNumberTelco> lstPreNumTrans = null;
    public static List<PreNumberTelco> lstPreNumUsing = null;
    public static final String VIETTEL = "Viettel";
    public static final String VINA = "VinaPhone";
    public static final String MOBI = "Mobifone";
    public static final String GMOBILE = "Gmobile";
    public static final String VIETNAMMOBILE = "Vietnamobile";

    /**
     * Lay danh sach cac dau so chuyen doi
     *
     * @return
     */
    public static List<PreNumberTelco> GetPreNumberTrans() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                connection = ConnectionPoolManager.getSMSConnection(loger);
            }

            String sql = "SELECT * FROM PRE_NUMBER_TELCO where PREFIX_TRANS is not null AND is_active=1";

            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            if (!rs.next()) {
                return new ArrayList<>();
            }
            List<PreNumberTelco> lstVal = new ArrayList<>();

            PreNumberTelco objPre;

            while (rs.next()) {
                objPre = new PreNumberTelco();
                objPre.setPREFIX_NUMBER_ID(rs.getInt("PREFIX_NUMBER_ID"));
                objPre.setPREFIX_NUMBER(rs.getString("PREFIX_NUMBER"));
                objPre.setTELCO(rs.getString("TELCO"));
                objPre.setPREFIX_TYPE(rs.getInt("PREFIX_TYPE"));
                objPre.setPREFIX_TRANS(rs.getString("PREFIX_TRANS"));
                objPre.setIS_ACTIVE(rs.getBoolean("IS_ACTIVE"));
                lstVal.add(objPre);
            }

            return lstVal;
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "GetPreNumberTrans", CommonUtils.getDateNow(), "NUll", e);
            return new ArrayList<>();
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

            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "checkTransNumber", CommonUtils.getDateNow(), "Can not close connectionLog function checkAppIDAnotherAlive:", e2);
            }
        }
    }

    /**
     * Lay danh sach dau so dang hoat dong
     *
     * @return
     */
    public static List<PreNumberTelco> GetPreNumberUsing() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            if (connection == null || connection.isClosed()) {
                connection = ConnectionPoolManager.getSMSConnection(loger);
            }

            String sql = "SELECT * FROM PRE_NUMBER_TELCO where is_active=1";

            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            if (!rs.next()) {
                return new ArrayList<>();
            }
            List<PreNumberTelco> lstVal = new ArrayList<>();

            PreNumberTelco objPre;

            while (rs.next()) {
                objPre = new PreNumberTelco();
                objPre.setPREFIX_NUMBER_ID(rs.getInt("PREFIX_NUMBER_ID"));
                objPre.setPREFIX_NUMBER(rs.getString("PREFIX_NUMBER"));
                objPre.setTELCO(rs.getString("TELCO"));
                objPre.setPREFIX_TYPE(rs.getInt("PREFIX_TYPE"));
                objPre.setPREFIX_TRANS(rs.getString("PREFIX_TRANS"));
                objPre.setIS_ACTIVE(rs.getBoolean("IS_ACTIVE"));
                lstVal.add(objPre);
            }

            return lstVal;
        } catch (SQLException | NumberFormatException e) {
            LogUtil.ErrorExt(loger, CLASS_NAME, "GetPreNumberUsing", CommonUtils.getDateNow(), "NUll", e);
            return new ArrayList<>();
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

            } catch (SQLException e2) {
                LogUtil.ErrorExt(loger, CLASS_NAME, "checkTransNumber", CommonUtils.getDateNow(), "Can not close connectionLog function checkAppIDAnotherAlive:", e2);
            }
        }
    }

    
    public static boolean ValidateMobile(String mobile, List<PreNumberTelco> lstPreNum) {

        if ("".equals(mobile)) {
            return false;
        }
        if (mobile.length() < 9 && mobile.length() > 12) {
            return false;
        }
        if (lstPreNum == null) {
            lstPreNum = GetPreNumberUsing();
        }

        if (lstPreNum.isEmpty()) {
            return false;
        }

        String number2 = mobile.substring(0, mobile.length() - 7);
        int numberLength = mobile.length();
        boolean retVal = false;
        for (PreNumberTelco pre : lstPreNum) {
            if (pre.getPREFIX_TYPE() == numberLength && pre.getPREFIX_NUMBER().contains(number2)) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    public static boolean isGmobilePhoneNumber(String phoneNumber) {

        if ("".equals(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() < 9 && phoneNumber.length() > 12) {
            return false;
        }
        if (lstPreNumUsing == null) {
            lstPreNumUsing = GetPreNumberUsing();
        }
        if (lstPreNumUsing.isEmpty()) {
            return false;
        }

        List<PreNumberTelco> lstPreNum = new ArrayList<>();
        for (PreNumberTelco pre : lstPreNumUsing) {
            if (pre.getTELCO() != null && pre.getTELCO().equals(GMOBILE)) {
                lstPreNum.add(pre);
            }
        }
        boolean match = ValidateMobile(phoneNumber, lstPreNum);
        return match;
    }

    public static boolean isVietnamobilePhoneNumber(String phoneNumber) {

        if ("".equals(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() < 9 && phoneNumber.length() > 12) {
            return false;
        }
        if (lstPreNumUsing == null) {
            lstPreNumUsing = GetPreNumberUsing();
        }
        if (lstPreNumUsing.isEmpty()) {
            return false;
        }

        List<PreNumberTelco> lstPreNum = new ArrayList<>();
        for (PreNumberTelco pre : lstPreNumUsing) {
            if (pre.getTELCO() != null && pre.getTELCO().equals(VIETNAMMOBILE)) {
                lstPreNum.add(pre);
            }
        }
        boolean match = ValidateMobile(phoneNumber, lstPreNum);
        return match;
    }

    public static boolean isVinaPhoneNumber(String phoneNumber, int isNumerRouting, String currOperator) {

        if ("".equals(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() < 9 && phoneNumber.length() > 12) {
            return false;
        }
        if (lstPreNumUsing == null) {
            lstPreNumUsing = GetPreNumberUsing();
        }
        if (lstPreNumUsing.isEmpty()) {
            return false;
        }
        
        //Thue bao chuyen mang ve mang VINA
        if(isNumerRouting==1 && currOperator.equals(GlobalConstant.CURR_OPERATOR_VINA) ){
            return true;
        }
        List<PreNumberTelco> lstPreNum = new ArrayList<>();
        for (PreNumberTelco pre : lstPreNumUsing) {
            if (pre.getTELCO() != null && pre.getTELCO().equals(VINA)) {
                lstPreNum.add(pre);
            }
        }
        boolean match = ValidateMobile(phoneNumber, lstPreNum);
        return match;
    }

    public static boolean isMobiPhoneNumber(String phoneNumber, int isNumerRouting, String currOperator) {

        if ("".equals(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() < 9 && phoneNumber.length() > 12) {
            return false;
        }
        
        //Thue bao chuyen mang ve mang MObi
        if(isNumerRouting==1 && currOperator.equals(GlobalConstant.CURR_OPERATOR_MOBI) ){
            return true;
        }
        if (lstPreNumUsing == null) {
            lstPreNumUsing = GetPreNumberUsing();
        }
        if (lstPreNumUsing.isEmpty()) {
            return false;
        }

        List<PreNumberTelco> lstPreNum = new ArrayList<>();
        for (PreNumberTelco pre : lstPreNumUsing) {
            if (pre.getTELCO() != null && pre.getTELCO().equals(MOBI)) {
                lstPreNum.add(pre);
            }
        }
        boolean match = ValidateMobile(phoneNumber, lstPreNum);
        return match;
    }

    public static boolean isViettelPhoneNumber(String phoneNumber, int isNumerRouting, int  isHome) {

        if ("".equals(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() < 9 && phoneNumber.length() > 12) {
            return false;
        }
        //Thue bao chuyen mang ve mang MObi
        if(isNumerRouting==1 && isHome==1){
            return true;
        }
        
        if (lstPreNumUsing == null) {
            lstPreNumUsing = GetPreNumberUsing();
        }
        if (lstPreNumUsing.isEmpty()) {
            return false;
        }

        List<PreNumberTelco> lstPreNum = new ArrayList<>();
        for (PreNumberTelco pre : lstPreNumUsing) {
            if (pre.getTELCO() != null && pre.getTELCO().equals(VIETTEL)) {
                lstPreNum.add(pre);
            }
        }
        boolean match = ValidateMobile(phoneNumber, lstPreNum);
        return match;
    }

}
