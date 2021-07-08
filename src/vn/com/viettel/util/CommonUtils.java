/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class CommonUtils {

    public static int getInt(String number, int def) {
        try {
            def = Integer.parseInt(number);
        } catch (NumberFormatException ex) {
            //bo qua
        }
        return def;
    }

    public static int getInt(Object number, int def) {
        try {
            def = Integer.parseInt(number.toString());
        } catch (NumberFormatException ex) {
            //bo qua
        }
        return def;
    }

    public static int getInt(String columnName, ResultSet rs, int def) throws SQLException {
        int result = def;
        if (rs.getObject(columnName) != null && !rs.wasNull()) {
            result = rs.getInt(columnName);
        }
        return result;
    }

    public static int getInt(String columnName, ResultSet rs) throws SQLException {
        return CommonUtils.getInt(columnName, rs, 0);
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        }
    }

    public static String convertDateToString(Date date, String f) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(f);
            return dateFormat.format(date);
        }
    }

    public static Date convertStringToDate(String s, String f) throws ParseException {
        DateFormat df = new SimpleDateFormat(f);
        Date d = df.parse(s);
        return d;
    }

    public static Date getDateNow() {
        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static Date getFirstDateOfMonth() {
        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static int getMonth(Date date) {
        //get month in date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        //get Year in date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static String getDateSQL(Date dt) {

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        return dateFormat.format(dt);
    }

    //>0 dt>now
    //=0 dt=now
    //< dt<now
    public static int compareWithNowByDate(Date dt, int date) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -date);
        return dt.compareTo(cal.getTime());
    }

    public static Date addDate(Date d, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, i);
        return cal.getTime();
    }

    //>0 dt>now
    //=0 dt=now
    //< dt<now
    public static int compareWithNowByHourse(Date dt, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -minute);
        return dt.compareTo(cal.getTime());
    }
     public static String GetPhysicalFile(String pathFile) throws IOException {
       
        File file = new File(pathFile);
        String absolutePath = file.getCanonicalPath();
        return absolutePath;
    }

}
