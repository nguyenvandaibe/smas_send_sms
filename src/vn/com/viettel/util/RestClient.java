/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import vn.com.viettel.BO.RestResponseBO;


public class RestClient {

    public static String SMAS_API="http://10.60.135.245:8082/api/Notification/SendNotificationFromFirebaseCloud";
    public static final String USERNAME="SMAS";
    public static final String PASSWORD="WP2bTT1Z";
    
    public static RestResponseBO getRest(String api, String param) throws MalformedURLException, ProtocolException, IOException {
        RestResponseBO res = new RestResponseBO();
        res.setReponseCode(200);
        URL url = new URL(SMAS_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("username", USERNAME);
        conn.setRequestProperty("password", PASSWORD);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            byte[] bytes = param.getBytes("UTF-8");
            wr.write(bytes);
        }
        int responseCode = conn.getResponseCode();
        res.setReponseCode(responseCode);
        if (responseCode == 200) {
            try (InputStreamReader inputReader = new InputStreamReader(
                    (conn.getInputStream()))) {
                try (BufferedReader br = new BufferedReader(inputReader)) {
                    String inputLine;
                    StringBuilder responseData = new StringBuilder();
                    while ((inputLine = br.readLine()) != null) {
                        responseData.append(inputLine);
                    }
                    res.setReponseData(responseData.toString());
                }
            }
        } else {
            try (InputStreamReader inputReader = new InputStreamReader(
                    (conn.getErrorStream()))) {
                try (BufferedReader br = new BufferedReader(inputReader)) {
                    String inputLine;
                    StringBuilder responseData = new StringBuilder();
                    while ((inputLine = br.readLine()) != null) {
                        responseData.append(inputLine);
                    }
                    res.setReponseData(responseData.toString());
                }
            }
        }
        conn.disconnect();
        return res;
    }
}
