/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.viettel.smas;

import java.io.IOException;
import java.net.ProtocolException;
import org.apache.log4j.Logger;
import vn.com.viettel.BO.RestResponseBO;
import vn.com.viettel.BO.SmsMT;
import vn.com.viettel.thread.KThreadPoolExecutor;
import vn.com.viettel.util.JsonConverter;
import vn.com.viettel.util.RestClient;

/**
 *
 * @author chiendd1
 */
public class SendNotification {

    private final Logger logger = Logger.getLogger("SEND_NOTIFICATION");

    void processSendNotification(final SmsMT objSms) {
        KThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if ("".equals(objSms.getDEVICE_TOKEN()) || objSms.getDEVICE_TOKEN() == null) {
                    return;
                }
                logger.info("Send notitioncation Token=" + objSms.getDEVICE_TOKEN());
                JsonConverter jsonConverter = new JsonConverter();
                String param = jsonConverter.toString(objSms);
                try {
                    RestResponseBO res = RestClient.getRest(RestClient.SMAS_API, param);
                    if (res.getReponseCode() == 200) {
                        logger.error("Tao notification thanh cong: " + res.getReponseData());
                    } else {
                        logger.error("Tao notification khong thanh cong: " + res.getReponseData());
                    }
                    // end process send sms
                } catch (ProtocolException ex) {
                    logger.error("processSendNotification Error: " + ex.getMessage(), ex);
                } catch (IOException ex) {
                    logger.error("processSendNotification Error: " + ex.getMessage(), ex);
                }
            }

        });
    }
}
